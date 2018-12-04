package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static edu.hm.cs.seng.hypershop.SpeechTextConstants.RECIPE_REMOVE_NOT_FOUND;

public class RemoveRecipeIntentHandlerTest {

    private HandlerInput input = Mockito.mock(HandlerInput.class);

    @Before
    public void before() {
        HandlerTestHelper.buildInput("removerecipe.json", input);
    }


    @Test
    public void remove_Empty_ShoppingList() {
        RemoveRecipeIntentHandler handler = new RemoveRecipeIntentHandler();

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(String.format(RECIPE_REMOVE_NOT_FOUND, "currywurst"), responseString);
    }

    @Test
    public void remove_Recipe_ShoppingList() {
        RemoveRecipeIntentHandler handler = new RemoveRecipeIntentHandler();

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        Assert.assertTrue(responseString.contains("currywurst"));
    }

    @Test
    public void createAddRemoveDelete() {
        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);

        Assert.assertEquals(0, listService.getRecipeStrings().size());
        Assert.assertEquals(0, listService.getAddedRecipeStrings().size());

        listService.createRecipe("Currywurst");
        Assert.assertEquals(1, listService.getRecipeStrings().size());
        Assert.assertEquals(0, listService.getAddedRecipeStrings().size());

        listService.addRecipes("Currywurst", 1);
        Assert.assertEquals(1, listService.getRecipeStrings().size());
        Assert.assertEquals(1, listService.getAddedRecipeStrings().size());

        listService.removeRecipes("Currywurst", 1);
        Assert.assertEquals(1, listService.getRecipeStrings().size());
        Assert.assertEquals(0, listService.getAddedRecipeStrings().size());

        listService.deleteRecipe("Currywurst");
        Assert.assertEquals(0, listService.getRecipeStrings().size());
        Assert.assertEquals(0, listService.getAddedRecipeStrings().size());
    }

}