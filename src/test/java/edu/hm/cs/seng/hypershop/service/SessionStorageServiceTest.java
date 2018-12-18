package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.handlers.HandlerTestHelper;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Pair;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SessionStorageServiceTest {

    private HandlerInput input = Mockito.mock(HandlerInput.class);

    @Test
    public void currentRecipeInitiallyNull() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);

        final String currentRecipe = SessionStorageService.getCurrentRecipe(input);
        assertNull(currentRecipe);
    }

    @Test
    public void shouldBeSavedAndRetrived() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);

        SessionStorageService.setCurrentRecipe(input, "lebkuchen");
        assertEquals("lebkuchen", SessionStorageService.getCurrentRecipe(input));
    }

    @Test
    public void shouldOverride() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);

        SessionStorageService.setCurrentRecipe(input, "foobar");
        assertEquals("foobar", SessionStorageService.getCurrentRecipe(input));
        SessionStorageService.setCurrentRecipe(input, "lebkuchen");
        assertEquals("lebkuchen", SessionStorageService.getCurrentRecipe(input));
    }

    @Test
    public void shouldStoreConfirmationRequest() {
        HandlerTestHelper.buildInput("requestclearlist.json", input);

        SessionStorageService.requestConfirmation(input);
        assertTrue(SessionStorageService.needsConfirmation(input, Constants.INTENT_LIST_CLEAR));
    }

    @Test
    public void shouldClearConfirmationRequest() {
        HandlerTestHelper.buildInput("requestclearlist.json", input);

        assertFalse(SessionStorageService.needsConfirmation(input, Constants.INTENT_LIST_CLEAR));
        assertFalse(SessionStorageService.clearConfirmationRequest(input));

        SessionStorageService.requestConfirmation(input);
        assertTrue(SessionStorageService.needsConfirmation(input, Constants.INTENT_LIST_CLEAR));

        assertTrue(SessionStorageService.clearConfirmationRequest(input));
        assertFalse(SessionStorageService.needsConfirmation(input, Constants.INTENT_LIST_CLEAR));

        assertFalse(SessionStorageService.clearConfirmationRequest(input));
        assertFalse(SessionStorageService.needsConfirmation(input, Constants.INTENT_LIST_CLEAR));
    }

    @Test
    public void getInitialCheckingListIndex() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);
        assertEquals(0, SessionStorageService.getIngredientOutputIndex(input));
    }

    @Test
    public void getUpdatedCheckingListIndex() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);
        SessionStorageService.setIngredientOutputIndex(input, 2);
        assertEquals(2, SessionStorageService.getIngredientOutputIndex(input));
    }

    @Test
    public void getTwiceUpdatedCheckingListIndex() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);
        SessionStorageService.setIngredientOutputIndex(input, 2);
        assertEquals(2, SessionStorageService.getIngredientOutputIndex(input));
        SessionStorageService.setIngredientOutputIndex(input, 49);
        assertEquals(49, SessionStorageService.getIngredientOutputIndex(input));
    }

    @Test
    public void getNullCheckingList() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);
        assertNull(SessionStorageService.getCheckingList(input));
    }

    @Test(expected = NullPointerException.class)
    public void storeNullCheckingList() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);
        SessionStorageService.storeCheckingList(input, null);
        assertNull(SessionStorageService.getCheckingList(input));
    }

    @Test
    public void storeEmptyCheckingList() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);
        final ArrayList<Pair<IngredientAmount, Boolean>> entries = new ArrayList<>();
        SessionStorageService.storeCheckingList(input, entries);

        assertEquals(entries, SessionStorageService.getCheckingList(input));
    }

    @Test
    public void storeN1CheckingList() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);
        final ArrayList<Pair<IngredientAmount, Boolean>> entries = new ArrayList<>();
        entries.add(new Pair<>(new IngredientAmount(2, "glas", "Kekse"), false));
        SessionStorageService.storeCheckingList(input, entries);

        assertEquals(entries, SessionStorageService.getCheckingList(input));
    }

    @Test
    public void storeN3CheckingList() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);
        final ArrayList<Pair<IngredientAmount, Boolean>> entries = new ArrayList<>();
        entries.add(new Pair<>(new IngredientAmount(2, "glas", "Kekse"), false));
        entries.add(new Pair<>(new IngredientAmount(2, "gramm", "Äpfel"), false));
        entries.add(new Pair<>(new IngredientAmount(2, "kilo", "Bananen"), false));
        SessionStorageService.storeCheckingList(input, entries);

        assertEquals(entries, SessionStorageService.getCheckingList(input));
    }
}