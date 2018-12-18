package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import edu.hm.cs.seng.hypershop.service.SessionStorageService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.YES_FALLBACK;

public class YesIntentFallbackHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.YesIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        SessionStorageService.clearConfirmationRequest(input);

        return input.getResponseBuilder()
                .withShouldEndSession(false)
                .withSpeech(YES_FALLBACK)
                .build();
    }
}
