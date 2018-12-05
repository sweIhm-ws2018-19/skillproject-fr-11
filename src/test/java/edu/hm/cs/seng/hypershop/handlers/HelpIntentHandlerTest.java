package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class HelpIntentHandlerTest {
    private HandlerInput input = mock(HandlerInput.class);

    @Before
    public void before() {
        HandlerTestHelper.buildInput("help.json", input);
    }
    @Test
    public void handle() {
        HelpIntentHandler handler = new HelpIntentHandler();

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(SpeechTextConstants.HELP_TEXT, responseString);
    }


    @Test
    public void canHandle() {
        HelpIntentHandler handler = new HelpIntentHandler();
        Assert.assertTrue(handler.canHandle(input));
    }
}