package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShoppingListServiceTest {

    private final ShoppingListService service = new ShoppingListService(new ShoppingList());

    @Test
    public void noExistingRecipes() {
        assertEquals(0, service.getRecipeStrings().size());
    }

    @Test
    public void addIngredients() {
        service.createRecipe("test");

        for (int index = 0; index < 10; index++) {
            String ingredientName = "ingredient" + index;
            int amount = 10 + index;
            String unitName = "kg";
            service.addIngredientRecipe(ingredientName, amount, unitName, "test");
        }

        assertEquals(1, service.getRecipeStrings().size());
        assertEquals(0, service.getIngredients().size());


        final Recipe recipe = service.getRecipe("test");
        assertEquals(10, recipe.getIngredients().size());
    }
}