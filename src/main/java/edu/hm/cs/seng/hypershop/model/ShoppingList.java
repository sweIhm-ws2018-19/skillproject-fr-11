package edu.hm.cs.seng.hypershop.model;

import java.util.*;
import java.util.stream.Collectors;

public class ShoppingList {

    private final Set<IngredientAmount> ingredients = new HashSet<>();
    private final Map<Recipe, Integer> recipes = new HashMap<>();

    public ShoppingList() {
        // nothing to do
    }

    public Map<Recipe, Integer> getRecipes() {
        return recipes;
    }

    public Set<IngredientAmount> getIngredients() {
        return ingredients;
    }

    public void addIngredient(IngredientAmount ingredient) {
        ingredients.add(ingredient);
    }

    public Set<IngredientAmount> getIngredients(String nameSearchString) {
        return ingredients.stream()
                .filter(ingredientAmount -> ingredientAmount.getName().equalsIgnoreCase(nameSearchString.toLowerCase()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShoppingList)) return false;
        ShoppingList that = (ShoppingList) o;
        return Objects.equals(getIngredients(), that.getIngredients()) &&
                Objects.equals(getRecipes(), that.getRecipes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredients, recipes);
    }
}
