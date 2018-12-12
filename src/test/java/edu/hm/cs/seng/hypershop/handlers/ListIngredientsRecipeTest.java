package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static edu.hm.cs.seng.hypershop.Constants.CONTEXT_RECIPE;
import static org.junit.Assert.assertTrue;

public class ListIngredientsRecipeTest {
    private HandlerInput input = Mockito.mock(HandlerInput.class);

    @Before
    public void before() {
        HandlerTestHelper.buildInput("listingredientrecipe.json", input);
    }

    @Test
    public void shouldNotListNotExistingRecipe() {
        ContextStackService.pushContext(input, CONTEXT_RECIPE);

        final ListIngredientsIntentHandler handler = new ListIngredientsIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        assertTrue(responseString.contains(String.format(SpeechTextConstants.RECIPE_LIST_NOT_FOUND, "haus")));
    }


    @Test
    public void shouldSucceedWithExistingRecipeName() {
        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);
        shoppingListService.createRecipe("haus");
        shoppingListService.save(modelService);

        final ListIngredientsIntentHandler handler = new ListIngredientsIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        assertTrue(responseString.contains(String.format(SpeechTextConstants.LIST_RECIPE_INGREDIENTS, 0)));

        assertTrue(ContextStackService.isCurrentContext(input, Constants.CONTEXT_RECIPE));
    }

    @Test
    public void testPiece() {
        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);
        shoppingListService.createRecipe("haus");
        shoppingListService.addIngredientRecipe("pizza", 1, "", "haus");
        shoppingListService.save(modelService);

        final ListIngredientsIntentHandler handler = new ListIngredientsIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        Assert.assertFalse(responseString.contains("piece"));
    }
}