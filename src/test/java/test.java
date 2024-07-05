import Utilities.DataDrivenUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.testng.annotations.Test;

import java.io.IOException;

public class test {
    @Test
    public static void execteDataDrivenAPIS() throws IOException, InvalidFormatException {
        DataDrivenUtil dataDrivenUtil = new DataDrivenUtil();
        dataDrivenUtil.executeDataDrivenAPIs("RestAssured", "Sheet2");
    }
}
