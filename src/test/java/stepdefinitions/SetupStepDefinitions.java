package stepdefinitions;


import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

import static com.automation.utils.Phats.getPhats;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;

public class SetupStepDefinitions {
    @Before
    public void setup(){
        OnStage.setTheStage(new OnlineCast());
        theActorCalled("Yoli");
        getPhats();

    }
}
