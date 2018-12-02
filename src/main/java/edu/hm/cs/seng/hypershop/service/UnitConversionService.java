package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.handlers.units.HypershopCustomUnits;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import tec.units.ri.format.SimpleUnitFormat;

import javax.measure.Unit;
import javax.measure.format.ParserException;
import java.util.Set;

public class UnitConversionService {

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

    public Unit getUnit(String unitName) throws ParserException {
        return SimpleUnitFormat.getInstance().parse(unitName);
    }

    public Unit getIntelligentUnit(Unit unit, int amount) {
        return null;
    }

}
