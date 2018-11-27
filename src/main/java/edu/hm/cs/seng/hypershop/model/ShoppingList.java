package edu.hm.cs.seng.hypershop.model;

import java.util.*;

public class ShoppingList {

    private final Set<IngredientAmount> ingredients = new HashSet<>();
    private List<Recipe> recipes;

    public ShoppingList(){

    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
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
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingList that = (ShoppingList) o;
        return Objects.equals(ingredients, that.ingredients) &&
                Objects.equals(recipes, that.recipes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredients, recipes);
    }
}
