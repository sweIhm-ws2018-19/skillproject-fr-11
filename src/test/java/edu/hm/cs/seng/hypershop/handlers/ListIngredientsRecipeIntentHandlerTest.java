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

import static edu.hm.cs.seng.hypershop.Constants.CONTEXT_RECIPE;
import static org.junit.Assert.*;

public class ListIngredientsRecipeIntentHandlerTest {
    private HandlerInput input = Mockito.mock(HandlerInput.class);

    @Test
    public void shouldNotHandleInContext() {
        HandlerTestHelper.buildInput("editrecipe-success.json", input);
        ContextStackService.pushContext(input, "foobar");

        final ListIngredientsIntentHandler handler = new ListIngredientsIntentHandler();
        assertFalse(handler.canHandle(input));
    }

    @Test
    public void shouldNotListNotExistingRecipe() {
        HandlerTestHelper.buildInput("editrecipe-nonexisting.json", input);
        ContextStackService.pushContext(input, CONTEXT_RECIPE);

        final ListIngredientsIntentHandler handler = new ListIngredientsIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        assertTrue(responseString.contains(String.format(SpeechTextConstants.RECIPE_LIST_NOT_FOUND, "bananen")));
    }


    @Test
    public void shouldSucceedWithExistingRecipeName() {
        HandlerTestHelper.buildInput("editrecipe-success.json", input);

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        new ShoppingListService().addRecipe("lebkuchen", s);
        new ModelService(input).save(s);

        final ListIngredientsIntentHandler handler = new ListIngredientsIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        assertTrue(responseString.contains(String.format(SpeechTextConstants.LIST_RECIPE_INGREDIENTS, 2)));

        assertTrue(ContextStackService.isCurrentContext(input, Constants.CONTEXT_RECIPE));
    }
}