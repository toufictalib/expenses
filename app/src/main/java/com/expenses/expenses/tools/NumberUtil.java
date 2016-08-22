package com.expenses.expenses.tools;

import android.util.Log;

/**
 * Created by User on 1/29/2016.
 */
public class NumberUtil {


    public static double toDouble(Object o)
    {
        if(o!=null)
        {
            try {
                return Double.parseDouble(o.toString());
            }
            catch (NumberFormatException e)
            {
                Log.d(NumberUtil.class.getSimpleName(),e.getMessage());
            }
        }
        return 0;

    }

}
