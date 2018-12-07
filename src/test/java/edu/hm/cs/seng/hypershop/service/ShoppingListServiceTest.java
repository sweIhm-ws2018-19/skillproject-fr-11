package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.*;

public class ShoppingListServiceTest {

    private final ShoppingList shoppingList = new ShoppingList();
    private final ShoppingListService service = new ShoppingListService(shoppingList);

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

    @Test
    public void constructorLoadsPersistentData() {
        final ModelService modelService = Mockito.mock(ModelService.class);
        new ShoppingListService(modelService);
        Mockito.verify(modelService, Mockito.times(1)).get(Mockito.anyString(), Mockito.any());
    }

    @Test
    public void shouldLoadEmptyList() {
        shoppingList.getRecipes().put(new Recipe("Lebkuchen"), 0);
        assertEquals(1, service.getRecipeStrings().size());

        final ShoppingList newSl = new ShoppingList();
        newSl.getRecipes().put(new Recipe("Lebkuchen"), 0);
        newSl.getRecipes().put(new Recipe("Bockwurst"), 0);

        final ModelService modelService = Mockito.mock(ModelService.class);
        Mockito.when(modelService.get(Mockito.anyString(), Mockito.any())).thenReturn(newSl);
        service.load(modelService);
        assertEquals(2, service.getRecipeStrings().size());
    }

    @Test
    public void shouldRemoveIngredients() {
        final IngredientAmount ingredientAmount = new IngredientAmount();
        ingredientAmount.setName("Kekse");
        ingredientAmount.setAmount(1);
        ingredientAmount.setUnit("glas");
        shoppingList.addIngredient(ingredientAmount);

        assertEquals(Collections.singleton(ingredientAmount), shoppingList.getIngredients("Kekse"));
        assertEquals(Collections.singleton(ingredientAmount), shoppingList.getIngredients());

        final boolean found = service.removeIngredient("Kekse");
        assertTrue(found);

        assertEquals(Collections.emptySet(), shoppingList.getIngredients("Kekse"));
        assertEquals(Collections.emptySet(), shoppingList.getIngredients());
    }

    @Test
    public void shouldRemoveIngredientsCase() {
        final IngredientAmount ingredientAmount = new IngredientAmount();
        ingredientAmount.setName("Kekse");
        ingredientAmount.setAmount(1);
        ingredientAmount.setUnit("glas");
        shoppingList.addIngredient(ingredientAmount);

        assertEquals(Collections.singleton(ingredientAmount), shoppingList.getIngredients("Kekse"));
        assertEquals(Collections.singleton(ingredientAmount), shoppingList.getIngredients());

        final boolean found = service.removeIngredient("kekse");
        assertTrue(found);

        assertEquals(Collections.emptySet(), shoppingList.getIngredients("Kekse"));
        assertEquals(Collections.emptySet(), shoppingList.getIngredients());
    }

    @Test
    public void shouldNotRemoveNotExistingIngredients() {
        final IngredientAmount ingredientAmount = new IngredientAmount();
        ingredientAmount.setName("Kekse");
        ingredientAmount.setAmount(1);
        ingredientAmount.setUnit("glas");
        shoppingList.addIngredient(ingredientAmount);

        assertEquals(Collections.singleton(ingredientAmount), shoppingList.getIngredients("Kekse"));
        assertEquals(Collections.singleton(ingredientAmount), shoppingList.getIngredients());

        final boolean found = service.removeIngredient("Blumen");
        assertFalse(found);

        assertEquals(Collections.singleton(ingredientAmount), shoppingList.getIngredients("Kekse"));
        assertEquals(Collections.singleton(ingredientAmount), shoppingList.getIngredients());
    }

    @Test
    public void removeIngredientFromNotExistingRecipe() {
        assertFalse(service.removeIngredientFromRecipe("Kekse", "Lebkuchen"));
    }

