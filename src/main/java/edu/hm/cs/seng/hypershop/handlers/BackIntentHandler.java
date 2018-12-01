package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.BACK_OK;

public class BackIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Constants.INTENT_BACK));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ContextStackService.popContext(input);

        String speechText = BACK_OK;
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HypershopSession", speechText)
                .withReprompt(speechText)
                .build();
    }

}
