package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.ui.Card;
import com.amazon.ask.model.ui.SimpleCard;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.SessionStorageService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

import static edu.hm.cs.seng.hypershop.Constants.*;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddIngredientRecipeIntentHandlerTest {

    private HandlerInput input = mock(HandlerInput.class);

    private AddIngredientRecipeIntentHandler handler = new AddIngredientRecipeIntentHandler();

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
        invalidInput(SLOT_AMOUNT, INGREDIENTS_ADD_RECIPE_NUMBER_ERROR);
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
    public void canHandle() {
        HandlerTestHelper.buildInput("addingredientsrecipe.json", input);
        Assert.assertTrue(handler.canHandle(input));
    }

    @Test
    public void canNotHandle() {
        HandlerTestHelper.buildInput("addingredientsrecipe3.json", input);
        Assert.assertFalse(handler.canHandle(input));
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
}