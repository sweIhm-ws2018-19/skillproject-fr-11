package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.Card;
import com.amazon.ask.model.ui.SimpleCard;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.service.ModelService;
import edu.hm.cs.seng.hypershop.service.ShoppingListService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static edu.hm.cs.seng.hypershop.SpeechTextConstants.RECIPE_REMOVE_NOT_FOUND;
import static org.junit.Assert.*;

public class RemoveRecipeIntentHandlerTest {

    private HandlerInput input = Mockito.mock(HandlerInput.class);
    private ShoppingList shoppingList = new ShoppingList();

    @Before
    public void before() {
        HandlerTestHelper.buildInput("removerecipe.json", input);
    }


    @Test
    public void remove_Empty_ShoppingList() {
        RemoveRecipeIntentHandler handler = new RemoveRecipeIntentHandler();
        Optional<Response> responseOptional = handler.handle(input);

        assertTrue(responseOptional.isPresent());
        final Card card = responseOptional.get().getCard();
        Assert.assertTrue(card instanceof SimpleCard);
        Assert.assertEquals(String.format(RECIPE_REMOVE_NOT_FOUND,"currywurst"), ((SimpleCard) card).getContent());
    }

    @Test
    public void remove_Recipe_ShoppingList() {
        RemoveRecipeIntentHandler handler = new RemoveRecipeIntentHandler();
        Optional<Response> responseOptional = handler.handle(input);

        assertTrue(responseOptional.isPresent());
        final Card card = responseOptional.get().getCard();
        Assert.assertTrue(card instanceof SimpleCard);
        Assert.assertTrue(((SimpleCard) card).getContent().contains("currywurst"));
    }

    @Test
    public void testRemove() {
        ShoppingListService listService = new ShoppingListService();
        listService.addRecipe("Currywurst", shoppingList);

        byte[] shoppingListAsBytes = ModelService.toBinary(shoppingList);
        ShoppingList actual = (ShoppingList) ModelService.fromBinary(shoppingListAsBytes, ShoppingList.class);

        assertEquals(shoppingList, actual);

        listService.removeRecipe("Currywurst", shoppingList);
        shoppingListAsBytes = ModelService.toBinary(shoppingList);
        actual = (ShoppingList) ModelService.fromBinary(shoppingListAsBytes, ShoppingList.class);

        assertEquals(shoppingList, actual);
    }

}