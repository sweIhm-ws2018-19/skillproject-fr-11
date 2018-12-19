package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ListStepIngredientsIntentHandlerTest {

    private final HandlerInput input = mock(HandlerInput.class);
    private final ListStepIngredientsIntentHandler handler = new ListStepIngredientsIntentHandler();

    @Test
    public void shouldHandleWithoutContext() {
        HandlerTestHelper.buildInput("liststepingredients.json", input);
        assertTrue(handler.canHandle(input));
    }

    @Test
    public void shouldNotHandleWithContext() {
        HandlerTestHelper.buildInput("liststepingredients.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_RECIPE);
        assertFalse(handler.canHandle(input));
    }

    @Test
    public void shouldPrintErrorOnEmptyList() {
        HandlerTestHelper.buildInput("liststepingredients.json", input);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(SpeechTextConstants.LIST_STEP_INGREDIENTS_EMPTY, responseString);

        assertTrue(ContextStackService.isCurrentContext(input, null));
    }

    @Test
    public void shouldPrintFirstIngredientN1List() {
        HandlerTestHelper.buildInput("liststepingredients.json", input);
        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);
        shoppingListService.addIngredient("Wein", 3, "glas");
        shoppingListService.save(modelService);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML("3 glas Wein", responseString);

        assertTrue(ContextStackService.isCurrentContext(input, Constants.CONTEXT_STEPS));
    }

    @Test
    public void shouldPrintFirstIngredientN3List() {
        HandlerTestHelper.buildInput("liststepingredients.json", input);
        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);
        shoppingListService.addIngredient("Wein", 3, "glas");
        shoppingListService.addIngredient("Lebkuchen", 10, "kg");
        shoppingListService.addIngredient("HappyMeal", 50, "g");
        shoppingListService.save(modelService);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML("10 kg Lebkuchen", responseString);

        assertTrue(ContextStackService.isCurrentContext(input, Constants.CONTEXT_STEPS));
    }
}