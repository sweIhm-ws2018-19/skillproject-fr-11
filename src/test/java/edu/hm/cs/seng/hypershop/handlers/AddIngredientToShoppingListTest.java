package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.handlers.units.HypershopCustomUnits;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static edu.hm.cs.seng.hypershop.Constants.SLOT_AMOUNT;
import static edu.hm.cs.seng.hypershop.Constants.SLOT_UNIT;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddIngredientToShoppingListTest {

    private HandlerInput input = Mockito.mock(HandlerInput.class);
    private HandlerInput input2 = Mockito.mock(HandlerInput.class);
    private HandlerInput input3 = Mockito.mock(HandlerInput.class);

    private final AddIngredientIntentHandler handler = new AddIngredientIntentHandler();

    @Before
    public void before() {
        HandlerTestHelper.buildInput("addingredients.json", input);
        HandlerTestHelper.buildInput("addingredients2.json", input2);
        HandlerTestHelper.buildInput("addingredients3.json", input3);
    }

    public static void addTestIngedients(ShoppingListService listService, ModelService modelService) {
        for (int index = 0; index < 10; index++) {
            String ingredientName = "ingredient" + index;
            int amount = 10 + index;
            String unitName = "kg";
            listService.addIngredient(ingredientName, amount, unitName);
        }
        listService.save(modelService);
    }

    @Test
    public void addIngredients() {
        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);

        addTestIngedients(listService, modelService);

        assertEquals(10, listService.getIngredients().size());
        assertEquals(0, listService.getRecipeStrings().size());
    }

    @Test
    public void addFirstIngredient() {
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        Assert.assertTrue(responseString.contains("Brot"));

        final String responseString2 = HandlerTestHelper.getResponseString(handler.handle(input2));
        Assert.assertTrue(responseString2.contains("wasser"));

        final ShoppingListService shoppingListService = new ShoppingListService(new ModelService(input));
        final Set<IngredientAmount> ingredients = shoppingListService.getIngredients();

        Assert.assertEquals(1, ingredients.size());
    }

    @Test
    public void addSecondIngredient() {
        final ModelService modelService = new ModelService(input2);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        shoppingListService.addIngredient("Brot", 10, "kg");
        shoppingListService.save(modelService);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input2));
        Assert.assertTrue(responseString.contains("wasser"));

        shoppingListService.load(modelService);
        final Set<IngredientAmount> ingredients = shoppingListService.getIngredients();
        Assert.assertEquals(2, ingredients.size());
    }

    @Test
    public void testIngredientSlotEmpty() {
        ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots().put(Constants.SLOT_INGREDIENT, null);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(INGREDIENTS_ADD_ERROR, responseString);
    }

    @Test
    public void testAmountNoNumber() {
        invalidInput(SLOT_AMOUNT, INGREDIENTS_ADD_NUMBER_ERROR);
    }

    @Test
    public void testUnitNotFound() {
        invalidInput(SLOT_UNIT, INGREDIENTS_ADD_UNIT_ERROR);
    }

    private void invalidInput(String slotUnit, String ingredientsAddUnitError) {
        Slot slot = Slot.builder().withName(slotUnit).withValue("test").build();
        ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots().put(slotUnit, slot);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(ingredientsAddUnitError, responseString);
    }

    @Test
    public void canHandle() {
        Assert.assertTrue(handler.canHandle(input));
    }

    @Test
    public void testResolution() {
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input3));

        Assert.assertTrue(responseString.contains("müll"));
    }

    @Test
    public void addThreeJamJars() {
        final HandlerInput input = Mockito.mock(HandlerInput.class);
        HandlerTestHelper.buildInput("addingredients-3-jam.json", input);
        final AddIngredientIntentHandler handler = new AddIngredientIntentHandler();

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        final String expected = String.format(SpeechTextConstants.INGREDIENTS_ADD_SUCCESS, "marmelade");
        HandlerTestHelper.compareSSML(expected, responseString);
    }

    @Test
    public void testEmptyUnitSlot() {
        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);
        Slot slot = Slot.builder().withName(SLOT_UNIT).build();
        ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots().put(SLOT_UNIT, slot);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(String.format(SpeechTextConstants.INGREDIENTS_ADD_SUCCESS, "Brot"), responseString);
        shoppingListService.load(modelService);
        final Set<IngredientAmount> ingredients = shoppingListService.getIngredients();
        Assert.assertEquals(1, ingredients.size());
        Assert.assertEquals(HypershopCustomUnits.PIECE.getSymbol(), ingredients.iterator().next().getUnit());
    }

}
