package runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        features = {"src/test/resources/features"},
        glue = {"stepdefinitions"}
)
public class FillData   {
    String inputFilePath = "src\\test\\resources\\input.xlsx";
    String outputFilePath = "src\\test\\resources\\output.xlsx";
}
