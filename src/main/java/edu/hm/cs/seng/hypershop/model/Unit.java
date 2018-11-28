package edu.hm.cs.seng.hypershop.model;

public class Unit {

    // TODO: static map Units!!

    private String name;

    public Unit(String unitName){
        this.name=unitName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
