package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ModelServiceTest {

    @Test
    public void listToByteFromByteEquals() {
        final ShoppingList shoppingList = new ShoppingList();

        byte[] shoppingListAsBytes = ModelService.toBinary(shoppingList);

        final ShoppingList actual = (ShoppingList) ModelService.fromBinary(shoppingListAsBytes, ShoppingList.class);

        assertEquals(shoppingList, actual);
    }

    @Test
    public void recipeToByteFromByteEquals() {
        final Recipe recipe = new Recipe("test");

        byte[] recipeAsBytes = ModelService.toBinary(recipe);

        Recipe actual = (Recipe) ModelService.fromBinary(recipeAsBytes, recipe.getClass());

        assertEquals(recipe, actual);

    }
}