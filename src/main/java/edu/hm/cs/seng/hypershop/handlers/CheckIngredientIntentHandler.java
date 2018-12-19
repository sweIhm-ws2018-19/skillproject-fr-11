package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class CheckIngredientIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return ContextStackService.isCurrentContext(input, Constants.CONTEXT_STEPS)
                && input.matches(intentName(Constants.INTENT_CHECK_INGREDIENT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        final ResponseBuilder responseBuilder = input.getResponseBuilder().withShouldEndSession(false);

        final boolean hasBeenChecked = shoppingListService.checkCurrentIngredient(input);

        if (!hasBeenChecked) {
            responseBuilder.withSpeech(SpeechTextConstants.CHECK_EMPTY);
        } else {
            responseBuilder.withSpeech(SpeechTextConstants.CHECK_OK);
        }

        return responseBuilder.build();
    }
}
