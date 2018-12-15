package edu.hm.cs.seng.hypershop.service;

import edu.hm.cs.seng.hypershop.handlers.units.HypershopCustomUnits;
import edu.hm.cs.seng.hypershop.model.IngredientAmount;
import tec.units.ri.format.SimpleUnitFormat;
import tec.units.ri.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.Unit;
import java.util.stream.Collectors;

public class UnitConversionService {

    private final HypershopCustomUnits customUnits;

    UnitConversionService() {
        customUnits = new HypershopCustomUnits();
    }

    IngredientAmount summarizeIngredients(final IngredientAmount ingredient, double amount, String unitName) {

        if (canSummarize(ingredient.getUnit(), unitName)) {
            Unit baseUnit = getUnit(ingredient.getUnit());
            Unit addUnit = getUnit(unitName);
            Quantity<?> baseQuantity = Quantities.getQuantity(ingredient.getAmount(), baseUnit);
            Quantity addQuantity = Quantities.getQuantity(amount, addUnit);

            Quantity<?> sum = baseQuantity.add(addQuantity);
            ingredient.setAmount(sum.getValue().doubleValue());
        }
        return ingredient;
    }

    boolean canSummarize(final String baseUnit, final String addUnit) {
        boolean a = customUnits.getCustomUnits().stream().map(Unit::getSymbol)
                .filter(unitName -> filterNames(unitName, baseUnit, addUnit)).collect(Collectors.toSet()).size() == 1;
        boolean b = customUnits.getCustomUnits().stream().noneMatch(unit -> unit.getSymbol().equalsIgnoreCase(baseUnit) ||
                unit.getSymbol().equalsIgnoreCase(addUnit));
        return a || b;
    }

    private boolean filterNames(String unitName, String baseUnit, String addUnit) {
        return unitName.equalsIgnoreCase(baseUnit) && unitName.equalsIgnoreCase(addUnit);
    }

    Unit getUnit(String unitName) {
        return SimpleUnitFormat.getInstance().parse(unitName);
    }

    public Unit getIntelligentUnit(Unit unit, int amount) {
        throw new UnsupportedOperationException();
    }

}
