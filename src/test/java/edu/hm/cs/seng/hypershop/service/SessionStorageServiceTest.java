package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.handlers.HandlerTestHelper;
import org.junit.Test;
import org.mockito.Mockito;

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
}