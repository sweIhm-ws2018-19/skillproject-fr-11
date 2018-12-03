package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class IngredientAmountService {

    public IngredientAmount mergeIngredients(final IngredientAmount firstIngredient,
                                             final IngredientAmount secondIngredient) {
        return null;
    }

    public StringBuilder getIngredientStringsRecipe(final Recipe recipe, StringBuilder sb){
        return getIngredientsString(recipe.getIngredients(),sb);
    }

    public StringBuilder getIngredientsString(ShoppingList shoppingList, StringBuilder sb) {
        return getIngredientsString(shoppingList.getIngredients(),sb);
    }

    private StringBuilder getIngredientsString(Set<IngredientAmount> ingredientAmountSet, StringBuilder sb){
        for (IngredientAmount ie : ingredientAmountSet) {
            Optional<IngredientAmount> firstIngredient = ingredientAmountSet.stream().findFirst();
            if (firstIngredient.isPresent() && ie != firstIngredient.get()) {
                sb.append(", ");
            }
            addIngredientToStringBuilder(sb, ie);
        }
        return sb;
    }

    private void addIngredientToStringBuilder(final StringBuilder sb, IngredientAmount ia) {
        sb.append(ia.getAmount());
        sb.append(" ");
        sb.append(ia.getUnit().getName());
        sb.append(" ");
        sb.append(ia.getName());
    }

}
