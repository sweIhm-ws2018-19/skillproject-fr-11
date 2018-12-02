package edu.hm.cs.seng.hypershop.handlers.units;

import tec.units.ri.format.SimpleUnitFormat;
import tec.units.ri.function.MultiplyConverter;
import tec.units.ri.unit.TransformedUnit;
import tec.units.ri.unit.Units;

import javax.measure.Unit;
import javax.measure.quantity.Volume;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HypershopCustomUnits extends Units {

    private static HashSet<Unit<?>> UNITS = new HashSet<>();

    private static final HypershopCustomUnits INSTANCE = new HypershopCustomUnits();

    public static final Unit<Volume> GLASS = myUnits(new TransformedUnit<Volume>("glass", CUBIC_METRE, CUBIC_METRE, new MultiplyConverter(0.000005)));
    public static final Unit<Volume> TEASPOON = myUnits(new TransformedUnit<Volume>("tsp", CUBIC_METRE, CUBIC_METRE, new MultiplyConverter(0.000005)));

    public static HypershopCustomUnits getInstance() {
        return INSTANCE;
    }

    @Override
    public Set<Unit<?>> getUnits() {
        Set<Unit<?>> resultSet = new HashSet<>();
        resultSet.addAll(UNITS);
        resultSet.addAll(Units.getInstance().getUnits());
        return Collections.unmodifiableSet(resultSet);
    }


    private static <U extends Unit<?>> U myUnits(U unit) {
        UNITS.add(unit);
        SimpleUnitFormat.getInstance().label(unit, unit.getSymbol());
        return unit;
    }


}
