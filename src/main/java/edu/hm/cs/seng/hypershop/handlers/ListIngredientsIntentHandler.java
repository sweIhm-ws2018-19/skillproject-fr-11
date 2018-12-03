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
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class ListIngredientsIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Constants.INTENT_LIST_INGREDIENTS)) && ContextStackService.isCurrentContext(input, null);
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        final int listSize = shoppingListService.getIngredients().size();
        final StringBuilder sb = new StringBuilder(String.format("Du hast %d Zutaten in deiner Einkaufsliste", listSize));
        if (listSize == 0) {
            sb.append(".");
        } else {
            sb.append(": ");
        }
        for (IngredientAmount ie : shoppingListService.getIngredients()) {
            Optional<IngredientAmount> firstIngredient = shoppingListService.getIngredients().stream().findFirst();
            if (firstIngredient.isPresent() && ie != firstIngredient.get()) {
                sb.append(", ");
            }
            addIngredientToStringBuilder(sb, ie);
        }

        final String speechText = sb.toString();

        ResponseBuilder responseBuilder = input.getResponseBuilder();

        responseBuilder.withSimpleCard("HypershopSession", speechText)
                .withSpeech(speechText)
                .withShouldEndSession(false);

        return responseBuilder.build();
    }

    private void addIngredientToStringBuilder(final StringBuilder sb, IngredientAmount ia) {
        sb.append("<say-as interpret-as=\"number\">");
        sb.append(fmt(ia.getAmount()));
        sb.append("</say-as>");
        sb.append(" ");
        sb.append("<say-as interpret-as=\"unit\">");
        sb.append(ia.getUnit());
        sb.append("</say-as>");
        sb.append(" ");
        sb.append(ia.getName());
    }

    private static String fmt(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }
}
