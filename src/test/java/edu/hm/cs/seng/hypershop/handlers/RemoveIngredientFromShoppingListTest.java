package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.Card;
import com.amazon.ask.model.ui.SimpleCard;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static edu.hm.cs.seng.hypershop.SpeechTextConstants.INGREDIENTS_REMOVE_ERROR;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.INGREDIENTS_REMOVE_SUCCESS;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RemoveIngredientFromShoppingListTest {

    private HandlerInput input = Mockito.mock(HandlerInput.class);

    @Before
    public void before() {
        HandlerTestHelper.buildInput("removeingredient.json", input);
    }

    @Test
    public void testIngredientHandler_WithoutIngredients_ErrorMessage() {
        RemoveIngredientIntentHandler handler = new RemoveIngredientIntentHandler();
        Optional<Response> responseOptional = handler.handle(input);

        assertTrue(responseOptional.isPresent());
        final Card card = responseOptional.get().getCard();
        Assert.assertTrue(card instanceof SimpleCard);
        Assert.assertEquals(INGREDIENTS_REMOVE_ERROR, ((SimpleCard) card).getContent());
    }

    @Test
    public void testIngredientHandler_WithIngredients_SuccessMessage() {
        final ShoppingList s = (ShoppingList) new ModelService(input).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        final ShoppingListService shoppingListService = new ShoppingListService();

        shoppingListService.addIngredient("tomaten", 50, "g", s);
        shoppingListService.addIngredient("kartoffeln", 50, "g", s);

        final ModelService modelService = new ModelService(input);
        modelService.save(s);

        RemoveIngredientIntentHandler handler = new RemoveIngredientIntentHandler();
        Optional<Response> responseOptional = handler.handle(input);

        assertTrue(responseOptional.isPresent());
        final Card card = responseOptional.get().getCard();
        Assert.assertTrue(card instanceof SimpleCard);
        Assert.assertEquals(String.format(INGREDIENTS_REMOVE_SUCCESS, "tomaten"), ((SimpleCard) card).getContent());

        ShoppingList actual = (ShoppingList) modelService.get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        Assert.assertEquals(1, actual.getIngredients().size());
    }
}
