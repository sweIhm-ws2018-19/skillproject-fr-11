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
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.INGREDIENTS_REMOVE_SUCCESS;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RemoveIngredientFromShoppingListTest {

    private final HandlerInput input = Mockito.mock(HandlerInput.class);
    private final RemoveIngredientIntentHandler handler = new RemoveIngredientIntentHandler();

    @Before
    public void before() {
        HandlerTestHelper.buildInput("removeingredient.json", input);
    }

    @Test
    public void testIngredientHandler_WithoutIngredients_ErrorMessage() {
        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(INGREDIENTS_REMOVE_ERROR, responseString);
    }

    @Test
    public void testIngredientHandler_WithIngredients_SuccessMessage() {
        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        shoppingListService.addIngredient("tomaten", 50, "g");
        shoppingListService.addIngredient("kartoffeln", 50, "g");

        shoppingListService.save(modelService);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(String.format(INGREDIENTS_REMOVE_SUCCESS, "tomaten"), responseString);

        shoppingListService.load(modelService);
        Assert.assertEquals(1, shoppingListService.getIngredients().size());
    }
}
