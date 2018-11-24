package edu.hm.cs.seng.hypershop.model;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShoppingList {

    private final Set<IngredientAmount> ingredients = new HashSet<>();
    private List<Recipe> recipes;

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Set<IngredientAmount> getIngredients() {
        return ingredients;
    }

    public void addIngredient(IngredientAmount ingredient) {
        ingredients.add(ingredient);
    }

    private static Kryo getCryo() {
        Kryo kryo = new Kryo();
        kryo.register(ShoppingList.class);
        kryo.register(IngredientAmount.class);
        kryo.register(Unit.class);
        kryo.register(HashSet.class);
        kryo.register(ArrayList.class);
        return kryo;
    }

    public static ShoppingList fromBinary(Object o) {
        try {
            final byte[] data = (byte[]) o;
            final Kryo kryo = getCryo();
            Input input = new Input(new ByteArrayInputStream(data));
            final ShoppingList ret = kryo.readObject(input, ShoppingList.class);
            input.close();
            return ret;
        } catch (Exception e) {
            return new ShoppingList();
        }
    }

    public byte[] toBinary() {
        final Kryo kryo = getCryo();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeObject(output, this);
        output.close();
        return baos.toByteArray();
    }
}
