package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Test;
import org.mockito.Mockito;

import static edu.hm.cs.seng.hypershop.SpeechTextConstants.INGREDIENTS_REMOVE_ERROR;
import static org.junit.Assert.*;

public class DeleteRecipeIntentHandlerTest {

    private HandlerInput input = Mockito.mock(HandlerInput.class);

    @Test
    public void testIngredientHandler_WithoutRecipe_ErrorMessage() {
        HandlerTestHelper.buildInput("deleterecipe.json", input);
        final DeleteRecipeIntentHandler handler = new DeleteRecipeIntentHandler();

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(String.format(SpeechTextConstants.RECIPE_DELETE_NOT_FOUND, "tomatensuppe"), responseString);
    }
    @Test
    public void testIngredientHandler_WithRecipe_ErrorMessage() {
        HandlerTestHelper.buildInput("deleterecipe.json", input);
        final DeleteRecipeIntentHandler handler = new DeleteRecipeIntentHandler();

        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);
        shoppingListService.createRecipe("tomatensuppe");
        shoppingListService.save(modelService);

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(String.format(SpeechTextConstants.RECIPE_DELETE_SUCCESS, "tomatensuppe"), responseString);
    }
}