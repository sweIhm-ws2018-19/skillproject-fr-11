package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.ui.Card;
import com.amazon.ask.model.ui.SimpleCard;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static edu.hm.cs.seng.hypershop.Constants.SLOT_AMOUNT;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.INGREDIENTS_ADD_ERROR;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.INGREDIENTS_ADD_NUMBER_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddIngredientToShoppingListTest {

    private HandlerInput input = Mockito.mock(HandlerInput.class);
    private HandlerInput input2 = Mockito.mock(HandlerInput.class);

    private ShoppingList shoppingList;


    @Before
    public void before() {
        this.shoppingList = new ShoppingList();

        ObjectMapper objectMapper = new ObjectMapper();
        RequestEnvelope requestEnvelope = null;
        RequestEnvelope requestEnvelope2 = null;
        try {
            objectMapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
            ClassLoader classLoader = getClass().getClassLoader();
            String file1 = Objects.requireNonNull(classLoader.getResource("addingredients.json")).getFile();
            String file2 = Objects.requireNonNull(classLoader.getResource("addingredients2.json")).getFile();
            requestEnvelope = objectMapper.readValue(new FileReader(file1), RequestEnvelope.class);
            requestEnvelope2 = objectMapper.readValue(new FileReader(file2), RequestEnvelope.class);
        } catch (IOException e) {
            System.out.println(e);
            Assert.fail();
        }
        HandlerTestHelper.buildInput(requestEnvelope, input);
        HandlerTestHelper.buildInput(requestEnvelope2, input2);
    }


    @Test
    public void addIngredients() {
        ShoppingListService listService = new ShoppingListService();

        for (int index = 0; index < 10; index++) {
            String ingredientName = "ingredient" + index;
            int amount = 10 + index;
            String unitName = "kg";
            shoppingList = listService.addIngredient(ingredientName, amount, unitName, shoppingList);
        }

        byte[] shoppingListAsBytes = ModelService.toBinary(shoppingList);

        final ShoppingList actual = (ShoppingList) ModelService.fromBinary(shoppingListAsBytes, ShoppingList.class);

        assertEquals(shoppingList, actual);

        assertEquals(10, shoppingList.getIngredients().size());
        assertEquals(0, shoppingList.getRecipes().size());
    }

    @Test
    public void testIngredientHandler() {
        AddIngredientIntentHandler handler = new AddIngredientIntentHandler();
        Optional<Response> responseOptional = handler.handle(input);

        assertTrue(responseOptional.isPresent());
        final Card card = responseOptional.get().getCard();
        Assert.assertTrue(card instanceof SimpleCard && ((SimpleCard) card).getContent().contains("Brot"));

        Optional<Response> responseOptional2 = handler.handle(input2);
        assertTrue(responseOptional2.isPresent());
        final Card card2 = responseOptional2.get().getCard();
        Assert.assertTrue(card2 instanceof SimpleCard && ((SimpleCard) card2).getContent().contains("wasser"));
    }

    @Test
    public void testIngredientSlotEmpty() {
        AddIngredientIntentHandler handler = new AddIngredientIntentHandler();
        ((IntentRequest)input.getRequestEnvelope().getRequest()).getIntent().getSlots().put(Constants.SLOT_INGREDIENT,null);
        Optional<Response> responseOptional = handler.handle(input);

        assertTrue(responseOptional.isPresent());
        final SimpleCard card = (SimpleCard) responseOptional.get().getCard();
        Assert.assertEquals(card.getContent(),INGREDIENTS_ADD_ERROR);
    }

    @Test
    public void testAmountNoNumber() {
        AddIngredientIntentHandler handler = new AddIngredientIntentHandler();
        Slot slot = Slot.builder().withName(SLOT_AMOUNT).withValue("test").build();
        ((IntentRequest)input.getRequestEnvelope().getRequest()).getIntent().getSlots().put(SLOT_AMOUNT,slot);
        Optional<Response> responseOptional = handler.handle(input);

        assertTrue(responseOptional.isPresent());
        final SimpleCard card = (SimpleCard) responseOptional.get().getCard();
        Assert.assertEquals(card.getContent(),INGREDIENTS_ADD_NUMBER_ERROR);
    }

    @Test
    public void canHandle() {
        AddIngredientIntentHandler handler = new AddIngredientIntentHandler();
        Assert.assertTrue(handler.canHandle(input));
    }
}
