package edu.hm.cs.seng.hypershop.model;

import edu.hm.cs.seng.hypershop.handlers.units.HypershopCustomUnits;
import org.junit.Assert;
import org.junit.Test;
import tec.units.ri.format.SimpleUnitFormat;
import tec.units.ri.quantity.Quantities;
import tec.units.ri.unit.MetricPrefix;
import tec.units.ri.unit.Units;

import javax.measure.Quantity;
import javax.measure.quantity.Mass;

import static edu.hm.cs.seng.hypershop.handlers.units.HypershopCustomUnits.GLASS;
import static edu.hm.cs.seng.hypershop.handlers.units.HypershopCustomUnits.TEASPOON;
import static tec.units.ri.unit.Units.GRAM;

public class UnitConversionTest {

    @Test
    public void testCalculation() {
        Quantity<Mass> g = Quantities.getQuantity(2, GRAM);
        Assert.assertEquals(g.getUnit(), Units.GRAM);
        Assert.assertEquals(g.getValue(), 2);
        Quantity<Mass> kg = g.add(Quantities.getQuantity(3, MetricPrefix.KILO(GRAM)));
        Assert.assertEquals(kg.getUnit(), Units.GRAM);
        Assert.assertEquals(kg.getValue(), 3002);
        Assert.assertEquals(SimpleUnitFormat.getInstance().parse("g"), Units.GRAM);
    }

    @Test
    public void addUnitToInstances() {
        HypershopCustomUnits customUnits = new HypershopCustomUnits();
        Assert.assertTrue(customUnits.getUnits().size()>1);
        Assert.assertEquals(SimpleUnitFormat.getInstance().parse("glass"), GLASS);
        Assert.assertEquals(SimpleUnitFormat.getInstance().parse("tsp"), TEASPOON);
    }



}