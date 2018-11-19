package edu.hm.cs.seng.hypershop.model;

import org.junit.Test;

import static org.junit.Assert.assertNull;

public class ShoppingListTest {
    @Test
    public void listIsEmptyOnStart() {
        final ShoppingList shoppingList = new ShoppingList();
        assertNull(shoppingList.getIngredients());
        assertNull(shoppingList.getRecipes());
    }
}