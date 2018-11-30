package edu.hm.cs.seng.hypershop;

public class Constants {
    private Constants() {
        // disable public constructor
    }

    public static final String SLOT_AMOUNT = "amount";
    public static final String SLOT_UNIT = "unit";
    public static final String SLOT_INGREDIENT = "ingredient";
    public static final String SLOT_RECIPE = "recipe";

    public static final String KEY_SHOPPING_LIST = "shopping_list";

    public static final String KEY_STACK_POINTER = "stack_pointer"; // initially -1
    public static final String STACK_BASE_POINTER_PREFIX = "SPTR_1";

    public static final String CONTEXT_RECIPE = "context_recipe";
}
