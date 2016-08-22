package com.expenses.expenses.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.expenses.expenses.bean.Result;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by User on 1/29/2016.
 */
public class Data {
    public static final String RESULT = "result";
    public static final String MyPREFERENCES = "MyPREFERENCES";

    private Context context;
    public static Data instance;

    public Data(Context context) {
        this.context = context;
        instance = this;
    }


    public static Data get() {
        return instance;
    }

    public boolean addResults(List<Result> results) throws JsonProcessingException {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit().putString(RESULT, BeanConverter.get().toString(results));

        return editor.commit();
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public List<Result> getResults() throws IOException {
        String value = getSharedPreferences().getString(RESULT, null);
        if (value != null)
        {
            Result [] results =  BeanConverter.get().toObject(value, Result[].class);
            return new ArrayList<>(Arrays.asList(results));
        }
        return new ArrayList<>();
    }

    public boolean clearResults() {
        return getSharedPreferences().edit().remove(RESULT).commit();
    }
}
