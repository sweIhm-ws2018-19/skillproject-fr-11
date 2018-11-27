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
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.*;

public class AddIngredientIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AddIngredientIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        final Request request = input.getRequestEnvelope().getRequest();
        final IntentRequest intentRequest = (IntentRequest) request;
        final Intent intent = intentRequest.getIntent();
        final Map<String, Slot> slots = intent.getSlots();

        final Slot ingredientSlot = slots.get(Constants.SLOT_INGREDIENT);

        final String speechText;

        ResponseBuilder responseBuilder = input.getResponseBuilder();

        if (ingredientSlot != null) {
            final String ingredient = ingredientSlot.getValue();

            final AttributesManager attributesManager = input.getAttributesManager();
            final Map<String, Object> pam = attributesManager.getPersistentAttributes();

            final ShoppingList shoppingList = ShoppingList.fromBinary(pam.get(Constants.KEY_SHOPPING_LIST));
            final IngredientAmount ingredientAmount = new IngredientAmount();
            ingredientAmount.setName(ingredient);
            shoppingList.addIngredient(ingredientAmount);
            pam.put(Constants.KEY_SHOPPING_LIST, shoppingList.toBinary());
            attributesManager.setPersistentAttributes(pam);
            attributesManager.savePersistentAttributes();


            speechText = String.format(INGREDIENTS_ADD_SUCCESS, ingredient);

        } else {
            speechText = INGREDIENTS_ADD_ERROR;
            responseBuilder.withShouldEndSession(false)
                    .withReprompt(INGREDIENTS_ADD_REPROMPT);
        }

        responseBuilder.withSimpleCard("HypershopSession", speechText)
                .withSpeech(speechText)
                .withShouldEndSession(false);

        return responseBuilder.build();
    }

}
