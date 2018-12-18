package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.seng.hypershop.service.SessionStorageService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.NO_FALLBACK;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.NO_OK;

public class NoIntentFallbackHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.NoIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        final boolean wasConfirmationRequest = SessionStorageService.clearConfirmationRequest(input);

        final ResponseBuilder responseBuilder = input.getResponseBuilder().withShouldEndSession(false);

        if (wasConfirmationRequest) {
            responseBuilder.withSpeech(NO_OK);
        } else {
            responseBuilder.withSpeech(NO_FALLBACK);
        }

        return responseBuilder.build();
    }
}
