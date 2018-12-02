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

    public void setIngredients(Set<IngredientAmount> ingredients) {
        this.ingredients = ingredients;
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
        if (o == null || getClass() != o.getClass()) return false;
        Recipe that = (Recipe) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(ingredients, that.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, ingredients);
    }
}
