package edu.hm.cs.seng.hypershop.service;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import edu.hm.cs.seng.hypershop.Constants;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.ShoppingList;
import edu.hm.cs.seng.hypershop.model.Unit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

public class ModelService {

    private AttributesManager attributesManager;
    private Set<String> convertClasses = new HashSet<>(Arrays.asList(Constants.KEY_SHOPPING_LIST));

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

    public Object get(String key) {
        final Map<String, Object> pam = attributesManager.getPersistentAttributes();

        if (convertClasses.contains(key)) {
            return fromBinary(pam.get(key));
        }
        return null;
    }

    public static Object fromBinary(Object o) {
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

    public static byte[] toBinary(Object o) {
        final Kryo kryo = getCryo();
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeObject(output, o);
        output.close();
        return baos.toByteArray();
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


}
