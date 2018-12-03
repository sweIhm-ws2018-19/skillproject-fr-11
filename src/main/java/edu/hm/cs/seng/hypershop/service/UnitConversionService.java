package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.handlers.units.HypershopCustomUnits;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import tec.units.ri.format.SimpleUnitFormat;
import tec.units.ri.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.format.ParserException;

public class UnitConversionService {

    HypershopCustomUnits customUnits = new HypershopCustomUnits();

    public IngredientAmount convert(IngredientAmount ingredient, Unit unit) {
        return null;
    }

    public IngredientAmount convert(String ingredientName, String unitName) {
        return null;
    }

    public int convert(int amount, Unit unit) {
        return 0;
    }

    public Unit getNormalUnit(Unit unit) {
        return null;
    }


    public IngredientAmount summmarizeIngredients(final IngredientAmount ingredient, int amount, String unitName) {
        Unit baseUnit = getUnit(ingredient.getUnit());
        Unit addUnit = getUnit(unitName);


        Quantity<?> baseQuantity = Quantities.getQuantity(ingredient.getAmount(), baseUnit);
        Quantity addQuantity = Quantities.getQuantity(amount, addUnit);

        Quantity<?> sum = baseQuantity.add(addQuantity);
        ingredient.setAmount(sum.getValue().doubleValue());
        return ingredient;
    }

    public Unit getUnit(String unitName) throws ParserException {
        return SimpleUnitFormat.getInstance().parse(unitName);
    }

    public Unit getIntelligentUnit(Unit unit, int amount) {
        return null;
    }

}
