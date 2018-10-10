package alekseyld.collegetimetableutils.utils;

import alekseyld.collegetimetableutils.entity.Day;
import alekseyld.collegetimetableutils.entity.Lesson;
import alekseyld.collegetimetableutils.entity.ParsePreferences;
import alekseyld.collegetimetableutils.entity.TimeTable;
import com.google.gson.Gson;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Alekseyld on 04.09.2016.
 */

public class DataUtils {

    //timebot 252510225

    //public static Pattern groupPattern = Pattern.compile("[0-9]\\s[А-Я]{1,}[-][0-9]");
    public static Pattern groupPatternWithoutNum = Pattern.compile("[0-9]\\s[А-Я]{1,}([-][0-9]){0,}", Pattern.UNICODE_CHARACTER_CLASS);
    //public static Pattern fioPattern = Pattern.compile("([А-ЯЁа-яё]{1,}[\\s]([А-ЯЁа-яё]{1}[.]){2})");

    private static ParsePreferences parsePreferences;

    public static void setParsePreferences(ParsePreferences parsePreferences) {
        DataUtils.parsePreferences = parsePreferences;
    }

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static String getGroupUrl(String group) {
        return getGroupUrl("", group);
    }

    public static String getGroupUrl(String root, String group) {

        if (group == null || !groupPatternWithoutNum.matcher(group).matches())
            return "";

        String url = "";

        Set<String> neftGroups = new HashSet<String>() {{
            add("АПП");
            add("БНГ");
            add("В");
            add("ПНГ");
            add("ТАК");
            add("ТО");
            add("ТОВ");
            add("ЭНН");
            add("ЭННУ");
        }};

        String abbr = "";

        if (groupPatternWithoutNum.matcher(group).matches()) {
            abbr = group.split(" ")[1].split("-")[0];
        } else {
            abbr = group.split(" ")[1];
        }

        if (group.charAt(0) == '1' && neftGroups.contains(abbr)) {
            url = parsePreferences.getAbbreviationMap().get("1");
        } else {
            url = switchAbbr(abbr);
        }

        if (root.equals(""))
            root = parsePreferences.getRootUrl();

        return url.equals("") ? url : root + url;
    }

    private static String switchAbbr(String abbr) {
        return parsePreferences.getAbbreviationMap().get(abbr);
    }

