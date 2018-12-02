package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ModelService;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AddRecipeIntentHandlerTest {


    @Test
    public void shouldHandleNewRecipe() {
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        HandlerTestHelper.buildInput("addrecipe-normal.json", input);
        final AddRecipeIntentHandler handler = new AddRecipeIntentHandler();

        assertTrue(handler.canHandle(input));
        final Optional<Response> response = handler.handle(input);
        final SsmlOutputSpeech speech = (SsmlOutputSpeech) response.get().getOutputSpeech();
        final String formatted = String.format(SpeechTextConstants.RECIPE_ADD_SUCCESS, "schnitzel");
        assertTrue(speech.getSsml().contains(formatted));

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        assertEquals(1, s.getRecipes().size());
    }

    @Test
    public void shouldWarnAboutInvalidAction() {
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        HandlerTestHelper.buildInput("addrecipe-invalid-intent.json", input);
        final AddRecipeIntentHandler handler = new AddRecipeIntentHandler();

        assertTrue(handler.canHandle(input));
        final Optional<Response> response = handler.handle(input);
        final SsmlOutputSpeech speech = (SsmlOutputSpeech) response.get().getOutputSpeech();
        assertTrue(speech.getSsml().contains(SpeechTextConstants.RECIPE_ADD_INVALID_INTENT));

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        assertEquals(0, s.getRecipes().size());
    }

    @Test
    public void shouldExitWithError() {
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        HandlerTestHelper.buildInput("addrecipe-empty.json", input);
        final AddRecipeIntentHandler handler = new AddRecipeIntentHandler();

        assertTrue(handler.canHandle(input));
        final Optional<Response> response = handler.handle(input);
        final SsmlOutputSpeech speech = (SsmlOutputSpeech) response.get().getOutputSpeech();
        assertTrue(speech.getSsml().contains(SpeechTextConstants.RECIPE_ADD_ERROR));

        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        assertEquals(0, s.getRecipes().size());
    }
}