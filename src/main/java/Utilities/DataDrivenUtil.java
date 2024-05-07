package Utilities;

import io.restassured.response.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataDrivenUtil {

    Response response;

    /**
     * Executes data-driven API requests based on the provided Excel file and sheet name.
     *
     * @param fileName  The name of the Excel file containing the data-driven requests.
     * @param sheetName The name of the sheet within the Excel file.
     * @return true if all API requests are successfully executed, false otherwise.
     * @throws IOException If an I/O error occurs while reading the Excel file.
     */
    public boolean executeDataDrivenAPIs(String fileName, String sheetName) throws IOException {
        // Load data from data-driven sheet
        List<HashMap<String, String>> ddData = ExcelDataDriven.loadDSheetData(fileName, sheetName);

        // Read all keys from hashmap
        HashMap<String, String> anyRow = ddData.get(0);
        Object[] keys = anyRow.keySet().toArray();

        // Loop and execute each request from data-driven sheet
        for (HashMap<String, String> eachRequest : ddData) {
            String baseUrl = "";
            String basePath = "";
            String pathParameter = "";
            Map<String, String> pathParameterObject = null;
            String headers = "";
            Map<String, String> headerObject = null;
            String method = "";
            String requestBody = "";
            String statusCode = "";

            for (int key = 0; key < keys.length; key++) {
                String keyName = String.valueOf(keys[key]);

                switch (keyName.toLowerCase()) {
                    case "baseurl":
                        baseUrl = eachRequest.get(keyName);
                        break;
                    case "basepath":
                        basePath = eachRequest.get(keyName);
                        break;
                    case "pathparameter":
                        pathParameter = eachRequest.get(keyName);
                        pathParameterObject = JsonUtils.parseJsonObject(pathParameter);
                        break;
                    case "headers":
                        headers = eachRequest.get(keyName);
                        headerObject = JsonUtils.parseJsonObject(headers);
                        break;
                    case "method":
                        method = eachRequest.get(keyName);
                        break;
                    case "requestbody":
                        requestBody = eachRequest.get(keyName);
                        break;
                    case "statuscode":
                        statusCode = eachRequest.get(keyName);
                        break;
                }
            }

            // Print request details for debugging
            //System.out.println("baseUrl: " + baseUrl + "\nbasePath: " + basePath + "\npath" + pathParameter + "\n" + headerObject + "\nRequest: " + requestBody);

            try {
                // Perform API request based on the method obtained
                switch (method.toUpperCase()) {
                    case "POST":
                        response = RestUtils.performPost(baseUrl, basePath, requestBody, headerObject);
                        break;
                    case "PUT":
                        response = RestUtils.performPut(baseUrl, basePath, requestBody, headerObject);
                        break;
                    case "GET":
                        response = RestUtils.performGet(baseUrl, basePath, headerObject);
                        break;
                    case "PATCH":
                        //RestUtils.performPatch(baseUrl, basePath, requestBody, headerObject);
                        break;
                    case "DELETE":
                        //RestUtils.performDelete(baseUrl, basePath, headerObject);
                        break;
                    default:
                        System.out.println("Invalid method: " + method);
                        break;
                }
                /*// check the response status code here and compare it with expected statusCode
                String actualStatusCode = String.valueOf(response.getStatusCode());
                if (actualStatusCode.equals(statusCode)) {
                    // Update the Excel sheet with "PASS" value in the response cell
                    eachRequest.put("pass or fail", "PASS");
                    ExcelDataDriven.writeDataToExcel(fileName, sheetName, eachRequest);
                }else {
                    ExcelDataDriven.writeDataToExcel(fileName, sheetName, eachRequest);

                }*/
            } catch (Exception e) {
                // Handle any exceptions occurred during API request
                System.err.println("Error executing API request: " + e.getMessage());
                e.printStackTrace();
                // Optionally, you can handle the error gracefully or log it to a file
            }
        }
        return false;
    }
}
