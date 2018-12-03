package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ModelService;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateRecipeIntentHandlerTest {


    @Test
    public void shouldHandleNewRecipe() {
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        HandlerTestHelper.buildInput("createrecipe-normal.json", input);
        final CreateRecipeIntentHandler handler = new CreateRecipeIntentHandler();

        assertTrue(handler.canHandle(input));
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        final String formatted = String.format(SpeechTextConstants.RECIPE_CREATE_SUCCESS, "schnitzel");
        assertTrue(responseString.contains(formatted));

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        assertEquals(1, s.getRecipes().size());
    }

    @Test
    public void shouldWarnAboutInvalidAction() {
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        HandlerTestHelper.buildInput("createrecipe-invalid-intent.json", input);
        final CreateRecipeIntentHandler handler = new CreateRecipeIntentHandler();

        assertTrue(handler.canHandle(input));
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        assertTrue(responseString.contains(SpeechTextConstants.RECIPE_CREATE_INVALID_INTENT));

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        assertEquals(0, s.getRecipes().size());
    }

    @Test
    public void shouldExitWithError() {
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        HandlerTestHelper.buildInput("createrecipe-empty.json", input);
        final CreateRecipeIntentHandler handler = new CreateRecipeIntentHandler();

        assertTrue(handler.canHandle(input));
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        assertTrue(responseString.contains(SpeechTextConstants.RECIPE_CREATE_ERROR));

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        assertEquals(0, s.getRecipes().size());
    }
}