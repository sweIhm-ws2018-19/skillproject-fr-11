package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import edu.hm.cs.seng.hypershop.service.SsmlService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ListIngredientShoppingListTest {

    private final HandlerInput input = mock(HandlerInput.class);
    private final ListIngredientsIntentHandler listIngredientsIntentHandler = new ListIngredientsIntentHandler();

    @Before
    public void before() {
        HandlerTestHelper.buildInput("listingredients.json", input);
    }

    @Test
    public void testEmptyList() {
        final String responseString = HandlerTestHelper.getResponseString(listIngredientsIntentHandler.handle(input));

        HandlerTestHelper.compareSSML("<p>Du hast 0 Zutaten in deiner Einkaufsliste.</p>\n<p>Du hast 0 Rezepte in deiner Einkaufsliste.</p>", responseString);
    }

    @Test
    public void testListIngredients() {
        ModelService modelService = new ModelService(input);
        ShoppingListService listService = new ShoppingListService(modelService);
        AddIngredientToShoppingListTest.addTestIngedients(listService, modelService);

        final String responseString = HandlerTestHelper.getResponseString(listIngredientsIntentHandler.handle(input));

        Assert.assertTrue(responseString.contains("ingredient0"));
        Assert.assertTrue(responseString.contains("ingredient9"));
        Assert.assertFalse(responseString.endsWith(", "));
    }

    @Test
    public void testListIngredientsAndRecipes() {
        ModelService modelService = new ModelService(input);
        ShoppingListService listService = new ShoppingListService(modelService);

        listService.addIngredient("tomaten", 100, "g");
        listService.addIngredient("käse", 150, "g");
        listService.addIngredient("wasser", 250, "ml");
        listService.createRecipe("tomatensuppe");
        listService.createRecipe("burger");
        listService.addRecipes("tomatensuppe", 1);
        listService.addRecipes("burger", 5);

        listService.save(modelService);

        final String responseString = HandlerTestHelper.getResponseString(listIngredientsIntentHandler.handle(input));

        assertTrue(responseString.contains(String.format(SpeechTextConstants.LIST_INGREDIENTS, 3)));
        assertTrue(responseString.contains(String.format(SpeechTextConstants.LIST_RECIPES, 2)));
        Assert.assertFalse(responseString.endsWith(", "));
    }

    @Test
    public void testListRecipe() {
        ModelService modelService = new ModelService(input);
        ShoppingListService listService = new ShoppingListService(modelService);
        listService.createRecipe("tomatensuppe");
        listService.addRecipes("tomatensuppe", 1);

        listService.save(modelService);

        final String responseString = HandlerTestHelper.getResponseString(listIngredientsIntentHandler.handle(input));

        assertTrue(responseString.contains(String.format(SpeechTextConstants.LIST_INGREDIENTS, 0)));
        assertTrue(responseString.contains(String.format(SpeechTextConstants.LIST_RECIPES, 1)));
        final String expected = new SsmlService()
                .beginParagraph()
                .enumerate(String.format(SpeechTextConstants.LIST_INGREDIENTS, 0), new HashSet())
                .endParagraph()
                .newLine()
                .beginParagraph()
                .append(String.format(SpeechTextConstants.LIST_RECIPES, 1)).append(": 1 Portionen tomatensuppe")
                .endParagraph().getSpeechString();
        HandlerTestHelper.compareSSML(expected, responseString);
        Assert.assertFalse(responseString.endsWith(", "));
    }

    @Test
    public void canHandle() {
        Assert.assertTrue(listIngredientsIntentHandler.canHandle(input));
    }

    @Test
    public void testPiece(){
        ModelService modelService = new ModelService(input);
        ShoppingListService listService = new ShoppingListService(modelService);

        listService.addIngredient("pizza", 1, "");
        listService.save(modelService);
        final String responseString = HandlerTestHelper.getResponseString(listIngredientsIntentHandler.handle(input));

        Assert.assertFalse(responseString.contains("piece"));
    }

}
