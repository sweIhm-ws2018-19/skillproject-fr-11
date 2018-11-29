package edu.hm.cs.seng.hypershop.model;

import edu.hm.cs.seng.hypershop.service.ModelService;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class RecipeTest {

    @Test
    public void recipeToByteFromByteEquals() {
        final Recipe recipe = new Recipe();

        recipe.setName("test");

        byte[] recipeAsBytes = ModelService.toBinary(recipe);

        Recipe actual = (Recipe) ModelService.fromBinary(recipeAsBytes, recipe.getClass());

        assertEquals(recipe, actual);

    }
}
