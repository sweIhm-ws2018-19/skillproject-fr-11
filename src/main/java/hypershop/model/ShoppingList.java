package main.java.hypershop.model;

import java.util.List;
import java.util.Set;

public class ShoppingList {

    private Set<IngredientAmount> ingredients;

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    private List<Recipe> recipes;

    public Set<IngredientAmount> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<IngredientAmount> ingredients) {
        this.ingredients = ingredients;
    }
}
