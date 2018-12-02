package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SimpleCard;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ListIngredientShoppingListTest {

    private HandlerInput input = mock(HandlerInput.class);
    private ShoppingList shoppingList;
    @Mock
    private ModelService modelService;
    @InjectMocks
    @Spy
    private ListIngredientsIntentHandler listIngredientsIntentHandler = new ListIngredientsIntentHandler();

    @Before
    public void before() {
        HandlerTestHelper.buildInput("listingredients.json", input);

        shoppingList = new ShoppingList();
        addIngredients2ShoppingList();
    }

    private void addIngredients2ShoppingList() {
        ShoppingListService listService = new ShoppingListService();
        for (int index = 0; index < 10; index++) {
            String ingredientName = "ingredient" + index;
            int amount = 10 + index;
            String unitName = "kg";
            try {
                shoppingList = listService.addIngredient(ingredientName, amount, unitName, shoppingList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testEmptyList() {
        when(modelService.get(any(), any())).thenReturn(new ShoppingList());
        when(listIngredientsIntentHandler.getModelService()).thenReturn(modelService);
        Optional<Response> responseOptional = listIngredientsIntentHandler.handle(input);

        assertTrue(responseOptional.isPresent());
        final SimpleCard card = (SimpleCard) responseOptional.get().getCard();
        Assert.assertTrue(card.getContent().endsWith("."));
    }

    @Test
    public void testListIngredients() {
        when(modelService.get(any(), any())).thenReturn(shoppingList);
        when(listIngredientsIntentHandler.getModelService()).thenReturn(modelService);
        Optional<Response> responseOptional = listIngredientsIntentHandler.handle(input);

        assertTrue(responseOptional.isPresent());
        final SimpleCard card = (SimpleCard) responseOptional.get().getCard();
        Assert.assertTrue(card.getContent().contains("ingredient0"));
        Assert.assertTrue(card.getContent().contains("ingredient9"));
        Assert.assertFalse(card.getContent().contains(".0"));
        Assert.assertFalse(card.getContent().endsWith(", "));
    }
    @Test
    public void canHandle() {
        Assert.assertTrue(listIngredientsIntentHandler.canHandle(input));
    }

}
