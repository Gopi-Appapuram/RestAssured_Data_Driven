import Utilities.DataDrivenUtil;
import org.testng.annotations.Test;

import java.io.IOException;

public class test {
    @Test
    public static void execteDataDrivenAPIS() throws IOException {
        DataDrivenUtil dataDrivenUtil = new DataDrivenUtil();
        dataDrivenUtil.executeDataDrivenAPIs("RestAssured", "Sheet2");
    }
}
