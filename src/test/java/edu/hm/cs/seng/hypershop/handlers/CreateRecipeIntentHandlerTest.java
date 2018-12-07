package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateRecipeIntentHandlerTest {

    private final HandlerInput input = Mockito.mock(HandlerInput.class);
    private final CreateRecipeIntentHandler handler = new CreateRecipeIntentHandler();

    @Test
    public void shouldHandleNewRecipe() {
        HandlerTestHelper.buildInput("createrecipe-normal.json", input);

        assertTrue(handler.canHandle(input));
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        final String formatted = String.format(SpeechTextConstants.RECIPE_CREATE_SUCCESS, "schnitzel");
        HandlerTestHelper.compareSSML(formatted, responseString);

        final ShoppingListService shoppingListService = new ShoppingListService(new ModelService(input));
        assertEquals(1, shoppingListService.getRecipeStrings().size());
    }

    @Test
    public void shouldNotCreateExistingRecipe() {
        HandlerTestHelper.buildInput("createrecipe-normal.json", input);

        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);
        shoppingListService.createRecipe("schnitzel");
        shoppingListService.save(modelService);

        assertTrue(handler.canHandle(input));
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        final String formatted = String.format(SpeechTextConstants.RECIPE_CREATE_DUPLICATE, "schnitzel");
        HandlerTestHelper.compareSSML(formatted, responseString);

        shoppingListService.load(modelService);
        assertEquals(1, shoppingListService.getRecipeStrings().size());
    }

    @Test
    public void shouldWarnAboutInvalidAction() {
        HandlerTestHelper.buildInput("createrecipe-invalid-intent.json", input);

        assertTrue(handler.canHandle(input));
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(SpeechTextConstants.RECIPE_CREATE_INVALID_INTENT, responseString);

        final ShoppingListService shoppingListService = new ShoppingListService(new ModelService(input));
        assertEquals(0, shoppingListService.getRecipeStrings().size());
    }

    @Test
    public void shouldExitWithError() {
        HandlerTestHelper.buildInput("createrecipe-empty.json", input);
        final CreateRecipeIntentHandler handler = new CreateRecipeIntentHandler();

        assertTrue(handler.canHandle(input));
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(SpeechTextConstants.RECIPE_CREATE_ERROR, responseString);

        final ShoppingListService shoppingListService = new ShoppingListService(new ModelService(input));
        assertEquals(0, shoppingListService.getRecipeStrings().size());
    }
}