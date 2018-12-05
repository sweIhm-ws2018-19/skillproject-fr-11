package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.*;

public class RemoveRecipeIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Constants.INTENT_REMOVE_RECIPE)) && ContextStackService.isCurrentContext(input, null);
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        final IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
        final Map<String, Slot> slots = intentRequest.getIntent().getSlots();

        final Slot recipeSlot = slots.get(Constants.SLOT_RECIPE);

        final ResponseBuilder responseBuilder = input.getResponseBuilder();

        if (recipeSlot.getValue() == null) {
            return responseBuilder.withSpeech(RECIPE_EDIT_ERROR).withReprompt(RECIPE_REMOVE_REPROMPT).build();
        }

        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        final String recipeName = recipeSlot.getValue();
        String speechText;
        try {
            if (!shoppingListService.removeRecipes(recipeName, 1)) {
                throw new IllegalArgumentException("recipe does not exist");
            }
            shoppingListService.save(modelService);
            speechText = String.format(RECIPE_REMOVE_SUCCESS, recipeName);
        } catch (IllegalArgumentException ex) {
            speechText = String.format(RECIPE_REMOVE_NOT_FOUND, recipeName);
        }

        responseBuilder.withSimpleCard("HypershopSession", speechText).withSpeech(speechText).withShouldEndSession(false);
        return responseBuilder.build();
    }

}
