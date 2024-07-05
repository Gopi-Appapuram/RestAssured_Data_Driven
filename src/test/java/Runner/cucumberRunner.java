package Runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;


@CucumberOptions(
        features = "src/test/resources/Features",
        glue = {"StepDefinitions"},
        monochrome = true,
        plugin = {
                "pretty", "html:Reports/cucumber-reports.html",
                "pretty", "json:Reports/cucumber-reports.json"
                }
    )
public class cucumberRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios(){
        return super.scenarios();
    }
}
