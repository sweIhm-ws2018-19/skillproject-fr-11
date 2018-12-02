package edu.hm.cs.seng.hypershop.model;

import java.util.Objects;

public class HypershopUnit {

    private String name;

    public HypershopUnit() {

    }

    public HypershopUnit(String unitName) {
        this.name = unitName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HypershopUnit)) return false;
        HypershopUnit unit = (HypershopUnit) o;
        return Objects.equals(getName(), unit.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
