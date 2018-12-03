package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SsmlOutputSpeech;
import com.amazon.ask.response.ResponseBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.cs.seng.hypershop.util.PersistenceAdapterMockImpl;
import org.junit.Assert;
import org.mockito.Mockito;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class HandlerTestHelper {

    public static void buildInput(String filename, final HandlerInput input) {
        buildInput(envelopeFromFile(filename), input);
    }

    private static void buildInput(final RequestEnvelope requestEnvelope, final HandlerInput input){
        Mockito.when(input.getRequestEnvelope()).thenReturn(requestEnvelope);
        final AttributesManager.Builder b = AttributesManager.builder();

        final AttributesManager am = b.withPersistenceAdapter(new PersistenceAdapterMockImpl()).withRequestEnvelope(requestEnvelope).build();
        final Map<String, Object> readAttributes = requestEnvelope.getSession().getAttributes();
        final Map<String, Object> newAttributesMap;
        if(readAttributes == null) {
            newAttributesMap = new HashMap<>();
        } else {
            newAttributesMap = readAttributes;
        }
        am.setSessionAttributes(newAttributesMap);
        Mockito.when(input.getAttributesManager()).thenReturn(am);
        Mockito.when(input.getResponseBuilder()).thenReturn(new ResponseBuilder());
        Mockito.when(input.matches(Mockito.any())).thenCallRealMethod();
    }

    private static RequestEnvelope envelopeFromFile(String filename) {
        final ObjectMapper objectMapper = new ObjectMapper();
        RequestEnvelope requestEnvelope = null;
        try {
            objectMapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
            final ClassLoader classLoader = HandlerTestHelper.class.getClassLoader();
            String fileContent = Objects.requireNonNull(classLoader.getResource(filename)).getFile();
            requestEnvelope = objectMapper.readValue(new FileReader(fileContent), RequestEnvelope.class);
        } catch (IOException e) {
            System.out.println(e);
            Assert.fail();
        }
        return requestEnvelope;
    }

    public static String getResponseString(Optional<Response> response) {
        return ((SsmlOutputSpeech) response.get().getOutputSpeech()).getSsml();
    }

    public static void compareSSML(String expected, String actual) {
        Assert.assertEquals("<speak>" + expected + "</speak>", actual);
    }
}
