package edu.hm.cs.seng.hypershop.model;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.Card;
import com.amazon.ask.model.ui.SimpleCard;
import com.amazon.ask.response.ResponseBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.cs.seng.hypershop.handlers.AddIngredientIntentHandler;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.junit.Assert.*;

public class AddIngredientToShoppingListTest {

    @InjectMocks
    private ModelService modelService;
    @InjectMocks
    private ShoppingListService shoppingListService;

    private HandlerInput input = Mockito.mock(HandlerInput.class);
    private HandlerInput input2 = Mockito.mock(HandlerInput.class);

    private ShoppingList shoppingList;



    @Before
    public void before() {
        this.shoppingList = new ShoppingList();

        ObjectMapper objectMapper = new ObjectMapper();
        RequestEnvelope requestEnvelope=null;
        RequestEnvelope requestEnvelope2=null;
        try {
            objectMapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
            //TODO: move to ressources folder
            requestEnvelope = objectMapper.readValue(new File(System.getProperty("user.dir") +
                    "/src/test/java/edu/hm/cs/seng/hypershop/model/addingredients.json"), RequestEnvelope.class);
            requestEnvelope2 = objectMapper.readValue(new File(System.getProperty("user.dir") +
                    "/src/test/java/edu/hm/cs/seng/hypershop/model/addingredients2.json"), RequestEnvelope.class);
        } catch (IOException e) {
            System.out.println(e);
            Assert.fail();
        }
        buildInput(requestEnvelope,input);
        buildInput(requestEnvelope2,input2);
    }

    private void buildInput(RequestEnvelope requestEnvelope, HandlerInput input){
        Mockito.when(input.getRequestEnvelope()).thenReturn(requestEnvelope);
        AttributesManager.Builder b = AttributesManager.builder();

        PersistenceAdapter pa = Mockito.mock(PersistenceAdapter.class);
        AttributesManager am = b.withPersistenceAdapter(pa).withRequestEnvelope(requestEnvelope).build();
        Mockito.when(input.getAttributesManager()).thenReturn(am);
        Mockito.when(input.getResponseBuilder()).thenReturn(new ResponseBuilder());
    }

    @Test
    public void addIngredients() {
        ShoppingListService listService = new ShoppingListService();

        for(int index=0; index < 10; index++){
            String ingredientName = "ingredient"+index;
            int amount = 10+index;
            String unitName = "kg";
            shoppingList = listService.addIngredient(ingredientName,amount,unitName,shoppingList);
        }

        byte[] shoppingListAsBytes = ModelService.toBinary(shoppingList);

        final ShoppingList actual = (ShoppingList) ModelService.fromBinary(shoppingListAsBytes, ShoppingList.class);

        assertEquals(shoppingList, actual);

        assertEquals(10,shoppingList.getIngredients().size());
        assertNull(shoppingList.getRecipes());
    }

    @Test
    public void testIngredientHandler(){
        AddIngredientIntentHandler handler = new AddIngredientIntentHandler();
        Optional<Response> responseOptional = handler.handle(input);

        assertTrue(responseOptional.isPresent());
        final Card card = responseOptional.get().getCard();
        Assert.assertTrue(card instanceof SimpleCard && ((SimpleCard)card).getContent().contains("Brot"));

        Optional<Response> responseOptional2 = handler.handle(input2);
        assertTrue(responseOptional2.isPresent());
        final Card card2 = responseOptional2.get().getCard();
        Assert.assertTrue(card2 instanceof SimpleCard && ((SimpleCard)card2).getContent().contains("wasser"));
    }

}
