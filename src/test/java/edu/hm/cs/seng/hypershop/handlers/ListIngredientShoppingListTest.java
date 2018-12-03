package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ListIngredientShoppingListTest {

    private final HandlerInput input = mock(HandlerInput.class);
    private ModelService modelService;
    private ShoppingListService listService;
    private final ListIngredientsIntentHandler listIngredientsIntentHandler = new ListIngredientsIntentHandler();

    @Before
    public void before() {
        HandlerTestHelper.buildInput("listingredients.json", input);
        modelService = new ModelService(input);
        listService = new ShoppingListService(modelService);
    }

    @Test
    public void testEmptyList() {
        final String responseString = HandlerTestHelper.getResponseString(listIngredientsIntentHandler.handle(input));

        HandlerTestHelper.compareSSML("Du hast 0 Zutaten in deiner Einkaufsliste.", responseString);
    }

    @Test
    public void testListIngredients() {
        AddIngredientToShoppingListTest.addTestIngedients(listService, modelService);

        final String responseString = HandlerTestHelper.getResponseString(listIngredientsIntentHandler.handle(input));

        Assert.assertTrue(responseString.contains("ingredient0"));
        Assert.assertTrue(responseString.contains("ingredient9"));
        Assert.assertFalse(responseString.endsWith(", "));
    }

    @Test
    public void canHandle() {
        Assert.assertTrue(listIngredientsIntentHandler.canHandle(input));
    }

}
