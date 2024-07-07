package Utilities;

import java.util.HashMap;
import java.util.Map;

public class helperUtils {

    /**
     * Loads path parameters into a map.
     *
     * @param params the path parameters in the format "key1:value1,key2:value2"
     * @return a map containing the path parameters
     * ex: String queryParamsString = "param1:value1,param2:value2";
     */
    public static Map<String, String> loadParameters(String params) {
        Map<String, String> pathParams = new HashMap<>();

        if (params != null && !params.isEmpty()) {
            String[] keyValuePairs = params.split(",");
            for (String pair : keyValuePairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    pathParams.put(keyValue[0].trim(), keyValue[1].trim());
                }
            }
        }

        return pathParams;
    }


}
