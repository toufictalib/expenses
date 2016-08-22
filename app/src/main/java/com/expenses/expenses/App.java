package com.expenses.expenses;

import android.app.Application;

import com.expenses.expenses.cache.Data;

/**
 * Created by User on 2/1/2016.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        new Data(this);
    }
}
