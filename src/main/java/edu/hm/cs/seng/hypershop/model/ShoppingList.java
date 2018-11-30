package edu.hm.cs.seng.hypershop.model;

import java.util.*;

public class ShoppingList {

    private final Set<IngredientAmount> ingredients = new HashSet<>();
    private final List<Recipe> recipes = new ArrayList<>();

    public ShoppingList(){

    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public Set<IngredientAmount> getIngredients() {
        return ingredients;
    }

    public void addIngredient(IngredientAmount ingredient) {
        ingredients.add(ingredient);
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
