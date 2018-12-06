package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static edu.hm.cs.seng.hypershop.SpeechTextConstants.INGREDIENTS_REMOVE_ERROR;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.INGREDIENTS_REMOVE_RECIPE_SUCCESS;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RemoveIngredientRecipeTest {

    private final HandlerInput input = Mockito.mock(HandlerInput.class);
    private final RemoveIngredientIntentHandler handler = new RemoveIngredientIntentHandler();

    @Before
    public void before() {
        HandlerTestHelper.buildInput("removeingredientrecipe.json", input);
    }

    @Test
    public void testIngredientHandler_WithoutRecipe_ErrorMessage() {
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(INGREDIENTS_REMOVE_ERROR, responseString);
    }

    @Test
    public void testIngredientHandler_WithoutIngredients_ErrorMessage() {
        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);
        shoppingListService.createRecipe("tomatensuppe");

        shoppingListService.save(modelService);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(INGREDIENTS_REMOVE_ERROR, responseString);
    }

    @Test
    public void testIngredientHandler_WithIngredients_SuccessMessage() {
        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);
        shoppingListService.createRecipe("tomatensuppe");

        shoppingListService.addIngredientRecipe("tomaten", 50, "g", "tomatensuppe");
        shoppingListService.addIngredientRecipe("suppe", 150, "ml", "tomatensuppe");

        shoppingListService.save(modelService);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(String.format(INGREDIENTS_REMOVE_RECIPE_SUCCESS, "tomaten"), responseString);

        shoppingListService.load(modelService);
        Assert.assertEquals(1, shoppingListService.getRecipe("tomatensuppe").getIngredients().size());
    }
}