    public static TimeTable parseDocument(Document document, String group) {

        if (document == null || group == null || !groupPatternWithoutNum.matcher(group).matches()) {
            return new TimeTable();
        }

        Elements table = document.select("tr").select("td");

        Pattern numberPattern = Pattern.compile("^[0-9]");
        Pattern dayPattern = Pattern.compile("[А-Я]\\s[А-Я]\\s\\b");

        TimeTable timeTable = new TimeTable()
                .setLastRefresh(new Date())
                .setGroup(group);

        List<Lesson> lessons = new ArrayList<>();
//        HashMap<TimeTable.Day, String> days = new HashMap<>();
        String[] dayString = new String[]{"", ""};

        //Искать номер пары
        boolean space = false;

        //Первая итерация
        boolean first = true;

        //Пропуск до названия пары
        boolean spaceToLessonBlock = false;

        //Переход к номеру пары
        boolean toLesson = false;

        boolean firstDoubleLesson = true;

        //Пропуск группы
        int iSpace = 0;
        int lessonSpace = -1;

        //Счетчик дней
        int day = -1;
        //Счетчик пар
        int lesson = 0;

        int i = 0;

        for (int iterator = 0; iterator < table.size(); iterator++) {

            if (dayPattern.matcher(table.get(iterator).text()).find()) {
                dayString[0] = dayString[1];
                dayString[1] = table.get(iterator).text();
            }

            //Ищем начало групп
            if (table.get(iterator).text().equals("День/Пара") && first) {
                space = true;
                first = false;
            }
            //Считаем положение группы в таблице
            if (space) {
                iSpace++;
            }

            //Если элемент совпадает с название группы, то останавливаем счетчик
            if (table.get(iterator).text().equals(group)) {
                space = false;
                toLesson = true;
            }

            i++;
            if (i == table.size() - 1 && day != -1) {

                String dateName = day == 0 && !dayString[0].equals("") || (day != 0 && !dayString[0].equals(timeTable.getDayList().get(day - 1).getDateName()))  ? dayString[0] : dayString[1];

                timeTable.addDay(
                        new Day()
                                .setId(day)
                                .setDateName(dateName)
                                .setDate(getDateFromDateName(dateName))
                                .setDayLessons(lessons)
                );
            }

            if (toLesson && numberPattern.matcher(table.get(iterator).text()).matches()) {
                toLesson = false;
                spaceToLessonBlock = true;
                lessonSpace = 0;

                lesson = Integer.parseInt(table.get(iterator).text());

                if (lesson == 0 && day != -1) {
                    String dateName = day == 0 && !dayString[0].equals("") || !dayString[0].equals(timeTable.getDayList().get(day - 1).getDateName())  ? dayString[0] : dayString[1];

                    timeTable.addDay(
                            new Day()
                                    .setId(day)
                                    .setDateName(dateName)
                                    .setDate(getDateFromDateName(dateName))
                                    .setDayLessons(lessons)
                    );

                    lessons = new ArrayList<>();
                    day++;
                }
                if (day == -1)
                    day++;
            }

            if (spaceToLessonBlock) {
                lessonSpace++;
            }

            if (lessonSpace == iSpace) {
                String text = table.get(iterator).text();

                boolean second = table.get(iterator).attr("colspan").equals("");
                String secondName = second ? table.get(iterator + 1).text() : null;

                boolean isChange = table.get(iterator).children().attr("color").equals("blue");

                String teacherName = getTeacherName(table.get(iterator).children(),
                        second ? table.get(iterator + 1).children() : null,
                        isChange);

                lessons.add(
                        new Lesson()
                                .setNumber(lesson)
                                .setName(text)
                                .setSecondName(secondName)
                                //todo Второй преподаватель
                                .setTeacher(teacherName)
                                .setChange(isChange)
                );

                lessonSpace = 0;
                toLesson = true;
                spaceToLessonBlock = false;
            } else {
                if (table.get(iterator).tagName().equals("td") &&
                        table.get(iterator).attr("colspan").equals("") &&
                        table.get(iterator).attr("rowspan").equals("") &&
                        !numberPattern.matcher(table.get(iterator).text()).matches() &&
                        firstDoubleLesson) {
                    lessonSpace--;
                    firstDoubleLesson = false;
                } else {
                    firstDoubleLesson = true;
                }
            }
        }

        return timeTable;
    }

    private static String getTeacherName(Elements childs, Elements secondChilds, boolean isChange) {
        String ret = "";

        if (isChange) {
            childs = childs.get(0).children();
        } else if (childs.size() < 3){
            return ret;
        }

        ret = childs.get(2).text();

        if (secondChilds != null){
            ret += " / " + secondChilds.get(2).text();
        }

        return ret;
    }

    public static TimeTable getEmptyWeekTimeTable() {
        TimeTable timeTable = new TimeTable()
                .setGroup("")
                .setLastRefresh(new Date());

        for (int i = 0; i < 7; i++){
            Day day = new Day()
                    .setDateName("")
                    .setId(i);

            for (int i1 = 0; i1 < 7; i1++) {
                day.getDayLessons()
                        .add(new Lesson()
                                .setName("")
                                .setTeacher("")
                                .setNumber(i1)
                                .setChange(false)
                                //fixme убрать этот null и переделать на ""
                                .setSecondName(null));
            }

            timeTable.addDay(day);
        }


        return timeTable;
    }

