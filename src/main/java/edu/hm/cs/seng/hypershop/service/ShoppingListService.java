package edu.hm.cs.seng.hypershop.service;

import com.sun.javafx.tools.packager.PackagerException;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import tec.units.ri.quantity.QuantityDimension;

import javax.measure.Unit;
import javax.measure.format.ParserException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

    public ShoppingList addIngredient(String name, int amount, String unitName, ShoppingList shoppingList) throws ParserException {

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
