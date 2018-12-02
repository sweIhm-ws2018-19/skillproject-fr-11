package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;

import java.util.Map;

public class SessionStorageService {
    private static final String KEY_CURRENT_RECIPE = "current_recipe";

    private SessionStorageService() {
        // hide public ctor
    }

    private static void putValue(HandlerInput input, String key, String value) {
        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        sessionAttributes.put(key, value);
        input.getAttributesManager().setSessionAttributes(sessionAttributes);
    }

    private static String getValue(HandlerInput input, String key) {
        return (String) input.getAttributesManager().getSessionAttributes().get(key);
    }

    public static void setCurrentRecipe(HandlerInput input, String recipeName) {
        putValue(input, KEY_CURRENT_RECIPE, recipeName);
    }

    public static String getCurrentRecipe(HandlerInput input) {
        return getValue(input, KEY_CURRENT_RECIPE);
    }
}
