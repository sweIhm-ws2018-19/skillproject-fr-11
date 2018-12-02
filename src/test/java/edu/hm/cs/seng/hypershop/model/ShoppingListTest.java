package edu.hm.cs.seng.hypershop.model;

import edu.hm.cs.seng.hypershop.service.ModelService;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class ShoppingListTest {
    @Test
    public void listIsEmptyOnStart() {
        final ShoppingList shoppingList = new ShoppingList();
        assertEquals(0, shoppingList.getIngredients().size());
        assertEquals(0, shoppingList.getRecipes().size());
    }

    @Test
    public void listToByteFromByteEquals() {
        final ShoppingList shoppingList = new ShoppingList();

        byte[] shoppingListAsBytes = ModelService.toBinary(shoppingList);

        final ShoppingList actual = (ShoppingList) ModelService.fromBinary(shoppingListAsBytes, ShoppingList.class);

        assertEquals(shoppingList, actual);
    }
}