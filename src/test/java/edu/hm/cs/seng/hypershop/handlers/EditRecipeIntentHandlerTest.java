package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
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
        assertTrue(responseString.contains(String.format(SpeechTextConstants.RECIPE_EDIT_NOT_FOUND, "bananen")));

        assertTrue(ContextStackService.isCurrentContext(input, null));
    }

    @Test
    public void shouldFailWithMissingRecipeName() {
        HandlerTestHelper.buildInput("editrecipe-invalid-intent.json", input);

        final EditRecipeIntentHandler handler = new EditRecipeIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        assertTrue(responseString.contains(SpeechTextConstants.RECIPE_EDIT_ERROR));

        assertTrue(ContextStackService.isCurrentContext(input, null));
    }

    @Test
    public void shouldSucceedWithExistingRecipeName() {
        HandlerTestHelper.buildInput("editrecipe-success.json", input);

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        new ShoppingListService().addRecipe("lebkuchen", s);
        new ModelService(input).save(s);

        final EditRecipeIntentHandler handler = new EditRecipeIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        assertTrue(responseString.contains(String.format(SpeechTextConstants.RECIPE_EDIT_SUCCESS, "lebkuchen")));

        assertTrue(ContextStackService.isCurrentContext(input, Constants.CONTEXT_RECIPE));
    }
}