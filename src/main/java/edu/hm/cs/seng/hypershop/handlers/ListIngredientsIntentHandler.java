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
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.service.*;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.seng.hypershop.Constants.CONTEXT_RECIPE;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.*;

public class ListIngredientsIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Constants.INTENT_LIST_INGREDIENTS));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        ResponseBuilder responseBuilder = input.getResponseBuilder();
        final int listSize;
        final StringBuilder sb;
        final IngredientAmountService ingredientAmountService = new IngredientAmountService();
        if (ContextStackService.isCurrentContext(input, CONTEXT_RECIPE)) {
            String recipeName = SessionStorageService.getCurrentRecipe(input);
            if (!shoppingListService.containsRecipe(recipeName)) {
                return responseBuilder.withSpeech(String.format(RECIPE_EDIT_NOT_FOUND, recipeName)).build();
            }

            Recipe recipe = shoppingListService.getRecipe(recipeName);
            listSize = recipe.getIngredients().size();
            sb = new StringBuilder(String.format(LIST_RECIPE_INGREDIENTS, listSize));
            finalizeHeader(listSize, sb);
            ingredientAmountService.getIngredientStringsRecipe(recipe, sb);

        } else {
            listSize = shoppingListService.getIngredients().size();
            sb = new StringBuilder(String.format(LIST_INGREDIENTS, listSize));
            finalizeHeader(listSize, sb);
            ingredientAmountService.getIngredientsString(shoppingListService, sb);
        }

        final String speechText = sb.toString();

        responseBuilder.withSimpleCard("HypershopSession", speechText)
                .withSpeech(speechText)
                .withShouldEndSession(false);

        return responseBuilder.build();
    }

    private void finalizeHeader(final int listSize, StringBuilder sb) {
        if (listSize == 0) {
            sb.append(".");
        } else {
            sb.append(": ");
        }
    }
}
