package edu.hm.cs.seng.hypershop.model;

import java.util.Objects;

public class Unit {

    // TODO: static map Units!!

    private String name;

    public Unit(){

    }

    public Unit(String unitName){
        this.name=unitName;
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
        if (!(o instanceof Unit)) return false;
        Unit unit = (Unit) o;
        return Objects.equals(getName(), unit.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
