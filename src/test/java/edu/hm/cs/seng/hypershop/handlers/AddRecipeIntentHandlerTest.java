package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ModelService;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AddRecipeIntentHandlerTest {


    @Test
    public void shouldHandleNewRecipe() {
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        HandlerTestHelper.buildInput("addrecipe-normal.json", input);
        final AddRecipeIntentHandler handler = new AddRecipeIntentHandler();

        assertTrue(handler.canHandle(input));
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        final String formatted = String.format(SpeechTextConstants.RECIPE_ADD_SUCCESS, "schnitzel");
        assertTrue(responseString.contains(formatted));

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        assertEquals(1, s.getRecipes().size());
    }

    @Test
    public void shouldWarnAboutInvalidAction() {
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        HandlerTestHelper.buildInput("addrecipe-invalid-intent.json", input);
        final AddRecipeIntentHandler handler = new AddRecipeIntentHandler();

        assertFalse(handler.canHandle(input));
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        assertTrue(responseString.contains(SpeechTextConstants.RECIPE_ADD_INVALID_INTENT));

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        assertEquals(0, s.getRecipes().size());
    }

    @Test
    public void shouldExitWithError() {
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        HandlerTestHelper.buildInput("addrecipe-empty.json", input);
        final AddRecipeIntentHandler handler = new AddRecipeIntentHandler();

        assertTrue(handler.canHandle(input));
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        assertTrue(responseString.contains(SpeechTextConstants.RECIPE_ADD_ERROR));

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        assertEquals(0, s.getRecipes().size());
    }
}