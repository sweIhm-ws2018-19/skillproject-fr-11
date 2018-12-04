package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.handlers.HandlerTestHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UnitConversionServiceTest {

    private final HandlerInput input = Mockito.mock(HandlerInput.class);
    private ShoppingListService listService;

    @Before
    public void before() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);
        listService = new ShoppingListService(new ModelService(input));
    }

    @Test
    public void testAddTwice_SameUnit() {
        for (int index = 0; index < 10; index++) {
            String ingredientName = "ingredient";
            int amount = 10;
            String unitName = "kg";
            listService.addIngredient(ingredientName, amount, unitName);
        }
        Assert.assertEquals(listService.getIngredients().size(), 1);
        Assert.assertEquals(100.0, listService.getIngredients().iterator().next().getAmount(), 0.001);
        Assert.assertEquals("kg", listService.getIngredients().iterator().next().getUnit());
    }

    @Test
    public void testAddTwice_OtherUnit() {
        String ingredientName;
        int amount;
        String unitName;
        for (int index = 0; index < 2; index++) {
            ingredientName = "ingredient";
            amount = 10;
            unitName = "kg";
            listService.addIngredient(ingredientName, amount, unitName);
        }
        ingredientName = "ingredient";
        amount = 10;
        unitName = "g";
        listService.addIngredient(ingredientName, amount, unitName);
        Assert.assertEquals(listService.getIngredients().size(), 1);
        Assert.assertEquals(20.01, listService.getIngredients().iterator().next().getAmount(), 0.001);
        Assert.assertEquals("kg", listService.getIngredients().iterator().next().getUnit());
    }

    @Test
    public void testAddTwice_OtherUnit_Volume() {
        String ingredientName;
        int amount;
        String unitName;
        for (int index = 0; index < 2; index++) {
            ingredientName = "ingredient";
            amount = 100;
            unitName = "ml";
            listService.addIngredient(ingredientName, amount, unitName);
        }
        ingredientName = "ingredient";
        amount = 1;
        unitName = "glas";
        listService.addIngredient(ingredientName, amount, unitName);
        Assert.assertEquals(listService.getIngredients().size(), 1);
        Assert.assertEquals(400.0, listService.getIngredients().iterator().next().getAmount(), 0.001);
        Assert.assertEquals("ml", listService.getIngredients().iterator().next().getUnit());
    }


}