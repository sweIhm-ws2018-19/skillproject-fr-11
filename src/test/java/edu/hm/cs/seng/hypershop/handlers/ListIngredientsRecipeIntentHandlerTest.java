package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static edu.hm.cs.seng.hypershop.Constants.CONTEXT_RECIPE;
import static org.junit.Assert.*;

public class ListIngredientsRecipeIntentHandlerTest {
    private HandlerInput input = Mockito.mock(HandlerInput.class);

    @Before
    public void before() {
        HandlerTestHelper.buildInput("listingredientrecipe.json", input);
    }

        @Test
    public void shouldNotHandleInContext() {
        ContextStackService.pushContext(input, "foobar");

        final ListIngredientsIntentHandler handler = new ListIngredientsIntentHandler();
        assertFalse(handler.canHandle(input));
    }

    @Test
    public void shouldNotListNotExistingRecipe() {
        ContextStackService.pushContext(input, CONTEXT_RECIPE);

        final ListIngredientsRecipeIntentHandler handler = new ListIngredientsRecipeIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        System.out.println(responseString);
        assertTrue(responseString.contains(String.format(SpeechTextConstants.RECIPE_LIST_NOT_FOUND, "haus")));
    }


    @Test
    public void shouldSucceedWithExistingRecipeName() {
        ContextStackService.pushContext(input, CONTEXT_RECIPE);

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        new ShoppingListService().addRecipe("haus", s);
        new ModelService(input).save(s);

        final ListIngredientsRecipeIntentHandler handler = new ListIngredientsRecipeIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        System.out.println(responseString);
        assertTrue(responseString.contains(String.format(SpeechTextConstants.LIST_RECIPE_INGREDIENTS, 0)));

        assertTrue(ContextStackService.isCurrentContext(input, Constants.CONTEXT_RECIPE));
    }
}