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
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.*;

public class AddRecipeIntentHandler implements RequestHandler {


    private ShoppingListService shoppingListService = new ShoppingListService();

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AddRecipeIntent")) && ContextStackService.isCurrentContext(input, null);
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        final Request request = input.getRequestEnvelope().getRequest();
        final IntentRequest intentRequest = (IntentRequest) request;
        final Intent intent = intentRequest.getIntent();
        final Map<String, Slot> slots = intent.getSlots();

        final Slot recipeSlot = slots.get(Constants.SLOT_RECIPE);

        String speechText;

        ResponseBuilder responseBuilder = input.getResponseBuilder();

        if (recipeSlot != null) {
            ModelService modelService = new ModelService(input);
            final ShoppingList shoppingList = (ShoppingList) modelService.get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);

            final String recipeName = recipeSlot.getValue();

            shoppingListService.addRecipe(recipeName, shoppingList);

            modelService.save(shoppingList);
            speechText = String.format(RECIPE_ADD_SUCCESS, recipeName);

            ContextStackService.pushContext(input, Constants.CONTEXT_RECIPE);
        } else {
            speechText = RECIPE_ADD_ERROR;
            responseBuilder.withReprompt(RECIPE_ADD_REPROMPT);
        }
        responseBuilder.withSimpleCard("HypershopSession", speechText).withSpeech(speechText).withShouldEndSession(false);

        return responseBuilder.build();
    }

}
