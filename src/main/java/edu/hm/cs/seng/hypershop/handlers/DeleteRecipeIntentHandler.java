package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.SessionStorageService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.RECIPE_DELETE_CONFIRMATION;

public class DeleteRecipeIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.YesIntent")) && SessionStorageService.needsConfirmation(input, Constants.INTENT_DELETE_RECIPE)
                || input.matches(intentName(Constants.INTENT_DELETE_RECIPE)) && ContextStackService.isCurrentContext(input, null);
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        final IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
        final Map<String, Slot> slots = intentRequest.getIntent().getSlots();

        final ResponseBuilder responseBuilder = input.getResponseBuilder().withShouldEndSession(false);

        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        if (input.matches(intentName(Constants.INTENT_DELETE_RECIPE))) {
            final Slot recipeSlot = slots.get(Constants.SLOT_RECIPE);
            if (recipeSlot == null) {
                return responseBuilder.withReprompt(SpeechTextConstants.RECIPE_DELETE_REPROMPT).build();
            }

            final String recipe = recipeSlot.getValue();

            if (!shoppingListService.recipeExists(recipe)) {
                return responseBuilder.withSpeech(String.format(SpeechTextConstants.RECIPE_DELETE_NOT_FOUND, recipe)).build();
            }

            SessionStorageService.setCurrentRecipe(input, recipe);
            SessionStorageService.requestConfirmation(input);
            return responseBuilder.withSpeech(String.format(RECIPE_DELETE_CONFIRMATION, recipe)).build();
        }


        SessionStorageService.clearConfirmationRequest(input);
        final String recipe = SessionStorageService.getCurrentRecipe(input);
        if (shoppingListService.deleteRecipe(recipe)) {
            shoppingListService.save(modelService);
            responseBuilder.withSpeech(String.format(SpeechTextConstants.RECIPE_DELETE_SUCCESS, recipe));
        } else {
            responseBuilder.withSpeech(String.format(SpeechTextConstants.RECIPE_DELETE_NOT_FOUND, recipe));
        }

        return responseBuilder.build();
    }
}
