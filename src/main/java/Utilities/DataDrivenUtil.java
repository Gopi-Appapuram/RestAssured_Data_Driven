package Utilities;

import io.restassured.response.Response;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DataDrivenUtil {

    private Response response;
    private final String projectPath = System.getProperty("user.dir");
    private Properties environmentProperties;

    /**
     * Loads environment data from the specified properties file.
     *
     * @param environment The name of the environment properties file (without extension).
     */
    public void loadEnvironmentData(String environment) {
        try (FileReader reader = new FileReader(projectPath + "/src/test/resources/" + environment + ".properties")) {
            environmentProperties = new Properties();
            environmentProperties.load(reader);
        } catch (Exception ex) {
            System.err.println("Error occurred while reading environment file: " + ex.getMessage());
        }
    }

    /**
     * Executes data-driven API requests based on the provided Excel file and sheet name.
     *
     * @param fileName  The name of the Excel file containing the data-driven requests.
     * @param sheetName The name of the sheet within the Excel file.
     * @throws IOException            If an I/O error occurs while reading the Excel file.
     * @throws InvalidFormatException If the Excel format is invalid.
     */
    public void executeDataDrivenAPIs(String fileName, String sheetName) throws IOException, InvalidFormatException {
        // Load data from data-driven sheet
        List<LinkedHashMap<String, String>> testData = ExcelDataDriven.loadDSheetData(fileName, sheetName);
        List<LinkedHashMap<String, String>> updatedExcelDataList = new ArrayList<>();

        // Read all keys from the first row
        Set<String> keys = testData.get(0).keySet();

        String baseUrl = environmentProperties.getProperty("baseUrl");

        // Loop and execute each request from data-driven sheet
        for (LinkedHashMap<String, String> requestData : testData) {
            String basePath = "";
            Map<String, String> pathParams = null;
            Map<String, String> queryParams = null;
            Map<String, String> headers = null;
            String method = "";
            String requestBody = "";
            String expectedStatusCode = "";

            for (String key : keys) {
                switch (key.toLowerCase()) {
                    case "baseurl":
                        baseUrl = environmentProperties.getProperty("baseUrl", baseUrl);
                        break;
                    case "basepath":
                        basePath = requestData.get(key);
                        break;
                    case "pathparameters":
                        pathParams = helperUtils.loadParameters(requestData.get(key));
                        break;
                    case "queryparameters":
                        queryParams = helperUtils.loadParameters(requestData.get(key));
                        break;
                    case "headers":
                        headers = JsonUtils.parseJsonObject(requestData.get(key));
                        break;
                    case "method":
                        method = requestData.get(key);
                        break;
                    case "requestbody":
                        requestBody = requestData.get(key);
                        requestBody = JsonUtils.convertToJsonString(JsonUtils.randomRequestJsonObject(requestBody));
                        break;
                    case "statuscode":
                        expectedStatusCode = requestData.get(key);
                        break;
                }
            }

            LinkedHashMap<String, String> updatedExcelData  = executeAPIRequest(method, baseUrl, basePath, requestBody, pathParams, queryParams, headers, expectedStatusCode, requestData);
            updatedExcelDataList.add(updatedExcelData);
        }
        Masking masking = new Masking();
        String[] headers = new String[]{"Headers", "RequestBody", "Base_URL"};
        String[] keysToMask = new String[] {"Content-Type", "email"};
        updatedExcelDataList = masking.maskValuesOfMap(updatedExcelDataList, headers, keysToMask);

        ExcelDataDriven.writeDataToExcel(updatedExcelDataList, fileName + "_report_" + DateFormatter.formatPresentDateTime(), sheetName + "_report_" + DateFormatter.formatPresentDateTime());
    }

    /**
     * Executes an API request based on the specified HTTP method, URL components,
     * request payload, path parameters, query parameters, headers, and expected status code.
     *
     * @param method             The HTTP method (GET, POST, PUT, DELETE, PATCH) for the API request.
     * @param baseUrl            The base URL of the API.
     * @param basePath           The base path of the API endpoint.
     * @param requestPayload     The payload/body of the request, represented as an Object.
     * @param pathParams         The path parameters as a Map<String, String>. Can be null if not applicable.
     * @param queryParams        The query parameters as a Map<String, String>. Can be null if not applicable.
     * @param headers            The headers as a Map<String, String>. Can be null if not applicable.
     * @param expectedStatusCode The expected HTTP status code as a String.
     * @param requestMap         The map containing the request data to be updated with response details.
     * @return The updated request map with response details.
     */
    private LinkedHashMap<String, String> executeAPIRequest(String method, String baseUrl, String basePath,
                                                            Object requestPayload, Map<String, String> pathParams,
                                                            Map<String, String> queryParams, Map<String, String> headers,
                                                            String expectedStatusCode, LinkedHashMap<String, String> requestMap) {

        try {
            switch (method.toUpperCase()) {
                case "GET":
                    if (pathParams != null && !pathParams.isEmpty() && queryParams != null && !queryParams.isEmpty()) {
                        response = RestUtils.performGetWithPathAndQueryParams(baseUrl, basePath, pathParams, queryParams, headers);
                    } else if (pathParams != null && !pathParams.isEmpty()) {
                        response = RestUtils.performGetWithPathParams(baseUrl, basePath, pathParams, headers);
                    } else if (queryParams != null && !queryParams.isEmpty()) {
                        response = RestUtils.performGetWithQueryParams(baseUrl, basePath, queryParams, headers);
                    } else {
                        response = RestUtils.performGet(baseUrl, basePath, headers);
                    }
                    break;
                case "POST":
                    if (pathParams != null && !pathParams.isEmpty() && queryParams != null && !queryParams.isEmpty()) {
                        response = RestUtils.performPostWithPathAndQueryParams(baseUrl, basePath, requestPayload, pathParams, queryParams, headers);
                    } else if (pathParams != null && !pathParams.isEmpty()) {
                        response = RestUtils.performPostWithPathParams(baseUrl, basePath, requestPayload, pathParams, headers);
                    } else if (queryParams != null && !queryParams.isEmpty()) {
                        response = RestUtils.performPostWithQueryParams(baseUrl, basePath, requestPayload, queryParams, headers);
                    } else {
                        response = RestUtils.performPost(baseUrl, basePath, requestPayload, headers);
                    }
                    break;
                case "PUT":
                    if (pathParams != null && !pathParams.isEmpty() && queryParams != null && !queryParams.isEmpty()) {
                        response = RestUtils.performPutWithPathAndQueryParams(baseUrl, basePath, requestPayload, pathParams, queryParams, headers);
                    } else if (pathParams != null && !pathParams.isEmpty()) {
                        response = RestUtils.performPutWithPathParams(baseUrl, basePath, requestPayload, pathParams, headers);
                    } else if (queryParams != null && !queryParams.isEmpty()) {
                        response = RestUtils.performPutWithQueryParams(baseUrl, basePath, requestPayload, queryParams, headers);
                    } else {
                        response = RestUtils.performPut(baseUrl, basePath, requestPayload, headers);
                    }
                    break;
                case "DELETE":
                    if (pathParams != null && !pathParams.isEmpty() && queryParams != null && !queryParams.isEmpty()) {
                        response = RestUtils.performDeleteWithPathAndQueryParams(baseUrl, basePath, pathParams, queryParams, headers);
                    } else if (pathParams != null && !pathParams.isEmpty()) {
                        response = RestUtils.performDeleteWithPathParams(baseUrl, basePath, pathParams, headers);
                    } else if (queryParams != null && !queryParams.isEmpty()) {
                        response = RestUtils.performDeleteWithQueryParams(baseUrl, basePath, queryParams, headers);
                    } else {
                        response = RestUtils.performDelete(baseUrl, basePath, headers);
                    }
                    break;
                case "PATCH":
                    if (pathParams != null && !pathParams.isEmpty() && queryParams != null && !queryParams.isEmpty()) {
                        response = RestUtils.performPatchWithPathAndQueryParams(baseUrl, basePath, requestPayload, pathParams, queryParams, headers);
                    } else if (pathParams != null && !pathParams.isEmpty()) {
                        response = RestUtils.performPatchWithPathParams(baseUrl, basePath, requestPayload, pathParams, headers);
                    } else if (queryParams != null && !queryParams.isEmpty()) {
                        response = RestUtils.performPatchWithQueryParams(baseUrl, basePath, requestPayload, queryParams, headers);
                    } else {
                        response = RestUtils.performPatch(baseUrl, basePath, requestPayload, headers);
                    }
                    break;
                default:
                    System.out.println("Unsupported HTTP method: " + method);
            }

            requestMap = addBaseUrlToRequest(baseUrl, requestMap);
            // Update request map with response data
            requestMap = updateRequestMap(response, expectedStatusCode, requestMap);

        } catch (Exception e) {
            System.err.println("Error executing API request: " + e.getMessage());
            e.printStackTrace();
        }
        return requestMap;
    }

    private LinkedHashMap<String, String> addBaseUrlToRequest(String baseUrl, LinkedHashMap<String, String> requestMap) {
        // Create a new LinkedHashMap with "Base_URL" as the first entry
        LinkedHashMap<String, String> updatedRequestMap = new LinkedHashMap<>();
        updatedRequestMap.put("Base_URL", baseUrl);
        updatedRequestMap.putAll(requestMap);
        return updatedRequestMap;
    }

    /**
     * Updates the request map with API response details.
     *
     * @param response           The API response.
     * @param expectedStatusCode The expected status code.
     * @param requestMap         The map containing the request data to be updated.
     * @return The updated request map with response details.
     */
    private LinkedHashMap<String, String> updateRequestMap(Response response, String expectedStatusCode, LinkedHashMap<String, String> requestMap) {
        String actualStatusCode = String.valueOf(response.getStatusCode());
        requestMap.put("PassOrFail", actualStatusCode.equals(expectedStatusCode) ? "PASS" : "FAIL");

        String responseBody = response.getBody().asPrettyString();
        if (!actualStatusCode.equals(expectedStatusCode)) {
            requestMap.put("ResponseBody", responseBody);
        } else {
            requestMap.put("ResponseBody", "");
        }
        requestMap.put("ResponseStatusCode", response.getStatusLine());

        return requestMap;
    }

}
