package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EditRecipeIntentHandlerTest {
    private HandlerInput input = Mockito.mock(HandlerInput.class);

    @Test
    public void shouldNotHandleInContext() {
        HandlerTestHelper.buildInput("editrecipe-success.json", input);
        ContextStackService.pushContext(input, "foobar");

        final EditRecipeIntentHandler handler = new EditRecipeIntentHandler();
        assertFalse(handler.canHandle(input));
    }

    @Test
    public void shouldNotEditNotExistingRecipe() {
        HandlerTestHelper.buildInput("editrecipe-nonexisting.json", input);

        final EditRecipeIntentHandler handler = new EditRecipeIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(String.format(SpeechTextConstants.RECIPE_EDIT_NOT_FOUND, "bananen"), responseString);

        assertTrue(ContextStackService.isCurrentContext(input, null));
    }

    @Test
    public void shouldFailWithMissingRecipeName() {
        HandlerTestHelper.buildInput("editrecipe-invalid-intent.json", input);

        final EditRecipeIntentHandler handler = new EditRecipeIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(SpeechTextConstants.RECIPE_EDIT_ERROR, responseString);


        assertTrue(ContextStackService.isCurrentContext(input, null));
    }

    @Test
    public void shouldSucceedWithExistingRecipeName() {
        HandlerTestHelper.buildInput("editrecipe-success.json", input);

        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        shoppingListService.createRecipe("lebkuchen");
        shoppingListService.save(modelService);

        final EditRecipeIntentHandler handler = new EditRecipeIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(String.format(SpeechTextConstants.RECIPE_EDIT_SUCCESS, "lebkuchen"), responseString);

        assertTrue(ContextStackService.isCurrentContext(input, Constants.CONTEXT_RECIPE));
    }
}