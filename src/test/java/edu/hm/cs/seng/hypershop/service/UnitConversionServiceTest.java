package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.handlers.HandlerTestHelper;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class UnitConversionServiceTest {

    private final HandlerInput input = Mockito.mock(HandlerInput.class);
    private ShoppingListService listService;
    private UnitConversionService unitConversionService = new UnitConversionService();

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
        Assert.assertEquals(2, listService.getIngredients().size());
        List<IngredientAmount> ingredients = new ArrayList<>(listService.getIngredients());

        Assert.assertEquals(200, ingredients.get(1).getAmount(), 0.001);
        Assert.assertEquals(1.0, ingredients.get(0).getAmount(), 0.001);
        Assert.assertEquals("ml", ingredients.get(1).getUnit());
        Assert.assertEquals("glas", ingredients.get(0).getUnit());
    }

    @Test
    public void summarizeIngredientsTest() {

        String ingredientName = "ingredient";
        int amount = 1;
        String unitName = "glas";
        IngredientAmount ingredientAmount1 = new IngredientAmount();
        ingredientAmount1.setName(ingredientName);
        ingredientAmount1.setAmount(amount);
        ingredientAmount1.setUnit(unitName);

        IngredientAmount newIngredientAmount =
                unitConversionService.summarizeIngredients(ingredientAmount1, amount, "kg");
        Assert.assertEquals(amount, newIngredientAmount.getAmount(), 0.001);
        newIngredientAmount =
                unitConversionService.summarizeIngredients(ingredientAmount1, amount, unitName);
        Assert.assertEquals(2.0, newIngredientAmount.getAmount(), 0.001);
    }

    @Test
    public void canSummarize() {
        String normalUnit1 = "kg";
        String normalUnit2 = "g";
        String customUnit1 = "glas";
        String customUnit2 = "piece";
        Assert.assertTrue(unitConversionService.canSummarize(normalUnit1, normalUnit2));
        Assert.assertTrue(unitConversionService.canSummarize(customUnit1, customUnit1));
        Assert.assertFalse(unitConversionService.canSummarize(normalUnit1, customUnit1));
        Assert.assertFalse(unitConversionService.canSummarize(customUnit1, customUnit2));
    }


}