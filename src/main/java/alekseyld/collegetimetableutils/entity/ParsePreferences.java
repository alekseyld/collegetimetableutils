package alekseyld.collegetimetableutils.entity;

import java.util.Map;

/**
 * Created by Alekseyld on 30.05.2018.
 */
public class ParsePreferences {

    String rootUrl;
    Map<String, String> abbreviationMap;

    public Map<String, String> getAbbreviationMap() {
        return abbreviationMap;
    }

    public String getRootUrl() {
        return rootUrl;
    }
}
