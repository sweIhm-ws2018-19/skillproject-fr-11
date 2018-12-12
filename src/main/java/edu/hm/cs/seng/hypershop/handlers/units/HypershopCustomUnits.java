package edu.hm.cs.seng.hypershop.handlers.units;

import tec.units.ri.format.SimpleUnitFormat;
import tec.units.ri.function.MultiplyConverter;
import tec.units.ri.unit.BaseUnit;
import tec.units.ri.unit.TransformedUnit;
import tec.units.ri.unit.Units;

import javax.measure.*;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HypershopCustomUnits extends Units {

    private static final HashSet<Unit<?>> UNITS = new HashSet<>();

    private static final HypershopCustomUnits INSTANCE = new HypershopCustomUnits();

    public static final Unit<Volume> GLASS = myUnits(new TransformedUnit<Volume>("glas", CUBIC_METRE, CUBIC_METRE, new MultiplyConverter(0.0002)));
    public static final Unit<Volume> TEASPOON = myUnits(new TransformedUnit<Volume>("teelöffel", CUBIC_METRE, CUBIC_METRE, new MultiplyConverter(0.000005)));
    public static final Unit<Mass> PIECE = myUnits(new BaseUnit<Mass>("stück", ""));


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

    public Set<Unit<?>> getCustomUnits() {
        return UNITS;
    }

    private static <U extends Unit<?>> U myUnits(U unit) {
        UNITS.add(unit);
        SimpleUnitFormat.getInstance().label(unit, unit.getSymbol());
        return unit;
    }



}
