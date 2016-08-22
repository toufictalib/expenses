package com.expenses.expenses.model;

import java.io.Serializable;

/**
 * Created by User on 2/1/2016.
 */
public class BaseModel implements Serializable {
    protected int id;

    public BaseModel() {
    }

    public BaseModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
