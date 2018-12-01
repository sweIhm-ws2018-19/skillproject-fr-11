package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.model.Unit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ShoppingListService {

    private UnitConversionService unitConversionService = new UnitConversionService();

    public void addIngredient(IngredientAmount ingredientAmount){

    }

    private Set<IngredientAmount> summarizeIngredients(){
        return new HashSet<>();
    }

    public boolean removeIngredient(String ingredient, ShoppingList shoppingList) {
        return removeIngredient(
                shoppingList.getIngredients(ingredient), shoppingList);
    }

    private boolean removeIngredient(Iterable<IngredientAmount> ingredients, ShoppingList shoppingList){
        boolean result = false;
        for (IngredientAmount ingredient : ingredients) {
            result |= shoppingList.removeIngredient(ingredient);
        }
        return result;
    }

    public void removeRecipe(String recipeName, ShoppingList shoppingList){

    }

    private void removeRecipe(Recipe recipe){

    }

    public void addRecipe(String recipeName){

    }

    public ShoppingList addIngredient(String name, int amount, String unitName, ShoppingList shoppingList) {

        final IngredientAmount ingredientAmount = new IngredientAmount();
        ingredientAmount.setName(name);
        ingredientAmount.setAmount(amount);

        Unit unit = unitConversionService.getUnit(unitName);

        ingredientAmount.setUnit(unit);

        shoppingList.addIngredient(ingredientAmount);
        return shoppingList;
    }

    public List<String> getRecipeStrings(){
        return new ArrayList<>();
    }

    public List<String> getIngredientStrings(){
        return new ArrayList<>();
    }

}
