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

    private TimeTable getTimeTableFromSite(String group) {
        TimeTable timeTable = DataUtils.getEmptyWeekTimeTable();

        try {
            String url = DataUtils.getGroupUrl(group);
            Document document = Jsoup.connect(url).timeout(5000).get();

            timeTable = DataUtils.parseDocument(document, group);

        } catch (Exception e){
            e.printStackTrace();
        }

        return timeTable;
    }

    private TimeTable getTimeTable(boolean fromSite) {

        if (arguments.fromSite) {

            return getTimeTableFromSite(arguments.group);
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
