package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.ui.Card;
import com.amazon.ask.model.ui.SimpleCard;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.SessionStorageService;
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

import static edu.hm.cs.seng.hypershop.Constants.*;
import static edu.hm.cs.seng.hypershop.SpeechTextConstants.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddIngredientRecipeIntentHandlerTest {


    private ShoppingListService listService = new ShoppingListService();

    private HandlerInput input = mock(HandlerInput.class);
    private HandlerInput input2 = mock(HandlerInput.class);
    private HandlerInput input3 = mock(HandlerInput.class);
    private HandlerInput input4 = mock(HandlerInput.class);

    private ShoppingList shoppingList;

    @Mock
    private ModelService modelService;
    @InjectMocks
    @Spy
    private AddIngredientRecipeIntentHandler addIngredientRecipeIntentHandler = new AddIngredientRecipeIntentHandler();


    @Before
    public void before() {
        this.shoppingList = new ShoppingList();

        HandlerTestHelper.buildInput("addingredientsrecipe.json", input);
        HandlerTestHelper.buildInput("addingredientsrecipe2.json", input2);
        HandlerTestHelper.buildInput("addingredientsrecipe3.json", input3);
        HandlerTestHelper.buildInput("addingredientsrecipe-3-jam.json", input4);

        ContextStackService.pushContext(input, CONTEXT_RECIPE);
        ContextStackService.pushContext(input2, CONTEXT_RECIPE);
        ContextStackService.pushContext(input4, CONTEXT_RECIPE);

        SessionStorageService.setCurrentRecipe(input, "test");
        SessionStorageService.setCurrentRecipe(input2, "test");
        SessionStorageService.setCurrentRecipe(input4, "test");


        listService.addRecipe("test", shoppingList);

    }


    @Test
    public void addIngredients() {
        for (int index = 0; index < 10; index++) {
            String ingredientName = "ingredient" + index;
            int amount = 10 + index;
            String unitName = "kg";
            listService.addIngredientRecipe(ingredientName, amount, unitName, shoppingList, "test");
        }

        byte[] shoppingListAsBytes = ModelService.toBinary(shoppingList);

        final ShoppingList actual = (ShoppingList) ModelService.fromBinary(shoppingListAsBytes, ShoppingList.class);

        assertNotNull(actual);
        assertEquals(shoppingList, actual);

        assertEquals(1, shoppingList.getRecipes().size());
        assertEquals(0, shoppingList.getIngredients().size());

        Recipe actualRecipe = actual.getRecipes().keySet().iterator().next();
        assertEquals(10, actualRecipe.getIngredients().size());
    }

    @Test
    public void testIngredientHandler() {
        when(modelService.get(any(), any())).thenReturn(shoppingList);
        when(addIngredientRecipeIntentHandler.getModelService()).thenReturn(modelService);

        Optional<Response> responseOptional = addIngredientRecipeIntentHandler.handle(input);

        assertTrue(responseOptional.isPresent());
        final Card card = responseOptional.get().getCard();
        Assert.assertTrue(card instanceof SimpleCard && ((SimpleCard) card).getContent().contains("Brot"));

        Optional<Response> responseOptional2 = addIngredientRecipeIntentHandler.handle(input2);

        assertTrue(responseOptional2.isPresent());
        final Card card2 = responseOptional2.get().getCard();
        Assert.assertTrue(card2 instanceof SimpleCard && ((SimpleCard) card2).getContent().contains("wasser"));

        Assert.assertEquals(1, shoppingList.getRecipes().size());
        Assert.assertEquals(2, shoppingList.getRecipes().keySet().iterator().next().getIngredients().size());
    }

    @Test
    public void testIngredientSlotEmpty() {
        AddIngredientRecipeIntentHandler handler = new AddIngredientRecipeIntentHandler();
        ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots().put(Constants.SLOT_INGREDIENT, null);
        Optional<Response> responseOptional = handler.handle(input);

        assertTrue(responseOptional.isPresent());
        final SimpleCard card = (SimpleCard) responseOptional.get().getCard();
        Assert.assertEquals(INGREDIENTS_ADD_ERROR, card.getContent());
    }

    @Test
    public void testAmountNoNumber() {
        invalidInput(SLOT_AMOUNT, INGREDIENTS_ADD_RECIPE_NUMBER_ERROR);
    }

    @Test
    public void testUnitNotFound() {
        invalidInput(SLOT_UNIT, INGREDIENTS_ADD_UNIT_ERROR);
    }

    private void invalidInput(String slotUnit, String ingredientsAddError) {
        when(modelService.get(any(), any())).thenReturn(shoppingList);
        when(addIngredientRecipeIntentHandler.getModelService()).thenReturn(modelService);
        Slot slot = Slot.builder().withName(slotUnit).withValue("test").build();
        ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots().put(slotUnit, slot);
        Optional<Response> responseOptional = addIngredientRecipeIntentHandler.handle(input);

        assertTrue(responseOptional.isPresent());
        final SimpleCard card = (SimpleCard) responseOptional.get().getCard();

        Assert.assertEquals(ingredientsAddError, card.getContent());
    }

    @Test
    public void canHandle() {
        AddIngredientRecipeIntentHandler handler = new AddIngredientRecipeIntentHandler();
        Assert.assertTrue(handler.canHandle(input));
        Assert.assertFalse(handler.canHandle(input3));
    }

    @Test
    public void testResolution() {
        when(modelService.get(any(), any())).thenReturn(shoppingList);
        when(addIngredientRecipeIntentHandler.getModelService()).thenReturn(modelService);

        Optional<Response> responseOptional = addIngredientRecipeIntentHandler.handle(input2);

        assertTrue(responseOptional.isPresent());
        final Card card = responseOptional.get().getCard();
        Assert.assertTrue(((SimpleCard) card).getContent().contains("wasser"));
    }

    @Test
    public void addThreeJamJars() {

        when(modelService.get(any(), any())).thenReturn(shoppingList);
        when(addIngredientRecipeIntentHandler.getModelService()).thenReturn(modelService);

        assertTrue(addIngredientRecipeIntentHandler.canHandle(input4));

        final String responseString = HandlerTestHelper.getResponseString(addIngredientRecipeIntentHandler.handle(input4));
        final String expected = String.format(SpeechTextConstants.INGREDIENTS_ADD_RECIPE_SUCCESS, "marmelade");
        HandlerTestHelper.compareSSML(expected, responseString);
    }

}