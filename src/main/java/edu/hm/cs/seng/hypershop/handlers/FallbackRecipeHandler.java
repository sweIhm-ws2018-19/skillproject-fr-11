package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;

import java.util.Optional;

import static edu.hm.cs.seng.hypershop.SpeechTextConstants.RECIPE_FALLBACK;

public class FallbackRecipeHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return ContextStackService.isCurrentContext(input, Constants.CONTEXT_RECIPE);
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = RECIPE_FALLBACK;
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HypershopSession", speechText)
                .withReprompt(speechText)
                .build();
    }

}
