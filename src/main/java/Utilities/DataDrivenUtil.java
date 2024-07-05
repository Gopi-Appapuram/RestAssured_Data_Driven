package Utilities;

import io.restassured.response.Response;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DataDrivenUtil {

    private Response response;
    private final String projectPath = System.getProperty("user.dir");
    private Properties envProp;

    /**
     * Loads environment data from the specified properties file.
     *
     * @param environment The name of the environment properties file (without extension).
     */
    public void loadEnvironmentData(String environment) {
        try (FileReader reader = new FileReader(projectPath + "/src/test/resources/" + environment + ".properties")) {
            envProp = new Properties();
            envProp.load(reader);
        } catch (Exception ex) {
            System.err.println("Error occurred while reading environment file: " + ex.getMessage());
        }
    }

    /**
     * Executes data-driven API requests based on the provided Excel file and sheet name.
     *
     * @param fileName  The name of the Excel file containing the data-driven requests.
     * @param sheetName The name of the sheet within the Excel file.
     * @return true if all API requests are successfully executed, false otherwise.
     * @throws IOException If an I/O error occurs while reading the Excel file.
     */
    public boolean executeDataDrivenAPIs(String fileName, String sheetName) throws IOException, InvalidFormatException {
        // Load data from data-driven sheet
        List<LinkedHashMap<String, String>> ddData = ExcelDataDriven.loadDSheetData(fileName, sheetName);

        // Read all keys from the first row
        Map<String, String> anyRow = ddData.get(0);
        Set<String> keys = anyRow.keySet();

        String baseUrl = envProp.getProperty("baseUrl");

        // Loop and execute each request from data-driven sheet
        for (Map<String, String> eachRequest : ddData) {
            String basePath = "";
            Map<String, String> pathParameterObject = null;
            Map<String, String> headerObject = null;
            String method = "";
            String requestBody = "";
            String statusCode = "";

            for (String keyName : keys) {
                switch (keyName.toLowerCase()) {
                    case "baseurl":
                        baseUrl = envProp.getProperty("baseUrl", baseUrl);
                        break;
                    case "basepath":
                        basePath = eachRequest.get(keyName);
                        break;
                    case "pathparameter":
                        pathParameterObject = JsonUtils.parseJsonObject(eachRequest.get(keyName));
                        break;
                    case "headers":
                        headerObject = JsonUtils.parseJsonObject(eachRequest.get(keyName));
                        break;
                    case "method":
                        method = eachRequest.get(keyName);
                        break;
                    case "requestbody":
                        requestBody = eachRequest.get(keyName);
                        Map<String, String> randomRequestValues = JsonUtils.randomRequestJsonObject(requestBody);
                        requestBody = JsonUtils.convertToJsonString(randomRequestValues);
                        break;
                    case "statuscode":
                        statusCode = eachRequest.get(keyName);
                        break;
                }
            }

            executeAPIRequest(baseUrl, basePath, pathParameterObject, headerObject, method, requestBody, statusCode, eachRequest);
        }

        ExcelDataDriven.writeDataToExcel(ddData, fileName + "_report_" + DateFormater.formatPresentDateTime(), sheetName + "_report_" + DateFormater.formatPresentDateTime());
        return true;
    }

    /**
     * Executes the API request and updates the request map with the response data.
     *
     * @param baseUrl           The base URL of the API.
     * @param basePath          The base path of the API.
     * @param pathParameterObj  The path parameters as a map.
     * @param headerObj         The headers as a map.
     * @param method            The HTTP method to be used.
     * @param requestBody       The request body as a string.
     * @param expectedStatusCode The expected status code.
     * @param requestMap        The map containing the request data.
     */
    private void executeAPIRequest(String baseUrl, String basePath, Map<String, String> pathParameterObj, Map<String, String> headerObj, String method, String requestBody, String expectedStatusCode, Map<String, String> requestMap) {
        try {
            switch (method.toUpperCase()) {
                case "POST":
                    response = RestUtils.performPost(baseUrl, basePath, requestBody, headerObj);
                    break;
                case "PUT":
                    response = RestUtils.performPut(baseUrl, basePath, requestBody, headerObj);
                    break;
                case "GET":
                    response = RestUtils.performGet(baseUrl, basePath, headerObj);
                    break;
                case "PATCH":
                    //response = RestUtils.performPatch(baseUrl, basePath, requestBody, headerObj);
                    break;
                case "DELETE":
                    //response = RestUtils.performDelete(baseUrl, basePath, headerObj);
                    break;
                default:
                    System.err.println("Invalid method: " + method);
                    return;
            }

            // Update request map with response data
            validateResponse(response, expectedStatusCode, requestMap);

        } catch (Exception e) {
            System.err.println("Error executing API request: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Validates the API response against the expected status code and updates the request map.
     *
     * @param response          The API response.
     * @param expectedStatusCode The expected status code.
     * @param requestMap        The map containing the request data.
     */
    private void validateResponse(Response response, String expectedStatusCode, Map<String, String> requestMap) {
        String actualStatusCode = String.valueOf(response.getStatusCode());
        requestMap.put("PassOrFail", actualStatusCode.equals(expectedStatusCode) ? "PASS" : "FAIL");
        requestMap.put("ResponseBody", response.getBody().asPrettyString());
        requestMap.put("ResponseStatusCode", response.getStatusLine());
    }
}
