package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SimpleCard;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

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
        Optional<Response> responseOptional = handler.handle(input);

        Assert.assertTrue(responseOptional.isPresent());
        SimpleCard card = (SimpleCard) responseOptional.get().getCard();
        Assert.assertEquals(SpeechTextConstants.LAUNCH_TEXT, card.getContent());
    }

    @Test
    public void canHandle() {
        LaunchRequestHandler handler = new LaunchRequestHandler();
        Assert.assertTrue(handler.canHandle(input));
    }
}
