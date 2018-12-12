package edu.hm.cs.seng.hypershop.model;

import java.util.Objects;

public class IngredientAmount {

    public IngredientAmount() { }
    public IngredientAmount(double amount, String unitName, String name) {
        this.name = name;
        this.unitName = unitName;
        this.amount = amount;
    }

    private String name;
    private String unitName;
    private double amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unitName;
    }

    public void setUnit(String unitName) {
        this.unitName = unitName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientAmount)) return false;
        IngredientAmount that = (IngredientAmount) o;
        return getAmount() == that.getAmount() &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getUnit(), that.getUnit());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUnit(), getAmount());
    }

    @Override
    public String toString() {
        final String amountFormatted = amount == (long)amount? String.format("%d", (long)amount) : String.format("%s", amount);
        return amountFormatted + " " + unitName + " " + name;
    }
}
