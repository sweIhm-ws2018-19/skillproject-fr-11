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
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.IngredientAmountService;
import edu.hm.cs.seng.hypershop.service.ModelService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ListIngredientsIntentHandler implements RequestHandler {


    private ModelService modelService;

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Constants.INTENT_LIST_INGREDIENTS)) && ContextStackService.isCurrentContext(input, null);
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        modelService = new ModelService(input);

        final ShoppingList shoppingList = (ShoppingList) getModelService().get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        final int listSize = shoppingList.getIngredients().size();
        StringBuilder sb = new StringBuilder(String.format("Du hast %d Zutaten in deiner Einkaufsliste", listSize));
        if (listSize == 0) {
            sb.append(".");
        } else {
            sb.append(": ");
        }
        IngredientAmountService ingredientAmountService = new IngredientAmountService();
        sb = ingredientAmountService.getIngredientsString(shoppingList,sb);

        final String speechText = sb.toString();

        ResponseBuilder responseBuilder = input.getResponseBuilder();

        responseBuilder.withSimpleCard("HypershopSession", speechText)
                .withSpeech(speechText)
                .withShouldEndSession(false);

        return responseBuilder.build();
    }



    public ModelService getModelService() {
        return modelService;
    }

}
