package edu.hm.cs.seng.hypershop.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Recipe {

    private String name;
    private Set<IngredientAmount> ingredients = new HashSet<>();

    private Recipe() {
        // private constructor for kryo
    }

    public Recipe(String name) {
        this.name = name;
    }

    public Set<IngredientAmount> getIngredients() {
        return ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(getName(), recipe.getName()) &&
                Objects.equals(getIngredients(), recipe.getIngredients());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ingredients);
    }

    public void addIngredient(IngredientAmount ingredientAmount) {
        ingredients.add(ingredientAmount);
    }
}
