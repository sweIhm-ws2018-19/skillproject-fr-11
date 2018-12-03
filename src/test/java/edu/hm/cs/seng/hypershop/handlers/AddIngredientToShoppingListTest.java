package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.ui.SimpleCard;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.Set;

import static edu.hm.cs.seng.hypershop.Constants.SLOT_AMOUNT;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.INGREDIENTS_ADD_ERROR;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.INGREDIENTS_ADD_NUMBER_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddIngredientToShoppingListTest {

    private HandlerInput input = Mockito.mock(HandlerInput.class);
    private HandlerInput input2 = Mockito.mock(HandlerInput.class);

    @Before
    public void before() {
        HandlerTestHelper.buildInput("addingredients.json", input);
        HandlerTestHelper.buildInput("addingredients2.json", input2);
    }

    public static void addTestIngedients(ShoppingListService listService, ModelService modelService) {
        for (int index = 0; index < 10; index++) {
            String ingredientName = "ingredient" + index;
            int amount = 10 + index;
            String unitName = "kg";
            listService.addIngredient(ingredientName, amount, unitName);
        }
        listService.save(modelService);
    }

    @Test
    public void addIngredients() {
        final ModelService modelService = new ModelService(input);
        final ShoppingListService listService = new ShoppingListService(modelService);

        addTestIngedients(listService, modelService);

        assertEquals(10, listService.getIngredients().size());
        assertEquals(0, listService.getRecipeStrings().size());
    }

    @Test
    public void addFirstIngredient() {
        AddIngredientIntentHandler handler = new AddIngredientIntentHandler();

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        Assert.assertTrue(responseString.contains("Brot"));

        final String responseString2 = HandlerTestHelper.getResponseString(handler.handle(input2));
        Assert.assertTrue(responseString2.contains("wasser"));

        final ShoppingListService shoppingListService = new ShoppingListService(new ModelService(input));
        final Set<IngredientAmount> ingredients = shoppingListService.getIngredients();

        Assert.assertEquals(1, ingredients.size());
    }

    @Test
    public void addSecondIngredient() {
        AddIngredientIntentHandler handler = new AddIngredientIntentHandler();
        final ModelService modelService = new ModelService(input2);
        final ShoppingListService shoppingListService = new ShoppingListService(modelService);

        shoppingListService.addIngredient("Brot", 10, "kg");
        shoppingListService.save(modelService);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input2));
        Assert.assertTrue(responseString.contains("wasser"));

        shoppingListService.load(modelService);
        final Set<IngredientAmount> ingredients = shoppingListService.getIngredients();
        Assert.assertEquals(2, ingredients.size());
    }

    @Test
    public void testIngredientSlotEmpty() {
        AddIngredientIntentHandler handler = new AddIngredientIntentHandler();
        ((IntentRequest)input.getRequestEnvelope().getRequest()).getIntent().getSlots().put(Constants.SLOT_INGREDIENT,null);
        Optional<Response> responseOptional = handler.handle(input);

        assertTrue(responseOptional.isPresent());
        final SimpleCard card = (SimpleCard) responseOptional.get().getCard();
        Assert.assertEquals(INGREDIENTS_ADD_ERROR, card.getContent());
    }

    @Test
    public void testAmountNoNumber() {
        AddIngredientIntentHandler handler = new AddIngredientIntentHandler();
        Slot slot = Slot.builder().withName(SLOT_AMOUNT).withValue("test").build();
        ((IntentRequest)input.getRequestEnvelope().getRequest()).getIntent().getSlots().put(SLOT_AMOUNT,slot);
        Optional<Response> responseOptional = handler.handle(input);

        assertTrue(responseOptional.isPresent());
        final SimpleCard card = (SimpleCard) responseOptional.get().getCard();
        Assert.assertEquals(INGREDIENTS_ADD_NUMBER_ERROR, card.getContent());
    }

    @Test
    public void canHandle() {
        AddIngredientIntentHandler handler = new AddIngredientIntentHandler();
        Assert.assertTrue(handler.canHandle(input));
    }
}
