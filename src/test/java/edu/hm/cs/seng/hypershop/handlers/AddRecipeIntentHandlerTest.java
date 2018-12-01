package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ModelService;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AddRecipeIntentHandlerTest {


    @Test
    public void shouldHandleNewRecipe() {
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        HandlerTestHelper.buildInput("addrecipe-normal.json", input);
        final AddRecipeIntentHandler handler = new AddRecipeIntentHandler();

        assertTrue(handler.canHandle(input));
        handler.handle(input);

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        assertEquals(1, s.getRecipes().size());
    }

    @Test
    public void shuldWarnAboutInvalidAction() {
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        HandlerTestHelper.buildInput("addrecipe-invalid-intent.json", input);
        final AddRecipeIntentHandler handler = new AddRecipeIntentHandler();

        assertTrue(handler.canHandle(input));
        handler.handle(input);

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        assertEquals(0, s.getRecipes().size());
    }

}