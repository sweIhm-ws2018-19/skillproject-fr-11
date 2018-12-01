package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ModelService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AddRecipeIntentHandlerTest {
    private HandlerInput inputNormal = Mockito.mock(HandlerInput.class);

    private ShoppingList shoppingList;

    @Before
    public void before() {
        this.shoppingList = new ShoppingList();

        final ObjectMapper objectMapper = new ObjectMapper();
        RequestEnvelope requestEnvelope = null;
        try {
            objectMapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
            final ClassLoader classLoader = getClass().getClassLoader();
            String fileNormal = Objects.requireNonNull(classLoader.getResource("addrecipe-normal.json")).getFile();
            requestEnvelope = objectMapper.readValue(new FileReader(fileNormal), RequestEnvelope.class);
        } catch (IOException e) {
            System.out.println(e);
            Assert.fail();
        }
        HandlerTestHelper.buildInput(requestEnvelope, inputNormal);
    }

    @Test
    public void shouldHandleNewRecipe() {
        final AddRecipeIntentHandler handler = new AddRecipeIntentHandler();

        assertTrue(handler.canHandle(inputNormal));
        handler.handle(inputNormal);

        final ShoppingList s = (ShoppingList) new ModelService(inputNormal).get(Constants.KEY_SHOPPING_LIST, ShoppingList.class);
        assertEquals(1, s.getRecipes().size());
    }

}