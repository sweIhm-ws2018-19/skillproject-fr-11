package edu.hm.cs.seng.hypershop.model;

import javax.measure.Unit;

public class UnitConversion {

    private Unit unitConversionFrom;
    private Unit unitConversionTo;
    private double conversionFactor;

    public Unit getUnitConversionFrom() {
        return unitConversionFrom;
    }

    public void setUnitConversionFrom(Unit unitConversionFrom) {
        this.unitConversionFrom = unitConversionFrom;
    }

    public Unit getUnitConversionTo() {
        return unitConversionTo;
    }

    public void setUnitConversionTo(Unit unitConversionTo) {
        this.unitConversionTo = unitConversionTo;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }


}
