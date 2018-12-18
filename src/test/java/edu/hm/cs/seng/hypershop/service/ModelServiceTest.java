package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.model.Pair;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ModelServiceTest {

    @Test
    public void listToByteFromByteEquals() {
        final ShoppingList shoppingList = new ShoppingList();

        byte[] shoppingListAsBytes = ModelService.toBinary(shoppingList);

        final ShoppingList actual = (ShoppingList) ModelService.fromBinary(shoppingListAsBytes, ShoppingList.class);

        assertEquals(shoppingList, actual);
    }

    @Test
    public void recipeToByteFromByteEquals() {
        final Recipe recipe = new Recipe("test");

        byte[] recipeAsBytes = ModelService.toBinary(recipe);

        Recipe actual = (Recipe) ModelService.fromBinary(recipeAsBytes, recipe.getClass());

        assertEquals(recipe, actual);

    }

    @Test
    public void arrayListToByteFromByteEquals() {
        final ArrayList<String> strings = new ArrayList<>();
        strings.add("test");

        final byte[] bytes = ModelService.toBinary(strings);

        ArrayList<String> actual = (ArrayList<String>) ModelService.fromBinary(bytes, strings.getClass());

        assertEquals(strings, actual);
    }

    @Test
    public void simpleEntryToByteFromByteEquals() {
        final Pair<String, String> entry = new Pair<String, String>("key", "value");

        final byte[] bytes = ModelService.toBinary(entry);

        Pair<String, String> actual = (Pair<String, String>) ModelService.fromBinary(bytes, entry.getClass());

        assertEquals(entry, actual);
    }

    @Test
    public void arrayListMapEntryToByteFromByteEquals() {
        final ArrayList<Pair<String, String>> strings = new ArrayList<>();
        strings.add(new Pair<>("key", "value"));

        final byte[] bytes = ModelService.toBinary(strings);

        ArrayList<Pair<String, String>> actual = (ArrayList<Pair<String, String>>) ModelService.fromBinary(bytes, strings.getClass());

        assertEquals(strings, actual);
    }
}