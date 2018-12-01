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

public class CancelandStopIntentHandlerTest {

    private HandlerInput input = mock(HandlerInput.class);

    @Before
    public void before() {
        HandlerTestHelper.buildInput("cancel.json", input);
    }
    @Test
    public void handle() {
        CancelandStopIntentHandler handler = new CancelandStopIntentHandler();
        Optional<Response> responseOptional = handler.handle(input);

        Assert.assertTrue(responseOptional.isPresent());
        SimpleCard card = (SimpleCard) responseOptional.get().getCard();
        Assert.assertEquals(SpeechTextConstants.STOP_TEXT, card.getContent());
    }

    @Test
    public void canHandle() {
        CancelandStopIntentHandler handler = new CancelandStopIntentHandler();
        Assert.assertTrue(handler.canHandle(input));
    }
}