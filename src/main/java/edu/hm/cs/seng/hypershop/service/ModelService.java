package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Recipe;
import edu.hm.cs.seng.hypershop.model.ShoppingList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

public class ModelService {

    private AttributesManager attributesManager;
    private Set<String> convertClasses = new HashSet<>(Arrays.asList(Constants.KEY_SHOPPING_LIST));

    public ModelService() {

    }

    public ModelService(HandlerInput input) {
        attributesManager = input.getAttributesManager();
    }

    public void save(Object o) {
        final Map<String, Object> pam = attributesManager.getPersistentAttributes();
        if (o instanceof ShoppingList) {
            pam.put(Constants.KEY_SHOPPING_LIST, toBinary(o));
            attributesManager.setPersistentAttributes(pam);
            attributesManager.savePersistentAttributes();
        }
    }

    public Object get(String key, Class clazz) {
        if (attributesManager != null) {
            final Map<String, Object> pam = attributesManager.getPersistentAttributes();

            if (convertClasses.contains(key)) {
                return fromBinary(pam.get(key), clazz);
            }
        }
        return null;
    }

    public static Object fromBinary(Object o, Class clazz) {
        try {
            final byte[] data = (byte[]) o;
            final Kryo kryo = getCryo(clazz);
            Input input = new Input(new ByteArrayInputStream(data));
            final Object ret = kryo.readObject(input, clazz);
            input.close();
            return ret;
        } catch (Exception e) {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] toBinary(Object o) {
        final Kryo kryo = getCryo(o.getClass());
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeObject(output, o);
        output.close();
        return baos.toByteArray();
    }

    private static Kryo getCryo(Class clazz) {
        Kryo kryo = new Kryo();
        kryo.register(IngredientAmount.class);
        kryo.register(Recipe.class);
        kryo.register(HashSet.class);
        kryo.register(ArrayList.class);
        kryo.register(clazz);
        return kryo;
    }



}
