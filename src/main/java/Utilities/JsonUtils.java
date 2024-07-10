package Utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Utility class for working with JSON data.
 */
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads JSON data from a file and returns it as a map.
     *
     * @param jsonFileName The name of the JSON file to read.
     * @return A map representing the JSON data.
     * @throws IOException If an I/O error occurs while reading the JSON file.
     */
    public static Map<String, Object> getJsonDataAsMap(String jsonFileName) throws IOException {
        String completeJsonFilePath = System.getProperty("user.dir") + "/src/test/resources/" + jsonFileName;
        return objectMapper.readValue(new File(completeJsonFilePath), new TypeReference<Map<String, Object>>() {});
    }

    /**
     * Parses JSON data from an Excel cell and returns it as an iterator of maps.
     *
     * @param cellName The name of the Excel cell containing JSON data.
     * @return An iterator of maps representing the parsed JSON data.
     * @throws IOException If an I/O error occurs while parsing the JSON data.
     */
    public static Iterator<Map<String, String>> getJsonData(String filename, String sheetName, String cellName) throws IOException, InvalidFormatException {
        List<LinkedHashMap<String, String>> excelDataAsListOfMap = ExcelDataDriven.loadDSheetData("src/test/resources/TestData/" + filename + ".xlsx", sheetName);
        List<Map<String, String>> headersList = new ArrayList<>();

        for (Map<String, String> data : excelDataAsListOfMap) {
            String headersJson = data.get(cellName);
            // Parse JSON headers into a map
            Map<String, String> headersMap = objectMapper.readValue(headersJson, new TypeReference<Map<String, String>>() {});
            headersList.add(headersMap);
        }
        return headersList.iterator();
    }

    /**
     * Parses a JSON object string and returns it as a map.
     *
     * @param jsonObjectString The JSON object string to parse.
     * @return A map representing the parsed JSON object.
     */
    public static Map<String, String> parseJsonObject(String jsonObjectString) {
        Map<String, String> jsonObjectMap = new HashMap<>();

        // Remove leading and trailing braces from the JSON string
        jsonObjectString = jsonObjectString.replace("{", "").replace("}", "");

        // Split the JSON string by commas to separate key-value pairs
        String[] keyValues = jsonObjectString.split(",");

        // Iterate through each key-value pair
        for (String keyValue : keyValues) {
            // Split the pair by colon to get the key and value
            String[] pair = keyValue.split(":");
            if (pair.length == 2) {
                // Trim and remove surrounding quotes from the key and value
                String key = pair[0].trim().replaceAll("^\"|\"$", "");
                String value = pair[1].trim().replaceAll("^\"|\"$", "");
                // Put the key-value pair into the map
                jsonObjectMap.put(key, value);
            }
        }

        return jsonObjectMap;
    }

    /**
     * Converts an input object to a JSON string.
     *
     * @param input The input object to convert.
     * @return A JSON string representation of the input object.
     * @throws IllegalArgumentException If the input is not a Map or List<Map<String, String>>.
     */
    public static String convertToJsonString(Object input) {
        if (input instanceof Map) {
            return convertMapToJsonString((Map<String, String>) input);
        } else if (input instanceof List) {
            return convertListOfMapsToJsonString((List<Map<String, String>>) input);
        } else {
            throw new IllegalArgumentException("Input must be a Map or List<Map<String, String>>");
        }
    }

    private static String convertListOfMapsToJsonString(List<Map<String, String>> listOfMaps) {
        StringBuilder jsonBuilder = new StringBuilder("[");

        for (Map<String, String> map : listOfMaps) {
            jsonBuilder.append(convertMapToJsonString(map)).append(",");
        }

        // Remove the trailing comma if there's at least one map
        if (!listOfMaps.isEmpty()) {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }

        jsonBuilder.append("]");

        return jsonBuilder.toString();
    }

    private static String convertMapToJsonString(Map<String, String> jsonObjectMap) {
        StringBuilder jsonBuilder = new StringBuilder("{");

        for (Map.Entry<String, String> entry : jsonObjectMap.entrySet()) {
            // Append key and value with surrounding quotes
            jsonBuilder.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue()).append("\",");
        }

        // Remove the trailing comma if there's at least one entry
        if (!jsonObjectMap.isEmpty()) {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }

        jsonBuilder.append("}");

        return jsonBuilder.toString();
    }

    /**
     * Generates a map with random values based on a provided JSON object string.
     *
     * @param jsonObjectString The JSON object string containing key-value pairs.
     * @return A map with keys from the JSON object string and corresponding random values.
     */
    public static Map<String, String> randomRequestJsonObject(String jsonObjectString) {
        Map<String, String> jsonObjectMap = new HashMap<>();

        // Remove leading and trailing braces from the JSON string
        jsonObjectString = jsonObjectString.replace("{", "").replace("}", "");

        // Split the JSON string by commas to separate key-value pairs
        String[] keyValues = jsonObjectString.split(",");

        // Iterate through each key-value pair
        for (String keyValue : keyValues) {
            // Split the pair by colon to get the key and value
            String[] pair = keyValue.split(":");
            if (pair.length == 2) {
                // Trim and remove surrounding quotes from the key and value
                String key = pair[0].trim().replaceAll("^\"|\"$", "");
                String value = pair[1].trim().replaceAll("^\"|\"$", "");
                String responseValue = getRandomValue(value);
                jsonObjectMap.put(key, responseValue);
            }
        }
        return jsonObjectMap;
    }

    private static String getRandomValue(String value) {
        if (value.contains("$")) {
            switch (value.replace("$Random", "").toUpperCase()) {
                case "PASTDATE":
                    return RandomDataGenerator.getRandomPastDate();
                case "FUTUREDATE":
                    return RandomDataGenerator.getRandomFutureDate();
                case "FULLNAME":
                    return RandomDataGenerator.getRandomDataFor(RandomDataTypeNames.FULLNAME);
                case "FIRSTNAME":
                    return RandomDataGenerator.getRandomDataFor(RandomDataTypeNames.FIRSTNAME);
                case "LASTNAME":
                    return RandomDataGenerator.getRandomDataFor(RandomDataTypeNames.LASTNAME);
                case "EMAIL":
                    return RandomDataGenerator.getRandomDataFor(RandomDataTypeNames.EMAIL);
                case "BOOLEANVALUE":
                    return String.valueOf(RandomDataGenerator.getRandomBooleanValue());
                case "COMPUTERIP":
                    return RandomDataGenerator.getRandomDataFor(RandomDataTypeNames.IP_ADDRESS);
                default:
                    System.out.println("Unsupported Key word " + value.replace("$Random", "").toUpperCase());
                    return value;
            }
        } else {
            return value;
        }
    }

    public static JsonObject convertToJsonObject(Object object) {
        Gson gson = new Gson();
        // Convert map to JSON string
        String jsonString = gson.toJson(object);
        // Parse JSON string to JsonObject
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
        return jsonObject;
    }

    // Convert a string to a JsonObject
    public static JSONObject stringToJsonObject(String jsonString) {
        return new JSONObject(jsonString);
    }

    // Convert a JsonObject to a string
    public static String jsonObjectToString(JSONObject jsonObject) {
        return jsonObject.toString();
    }
    /**
     * Converts a JSONObject to a pretty formatted JSON string.
     *
     * @param jsonObject the JSONObject to convert
     * @return the pretty formatted JSON string
     */
    public static String jsonObjectToPrettyString(JSONObject jsonObject) {
        return jsonObject.toString(4); // 4 is the number of spaces for indentation
    }

}
