package alekseyld.collegetimetableutils.utils;

import alekseyld.collegetimetableutils.entity.ParsePreferences;
import alekseyld.collegetimetableutils.entity.TimeTable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Alekseyld on 27.05.2018.
 */
public class FilesUtils {

    public static File getCacheDir() {
        File cacheDir = new File("cache");

        if (!cacheDir.exists()){
            cacheDir.mkdir();
        }

        return cacheDir;
    }

    public static String saveTimeTableToCache(String group, TimeTable timeTable, boolean json) throws IOException {

        File cacheFile = new File(getCacheDir(), group + "." + "json");

        String isChanges = "";

        TimeTable oldTimeTable = null;

        if (!cacheFile.exists()) {
            cacheFile.createNewFile();
        } else {
            oldTimeTable = getTimeTableFromCacheSafely(group);
        }

        isChanges = timeTable.equals(oldTimeTable);

        try (PrintWriter out = new PrintWriter(cacheFile)) {
            out.print(
                json ? DataUtils.convertTimeTableToJson(timeTable) : DataUtils.convertTimeTableToText(timeTable)
            );
        }

        return isChanges;
    }

    private static String readFile(String path, Charset encoding) throws IOException {

        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static ParsePreferences getParsePreference() throws IOException {

        File cacheFile = new File("pref.json");

        if (!cacheFile.exists()) {
            return null;
        }

        String json = readFile(cacheFile.getAbsolutePath(), Charset.defaultCharset());

        Gson gson = new Gson();
        Type parsepref = new TypeToken<ParsePreferences>(){}.getType();

        return gson.fromJson(json, parsepref);
    }

    public static TimeTable getTimeTableFromCache(String group) throws IOException {

        File cacheFile = new File(getCacheDir(), group + "." + "json");

        TimeTable timeTable = DataUtils.getEmptyWeekTimeTable();

        if (!cacheFile.exists()) {
            return timeTable;
        }

        String json = readFile(cacheFile.getAbsolutePath(), Charset.defaultCharset());

        Gson gson = new Gson();
        Type timeTableType = new TypeToken<TimeTable>(){}.getType();

        timeTable = gson.fromJson(json, timeTableType);

        return timeTable;
    }

    public static String saveTimeTableToCacheSafely(String group, TimeTable timeTable, boolean json) {
        try {
            return saveTimeTableToCache(group, timeTable, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static TimeTable getTimeTableFromCacheSafely(String group) {

        TimeTable timeTable = new TimeTable();

        try {
            timeTable = getTimeTableFromCache(group);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return timeTable;
    }

}
