package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class DeleteRecipeIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Constants.INTENT_DELETE_RECIPE)) && ContextStackService.isCurrentContext(input, null);
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        final Request request = input.getRequestEnvelope().getRequest();
        final IntentRequest intentRequest = (IntentRequest) request;
        final Intent intent = intentRequest.getIntent();
        final Map<String, Slot> slots = intent.getSlots();

        final Slot recipeSlot = slots.get(Constants.SLOT_RECIPE);

        final ResponseBuilder responseBuilder = input.getResponseBuilder();

        final String speechText;
        if(recipeSlot != null) {
            final ModelService modelService = new ModelService(input);
            final String recipe = recipeSlot.getValue();
            final ShoppingListService shoppingListService = new ShoppingListService(modelService);
            if(shoppingListService.deleteRecipe(recipe)) {
                shoppingListService.save(modelService);
                speechText = String.format(SpeechTextConstants.RECIPE_DELETE_SUCCESS, recipe);
            }
            else {
                speechText = String.format(SpeechTextConstants.RECIPE_DELETE_NOT_FOUND, recipe);
            }
        }
        else {
            return responseBuilder.withReprompt(SpeechTextConstants.RECIPE_DELETE_REPROMPT).build();
        }
        return responseBuilder.withSimpleCard("HypershopSession", speechText).withSpeech(speechText).withShouldEndSession(false).build();
    }
}
