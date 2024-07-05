package Utilities;

import java.util.HashMap;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        String json = "{\"customer_id\" : 1, \"services\" : \"schedule\"}";

        randomRequestJsonObject(json);

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
                } else {
                    responseValue = value;
                }

                jsonObjectMap.put(key, responseValue);
            }
        }
        System.out.println(jsonObjectMap);
        return jsonObjectMap;
    }
}
