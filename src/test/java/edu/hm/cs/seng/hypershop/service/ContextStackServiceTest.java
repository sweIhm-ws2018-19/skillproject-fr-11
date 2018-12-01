package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.response.ResponseBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hm.cs.seng.hypershop.handlers.HandlerTestHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ContextStackServiceTest {

    private HandlerInput input = Mockito.mock(HandlerInput.class);

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        RequestEnvelope requestEnvelope = null;
        try {
            objectMapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);
            ClassLoader classLoader = getClass().getClassLoader();
            String file1 = Objects.requireNonNull(classLoader.getResource("addingredients.json")).getFile();
            requestEnvelope = objectMapper.readValue(new FileReader(file1), RequestEnvelope.class);
        } catch (IOException e) {
            System.out.println(e);
            Assert.fail();
        }

        Mockito.when(input.getRequestEnvelope()).thenReturn(requestEnvelope);
        final AttributesManager.Builder b = AttributesManager.builder();
        final AttributesManager am = b.withRequestEnvelope(requestEnvelope).build();
        am.setSessionAttributes(new HashMap<>());
        Mockito.when(input.getAttributesManager()).thenReturn(am);
        Mockito.when(input.getResponseBuilder()).thenReturn(new ResponseBuilder());
    }

    @Test
    public void sessionStoreTest() {
        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        assertEquals(0, sessionAttributes.size());
        sessionAttributes.put("test_key", "Peter");
        assertEquals(1, sessionAttributes.size());
        assertEquals("Peter", sessionAttributes.get("test_key"));
    }

    @Test
    public void emptyStack() {
        final boolean actual = ContextStackService.isCurrentContext(input, "some_context");
        assertFalse(actual);
        assertTrue(ContextStackService.isCurrentContext(input, null));
    }

    @Test
    public void pushOneToStack() {
        ContextStackService.pushContext(input, "first_context");
        assertTrue(ContextStackService.isCurrentContext(input, "first_context"));
        assertFalse(ContextStackService.isCurrentContext(input, "second_context"));
    }

    @Test
    public void pushTwoToStack() {
        pushOneToStack();
        ContextStackService.pushContext(input, "second_context");
        assertFalse(ContextStackService.isCurrentContext(input, "first_context"));
        assertTrue(ContextStackService.isCurrentContext(input, "second_context"));
    }

    @Test
    public void pushTwoPopOneStack() {
        pushTwoToStack();
        ContextStackService.popContext(input);
        assertTrue(ContextStackService.isCurrentContext(input, "first_context"));
        assertFalse(ContextStackService.isCurrentContext(input, "second_context"));
    }

    @Test
    public void pushTwoPopTwoStack() {
        pushTwoPopOneStack();
        ContextStackService.popContext(input);
        assertFalse(ContextStackService.isCurrentContext(input, "first_context"));
        assertFalse(ContextStackService.isCurrentContext(input, "second_context"));
    }

    @Test
    public void pushPopMany() {
        final int STACK_COUNT = 1000;
        for (int i = 0; i < STACK_COUNT; i++) {
            ContextStackService.pushContext(input, i + "");
        }
        for (int i = STACK_COUNT - 1; i >= 0; i--) {
            assertTrue(ContextStackService.isCurrentContext(input, i + ""));
            ContextStackService.popContext(input);
        }
        assertTrue(ContextStackService.isCurrentContext(input, null));
    }
}