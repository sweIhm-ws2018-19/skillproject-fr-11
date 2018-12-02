package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.model.ShoppingList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnitConversionServiceTest {

    private ShoppingList shoppingList;

    @Before
    public void before() {
        this.shoppingList = new ShoppingList();
    }

    @Test
    public void testAddTwice_SameUnit() {
        ShoppingListService listService = new ShoppingListService();

        for (int index = 0; index < 10; index++) {
            String ingredientName = "ingredient";
            int amount = 10;
            String unitName = "kg";
            shoppingList = listService.addIngredient(ingredientName, amount, unitName, shoppingList);
        }
        Assert.assertEquals(shoppingList.getIngredients().size(), 1);
        Assert.assertEquals( 100.0, shoppingList.getIngredients().iterator().next().getAmount(),0.001);
        Assert.assertEquals("kg", shoppingList.getIngredients().iterator().next().getUnit());
    }

    @Test
    public void testAddTwice_OtherUnit() {
        ShoppingListService listService = new ShoppingListService();
        String ingredientName;
        int amount;
        String unitName;
        for (int index = 0; index < 2; index++) {
            ingredientName = "ingredient";
            amount = 10;
            unitName = "kg";
            shoppingList = listService.addIngredient(ingredientName, amount, unitName, shoppingList);
        }
        ingredientName = "ingredient";
        amount = 10;
        unitName = "g";
        shoppingList = listService.addIngredient(ingredientName, amount, unitName, shoppingList);
        Assert.assertEquals(shoppingList.getIngredients().size(), 1);
        Assert.assertEquals(20.01, shoppingList.getIngredients().iterator().next().getAmount(), 0.001);
        Assert.assertEquals("kg", shoppingList.getIngredients().iterator().next().getUnit());
    }

    @Test
    public void testAddTwice_OtherUnit_Volume() {
        ShoppingListService listService = new ShoppingListService();
        String ingredientName;
        int amount;
        String unitName;
        for (int index = 0; index < 2; index++) {
            ingredientName = "ingredient";
            amount = 100;
            unitName = "ml";
            shoppingList = listService.addIngredient(ingredientName, amount, unitName, shoppingList);
        }
        ingredientName = "ingredient";
        amount = 1;
        unitName = "glass";
        shoppingList = listService.addIngredient(ingredientName, amount, unitName, shoppingList);
        Assert.assertEquals(shoppingList.getIngredients().size(), 1);
        Assert.assertEquals(400.0, shoppingList.getIngredients().iterator().next().getAmount(), 0.001);
        Assert.assertEquals("ml", shoppingList.getIngredients().iterator().next().getUnit());
    }


}