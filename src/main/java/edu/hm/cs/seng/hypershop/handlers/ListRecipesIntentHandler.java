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
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.RECIPES_LIST;

public class ListRecipesIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Constants.INTENT_LIST_RECIPES)) && ContextStackService.isCurrentContext(input, null);
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        final int recipesCount = shoppingListService.getRecipeStrings().size();
        final StringBuilder sb = new StringBuilder(String.format(RECIPES_LIST, recipesCount));
        if (recipesCount == 0) {
            sb.append(".");
        } else {
            sb.append(": ");
        }
        boolean isFirst = true;
        for (String r : shoppingListService.getRecipeStrings()) {
            if (isFirst)
                isFirst = false;
            else
                sb.append(", ");
            sb.append(r);
        }

        final String speechText = sb.toString();

        ResponseBuilder responseBuilder = input.getResponseBuilder();

        responseBuilder.withSimpleCard("HypershopSession", speechText)
                .withSpeech(speechText)
                .withShouldEndSession(false);

        return responseBuilder.build();
    }
}
