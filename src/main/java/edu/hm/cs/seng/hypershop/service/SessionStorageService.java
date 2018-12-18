package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;

import java.util.Map;

public class SessionStorageService {
    private static final String KEY_CURRENT_RECIPE = "current_recipe";
    private static final String KEY_LAST_CONFIRMATION_REQUEST = "last_confirmation_request";

    private SessionStorageService() {
        // hide public ctor
    }

    private static void putValue(HandlerInput input, String key, String value) {
        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        sessionAttributes.put(key, value);
        input.getAttributesManager().setSessionAttributes(sessionAttributes);
    }

    private static boolean removeValue(HandlerInput input, String key) {
        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        final Object removed = sessionAttributes.put(key, null);
        input.getAttributesManager().setSessionAttributes(sessionAttributes);
        return removed != null;
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

    public static void requestConfirmation(HandlerInput input) {
        final IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
        putValue(input, KEY_LAST_CONFIRMATION_REQUEST, intentRequest.getIntent().getName());
    }

    public static boolean needsConfirmation(HandlerInput input, String intentName) {
        final String lastConfirmationRequest = getValue(input, KEY_LAST_CONFIRMATION_REQUEST);
        return intentName.equals(lastConfirmationRequest);
    }

    public static boolean clearConfirmationRequest(HandlerInput input) {
        return removeValue(input, KEY_LAST_CONFIRMATION_REQUEST);
    }
}
