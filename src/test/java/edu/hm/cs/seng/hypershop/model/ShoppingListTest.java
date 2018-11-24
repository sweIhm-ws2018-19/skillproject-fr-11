package edu.hm.cs.seng.hypershop.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ShoppingListTest {
    @Test
    public void listIsEmptyOnStart() {
        final ShoppingList shoppingList = new ShoppingList();
        assertEquals(0, shoppingList.getIngredients().size());
        assertNull(shoppingList.getRecipes());
    }
}