    public static String getNormalizeDate(String date) {
        String[] splits = date.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splits.length; i++) {
            if (splits[i].length() > 1)
                sb.append(" ");
            sb.append(i == 0 ? splits[i] : splits[i].toLowerCase());
        }
        return sb.toString();
    }

    public static Date getDateFromDateName(String dateName) {
        String[] splits =  dateName.split(" ");
        try {
            return simpleDateFormat.parse(splits[splits.length - 1].replace(" ", ""));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Lesson minimizeMdk(Lesson lesson) {
        if (!parsePreferences.isMinimizeMdk() || !lesson.getDoubleName().toUpperCase().contains("МДК")) {
            return lesson;
        }

        if (lesson.getName().toUpperCase().contains("МДК")) {
            String[] l = lesson.getName().split(" ");
            lesson.setName(l[0] + " " + l[l.length - 2] + " " + l[l.length - 1]);

        } else if (lesson.getSecondName() != null && lesson.getSecondName().toUpperCase().contains("МДК")) {
            String[] l = lesson.getSecondName().split(" ");
            lesson.setSecondName(l[0] + l[l.length - 2] + l[l.length - 1]);
        }

        return lesson;
    }

    public static String convertTimeTableToText(TimeTable timeTable) {
        if (timeTable == null) return "";

        StringBuilder stringBuilder = new StringBuilder();

        //stringBuilder.append("Расписание группы ").append(timeTable.getGroup()).append("\n\n");

        for (Day day: timeTable.getDayList()) {
            String dayName = getNormalizeDate(day.getDateName());
            stringBuilder.append(dayName).append("\n\n");
            boolean needEmptyNum = true;

            for (Lesson lesson: day.getDayLessons()){
                if (!lesson.getName().equals(" ") && !lesson.getName().equals("")) {
                    needEmptyNum = false;
                    stringBuilder.append(lesson.getNumber()).append(". ").append(minimizeMdk(lesson).getDoubleName());
                    if (lesson.isChange())
                        stringBuilder.append(" ").append("(замена)");
                    stringBuilder.append("\n\n");
                } else {
                    if (needEmptyNum) {
                        stringBuilder.append(lesson.getNumber()).append(". ").append(minimizeMdk(lesson).getDoubleName()).append("-").append("\n\n");
                    }
                }
            }
            if (!dayName.equals("") && day.getId() != timeTable.getDayList().get(timeTable.getDayList().size() - 1).getId())
                stringBuilder.append("----------------------------------------\n");
        }

        return stringBuilder.toString().trim();
    }

    public static String convertTimeTableToJson(TimeTable timeTable) {
        return new Gson().toJson(timeTable);
    }

    public static TimeTable cutDays(TimeTable timeTable, int countDays) {

        for (int i = 7 - countDays; timeTable.getDayList().size() != countDays; i--) {
            timeTable.getDayList().remove(i);
        }

        return timeTable;
    }

    public static TimeTable getTimeTableByDate(TimeTable timeTable, Date date) {
        while (timeTable.getDayList().size() != 1) {
            if (timeTable.getDayList().get(0).getDate() == null) {
                return null;
            }

            if (timeTable.getDayList().get(0).getDate().getTime() == date.getTime()){
                timeTable.getDayList().set(timeTable.getDayList().size() - 1, timeTable.getDayList().get(0));
            }

            timeTable.getDayList().remove(0);
        }

        if (timeTable.getDayList().get(0).getDate().getTime() != date.getTime()){
            timeTable.getDayList().remove(0);
        }

        return timeTable;
    }

    public static TimeTable getTimeTableByDateName(TimeTable timeTable, String dateName) {
        while (timeTable.getDayList().size() != 1) {

            if (getNormalizeDate(timeTable.getDayList().get(0).getDateName()).contains(dateName)){
                timeTable.getDayList().set(timeTable.getDayList().size() - 1, timeTable.getDayList().get(0));
            }

            timeTable.getDayList().remove(0);
        }

        if (!getNormalizeDate(timeTable.getDayList().get(0).getDateName()).contains(dateName)){
            timeTable.getDayList().remove(0);
        }

        return timeTable;
    }

    public static TimeTable getTimeTableByIndex(TimeTable timeTable, String index) {
        String[] ind = index.split(",");

        int i = 0;
        while (true) {
            if (i >= timeTable.getDayList().size()){
                break;
            }

            Day day = timeTable.getDayList().get(i);
            boolean is = false;
            for (String in: ind) {
                if (Integer.parseInt(in) == day.getId()){
                    is = true;
                    break;
                }
            }

            if (is) {
                i++;

            } else {
                timeTable.getDayList().remove(i);
            }
        }

        return timeTable;
    }
}