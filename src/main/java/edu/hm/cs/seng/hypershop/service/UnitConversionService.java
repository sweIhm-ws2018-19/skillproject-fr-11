package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import edu.hm.cs.seng.hypershop.model.Unit;

public class UnitConversionService {

    public IngredientAmount convert(IngredientAmount ingredient, Unit unit){
        return null;
    }

    public IngredientAmount convert(String ingredientName, String unitName){
        return null;
    }

    public int convert(int amount, Unit unit){
        return 0;
    }

    public Unit getNormalUnit(Unit unit){
        return null;
    }

    public Unit getUnit(String unitName){
        //TODO: get Unit by name (Map)
        return new Unit(unitName);
    }

    public Unit getIntelligentUnit(Unit unit, int amount){
        return null;
    }

}
