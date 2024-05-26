package stepdefinitions;

import com.automation.task.ExcelDuplicator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;

public class FillDataStepDefinitions {
    @Given("the user upload the data")
    public void theUserUploadTheData() {
       theActorInTheSpotlight().attemptsTo(
                ExcelDuplicator.excelDuplicator()
        );
    }

    @When("execute the action")
    public void executeTheAction() {

    }

    @Then("fill data on the sheet")
    public void fillDataOnTheSheet() {

    }

}
