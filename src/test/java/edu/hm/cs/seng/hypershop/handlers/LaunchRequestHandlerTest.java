package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class LaunchRequestHandlerTest {


    private HandlerInput input = mock(HandlerInput.class);

    @Before
    public void before() {
        HandlerTestHelper.buildInput("hypershopopen.json", input);
    }

    @Test
    public void testLaunch() {
        LaunchRequestHandler handler = new LaunchRequestHandler();

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));

        HandlerTestHelper.compareSSML(SpeechTextConstants.LAUNCH_TEXT, responseString);
    }

    @Test
    public void canHandle() {
        LaunchRequestHandler handler = new LaunchRequestHandler();
        Assert.assertTrue(handler.canHandle(input));
    }
}
