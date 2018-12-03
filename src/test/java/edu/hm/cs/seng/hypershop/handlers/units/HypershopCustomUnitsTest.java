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
        Assert.assertTrue(customUnits.getUnits().size()>1);
        Assert.assertEquals(SimpleUnitFormat.getInstance().parse("glas"), GLASS);
        Assert.assertEquals(SimpleUnitFormat.getInstance().parse("teelöffel"), TEASPOON);
    }

}