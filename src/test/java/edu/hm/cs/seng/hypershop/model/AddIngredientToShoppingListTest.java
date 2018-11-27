package edu.hm.cs.seng.hypershop.model;

import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AddIngredientToShoppingListTest {

    private ShoppingList shoppingList;

    private ShoppingListService shoppingListService = new ShoppingListService();

    @Before
    public void listIsEmptyOnStart() {
        this.shoppingList = new ShoppingList();
        assertEquals(0, shoppingList.getIngredients().size());
        assertNull(shoppingList.getRecipes());
    }

    @Test
    public void addIngredient() {
        for(int index=0; index < 10; index++){
            String ingredientName = "ingredient"+index;
            int amount = 10+index;
            String unitName = "kg";
            shoppingList = shoppingListService.addIngredient(ingredientName,amount,unitName,shoppingList);
        }

        assertEquals(10,shoppingList.getIngredients().size());
        assertNull(shoppingList.getRecipes());
    }
}
