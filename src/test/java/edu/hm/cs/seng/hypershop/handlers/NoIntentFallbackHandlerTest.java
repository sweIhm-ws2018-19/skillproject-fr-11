package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.SessionStorageService;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class NoIntentFallbackHandlerTest {

    private final HandlerInput input = mock(HandlerInput.class);
    private final NoIntentFallbackHandler handler = new NoIntentFallbackHandler();

    @Test
    public void shouldHandleNoIntent() {
        HandlerTestHelper.buildInput("nointent.json", input);
        assertTrue(handler.canHandle(input));
    }

    @Test
    public void shouldNotHandleYesIntent() {
        HandlerTestHelper.buildInput("yesintent.json", input);
        assertFalse(handler.canHandle(input));
    }

    @Test
    public void handlePendingRequest() {
        HandlerTestHelper.buildInput("nointent.json", input);
        SessionStorageService.requestConfirmation(input);
        assertTrue(SessionStorageService.needsConfirmation(input, "AMAZON.NoIntent"));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        assertFalse(SessionStorageService.needsConfirmation(input, "AMAZON.NoIntent"));
        HandlerTestHelper.compareSSML(SpeechTextConstants.NO_OK, responseString);
    }

    @Test
    public void handleNotPendingRequest() {
        HandlerTestHelper.buildInput("nointent.json", input);
        assertFalse(SessionStorageService.needsConfirmation(input, "AMAZON.NoIntent"));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        assertFalse(SessionStorageService.needsConfirmation(input, "AMAZON.NoIntent"));
        HandlerTestHelper.compareSSML(SpeechTextConstants.NO_FALLBACK, responseString);
    }
}