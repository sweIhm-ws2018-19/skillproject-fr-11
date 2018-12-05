package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class CancelandStopIntentHandlerTest {

    private HandlerInput input = mock(HandlerInput.class);

    @Before
    public void before() {
        HandlerTestHelper.buildInput("cancel.json", input);
    }
    @Test
    public void handle() {
        CancelandStopIntentHandler handler = new CancelandStopIntentHandler();

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(SpeechTextConstants.STOP_TEXT, responseString);
    }

    @Test
    public void canHandle() {
        CancelandStopIntentHandler handler = new CancelandStopIntentHandler();
        Assert.assertTrue(handler.canHandle(input));
    }
}