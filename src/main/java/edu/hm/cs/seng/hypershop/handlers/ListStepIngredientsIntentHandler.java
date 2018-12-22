package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.SessionStorageService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ListStepIngredientsIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return ContextStackService.isCurrentContext(input, null)
                && input.matches(intentName(Constants.INTENT_LIST_STEP_INGREDIENTS));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        final ResponseBuilder responseBuilder = input.getResponseBuilder().withShouldEndSession(false);

        SessionStorageService.resetIngredientOutput(input);
        final String currentIngredient = shoppingListService.getCurrentIngredient(input);

        if (currentIngredient == null) {
            responseBuilder.withSpeech(SpeechTextConstants.LIST_STEP_INGREDIENTS_EMPTY);
        } else {
            ContextStackService.pushContext(input, Constants.CONTEXT_STEPS);
            responseBuilder.withSpeech(currentIngredient);
        }

        return responseBuilder.build();
    }
}