    @Test
    public void removeIngredientFromRecipe() {
        final Recipe lebkuchen = new Recipe("Lebkuchen");
        final IngredientAmount ingredientAmount = new IngredientAmount();
        ingredientAmount.setName("Kekse");
        ingredientAmount.setAmount(1);
        ingredientAmount.setUnit("glas");
        final IngredientAmount ingredientAmount2 = new IngredientAmount();
        ingredientAmount2.setName("Bockwurst");
        ingredientAmount2.setAmount(10);
        ingredientAmount2.setUnit("liter");

        lebkuchen.addIngredient(ingredientAmount);
        lebkuchen.addIngredient(ingredientAmount2);

        shoppingList.getRecipes().put(lebkuchen, 0);

        assertEquals(Collections.singletonMap(lebkuchen, 0), shoppingList.getRecipes());

        final boolean success = service.removeIngredientFromRecipe("Kekse", "Lebkuchen");
        assertTrue(success);

        assertEquals(Collections.singleton(new AbstractMap.SimpleEntry<>(lebkuchen, 0)), shoppingList.getRecipes().entrySet());

        final Recipe recipe = shoppingList.getRecipes().entrySet().iterator().next().getKey();
        assertEquals(Collections.singleton(ingredientAmount2), recipe.getIngredients());
    }

    @Test
    public void removeNonExistingIngredientFromRecipe() {
        final Recipe lebkuchen = new Recipe("Lebkuchen");
        final IngredientAmount ingredientAmount = new IngredientAmount();
        ingredientAmount.setName("Kekse");
        ingredientAmount.setAmount(1);
        ingredientAmount.setUnit("glas");

        lebkuchen.addIngredient(ingredientAmount);

        shoppingList.getRecipes().put(lebkuchen, 0);

        assertEquals(Collections.singletonMap(lebkuchen, 0), shoppingList.getRecipes());

        final boolean success = service.removeIngredientFromRecipe("Bockwurst", "Lebkuchen");
        assertFalse(success);

        assertEquals(Collections.singletonMap(lebkuchen, 0), shoppingList.getRecipes());

        final Recipe recipe = shoppingList.getRecipes().entrySet().iterator().next().getKey();
        assertEquals(Collections.singleton(ingredientAmount), recipe.getIngredients());
    }

    @Test
    public void containsRecipe() {
        shoppingList.getRecipes().put(new Recipe("Lebkuchen"), 0);

        assertTrue(service.containsRecipe("Lebkuchen"));
        assertTrue(service.containsRecipe("lebkucHen"));

        assertFalse(service.containsRecipe("Schokolade"));
    }

    private void deleteRecipeImpl(String deleteString) {
        final Recipe lebkuchen = new Recipe("Lebkuchen");
        final IngredientAmount ingredientAmount = new IngredientAmount();
        ingredientAmount.setName("Kekse");
        ingredientAmount.setAmount(1);
        ingredientAmount.setUnit("glas");

        lebkuchen.addIngredient(ingredientAmount);

        shoppingList.getRecipes().put(lebkuchen, 0);

        assertEquals(Collections.singletonMap(lebkuchen, 0), shoppingList.getRecipes());

        final boolean success = service.deleteRecipe(deleteString);
        assertTrue(success);

        assertEquals(Collections.emptySet(), shoppingList.getRecipes().entrySet());
    }

    @Test
    public void deleteRecipe() {
        deleteRecipeImpl("Lebkuchen");
    }

    @Test
    public void deleteRecipeCase() {
        deleteRecipeImpl("lebKucheN");
    }

    @Test
    public void deleteNonExistingRecipe() {
        assertFalse(service.deleteRecipe("Lebkuchen"));
    }

