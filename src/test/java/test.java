import Utilities.DataDrivenUtil;

import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        DataDrivenUtil dataDrivenUtil = new DataDrivenUtil();
        // loadDSheetData("src/test/resources/TestData/RestAssured.xlsx", "Sheet2");
        dataDrivenUtil.executeDataDrivenAPIs("src/test/resources/TestData/RestAssured.xlsx", "Sheet2");
    }
}
