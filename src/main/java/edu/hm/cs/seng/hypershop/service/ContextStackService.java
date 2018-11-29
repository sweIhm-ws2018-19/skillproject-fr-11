package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import edu.hm.cs.seng.hypershop.Constants;

import java.util.Map;

public class ContextStackService {
    private static int getStackPointer(Map<String, Object> sessionMap) {
        final Object o = sessionMap.get(Constants.KEY_STACK_POINTER);
        int sp;
        try {
            sp = (Integer) o;
        } catch(Exception e) {
            sp = -1;
        }
        return sp;
    }

    private static void setStackPointer(Map<String, Object> sessionMap, int sp) {
        sessionMap.put(Constants.KEY_STACK_POINTER, sp);
    }

    private static void saveSession(HandlerInput input, Map<String, Object> sessionMap) {
        input.getAttributesManager().setPersistentAttributes(sessionMap);
    }

    public static boolean isCurrentContext(HandlerInput input, String contextName) {
        final int sp = getStackPointer(input.getAttributesManager().getSessionAttributes());
        if(sp < 0) {
            return false;
        }

        final Object contextObj = input.getAttributesManager().getSessionAttributes().get(Constants.STACK_BASE_POINTER_PREFIX + sp);
        try {
            return contextObj.equals(contextName);
        } catch (Exception e) {
            return false;
        }
    }

    public static void pushContext(HandlerInput input, String contextName) {
        final Map<String, Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        final int sp = getStackPointer(sessionMap) + 1;
        setStackPointer(sessionMap, sp);
        sessionMap.put(Constants.STACK_BASE_POINTER_PREFIX + (sp + 1), contextName);
        saveSession(input, sessionMap);
    }

    public static void popContext(HandlerInput input) {
        final Map<String, Object> sessionMap = input.getAttributesManager().getSessionAttributes();
        final int sp = getStackPointer(sessionMap);
        final int newSp = sp < 0 ? -1 : sp - 1;
        setStackPointer(sessionMap, sp);
        sessionMap.remove(Constants.STACK_BASE_POINTER_PREFIX + (sp + 1));
        saveSession(input, sessionMap);
    }
}
