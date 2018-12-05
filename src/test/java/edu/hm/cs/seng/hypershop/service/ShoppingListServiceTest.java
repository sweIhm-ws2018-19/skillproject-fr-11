package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.model.ShoppingList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShoppingListServiceTest {

    @Test
    public void noExistingRecipes() {
        final ShoppingListService service = new ShoppingListService(new ShoppingList());

        assertEquals(0, service.getRecipeStrings().size());
    }
}