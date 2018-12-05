package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SimpleCard;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ListRecipesIntentHandlerTest {
    private HandlerInput input = mock(HandlerInput.class);

    @Before
    public void before() {
        HandlerTestHelper.buildInput("listrecipes.json", input);
    }

    private void addRecipe() {
        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);
        for (int index = 0; index < 10; index++) {
            String ingredientName = "recipe" + index;
            listService.createRecipe(ingredientName);
        }
        listService.save(modelService);
    }

    @Test
    public void testEmptyList() {
        Optional<Response> responseOptional = new ListRecipesIntentHandler().handle(input);

        assertTrue(responseOptional.isPresent());
        final SimpleCard card = (SimpleCard) responseOptional.get().getCard();
        Assert.assertTrue(card.getContent().endsWith("."));
    }

    @Test
    public void testListIngredients() {
        addRecipe();

        Optional<Response> responseOptional = new ListRecipesIntentHandler().handle(input);

        assertTrue(responseOptional.isPresent());
        final SimpleCard card = (SimpleCard) responseOptional.get().getCard();
        Assert.assertTrue(card.getContent().contains("recipe0"));
        Assert.assertTrue(card.getContent().contains("recipe9"));
        Assert.assertFalse(card.getContent().endsWith(", "));
    }

    @Test
    public void canHandle() {
        Assert.assertTrue(new ListRecipesIntentHandler().canHandle(input));
    }

}