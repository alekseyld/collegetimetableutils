package alekseyld.collegetimetableutils;

import alekseyld.collegetimetableutils.entity.Arguments;
import alekseyld.collegetimetableutils.entity.TimeTable;
import alekseyld.collegetimetableutils.utils.DataUtils;
import alekseyld.collegetimetableutils.utils.FilesUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Alekseyld on 26.05.2018.
 */
public class TimeTableProcessor {

    private Arguments arguments;

    public TimeTableProcessor(Arguments arguments) {
        this.arguments = arguments;

    }

    private TimeTable getTimeTableFromSite(String group, String urlproxy) {
        TimeTable timeTable = DataUtils.getEmptyWeekTimeTable();

        String url = null;
        if (urlproxy == null) {
            url = DataUtils.getGroupUrl(group);
        }

        try {
            for (int i = 0; i < 5; i++) {
                Document document = Jsoup.connect(urlproxy == null ? url : urlproxy).timeout(4000).get();
                if (document != null) {
                    timeTable = DataUtils.parseDocument(document, group);
                    break;
                }
            }
        } catch (Exception e){
            if (urlproxy == null) {
                return getTimeTableFromSite(group, url.replace("109.195.146.243", "ovswg33mnqxhe5i.nblz.ru"));
            }
//            e.printStackTrace();
        }

        return timeTable;
    }

    private TimeTable getTimeTable(boolean fromSite) {

        if (arguments.fromSite) {

            return getTimeTableFromSite(arguments.group, null);
        } else {

            return FilesUtils.getTimeTableFromCacheSafely(arguments.group);
        }
    }

    private TimeTable getTimeTableCutDays(TimeTable timeTable, int countDay) {
        return DataUtils.cutDays(timeTable, countDay);
    }

    public void process(){

        TimeTable timeTable = getTimeTable(arguments.fromSite);

        if (arguments.countDay != 7 && arguments.countDay != 0) {

            timeTable = getTimeTableCutDays(timeTable, arguments.countDay);
        }

        if (arguments.dayName != null) {

            timeTable = DataUtils.getTimeTableByDateName(timeTable, arguments.dayName);
        } else if (arguments.date != null) {

            timeTable = DataUtils.getTimeTableByDate(timeTable, arguments.date);
        } else if (arguments.index != null) {

            timeTable = DataUtils.getTimeTableByIndex(timeTable, arguments.index);
        }

        if (arguments.save) {

            Main.printOutData(
                    FilesUtils.saveTimeTableToCacheSafely(arguments.group, timeTable, arguments.json)
            );
        } else {

            Main.printOutData(
                    arguments.json ? DataUtils.convertTimeTableToJson(timeTable) : DataUtils.convertTimeTableToText(timeTable)
            );
        }
    }
}
