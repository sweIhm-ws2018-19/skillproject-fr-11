package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import tec.units.ri.quantity.Quantities;
import tec.units.ri.unit.MetricPrefix;
import tec.units.ri.unit.Units;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.ParserException;
import javax.measure.quantity.Mass;
import java.util.*;

import static tec.units.ri.unit.Units.GRAM;


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
        shoppingList.getRecipes().add(new Recipe(recipeName));
    }

    public ShoppingList addIngredient(String name, int amount, String unitName, ShoppingList shoppingList) {
        Optional<IngredientAmount> ingredientOptional = Optional.empty();
        for (IngredientAmount ingredient : shoppingList.getIngredients()) {
            if (ingredient.getName().equals(name)) {
                unitConversionService.summmarizeIngredients(ingredient, amount, unitName);
                ingredientOptional = Optional.of(ingredient);
            }
        }

        if (!ingredientOptional.isPresent()) {
            final IngredientAmount ingredientAmount = new IngredientAmount();
            ingredientAmount.setName(name);
            ingredientAmount.setAmount(amount);

            unitConversionService.getUnit(unitName);
            ingredientAmount.setUnit(unitName);
            ingredientOptional = Optional.of(ingredientAmount);
        }

        shoppingList.addIngredient(ingredientOptional.get());
        return shoppingList;
    }


    public List<String> getRecipeStrings() {
        return new ArrayList<>();
    }

    public List<String> getIngredientStrings() {
        return new ArrayList<>();
    }

    public boolean containsRecipe(String recipeName, ShoppingList shoppingList) {
        return shoppingList.getRecipes().stream().anyMatch(r -> r.getName().equalsIgnoreCase(recipeName));
    }
}
