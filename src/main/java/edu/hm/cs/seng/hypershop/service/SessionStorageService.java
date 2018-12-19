package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.IntentRequest;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Pair;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

public class SessionStorageService {
    private static final String KEY_CURRENT_RECIPE = "current_recipe";
    private static final String KEY_LAST_CONFIRMATION_REQUEST = "last_confirmation_request";
    private static final String KEY_INGREDIENT_OUTPUT_INDEX = "ingredient_output_index";
    private static final String KEY_CHECKING_LIST = "checking_list";

    private SessionStorageService() {
        // hide public ctor
    }

    private static void putValue(HandlerInput input, String key, Object value) {
        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        sessionAttributes.put(key, value);
        input.getAttributesManager().setSessionAttributes(sessionAttributes);
    }

    private static boolean removeValue(HandlerInput input, String key) {
        final Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        final Object removed = sessionAttributes.put(key, "none");
        input.getAttributesManager().setSessionAttributes(sessionAttributes);
        return removed != null && !"none".equals(removed);
    }

    private static String getValue(HandlerInput input, String key) {
        return (String) input.getAttributesManager().getSessionAttributes().get(key);
    }

    private static Object getRawValue(HandlerInput input, String key) {
        return input.getAttributesManager().getSessionAttributes().get(key);
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

    @java.lang.SuppressWarnings("squid:S1168")
    static List<Pair<IngredientAmount, Boolean>> getCheckingList(HandlerInput input) {
        Object value = getRawValue(input, KEY_CHECKING_LIST);

        if (value == null || value.equals("none")) {
            return null;
        }

        if (value instanceof String) {
            final String base64String = (String) value;
            try {
                value = Base64.getDecoder().decode(base64String);
            } catch (Exception e) {
                // will be null
            }
        }

        try {
            final Object o = ModelService.fromBinary(value, ArrayList.class);
            return (List<Pair<IngredientAmount, Boolean>>) o;
        } catch (Exception e) {
            return null;
        }
    }

    static void storeCheckingList(HandlerInput input, List<Pair<IngredientAmount, Boolean>> list) {
        final byte[] bytes = ModelService.toBinary(list);
        putValue(input, KEY_CHECKING_LIST, bytes);
    }

    static void setIngredientOutputIndex(HandlerInput input, int index) {
        putValue(input, KEY_INGREDIENT_OUTPUT_INDEX, index);
    }

    static int getIngredientOutputIndex(HandlerInput input) {
        final Object value = getRawValue(input, KEY_INGREDIENT_OUTPUT_INDEX);

        if (value == null) {
            return 0;
        }

        try {
            return (Integer) value;
        } catch (ClassCastException e) {
            return 0;
        }
    }

    public static void resetIngredientOutput(HandlerInput input) {
        setIngredientOutputIndex(input, 0);
        removeValue(input, KEY_CHECKING_LIST);
    }
}
