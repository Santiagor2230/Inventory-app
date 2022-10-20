package com.zybooks.myapplication;

public class items {

    int id;
    String name;
    String desc;
    String qty;

    public items() {
        super();
    }

    public items(int i, String name, String description, String qty) {
        super();
        this.id = i;

        this.name= name;
        this.desc = description;
        this.qty = qty;
    }

    // constructor
    public items(String name, String description, String qty) {
        this.name = name;

        this.desc = description;
        this.qty = qty;
    }

    //setter and getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}