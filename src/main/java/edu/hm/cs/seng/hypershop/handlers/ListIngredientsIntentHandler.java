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

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.ShoppingList;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ListIngredientsIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ListIngredientsIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        final AttributesManager attributesManager = input.getAttributesManager();
        final Map<String, Object> pam = attributesManager.getPersistentAttributes();

        final ShoppingList shoppingList = ShoppingList.fromBinary(pam.get(Constants.KEY_SHOPPING_LIST));

        final StringBuilder sb = new StringBuilder(String.format("Du hast %d Zutaten in deiner Einkaufsliste: ", shoppingList.getIngredients().size()));
        for(IngredientAmount ia : shoppingList.getIngredients()) {
            sb.append(ia.getName());
            sb.append(", ");
        }

        final String speechText = sb.toString();

        ResponseBuilder responseBuilder = input.getResponseBuilder();

        responseBuilder.withSimpleCard("HypershopSession", speechText)
                .withSpeech(speechText)
                .withShouldEndSession(false);

        return responseBuilder.build();
    }

}
