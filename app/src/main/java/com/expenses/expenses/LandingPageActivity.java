package com.expenses.expenses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.expenses.expenses.bean.Result;
import com.expenses.expenses.cache.Data;
import com.expenses.expenses.tools.NumberUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LandingPageActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    private boolean init;


    private TextView txtTotal;

    private List<View> txtValues = new ArrayList<>();

    private final static int ROW_MAX = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        txtTotal = (TextView) findViewById(R.id.total);

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(CreateBtnSaveAction());


        linearLayout = (LinearLayout) findViewById(R.id.container);

        fillData();


    }


    public static double getTotal() {
        try {
            List<Result> results = Data.get().getResults();
            double total = 0;
            for (Result r : results) {
                total += r.value;
            }
            return total;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void fillData() {

        linearLayout.removeAllViews();
        try {
            List<Result> results = Data.get().getResults();

            if (results.isEmpty()) {
                for (int i = 0; i < ROW_MAX; i++) {
                    addView();
                }
            } else {
                for (int i = 0; i < results.size(); i++) {
                    View view = addView();
                    EditText label = (EditText) view.findViewById(R.id.label);
                    EditText value = (EditText) view.findViewById(R.id.value);

                    label.setText(results.get(i).label);
                    value.setText(results.get(i).value + "");
                }
            }


            init = false;
        } catch (IOException e) {
            showMessage(e.getMessage());
            Log.d(LandingPageActivity.class.getSimpleName(), e.getMessage());
        }
    }

    @NonNull
    private View.OnClickListener CreateBtnSaveAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double total = 0;
                List<Result> results = new ArrayList<Result>();
                for (View txt : txtValues) {

                    EditText lblTxt = (EditText) txt.findViewById(R.id.label);
                    EditText valueTxt = (EditText) txt.findViewById(R.id.value);
                    double v1 = NumberUtil.toDouble(valueTxt.getText().toString());
                    total += v1;


                    Result result = new Result();
                    result.label = lblTxt.getText().toString();
                    result.value = v1;

                    if (result.isFull()) {
                        results.add(result);
                    }
                }

                try {
                    if (Data.get().addResults(results)) {
                        showMessage("Data has saved successfully.");
                    }
                } catch (JsonProcessingException e) {
                    showMessage(e.getMessage());
                    Log.d(LandingPageActivity.class.getSimpleName(), e.getMessage());
                }
                txtTotal.setText(total + "");


            }
        };
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @NonNull
    private View getView() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_row_field, null);
        EditText txt = (EditText) view.findViewById(R.id.value);
        EditText lbl = (EditText) view.findViewById(R.id.label);
        if (!init) {
            lbl.requestFocus();
        }
        txt.setOnEditorActionListener(createEditorActionListener());
        txtValues.add(view);
        return view;
    }


    @NonNull
    private TextView.OnEditorActionListener createEditorActionListener() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addView();
                    return true;
                }
                return false;
            }
        };
    }

    private View addView() {
        View view = getView();
        linearLayout.addView(view);
        return view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            fillData();

            return true;
        }
        if (id == R.id.action_clear_all) {

            if (Data.get().clearResults()) {
                txtValues.clear();
                fillData();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
