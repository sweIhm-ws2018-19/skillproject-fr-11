/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class HelpIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"))
                || input.matches(intentName(Constants.INTENT_HELP_RECIPES))
                || input.matches(intentName(Constants.INTENT_HELP_INGREDIENTS));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        final ResponseBuilder responseBuilder = input.getResponseBuilder();

        if (input.matches(intentName(Constants.INTENT_HELP_RECIPES))) {
            responseBuilder.withSpeech(SpeechTextConstants.HELP_RECIPES_TEXT);
        } else if (input.matches(intentName(Constants.INTENT_HELP_INGREDIENTS))) {
            responseBuilder.withSpeech(SpeechTextConstants.HELP_INGREDIENTS_TEXT);
        } else {
            if (ContextStackService.isCurrentContext(input, Constants.CONTEXT_RECIPE)) {
                responseBuilder.withSpeech(SpeechTextConstants.HELP_CONTEXT_RECIPE_TEXT);
            } else {
                responseBuilder.withSpeech(SpeechTextConstants.HELP_TEXT);
            }
        }

        return responseBuilder
                .withShouldEndSession(false)
                .build();
    }
}
