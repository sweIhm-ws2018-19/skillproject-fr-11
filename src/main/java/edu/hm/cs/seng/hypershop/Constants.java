package edu.hm.cs.seng.hypershop;

public class Constants {
    private Constants() {
        // disable public constructor
    }

    static final String SKILL_ID = "amzn1.ask.skill.83f45138-33db-4af6-874d-c78af275fa85";
    static final String DYNAMO_TABLE_NAME = "hypershopData";

    public static final String INTENT_ADD_INGREDIENT = "AddIngredientIntent";
    public static final String INTENT_ADD_RECIPE = "AddRecipeIntent";
    public static final String INTENT_REMOVE_INGREDIENT = "RemoveIngredientIntent";
    public static final String INTENT_LIST_INGREDIENTS = "ListIngredientsIntent";
    public static final String INTENT_LIST_RECIPES = "ListRecipesIntent";
    public static final String INTENT_BACK = "BackIntent";

    public static final String SLOT_AMOUNT = "amount";
    public static final String SLOT_UNIT = "unit";
    public static final String SLOT_INGREDIENT = "ingredient";
    public static final String SLOT_RECIPE = "recipe";

    public static final String KEY_SHOPPING_LIST = "shopping_list";

    public static final String KEY_STACK_POINTER = "stack_pointer"; // initially -1
    public static final String STACK_BASE_POINTER_PREFIX = "SPTR_";

    public static final String CONTEXT_RECIPE = "context_recipe";
}
