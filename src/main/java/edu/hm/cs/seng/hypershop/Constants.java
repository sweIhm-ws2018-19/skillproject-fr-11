package edu.hm.cs.seng.hypershop;

public class Constants {


    private Constants() {
        // disable public constructor
    }

    static final String SKILL_ID = "amzn1.ask.skill.83f45138-33db-4af6-874d-c78af275fa85";
    static final String DYNAMO_TABLE_NAME = "hypershopData";

    public static final String INTENT_ADD_INGREDIENT = "AddIngredientIntent";
    public static final String INTENT_ADD_RECIPES = "AddRecipesIntent";
    public static final String INTENT_CREATE_RECIPE = "CreateRecipeIntent";
    public static final String INTENT_REMOVE_INGREDIENT = "RemoveIngredientIntent";
    public static final String INTENT_LIST_INGREDIENTS = "ListIngredientsIntent";
    public static final String INTENT_LIST_STEP_INGREDIENTS = "ListStepIngredientsIntent";
    public static final String INTENT_BACK = "BackIntent";
    public static final String INTENT_EDIT_RECIPE = "EditRecipeIntent";
    public static final String INTENT_REMOVE_RECIPE = "RemoveRecipeIntent";
    public static final String INTENT_DELETE_RECIPE = "DeleteRecipeIntent";
    public static final String INTENT_LIST_RECIPES = "ListRecipesIntent";
    public static final String INTENT_LIST_CLEAR = "ClearListIntent";
    public static final String INTENT_HELP_RECIPES = "HelpRecipesIntent";
    public static final String INTENT_HELP_INGREDIENTS = "HelpIngredientsIntent";
    public static final String INTENT_REPEAT = "RepeatIntent";
    public static final String INTENT_NEXT_INGREDIENT = "NextIngredientIntent";


    public static final String SLOT_AMOUNT = "amount";
    public static final String SLOT_UNIT = "unit";
    public static final String SLOT_INGREDIENT = "ingredient";
    public static final String SLOT_RECIPE = "recipe";

    public static final String KEY_SHOPPING_LIST = "shopping_list";

    public static final String KEY_STACK_POINTER = "stack_pointer"; // initially -1
    public static final String STACK_BASE_POINTER_PREFIX = "SPTR_";

    public static final String CONTEXT_RECIPE = "context_recipe";
    public static final String CONTEXT_STEPS = "context_steps";
}
