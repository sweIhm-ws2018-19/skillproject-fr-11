package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static edu.hm.cs.seng.hypershop.SpeechTextConstants.RECIPE_ADD_MULTI_SUCCESS;
import static org.junit.Assert.*;

public class AddRecipesIntentHandlerTest {
    private HandlerInput input = Mockito.mock(HandlerInput.class);

    private final AddRecipesIntentHandler handler = new AddRecipesIntentHandler();

    @Test
    public void addOneGingerbread() {
        HandlerTestHelper.buildInput("addrecipes-ginger-one.json", input);
        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);

        listService.createRecipe("lebkuchen");
        listService.save(modelService);

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(String.format(SpeechTextConstants.RECIPE_ADD_SUCCESS, "lebkuchen"), responseString);

        listService.load(modelService);
        assertEquals(new ArrayList<>(Collections.singletonList("lebkuchen")), listService.getAddedRecipeStrings());

        final ArrayList<Map.Entry<String, Integer>> addedRecipes = new ArrayList<>();
        addedRecipes.add(new AbstractMap.SimpleEntry<>("lebkuchen", 1));

        assertEquals(addedRecipes, listService.getAddedRecipesWithAmount());
    }

    @Test
    public void addTenGingerbreads() {
        HandlerTestHelper.buildInput("addrecipes-ginger-ten.json", input);
        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);

        listService.createRecipe("lebkuchen");
        listService.save(modelService);

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(String.format(RECIPE_ADD_MULTI_SUCCESS, 10, "lebkuchen"), responseString);

        listService.load(modelService);
        assertEquals(new ArrayList<>(Collections.singletonList("lebkuchen")), listService.getAddedRecipeStrings());

        final ArrayList<Map.Entry<String, Integer>> addedRecipes = new ArrayList<>();
        addedRecipes.add(new AbstractMap.SimpleEntry<>("lebkuchen", 10));

        assertEquals(addedRecipes, listService.getAddedRecipesWithAmount());
    }

    @Test
    public void addTenGingerbreadsWithOthersExisting() {
        HandlerTestHelper.buildInput("addrecipes-ginger-ten.json", input);
        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);

        listService.createRecipe("lebkuchen");
        listService.createRecipe("döner");
        listService.addRecipes("lebkuchen", 5);
        listService.save(modelService);

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(String.format(RECIPE_ADD_MULTI_SUCCESS, 10, "lebkuchen"), responseString);

        listService.load(modelService);
        assertEquals(new ArrayList<>(Collections.singletonList("lebkuchen")), listService.getAddedRecipeStrings());

        final ArrayList<Map.Entry<String, Integer>> addedRecipes = new ArrayList<>();
        addedRecipes.add(new AbstractMap.SimpleEntry<>("lebkuchen", 15));

        assertEquals(addedRecipes, listService.getAddedRecipesWithAmount());
    }

    @Test
    public void shouldNotHandleInvalidIntent() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);
        assertFalse(handler.canHandle(input));
    }

    @Test
    public void shouldRespondWithErrorOnInvalidAmount() {
        HandlerTestHelper.buildInput("addrecipes-ginger-invalid-amount.json", input);
        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);

        listService.createRecipe("lebkuchen");
        listService.save(modelService);

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(SpeechTextConstants.RECIPE_ADD_ERROR_AMOUNT, responseString);

        listService.load(modelService);
        assertEquals(new ArrayList<>(), listService.getAddedRecipeStrings());
        assertEquals(new ArrayList<>(), listService.getAddedRecipesWithAmount());
    }

    @Test
    public void shouldRespondWithErrorOnNonexistentRecipe() {
        HandlerTestHelper.buildInput("addrecipes-ginger-one.json", input);

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(String.format(SpeechTextConstants.RECIPE_ADD_NOT_FOUND, "lebkuchen"), responseString);

        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);
        assertEquals(new ArrayList<>(), listService.getAddedRecipeStrings());
        assertEquals(new ArrayList<>(), listService.getAddedRecipesWithAmount());
    }

    @Test
    public void shouldRespondWithErrorOnNoRecipe() {
        HandlerTestHelper.buildInput("addrecipes-ginger-no-recipe.json", input);

        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(SpeechTextConstants.RECIPE_ADD_ERROR, responseString);

        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);
        assertEquals(new ArrayList<>(), listService.getAddedRecipeStrings());
        assertEquals(new ArrayList<>(), listService.getAddedRecipesWithAmount());
    }
}