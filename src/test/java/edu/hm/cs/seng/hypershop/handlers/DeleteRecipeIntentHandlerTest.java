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

public class DeleteRecipeIntentHandlerTest {

    private final HandlerInput input = Mockito.mock(HandlerInput.class);
    private final DeleteRecipeIntentHandler handler = new DeleteRecipeIntentHandler();

    @Test
    public void shouldHandleRequest() {
        HandlerTestHelper.buildInput("deletereciperequest.json", input);
        assertTrue(handler.canHandle(input));
    }

    @Test
    public void shouldHandleConfirmation() {
        HandlerTestHelper.buildInput("deleterecipeconfirmation.json", input);
        assertTrue(handler.canHandle(input));
    }

    @Test
    public void shouldNotHandleNoIntent() {
        HandlerTestHelper.buildInput("nointent.json", input);
        assertFalse(handler.canHandle(input));
    }

    @Test
    public void shouldNotRequestDeleteNonExistingRecipe() {
        HandlerTestHelper.buildInput("deletereciperequest.json", input);

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(String.format(SpeechTextConstants.RECIPE_DELETE_NOT_FOUND, "tomatensuppe"), responseString);

        assertFalse(SessionStorageService.needsConfirmation(input, Constants.INTENT_DELETE_RECIPE));
    }

    @Test
    public void shouldRequestDeleteExistingRecipe() {
        HandlerTestHelper.buildInput("deletereciperequest.json", input);
        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);
        shoppingListService.createRecipe("tomatensuppe");
        shoppingListService.save(modelService);

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(String.format(SpeechTextConstants.RECIPE_DELETE_CONFIRMATION, "tomatensuppe"), responseString);

        assertTrue(SessionStorageService.needsConfirmation(input, Constants.INTENT_DELETE_RECIPE));
        assertEquals("tomatensuppe", SessionStorageService.getCurrentRecipe(input));
    }

    @Test
    public void shouldNotDeleteNonExistingRecipe() {
        HandlerTestHelper.buildInput("deleterecipeconfirmation.json", input);
        assertTrue(SessionStorageService.needsConfirmation(input, Constants.INTENT_DELETE_RECIPE));

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(String.format(SpeechTextConstants.RECIPE_DELETE_NOT_FOUND, "tomatensuppe"), responseString);

        assertFalse(SessionStorageService.needsConfirmation(input, Constants.INTENT_DELETE_RECIPE));
    }

    @Test
    public void shouldDeleteRecipe() {
        HandlerTestHelper.buildInput("deleterecipeconfirmation.json", input);
        assertTrue(SessionStorageService.needsConfirmation(input, Constants.INTENT_DELETE_RECIPE));

        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);
        shoppingListService.createRecipe("tomatensuppe");
        shoppingListService.save(modelService);

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(String.format(SpeechTextConstants.RECIPE_DELETE_SUCCESS, "tomatensuppe"), responseString);
    }
}