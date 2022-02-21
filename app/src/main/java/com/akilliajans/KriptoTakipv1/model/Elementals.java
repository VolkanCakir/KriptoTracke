package com.akilliajans.KriptoTakipv1.model;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.akilliajans.KriptoTakipv1.R;
import com.akilliajans.KriptoTakipv1.adapter.SparklineAdapter;
import com.akilliajans.KriptoTakipv1.view.MainActivity;
import com.robinhood.spark.SparkView;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class Elementals extends AppCompatActivity {
    TextView itemname, priceChangeX, timelineText;
    TickerView tickerView;
    SparkView sparkline;
    CryptoModels cryptoModels;
    RadioGroup radioGroup;
    SparklineAdapter adapter;
    HashMap<String, Timeline> timelineMap = new HashMap<>();

    // Stats TextView
    TextView marketRank, marketCap, marketCapChange, marketCapChangePercent;
    TextView ath, athDate, totalVolume, cirulatingSupply, totalSupply;
    TextView todaysLow, todaysHigh;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);


        // Get the data from intent
        cryptoModels = (CryptoModels) getIntent().getSerializableExtra("itemdata");

        // Change action bar to add back button and change its text
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(cryptoModels.symbol.toUpperCase() + " " + "Details");

        // Initialize all views
        sparkline = findViewById(R.id.sparkline);
        itemname = findViewById(R.id.itemname);
        priceChangeX = findViewById(R.id.priceChange);
        timelineText = findViewById(R.id.timeline);
        radioGroup = findViewById(R.id.radiogroup);
        tickerView = findViewById(R.id.tickerView);
        tickerView.setCharacterLists(TickerUtils.provideNumberList());

        // Initialize Stats TextViews
        marketRank = findViewById(R.id.marketRank);
        marketCap = findViewById(R.id.marketCap);
        marketCapChange = findViewById(R.id.marketCapChange);
        marketCapChangePercent = findViewById(R.id.marketCapChangePercent);
        todaysLow = findViewById(R.id.todaysLow);
        todaysHigh = findViewById(R.id.todaysHigh);
        ath = findViewById(R.id.ath);
        athDate = findViewById(R.id.athDate);
        cirulatingSupply = findViewById(R.id.circulatingSupply);
        totalSupply = findViewById(R.id.totalSupply);
        totalVolume = findViewById(R.id.todaysVolume);


        // Easy way to map timeline text to enum
        timelineMap.put("1D", Timeline.ONEDAY);
        timelineMap.put("1W", Timeline.ONEWEEK);
        timelineMap.put("1M", Timeline.ONEMONTH);
        timelineMap.put("3M", Timeline.THREEMONTHS);
        timelineMap.put("1Y", Timeline.ONEYEAR);
        timelineMap.put("5Y", Timeline.FIVEYEARS);

        // Set statistics data
        marketRank.setText(cryptoModels.marketCapRank);
        marketCap.setText(cryptoModels.marketCap);
        marketCapChange.setText(cryptoModels.marketCapChange);
        marketCapChangePercent.setText(cryptoModels.marketCapChangePercent);
        ath.setText(cryptoModels.ath);
        athDate.setText(cryptoModels.athDate);
        todaysLow.setText(cryptoModels.low24h);
        todaysHigh.setText(cryptoModels.high24);
        cirulatingSupply.setText(cryptoModels.circulatingSupply);
        totalSupply.setText(cryptoModels.totalSupply);
        totalVolume.setText(cryptoModels.totalVolume);

        // Set listener for radio group
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = group.findViewById(checkedId);
            Timeline timeline = timelineMap.get(rb.getText());
            timelineText.setText(timeline.timelineText);
            getSparkline(timeline);
        });

        // Set up the sparkline SparkView
        adapter = new SparklineAdapter(cryptoModels.sparklineData7D);
        sparkline.setAdapter(adapter);
        sparkline.setScrubListener(
            value -> {
                if (value == null) { setInitialData(getPriceChange(), getPriceChangePercentage()); }
                else {
                    float initialValue = sparkline.getAdapter().getY(0);
                    float priceChange = (float)value - initialValue;
                    float priceChangePercent = priceChange/initialValue * 100;
                    setPriceChange(priceChange, priceChangePercent);
                    tickerView.setText("$"+value.toString());
                }
            }
        );

        // Load initial data and change theme based on price change
        setInitialData(getPriceChange(), getPriceChangePercentage());
        changeTheme(getPriceChange());
    }

    public void anasayfa (View view) {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);


    }

    protected float getPriceChange() {
        float initialPrice = adapter.sparkDt.get(0);
        float currentPrice = adapter.sparkDt.get(adapter.getCount()-1);
        return currentPrice-initialPrice;
    }

    protected float getPriceChangePercentage() {
        float priceChange = getPriceChange();
        return priceChange/adapter.getY(0) * 100;
    }

    protected void setInitialData(float priceChange, float priceChangePercent) {
        itemname.setText(cryptoModels.name);
        tickerView.setText("$" + cryptoModels.currentPrice);
        setPriceChange(priceChange, priceChangePercent);
    }

    protected void changeTheme(float priceChange) {
        int pink = ContextCompat.getColor(this, R.color.sparkRed);
        int green = ContextCompat.getColor(this, R.color.sparkGreen);

        RadioButton checkedRB = findViewById(radioGroup.getCheckedRadioButtonId());
        if (priceChange < 0) {
            itemname.setTextAppearance(R.style.price_down_theme);
            priceChangeX.setTextColor(pink);
            sparkline.setLineColor(pink);
            checkedRB.setBackgroundResource(R.drawable.radio_background_down);
        } else {
            itemname.setTextAppearance(R.style.price_up_theme);
            priceChangeX.setTextColor(green);
            sparkline.setLineColor(green);
            checkedRB.setBackgroundResource(R.drawable.radio_background_up);
        }
    }

    protected void setPriceChange(float priceChange, float priceChangePercent) {
        String sign = priceChange < 0 ? "-" : "+";
        String format = String.format("%sUS$%f (%s%f)", sign, Math.abs(priceChange), sign, Math.abs(priceChangePercent));
        priceChangeX.setText(format);
        int color = priceChange < 0 ?
                ContextCompat.getColor(this, R.color.sparkRed) :
                ContextCompat.getColor(this, R.color.sparkGreen);

        priceChangeX.setTextColor(color);
    }

    protected void updateSparklineAndTheme(ArrayList<Float> sparkline) {
        adapter.update(sparkline);
        float priceChange = getPriceChange();
        float priceChangePercent = getPriceChangePercentage();
        setPriceChange(priceChange, priceChangePercent);
        changeTheme(priceChange);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void getSparkline(Timeline timeline) {
        if (timeline == Timeline.ONEWEEK) {
            updateSparklineAndTheme(cryptoModels.sparklineData7D);
            return;
        }
        String API_KEY = "085f26fe482d2ebfd856ede279af7ef1b3ff8df0";
        String[] dateBounds = PricesTimeline.getStartAndEndDate(timeline);
        String url = String.format(
                "https://api.nomics.com/v1/currencies/sparkline?key=%s&ids=%s&start=%s",
                API_KEY, cryptoModels.symbol.toUpperCase(), dateBounds[0]
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray prices = new JSONArray(response).getJSONObject(0).getJSONArray("prices");
                        ArrayList<Float> sparkline = new ArrayList<>();
                        for (int i = 0; i < prices.length(); i++) {
                            sparkline.add(Float.parseFloat(prices.getString(i)));
                        }

                        updateSparklineAndTheme(sparkline);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Intent intent;
                    // Redirect to error page
                    System.out.println("Error");
                }
        );

        queue.add(request);
    }
}