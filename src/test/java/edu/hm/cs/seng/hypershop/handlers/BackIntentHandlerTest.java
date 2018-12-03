package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;

public class BackIntentHandlerTest {
    private HandlerInput input = Mockito.mock(HandlerInput.class);

    @Test
    public void shouldNotPopEmptyStack() {
        HandlerTestHelper.buildInput("back.json", input);

        final BackIntentHandler handler = new BackIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(SpeechTextConstants.BACK_OK, responseString);

        assertTrue(ContextStackService.isCurrentContext(input, null));
    }

    @Test
    public void shouldPopOneStack() {
        HandlerTestHelper.buildInput("back.json", input);
        ContextStackService.pushContext(input, "foobar");

        final BackIntentHandler handler = new BackIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(SpeechTextConstants.BACK_OK, responseString);

        assertTrue(ContextStackService.isCurrentContext(input, null));
    }

    @Test
    public void shouldPopTwoStack() {
        HandlerTestHelper.buildInput("back.json", input);
        ContextStackService.pushContext(input, "foo");
        ContextStackService.pushContext(input, "bar");

        final BackIntentHandler handler = new BackIntentHandler();
        assertTrue(handler.canHandle(input));

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(SpeechTextConstants.BACK_OK, responseString);

        assertTrue(ContextStackService.isCurrentContext(input, "foo"));
    }
}