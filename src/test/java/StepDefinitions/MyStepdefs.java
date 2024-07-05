package StepDefinitions;

import Utilities.DataDrivenUtil;
import io.cucumber.java.en.Given;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

public class MyStepdefs {
    DataDrivenUtil dataDrivenUtil = new DataDrivenUtil();

    @Given("I have {string} environment")
    public void iHaveEnvironment(String Env) {
        dataDrivenUtil.loadEnvironmentData("Dev");
    }

    @Given("I have data in {string} excel file and data is in {string}")
    public void iHaveDataInExcelFileAndDataIsIn(String fileName, String sheetName) throws IOException, InvalidFormatException {
        dataDrivenUtil.executeDataDrivenAPIs(fileName, sheetName);
    }
}
