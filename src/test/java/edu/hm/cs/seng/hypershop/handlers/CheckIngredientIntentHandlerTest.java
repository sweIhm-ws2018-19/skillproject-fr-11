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

public class CheckIngredientIntentHandlerTest {
    private final HandlerInput input = mock(HandlerInput.class);
    private final CheckIngredientIntentHandler handler = new CheckIngredientIntentHandler();

    @Test
    public void shouldNotHandleWithoutContext() {
        HandlerTestHelper.buildInput("checkingredient.json", input);
        assertFalse(handler.canHandle(input));
    }

    @Test
    public void shouldHandleWithContext() {
        HandlerTestHelper.buildInput("checkingredient.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_STEPS);
        assertTrue(handler.canHandle(input));
    }

    @Test
    public void shouldNotCheckAnyInN0List() {
        HandlerTestHelper.buildInput("nextingredient.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_STEPS);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(SpeechTextConstants.CHECK_EMPTY, responseString);
    }

    @Test
    public void shouldCheckFirstInN1List() {
        HandlerTestHelper.buildInput("nextingredient.json", input);
        ContextStackService.pushContext(input, Constants.CONTEXT_STEPS);

        final Map<String, Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        final ArrayList<Pair<IngredientAmount, Boolean>> checkingList = new ArrayList<>();
        checkingList.add(new Pair<>(new IngredientAmount(3, "kg", "Lebkuchen"), false));
        sessionMap.put("checking_list", ModelServiceTest.toBinary(checkingList));
        input.getAttributesManager().setSessionAttributes(sessionMap);

        final String responseString = HandlerTestHelper.getResponseString(handler.handle(input));
        HandlerTestHelper.compareSSML(SpeechTextConstants.CHECK_OK, responseString);

        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();

        final Pair<IngredientAmount, Boolean> old = checkingList.get(0);
        checkingList.set(0, new Pair<>(old.first, true));
        assertEquals(checkingList, ModelServiceTest.fromBinary(sessionAttributes.get("checking_list"), ArrayList.class));
    }

    @Test
    public void shouldCheckFirstInN2List() {
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
        HandlerTestHelper.compareSSML(SpeechTextConstants.CHECK_OK, responseString);

        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();

        final Pair<IngredientAmount, Boolean> old = checkingList.get(0);
        checkingList.set(0, new Pair<>(old.first, true));
        assertEquals(checkingList, ModelServiceTest.fromBinary(sessionAttributes.get("checking_list"), ArrayList.class));
    }

    @Test
    public void shouldCheckSecondInN2List() {
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
        HandlerTestHelper.compareSSML(SpeechTextConstants.CHECK_OK, responseString);

        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();

        final Pair<IngredientAmount, Boolean> old = checkingList.get(1);
        checkingList.set(1, new Pair<>(old.first, true));
        assertEquals(checkingList, ModelServiceTest.fromBinary(sessionAttributes.get("checking_list"), ArrayList.class));
    }
}