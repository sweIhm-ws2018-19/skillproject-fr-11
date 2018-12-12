package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class HelpIntentHandlerTest {

    private final HandlerInput input = mock(HandlerInput.class);
    private final HelpIntentHandler handler = new HelpIntentHandler();

    @Test
    public void handleNormal() {
        HandlerTestHelper.buildInput("help.json", input);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(SpeechTextConstants.HELP_TEXT, responseString);
    }

    @Test
    public void handleContextRecipe() {
        HandlerTestHelper.buildInput("help.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_RECIPE);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(SpeechTextConstants.HELP_CONTEXT_RECIPE_TEXT, responseString);
    }

    @Test
    public void handleIngredients() {
        HandlerTestHelper.buildInput("helpingredients.json", input);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(SpeechTextConstants.HELP_INGREDIENTS_TEXT, responseString);
    }

    @Test
    public void handleRecipes() {
        HandlerTestHelper.buildInput("helprecipes.json", input);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(SpeechTextConstants.HELP_RECIPES_TEXT, responseString);
    }

    @Test
    public void canHandleHelp() {
        HandlerTestHelper.buildInput("help.json", input);
        Assert.assertTrue(handler.canHandle(input));
    }

    @Test
    public void canHandleHelpContextRecipe() {
        HandlerTestHelper.buildInput("help.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_RECIPE);
        Assert.assertTrue(handler.canHandle(input));
    }

    @Test
    public void canHandleHelpIngredients() {
        HandlerTestHelper.buildInput("helpingredients.json", input);
        Assert.assertTrue(handler.canHandle(input));
    }

    @Test
    public void canHandleHelpRecipes() {
        HandlerTestHelper.buildInput("helprecipes.json", input);
        Assert.assertTrue(handler.canHandle(input));
    }
}