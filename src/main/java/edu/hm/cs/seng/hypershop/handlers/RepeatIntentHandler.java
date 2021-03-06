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

public class RepeatIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return ContextStackService.isCurrentContext(input, Constants.CONTEXT_STEPS)
                && input.matches(intentName(Constants.INTENT_REPEAT));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        final ResponseBuilder responseBuilder = input.getResponseBuilder().withShouldEndSession(false);

        final String currentIngredient = shoppingListService.getCurrentIngredient(input);

        if (currentIngredient == null) {
            responseBuilder.withSpeech(SpeechTextConstants.REPEAT_EMPTY);
        } else {
            responseBuilder.withSpeech(currentIngredient);
        }

        return responseBuilder.build();
    }
}
