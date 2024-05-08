package StepDefinitions;

import Utilities.DataDrivenUtil;
import io.cucumber.java.en.Given;

import java.io.IOException;

public class MyStepdefs {
    @Given("I have data in {string} excel file and data is in {string}")
    public void iHaveDataInExcelFileAndDataIsIn(String fileName, String sheetName) throws IOException {
            DataDrivenUtil dataDrivenUtil = new DataDrivenUtil();
            dataDrivenUtil.executeDataDrivenAPIs(fileName, sheetName);
    }
}
