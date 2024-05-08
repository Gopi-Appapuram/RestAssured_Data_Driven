package Utilities;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Reads JSON data from a file and returns it as a map.
     *
     * @param jsonFileName The name of the JSON file to read.
     * @return A map representing the JSON data.
     * @throws IOException If an I/O error occurs while reading the JSON file.
     */
    public static Map<String, Object> getJsonDataAsMap(String jsonFileName) throws IOException {
        String completeJsonFilePath = System.getProperty("user.dir") + "/src/test/resources/" + jsonFileName;
        Map<String, Object> data = objectMapper.readValue(new File(completeJsonFilePath), new TypeReference<>() {
        });
        return data;
    }

    /**
     * Parses JSON data from an Excel cell and returns it as an iterator of maps.
     *
     * @param CellName The name of the Excel cell containing JSON data.
     * @return An iterator of maps representing the parsed JSON data.
     * @throws IOException If an I/O error occurs while parsing the JSON data.
     */
    public static Iterator<Map<String, String>> getJsonData(String CellName) throws IOException {
        List<HashMap<String, String>> excelDataAsListOfMap = ExcelDataDriven.loadDSheetData("src/test/resources/TestData/RestAssured.xlsx", "Sheet2");
        List<Map<String, String>> headersList = new ArrayList<>();

        for (Map<String, String> data : excelDataAsListOfMap) {
            String headersJson = data.get(CellName);
            // Parse JSON headers into a map
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> headersMap = objectMapper.readValue(headersJson, new TypeReference<Map<String, String>>() {
            });
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
        StringBuilder jsonBuilder = new StringBuilder("{");

        for (Map<String, String> map : listOfMaps) {
            jsonBuilder.append(convertMapToJsonString(map)).append(",");
        }

        // Remove the trailing comma if there's at least one map
        if (!listOfMaps.isEmpty()) {
            jsonBuilder.deleteCharAt(jsonBuilder.length() - 1);
        }

        jsonBuilder.append("}");

        return jsonBuilder.toString();
    }


    public static String convertMapToJsonString(Map<String, String> jsonObjectMap) {
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
                String responseValue = "";
                if (value.contains("$")) {
                    switch (value.replace("$Random", "").toUpperCase()) {
                        case "PASTDATE":
                            responseValue = RandomDataGenerator.getRandomPastDate();
                            break;
                        case "FUTUREDATE":
                            responseValue = RandomDataGenerator.getRandomFutureDate();
                            break;
                        case "FULLNAME":
                            responseValue = RandomDataGenerator.getRandomDataFor(RandomDataTypeNames.FULLNAME);
                            break;
                        case "FIRSTNAME":
                            responseValue = RandomDataGenerator.getRandomDataFor(RandomDataTypeNames.FIRSTNAME);
                            break;
                        case "LASTNAME":
                            responseValue = RandomDataGenerator.getRandomDataFor(RandomDataTypeNames.LASTNAME);
                            break;
                        case "EMAIL":
                            responseValue = RandomDataGenerator.getRandomDataFor(RandomDataTypeNames.EMAIL);
                            break;
                        case "BOOLEANVALUE":
                            responseValue = String.valueOf(RandomDataGenerator.getRandomBooleanValue());
                            break;
                        case "COMPUTERIP":
                            responseValue = RandomDataGenerator.getRandomDataFor(RandomDataTypeNames.IP_ADDRESS);
                            break;
                        /*default:
                            System.out.println("Unsupported Key word " + value.replace("$Random", "").toUpperCase());*/
                    }
                }

                jsonObjectMap.put(key, responseValue);
            }
        }
        return jsonObjectMap;
    }

}
