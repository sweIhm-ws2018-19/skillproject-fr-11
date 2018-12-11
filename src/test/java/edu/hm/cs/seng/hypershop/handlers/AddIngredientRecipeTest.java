package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Slot;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.handlers.units.HypershopCustomUnits;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

import static edu.hm.cs.seng.hypershop.Constants.SLOT_AMOUNT;
import static edu.hm.cs.seng.hypershop.Constants.SLOT_UNIT;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddIngredientRecipeTest {

    private HandlerInput input = mock(HandlerInput.class);

    private AddIngredientIntentHandler handler = new AddIngredientIntentHandler();

    @Test
    public void addFirstIngredientToEmptyRecipe() {
        HandlerTestHelper.buildInput("addingredientsrecipe.json", input);

        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);

        listService.createRecipe("Auflauf");
        listService.save(modelService);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        Assert.assertTrue(responseString.contains("Brot"));

        listService.load(modelService);

        Assert.assertEquals(1, listService.getRecipeStrings().size());
        Assert.assertEquals(1, listService.getRecipe("Auflauf").getIngredients().size());
    }

    @Test
    public void addSecondIngredientToRecipe() {
        HandlerTestHelper.buildInput("addingredientsrecipe.json", input);

        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);

        listService.createRecipe("Auflauf");
        listService.addIngredientRecipe("Petersilie", 10, "kg", "Auflauf");
        listService.save(modelService);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        Assert.assertTrue(responseString.contains("Brot"));

        listService.load(modelService);

        Assert.assertEquals(1, listService.getRecipeStrings().size());
        Assert.assertEquals(2, listService.getRecipe("Auflauf").getIngredients().size());
    }

    @Test
    public void testIngredientSlotEmpty() {
        HandlerTestHelper.buildInput("addingredientsrecipe.json", input);

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

    private void invalidInput(String slotUnit, String ingredientsAddError) {
        HandlerTestHelper.buildInput("addingredientsrecipe.json", input);

        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);

        listService.createRecipe("Auflauf");
        listService.save(modelService);

        Slot slot = Slot.builder().withName(slotUnit).withValue("test").build();
        ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots().put(slotUnit, slot);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(ingredientsAddError, responseString);
    }

    @Test
    public void testResolution() {
        HandlerTestHelper.buildInput("addingredientsrecipe2.json", input);

        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);

        listService.createRecipe("Auflauf");
        listService.save(modelService);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        Assert.assertTrue(responseString.contains("wasser"));
    }

    @Test
    public void addThreeJamJars() {
        HandlerTestHelper.buildInput("addingredientsrecipe-3-jam.json", input);

        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);

        listService.createRecipe("Auflauf");
        listService.save(modelService);

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        final String expected = String.format(SpeechTextConstants.INGREDIENTS_ADD_RECIPE_SUCCESS, "marmelade");

        HandlerTestHelper.compareSSML(expected, responseString);
    }

    @Test
    public void testEmptyUnitSlot() {
        HandlerTestHelper.buildInput("addingredientsrecipe.json", input);

        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);

        listService.createRecipe("Auflauf");
        listService.save(modelService);

        Slot slot = Slot.builder().withName(SLOT_UNIT).build();
        ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots().put(SLOT_UNIT, slot);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(String.format(INGREDIENTS_ADD_RECIPE_SUCCESS, "Brot"), responseString);

        listService.load(modelService);
        Recipe recipe = listService.getRecipe("Auflauf");
        Assert.assertNotNull(recipe);
        Assert.assertEquals(1, recipe.getIngredients().size());
        Assert.assertEquals(HypershopCustomUnits.PIECE.getSymbol(), recipe.getIngredients().iterator().next().getUnit());
    }
}