package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.SessionStorageService;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class YesIntentFallbackHandlerTest {

    private final HandlerInput input = mock(HandlerInput.class);
    private final YesIntentFallbackHandler handler = new YesIntentFallbackHandler();


    @Test
    public void shouldNotHandleYesIntent() {
        HandlerTestHelper.buildInput("yesintent.json", input);
        assertTrue(handler.canHandle(input));
    }

    @Test
    public void shouldHandleNoIntent() {
        HandlerTestHelper.buildInput("nointent.json", input);
        assertFalse(handler.canHandle(input));
    }

    @Test
    public void shouldHandleFallbackYes() {
        HandlerTestHelper.buildInput("yesintent.json", input);
        SessionStorageService.requestConfirmation(input);
        assertTrue(SessionStorageService.needsConfirmation(input, "AMAZON.YesIntent"));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        assertFalse(SessionStorageService.needsConfirmation(input, "AMAZON.YesIntent"));
        HandlerTestHelper.compareSSML(SpeechTextConstants.YES_FALLBACK, responseString);
    }
}