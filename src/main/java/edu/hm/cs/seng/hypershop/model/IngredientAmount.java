package edu.hm.cs.seng.hypershop.model;

import javax.measure.Unit;
import java.util.Objects;

public class IngredientAmount {

    private String name;
    private Unit unit;
    private int amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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
