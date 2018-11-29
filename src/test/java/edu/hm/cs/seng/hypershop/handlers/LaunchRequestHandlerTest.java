package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SimpleCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LaunchRequestHandlerTest {


    private HandlerInput input = mock(HandlerInput.class);

    @Before
    public void before() {

        ObjectMapper objectMapper = new ObjectMapper();
        RequestEnvelope requestEnvelope = null;
        try {
            objectMapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);

            ClassLoader classLoader = getClass().getClassLoader();
            String file1 = Objects.requireNonNull(classLoader.getResource("hypershopopen.json")).getFile();
            requestEnvelope = objectMapper.readValue(new FileReader(file1), RequestEnvelope.class);
        } catch (IOException e) {
            System.out.println(e);
            Assert.fail();
        }
        HandlerTestHelper.buildInput(requestEnvelope, input);
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
        when(input.matches(any())).thenReturn(true);
        Assert.assertTrue(handler.canHandle(input));
    }
}
