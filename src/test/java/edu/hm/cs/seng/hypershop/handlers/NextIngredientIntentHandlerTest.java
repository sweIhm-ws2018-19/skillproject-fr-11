package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class NextIngredientIntentHandlerTest {
    private final HandlerInput input = mock(HandlerInput.class);
    private final NextIngredientIntentHandler handler = new NextIngredientIntentHandler();

    @Test
    public void shouldNotHandleWithoutContext() {
        HandlerTestHelper.buildInput("nextingredient.json", input);
        assertFalse(handler.canHandle(input));
    }

    @Test
    public void shouldHandleWithContext() {
        HandlerTestHelper.buildInput("nextingredient.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_STEPS);
        assertTrue(handler.canHandle(input));
    }

    @Test
    public void shouldStayAtSameN1List() {
        HandlerTestHelper.buildInput("nextingredient.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_STEPS);

        final ModelService modelService = new ModelService(input);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);
        shoppingListService.addIngredient("Lebkuchen", 3, "kg");
        shoppingListService.save(modelService);


    }
}