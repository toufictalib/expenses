package com.expenses.expenses.bean;

import com.expenses.expenses.model.BaseModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 1/29/2016.
 */
public class Result extends BaseModel {

    public String label;
    public double value;
    public final String date = getDate();

    public static String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        return dateFormat.format(new Date());
    }

   @JsonIgnore
    public boolean isFull() {
        return label != null && !label.isEmpty() && value != 0;
    }


}
