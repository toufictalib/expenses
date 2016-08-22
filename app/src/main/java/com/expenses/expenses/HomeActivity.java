package com.expenses.expenses;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.expenses.expenses.bean.Result;
import com.expenses.expenses.cache.Data;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private TextView txtTotalInDollar;
    private TextView txtTotalInLebanese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnExpenses = (Button) findViewById(R.id.btnExpenses);
        Button btnSendMail = (Button) findViewById(R.id.btnSendMail);
        Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
        txtTotalInDollar = (TextView) findViewById(R.id.totalInDollar);

        double total = LandingPageActivity.getTotal();
       final NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setCurrency(Currency.getInstance(Locale.US));
        txtTotalInDollar.setText(format.format(getDollar(total)) + "" + "");

        Locale locale = new Locale("ar","LB");
        format.setCurrency(Currency.getInstance(locale));
        txtTotalInLebanese = (TextView) findViewById(R.id.totalInLebanese);
        txtTotalInLebanese.setText(format.format(total)+"");
        
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double t = LandingPageActivity.getTotal();

                Locale locale = new Locale("ar","LB");
                format.setCurrency(Currency.getInstance(locale));
                txtTotalInLebanese.setText(format.format(t) + "");

                format.setCurrency(Currency.getInstance(Locale.US));
                txtTotalInDollar.setText(format.format(getDollar(t))+"");
            }
        });

        btnExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(HomeActivity.this, LandingPageActivity.class);
                startActivity(k);

            }
        });

        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"toufictalib@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Expenses");
                    email.putExtra(Intent.EXTRA_TEXT, getMessage());
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                } catch (IOException e) {
                    showMessage(e.getMessage());
                }
            }
        });
    }

    private double getDollar(double lbp)
    {
        return Math.round(lbp/1500);
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private String getMessage() throws IOException {
        List<Result> results = Data.get().getResults();
        if (results == null || results.isEmpty())
            return "";


        Map<String, List<Result>> map = new HashMap<>();

        StringBuilder allValue = new StringBuilder();
        StringBuilder distinctValue = new StringBuilder();

        double allValueTotal = 0;
        double distinctValueTotal= 0;
        for (Result result : results) {
            List<Result> results1 = map.get(result.label.trim());
            if (results1 == null) {
                results1 = new ArrayList<>();
                map.put(result.label.trim(), results1);
            }
            results1.add(result);
            allValueTotal+=result.value;
            allValue.append(getFormat(result.label, result.value) + "\n");
        }

        for (Map.Entry<String, List<Result>> entry : map.entrySet()) {
            double total = 0;
            for (Result r : entry.getValue()) {
                total += r.value;
            }

            distinctValueTotal+=total;
            distinctValue.append(getFormat(entry.getKey(), total) + "\n");
        }


        return "------ All Result ---------\n" +
                "Total : "+allValueTotal+"\n" +
                allValue + "\n" +
                "---------- Distinct Result ------------\n" +
                "Total : "+distinctValueTotal+"\n" +
                distinctValue


                ;
    }

    private String getFormat(String label, double value) {
        return label + " : " + value;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
