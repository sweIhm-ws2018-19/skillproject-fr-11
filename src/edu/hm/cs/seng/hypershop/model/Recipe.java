package edu.hm.cs.seng.hypershop.model;

import java.util.Set;

public class Recipe {

    private String name;
    private Set<IngredientAmount> ingredients;

    public Set<IngredientAmount> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<IngredientAmount> ingredients) {
        this.ingredients = ingredients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