    private void addRecipesImpl(int amount) {
        final Recipe lebkuchen = new Recipe("Lebkuchen");
        final IngredientAmount ingredientAmount = new IngredientAmount();
        ingredientAmount.setName("Kekse");
        ingredientAmount.setAmount(1);
        ingredientAmount.setUnit("glas");
        shoppingList.getRecipes().put(lebkuchen, 0);

        assertEquals(Collections.singletonMap(lebkuchen, 0), shoppingList.getRecipes());

        final boolean success = service.addRecipes("Lebkuchen", amount);
        assertTrue(success);

        assertEquals(Collections.singletonMap(lebkuchen, amount), shoppingList.getRecipes());
    }

    @Test
    public void add1Recipes() {
        addRecipesImpl(1);
    }

    @Test
    public void add99Recipes() {
        addRecipesImpl(99);
    }

    @Test(expected = IllegalArgumentException.class)
    public void add0Recipes() {
        addRecipesImpl(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNegativeRecipes() {
        service.addRecipes("Lebkuchen", -1);
    }

    @Test
    public void addNonExistingRecipe() {
        assertFalse(service.addRecipes("Lebkuchen", 1));
    }

    private void removeRecipesImpl(int initial, int remaining) {
        final Recipe lebkuchen = new Recipe("Lebkuchen");
        final IngredientAmount ingredientAmount = new IngredientAmount();
        ingredientAmount.setName("Kekse");
        ingredientAmount.setAmount(1);
        ingredientAmount.setUnit("glas");
        shoppingList.getRecipes().put(lebkuchen, initial);

        assertEquals(Collections.singletonMap(lebkuchen, initial), shoppingList.getRecipes());

        final boolean success = service.removeRecipes("Lebkuchen");
        assertTrue(success);

        assertEquals(Collections.singletonMap(lebkuchen, remaining), shoppingList.getRecipes());
    }

    @Test
    public void remove1Recipe() {
        removeRecipesImpl(1, 0);
    }


    @Test
    public void remove100Recipes() {
        removeRecipesImpl(100, 0);
    }

    @Test
    public void removeNonexistingRecipe() {
        assertFalse(service.removeRecipes("Lebkuchen"));
    }

    @Test
    public void getRecipeStringsEmpty() {
        assertEquals(0, service.getRecipeStrings().size());
    }

    @Test
    public void getRecipeStringsTwo() {
        shoppingList.getRecipes().put(new Recipe("Lebkuchen"), 0);
        shoppingList.getRecipes().put(new Recipe("Kekkuchen"), 10);
        shoppingList.getRecipes().put(new Recipe("Fookuchen"), 15);
        assertEquals(new HashSet<>(Arrays.asList("Lebkuchen", "Kekkuchen", "Fookuchen")), new HashSet<>(service.getRecipeStrings()));
    }

    @Test
    public void getAddedRecipeStringsEmpty() {
        assertEquals(0, service.getAddedRecipeStrings().size());
    }

    @Test
    public void getGetAddedRecipeStrings() {
        shoppingList.getRecipes().put(new Recipe("Lebkuchen"), 0);
        shoppingList.getRecipes().put(new Recipe("Kekkuchen"), 10);
        shoppingList.getRecipes().put(new Recipe("Fookuchen"), 15);
        assertEquals(new HashSet<>(Arrays.asList("Kekkuchen", "Fookuchen")), new HashSet<>(service.getAddedRecipeStrings()));
    }

    @Test
    public void getAddedRecipesWithAmountEmpty() {
        assertEquals(0, service.getAddedRecipesWithAmount().size());
    }

    @Test
    public void getGetAddedRecipesWithAmount() {
        shoppingList.getRecipes().put(new Recipe("Lebkuchen"), 0);
        shoppingList.getRecipes().put(new Recipe("Kekkuchen"), 10);
        shoppingList.getRecipes().put(new Recipe("Fookuchen"), 15);
        assertEquals(new HashSet<>(Arrays.asList(
                new AbstractMap.SimpleEntry<>("Kekkuchen", 10),
                new AbstractMap.SimpleEntry<>("Fookuchen", 15))), new HashSet<>(service.getAddedRecipesWithAmount()));
    }


}