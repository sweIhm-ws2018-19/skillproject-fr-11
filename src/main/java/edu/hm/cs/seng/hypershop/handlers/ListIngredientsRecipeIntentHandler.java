package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.*;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static edu.hm.cs.seng.hypershop.Constants.CONTEXT_RECIPE;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.*;

public class ListIngredientsRecipeIntentHandler implements RequestHandler {

    private ShoppingListService shoppingListService = new ShoppingListService();

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(Constants.INTENT_LIST_INGREDIENTS_RECIPE)) && ContextStackService.isCurrentContext(input, CONTEXT_RECIPE);
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        final IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
        final Map<String, Slot> slots = intentRequest.getIntent().getSlots();

        final Slot recipeSlot = slots.get(Constants.SLOT_RECIPE);

        final ResponseBuilder responseBuilder = input.getResponseBuilder();

        if (recipeSlot.getValue() == null) {
            return responseBuilder.withSpeech(RECIPE_LIST_ERROR).withReprompt(RECIPE_LIST_REPROMPT).build();
        }

        final ModelService modelService = new ModelService(input);
        final ShoppingList shoppingList = (ShoppingList) modelService.get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);

        final String recipeName = recipeSlot.getValue();

        if (!shoppingListService.containsRecipe(recipeName, shoppingList)) {
            return responseBuilder.withSpeech(String.format(RECIPE_EDIT_NOT_FOUND, recipeName)).build();
        }

        Recipe recipe = shoppingListService.getRecipe(recipeName, shoppingList);

        int ingredientsSize = recipe.getIngredients().size();
        StringBuilder sb = new StringBuilder(String.format(LIST_RECIPE_INGREDIENTS, ingredientsSize));
        if (ingredientsSize == 0) {
            sb.append(".");
        } else {
            sb.append(": ");
        }

        IngredientAmountService ingredientAmountService = new IngredientAmountService();
        sb = ingredientAmountService.getIngredientStringsRecipe(recipe, sb);

        final String speechText = sb.toString();
        responseBuilder.withSimpleCard("HypershopSession", speechText).withSpeech(speechText).withShouldEndSession(false);

        return responseBuilder.build();
    }

}
