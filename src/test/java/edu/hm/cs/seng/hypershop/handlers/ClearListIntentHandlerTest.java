package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.SessionStorageService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ClearListIntentHandlerTest {

    private final HandlerInput input = Mockito.mock(HandlerInput.class);
    private final ClearListIntentHandler handler = new ClearListIntentHandler();

    @Test
    public void shouldNotHandleWrongIntent() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);

        assertFalse(handler.canHandle(input));
    }

    @Test
    public void shouldKeepEmptyListEmpty() {
        HandlerTestHelper.buildInput("clearlist.json", input);

        assertTrue(SessionStorageService.needsConfirmation(input, Constants.INTENT_LIST_CLEAR));

        assertTrue(handler.canHandle(input));
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(SpeechTextConstants.LIST_CLEAR_SUCCESS, responseString);
        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        assertEquals(0, shoppingListService.getIngredients().size());
        assertEquals(0, shoppingListService.getAddedRecipeStrings().size());

        assertFalse(SessionStorageService.needsConfirmation(input, Constants.INTENT_LIST_CLEAR));
    }

    @Test
    public void shouldClearList() {
        HandlerTestHelper.buildInput("clearlist.json", input);

        assertTrue(SessionStorageService.needsConfirmation(input, Constants.INTENT_LIST_CLEAR));

        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        shoppingListService.addIngredient("peterschnittlauchzwiebelapfel", 100, "g");
        shoppingListService.createRecipe("lebkuchen");
        shoppingListService.addRecipes("lebkuchen", 10);
        shoppingListService.save(modelService);

        assertTrue(handler.canHandle(input));
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(SpeechTextConstants.LIST_CLEAR_SUCCESS, responseString);

        shoppingListService.load(modelService);

        assertEquals(0, shoppingListService.getIngredients().size());
        assertEquals(1, shoppingListService.getRecipeStrings().size());
        assertEquals(0, shoppingListService.getAddedRecipeStrings().size());

        assertFalse(SessionStorageService.needsConfirmation(input, Constants.INTENT_LIST_CLEAR));
    }

    @Test
    public void requestClear() {
        HandlerTestHelper.buildInput("requestclearlist.json", input);

        assertTrue(handler.canHandle(input));
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(SpeechTextConstants.LIST_CLEAR_CONFIRMATION, responseString);

        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        assertEquals(0, shoppingListService.getIngredients().size());
        assertEquals(0, shoppingListService.getAddedRecipeStrings().size());

        assertTrue(SessionStorageService.needsConfirmation(input, Constants.INTENT_LIST_CLEAR));
    }
}