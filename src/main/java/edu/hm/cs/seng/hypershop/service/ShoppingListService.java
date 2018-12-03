package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class ShoppingListService {

    private UnitConversionService unitConversionService = new UnitConversionService();

    public void addIngredient(IngredientAmount ingredientAmount) {
        throw new UnsupportedOperationException("not implemented");

    }

    private Set<IngredientAmount> summarizeIngredients() {
        return new HashSet<>();
    }


    private void removeIngredients(String ingredientAmount) {

    }

    public void removeIngredient(IngredientAmount ingredient) {

    }


    public void addRecipe(String recipeName) {

    }

    public boolean removeIngredient(String ingredient, ShoppingList shoppingList) {
        return removeIngredient(
                shoppingList.getIngredients(ingredient), shoppingList);
    }

    private boolean removeIngredient(Iterable<IngredientAmount> ingredients, ShoppingList shoppingList) {
        boolean result = false;
        for (IngredientAmount ingredient : ingredients) {
            result |= shoppingList.removeIngredient(ingredient);
        }
        return result;
    }


    public void removeRecipe(String recipeName, ShoppingList shoppingList) {
        throw new UnsupportedOperationException("not implemented");
    }

    private void removeRecipe(Recipe recipe) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addRecipe(String recipeName, ShoppingList shoppingList) {
        shoppingList.getRecipes().put(new Recipe(recipeName), 0);
    }

    public void addRecipe(Recipe recipe, ShoppingList shoppingList) {
        shoppingList.getRecipes().put(recipe, 0);
    }

    public ShoppingList addIngredient(String name, int amount, String unitName, ShoppingList shoppingList) {
        boolean matchingIngredient = (shoppingList.getIngredients().stream().filter(ingredientAmount -> ingredientAmount.getName().equals(name))
                .map(ingredientAmount ->
                        unitConversionService.summmarizeIngredients(ingredientAmount, amount, unitName))
                .count()) > 0;

        if (!matchingIngredient) {
            IngredientAmount ingredientAmount = createIngredient(name, amount, unitName);
            shoppingList.addIngredient(ingredientAmount);
        }


        return shoppingList;
    }

    public Recipe getRecipe(ShoppingList shoppingList, String nameSearchString) {
        List<Recipe> recipes = shoppingList.getRecipes().keySet().stream().filter(recipe -> recipe.getName().equals(nameSearchString))
                .collect(Collectors.toList());
        return recipes.size() == 1 ? recipes.get(0) : null;
    }


    public Recipe addIngredientRecipe(String name, int amount, String unitName, ShoppingList shoppingList,
                                      String recipeName) {
        Recipe recipe = getRecipe(shoppingList, recipeName);
        if (recipe != null) {
            boolean matchingIngredient = (recipe.getIngredients().stream().filter(ingredientAmount -> ingredientAmount.getName().equals(name))
                    .map(ingredientAmount ->
                            unitConversionService.summmarizeIngredients(ingredientAmount, amount, unitName))
                    .count()) > 0;
            if (!matchingIngredient) {
                IngredientAmount ingredientAmount = createIngredient(name, amount, unitName);
                recipe.addIngredient(ingredientAmount);
            }
        }
        return recipe;
    }

    public IngredientAmount createIngredient(String name, int amount, String unitName) {
        final IngredientAmount ingredientAmount = new IngredientAmount();
        ingredientAmount.setName(name);
        ingredientAmount.setAmount(amount);

        unitConversionService.getUnit(unitName);
        ingredientAmount.setUnit(unitName);
        return ingredientAmount;
    }


    public List<String> getRecipeStrings() {
        return new ArrayList<>();
    }

    public List<String> getIngredientStrings() {
        return new ArrayList<>();
    }

    public boolean containsRecipe(String recipeName, ShoppingList shoppingList) {
        return shoppingList.getRecipes().keySet().stream().anyMatch(r -> r.getName().equalsIgnoreCase(recipeName));
    }
}
