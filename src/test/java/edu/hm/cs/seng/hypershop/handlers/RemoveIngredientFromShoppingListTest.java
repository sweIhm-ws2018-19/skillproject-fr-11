package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.Card;
import com.amazon.ask.model.ui.SimpleCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.INGREDIENTS_ADD_SUCCESS;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.INGREDIENTS_REMOVE_ERROR;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.INGREDIENTS_REMOVE_SUCCESS;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RemoveIngredientFromShoppingListTest {

    private HandlerInput input = Mockito.mock(HandlerInput.class);

    @InjectMocks
    private ModelService modelService;
    @InjectMocks
    private ShoppingListService shoppingListService;

    private ShoppingList shoppingList;

//    @Before
//    public void before() {
//
//    }

    @Before
    public void before() {
        this.shoppingList = new ShoppingList();

        ObjectMapper objectMapper = new ObjectMapper();
        RequestEnvelope requestEnvelope = null;
        try {
            objectMapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
            ClassLoader classLoader = getClass().getClassLoader();
            String file1 = Objects.requireNonNull(classLoader.getResource("removeingredient.json")).getFile();
            requestEnvelope = objectMapper.readValue(new FileReader(file1), RequestEnvelope.class);
        } catch (IOException e) {
            System.out.println(e);
            Assert.fail();
        }
        HandlerTestHelper.buildInput(requestEnvelope, input);
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

        shoppingListService = new ShoppingListService();
        shoppingList = shoppingListService.addIngredient("tomaten", 50, "g", shoppingList);
        shoppingList = shoppingListService.addIngredient("kartoffeln", 50, "g", shoppingList);
        modelService = new ModelService(input);
        modelService.save(shoppingList);

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
