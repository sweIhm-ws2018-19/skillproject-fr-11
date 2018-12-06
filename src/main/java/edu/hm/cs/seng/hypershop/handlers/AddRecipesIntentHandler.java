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
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.*;

public class AddRecipesIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Constants.INTENT_ADD_RECIPES)) && ContextStackService.isCurrentContext(input, null);
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        final IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
        final Map<String, Slot> slots = intentRequest.getIntent().getSlots();

        final Slot recipeSlot = slots.get(Constants.SLOT_RECIPE);
        final Slot amountSlot = slots.get(Constants.SLOT_AMOUNT);

        final ResponseBuilder responseBuilder = input.getResponseBuilder().withShouldEndSession(false);

        if (recipeSlot.getValue() == null) {
            return responseBuilder.withSpeech(RECIPE_ADD_ERROR).withReprompt(RECIPE_ADD_REPROMPT).build();
        }

        final String recipeName = recipeSlot.getValue();

        int recipeAmount = 1;
        if (amountSlot.getValue() != null) {
            final String amount = amountSlot.getValue();
            try {
                recipeAmount = Integer.parseInt(amount);
            } catch (NumberFormatException e) {
                return responseBuilder.withSpeech(RECIPE_ADD_ERROR_AMOUNT).build();
            }
        }

        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        final boolean recipeFound = shoppingListService.addRecipes(recipeName, recipeAmount);
        if (!recipeFound) {
            return responseBuilder.withSpeech(String.format(RECIPE_ADD_NOT_FOUND, recipeName)).build();
        }
        shoppingListService.save(modelService);

        if (recipeAmount > 1) {
            responseBuilder.withSpeech(String.format(RECIPE_ADD_MULTI_SUCCESS, recipeAmount, recipeName));
        } else {
            responseBuilder.withSpeech(String.format(RECIPE_ADD_SUCCESS, recipeName));
        }

        return responseBuilder.build();
    }

}
