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
import com.amazon.ask.model.*;
import com.amazon.ask.model.slu.entityresolution.Resolution;
import com.amazon.ask.model.slu.entityresolution.ValueWrapper;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;

import javax.measure.format.ParserException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.*;

public class AddIngredientIntentHandler implements RequestHandler {


    private ShoppingListService shoppingListService = new ShoppingListService();

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Constants.INTENT_ADD_INGREDIENT)) && ContextStackService.isCurrentContext(input, null);
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        final Request request = input.getRequestEnvelope().getRequest();
        final IntentRequest intentRequest = (IntentRequest) request;
        final Intent intent = intentRequest.getIntent();
        final Map<String, Slot> slots = intent.getSlots();

        final Slot ingredientSlot = slots.get(Constants.SLOT_INGREDIENT);
        final Slot unitSlot = slots.get(Constants.SLOT_UNIT);
        final Slot amountSlot = slots.get(Constants.SLOT_AMOUNT);

        String speechText;

        ResponseBuilder responseBuilder = input.getResponseBuilder();

        if (ingredientSlot != null && unitSlot != null && amountSlot != null) {
            try {

                ModelService modelService = new ModelService(input);
                final ShoppingList shoppingList = (ShoppingList) modelService.get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);

                final String ingredient = ingredientSlot.getValue();
                String unit;
                Optional<List<String>> strings=Optional.empty();
                if (unitSlot.getResolutions() != null) {
                    strings = unitSlot.getResolutions().getResolutionsPerAuthority().stream().findFirst().map(
                            resolution1 -> resolution1.getValues().stream().map(valueWrapper -> valueWrapper.getValue().getName()).collect(Collectors.toList()));
                }
                if (strings.isPresent()) {
                    unit = strings.get().get(0);
                } else {
                    unit = unitSlot.getValue();
                }

                final String amount = amountSlot.getValue();
                int amountNumber = Integer.parseInt(amount);

                ShoppingList newShoppingList = shoppingListService.addIngredient(ingredient, amountNumber, unit, shoppingList);
                modelService.save(newShoppingList);
                speechText = String.format(INGREDIENTS_ADD_SUCCESS, ingredient);
            } catch (NumberFormatException ex) {
                speechText = INGREDIENTS_ADD_NUMBER_ERROR;
                responseBuilder.withShouldEndSession(false).withReprompt(INGREDIENTS_ADD_REPROMPT);
            } catch (ParserException ex) {
                speechText = INGREDIENTS_ADD_UNIT_ERROR;
                responseBuilder.withShouldEndSession(false).withReprompt(INGREDIENTS_ADD_REPROMPT);
            }
        } else {
            speechText = INGREDIENTS_ADD_ERROR;
            responseBuilder.withShouldEndSession(false).withReprompt(INGREDIENTS_ADD_REPROMPT);
        }
        responseBuilder.withSimpleCard("HypershopSession", speechText).withSpeech(speechText).
                withShouldEndSession(false);

        return responseBuilder.build();
    }

}
