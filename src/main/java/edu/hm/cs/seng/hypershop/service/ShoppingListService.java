package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.model.Unit;

import javax.annotation.Resource;
import java.util.*;


public class ShoppingListService {


    private UnitConversionService unitConversionService = new UnitConversionService();

    public void addIngredient(IngredientAmount ingredientAmount){

    }

    private Set<IngredientAmount> summarizeIngredients(){
        return new HashSet<>();
    }

    private void removeIngredients(String ingredientAmount){

    }

    public void removeIngredient(IngredientAmount ingredient){

    }

    public void removeRecipe(String recipeName){

    }

    private void removeRecipe(Recipe recipe){

    }

    public void addRecipe(String recipeName){

    }

    public ShoppingList addIngredient(String name, int amount, String unitName, ShoppingList shoppingList){

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
