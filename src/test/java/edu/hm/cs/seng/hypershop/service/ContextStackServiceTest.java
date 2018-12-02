package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.handlers.HandlerTestHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ContextStackServiceTest {

    private HandlerInput input = Mockito.mock(HandlerInput.class);

    @Before
    public void setUp() {
        HandlerTestHelper.buildInput("addingredients.json", input);
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