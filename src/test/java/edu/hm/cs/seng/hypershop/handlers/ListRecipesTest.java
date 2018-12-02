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
public class ListRecipesTest {

    private HandlerInput input = mock(HandlerInput.class);
    private ShoppingList shoppingList;
    @Mock
    private ModelService modelService;
    @InjectMocks
    @Spy
    private ListRecipesIntentHandler listRecipesIntentHandler = new ListRecipesIntentHandler();

    @Before
    public void before() {
        HandlerTestHelper.buildInput("listrecipes.json", input);

        shoppingList = new ShoppingList();
        addRecipe();
    }

    private void addRecipe() {
        ShoppingListService listService = new ShoppingListService();
        for (int index = 0; index < 10; index++) {
            String ingredientName = "recipe" + index;
            listService.addRecipe(ingredientName, shoppingList);
        }
    }

    @Test
    public void testEmptyList() {
        when(modelService.get(any(), any())).thenReturn(new ShoppingList());
        when(listRecipesIntentHandler.getModelService()).thenReturn(modelService);
        Optional<Response> responseOptional = listRecipesIntentHandler.handle(input);

        assertTrue(responseOptional.isPresent());
        final SimpleCard card = (SimpleCard) responseOptional.get().getCard();
        Assert.assertTrue(card.getContent().endsWith("."));
    }

    @Test
    public void testListIngredients() {
        when(modelService.get(any(), any())).thenReturn(shoppingList);
        when(listRecipesIntentHandler.getModelService()).thenReturn(modelService);
        Optional<Response> responseOptional = listRecipesIntentHandler.handle(input);

        assertTrue(responseOptional.isPresent());
        final SimpleCard card = (SimpleCard) responseOptional.get().getCard();
        Assert.assertTrue(card.getContent().contains("recipe0"));
        Assert.assertTrue(card.getContent().contains("recipe9"));
        Assert.assertFalse(card.getContent().endsWith(", "));
    }
    @Test
    public void canHandle() {
        Assert.assertTrue(listRecipesIntentHandler.canHandle(input));
    }

}
