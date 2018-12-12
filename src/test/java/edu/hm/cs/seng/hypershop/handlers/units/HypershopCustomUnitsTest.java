package edu.hm.cs.seng.hypershop.handlers.units;

import org.junit.Assert;
import org.junit.Test;
import tec.units.ri.format.SimpleUnitFormat;

import static edu.hm.cs.seng.hypershop.handlers.units.HypershopCustomUnits.GLASS;
import static edu.hm.cs.seng.hypershop.handlers.units.HypershopCustomUnits.TEASPOON;

public class HypershopCustomUnitsTest {

    @Test
    public void addUnitToInstances() {
        HypershopCustomUnits customUnits = new HypershopCustomUnits();
        Assert.assertTrue(customUnits.getUnits().size() > 1);
        Assert.assertEquals(GLASS, SimpleUnitFormat.getInstance().parse("glas"));
        Assert.assertEquals(TEASPOON, SimpleUnitFormat.getInstance().parse("teelöffel"));
    }

}