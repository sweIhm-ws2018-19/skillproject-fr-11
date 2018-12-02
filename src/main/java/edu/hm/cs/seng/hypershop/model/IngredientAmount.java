package edu.hm.cs.seng.hypershop.model;

import java.util.Objects;

public class IngredientAmount {

    private String name;
    private String unitName;
    private int amount;

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
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
}
