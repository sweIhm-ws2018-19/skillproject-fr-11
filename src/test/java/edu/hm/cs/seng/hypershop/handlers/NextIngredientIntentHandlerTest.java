package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.SpeechTextConstants;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Pair;
import edu.hm.cs.seng.hypershop.service.ContextStackService;
import edu.hm.cs.seng.hypershop.service.ModelServiceTest;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class NextIngredientIntentHandlerTest {
    private final HandlerInput input = mock(HandlerInput.class);
    private final NextIngredientIntentHandler handler = new NextIngredientIntentHandler();

    @Test
    public void shouldNotHandleWithoutContext() {
        HandlerTestHelper.buildInput("nextingredient.json", input);
        assertFalse(handler.canHandle(input));
    }

    @Test
    public void shouldHandleWithContext() {
        HandlerTestHelper.buildInput("nextingredient.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_STEPS);
        assertTrue(handler.canHandle(input));
    }

    @Test
    public void shouldStayAtSameN1List() {
        HandlerTestHelper.buildInput("nextingredient.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_STEPS);

        final Map<String, Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        final ArrayList<Pair<IngredientAmount, Boolean>> checkingList = new ArrayList<>();
        checkingList.add(new Pair<>(new IngredientAmount(3, "kg", "Lebkuchen"), false));
        sessionMap.put("checking_list", ModelServiceTest.toBinary(checkingList));
        input.getAttributesManager().setSessionAttributes(sessionMap);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML("3 kg Lebkuchen", responseString);

        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        assertEquals(0, sessionAttributes.get("ingredient_output_index"));

        assertEquals(checkingList, ModelServiceTest.fromBinary(sessionAttributes.get("checking_list"), ArrayList.class));
    }

    @Test
    public void shouldPrintSecondEntryN2List() {
        HandlerTestHelper.buildInput("nextingredient.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_STEPS);

        final Map<String, Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        final ArrayList<Pair<IngredientAmount, Boolean>> checkingList = new ArrayList<>();
        checkingList.add(new Pair<>(new IngredientAmount(3, "kg", "Lebkuchen"), false));
        checkingList.add(new Pair<>(new IngredientAmount(20, "gramm", "Mehl"), false));
        sessionMap.put("checking_list", ModelServiceTest.toBinary(checkingList));
        sessionMap.put("ingredient_output_index", 0);
        input.getAttributesManager().setSessionAttributes(sessionMap);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML("20 gramm Mehl", responseString);

        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        assertEquals(1, sessionAttributes.get("ingredient_output_index"));

        assertEquals(checkingList, ModelServiceTest.fromBinary(sessionAttributes.get("checking_list"), ArrayList.class));
    }

    @Test
    public void shouldPrintFirstEntryN2List() {
        HandlerTestHelper.buildInput("nextingredient.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_STEPS);

        final Map<String, Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        final ArrayList<Pair<IngredientAmount, Boolean>> checkingList = new ArrayList<>();
        checkingList.add(new Pair<>(new IngredientAmount(3, "kg", "Lebkuchen"), false));
        checkingList.add(new Pair<>(new IngredientAmount(20, "gramm", "Mehl"), false));
        sessionMap.put("checking_list", ModelServiceTest.toBinary(checkingList));
        sessionMap.put("ingredient_output_index", 1);
        input.getAttributesManager().setSessionAttributes(sessionMap);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML("3 kg Lebkuchen", responseString);

        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        assertEquals(0, sessionAttributes.get("ingredient_output_index"));

        assertEquals(checkingList, ModelServiceTest.fromBinary(sessionAttributes.get("checking_list"), ArrayList.class));
    }

    @Test
    public void shouldIgnoreCheckedIngredient() {
        HandlerTestHelper.buildInput("nextingredient.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_STEPS);

        final Map<String, Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        final ArrayList<Pair<IngredientAmount, Boolean>> checkingList = new ArrayList<>();
        checkingList.add(new Pair<>(new IngredientAmount(3, "kg", "Lebkuchen"), false));
        checkingList.add(new Pair<>(new IngredientAmount(500, "gramm", "Mandeln"), true));
        checkingList.add(new Pair<>(new IngredientAmount(20, "gramm", "Mehl"), false));
        sessionMap.put("checking_list", ModelServiceTest.toBinary(checkingList));
        sessionMap.put("ingredient_output_index", 0);
        input.getAttributesManager().setSessionAttributes(sessionMap);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML("20 gramm Mehl", responseString);

        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        assertEquals(2, sessionAttributes.get("ingredient_output_index"));

        assertEquals(checkingList, ModelServiceTest.fromBinary(sessionAttributes.get("checking_list"), ArrayList.class));
    }

    @Test
    public void shouldIgnoreCheckedIngredientWraparound() {
        HandlerTestHelper.buildInput("nextingredient.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_STEPS);

        final Map<String, Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        final ArrayList<Pair<IngredientAmount, Boolean>> checkingList = new ArrayList<>();
        checkingList.add(new Pair<>(new IngredientAmount(3, "kg", "Lebkuchen"), false));
        checkingList.add(new Pair<>(new IngredientAmount(500, "gramm", "Mandeln"), true));
        checkingList.add(new Pair<>(new IngredientAmount(20, "gramm", "Mehl"), false));
        sessionMap.put("checking_list", ModelServiceTest.toBinary(checkingList));
        sessionMap.put("ingredient_output_index", 2);
        input.getAttributesManager().setSessionAttributes(sessionMap);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML("3 kg Lebkuchen", responseString);

        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        assertEquals(0, sessionAttributes.get("ingredient_output_index"));

        assertEquals(checkingList, ModelServiceTest.fromBinary(sessionAttributes.get("checking_list"), ArrayList.class));
    }

    @Test
    public void shouldExitOnN0List() {
        HandlerTestHelper.buildInput("nextingredient.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_STEPS);

        final Map<String, Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        final ArrayList<Pair<IngredientAmount, Boolean>> checkingList = new ArrayList<>();
        checkingList.add(new Pair<>(new IngredientAmount(3, "kg", "Lebkuchen"), true));
        sessionMap.put("checking_list", ModelServiceTest.toBinary(checkingList));
        input.getAttributesManager().setSessionAttributes(sessionMap);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(SpeechTextConstants.NEXT_EMPTY, responseString);

        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();

        assertEquals(checkingList, ModelServiceTest.fromBinary(sessionAttributes.get("checking_list"), ArrayList.class));
        assertTrue(ContextStackService.isCurrentContext(input, null));
    }
}