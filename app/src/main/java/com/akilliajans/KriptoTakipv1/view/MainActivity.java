package com.akilliajans.KriptoTakipv1.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;

import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akilliajans.KriptoTakipv1.adapter.RecyclerViewAdapter;
import com.akilliajans.KriptoTakipv1.model.CryptoModels;
import com.akilliajans.KriptoTakipv1.model.Elementals;
import com.akilliajans.KriptoTakipv1.services.CyriptoApi;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.akilliajans.KriptoTakipv1.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onesignal.OneSignal;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private static final String ONESIGNAL_APP_ID = "7xxxxxx";

    TextView textView;
    ImageView imageView;
    EditText editText;
    Retrofit retrofit ;
    Button button;

    boolean isScrolling = false;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    int currentItems, totalItems, scrolledOutItems;
    int currentPage = 1;
    int itemsPerPage = 20;

    ArrayList<CryptoModels> cryptoModels ;

    private String BASE_URL = "https://api.nomics.com/v1/";

    public void setTopCryptoList(ArrayList<CryptoModels> topCryptoList) {
        this.topCryptoList = topCryptoList;
    }

    ArrayList<CryptoModels> topCryptoList = new ArrayList<>();
    RecyclerViewAdapter adapter;
    ProgressBar progressBar;
    SearchView search;

    RecyclerViewAdapter recyclerViewAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Hide the action bar on home page
        getSupportActionBar().hide();


        textView = findViewById(R.id.coinResultText);

        editText = findViewById(R.id.coinSearchText);

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        final EditText[] editText = {(EditText) findViewById(R.id.coinSearchText)};
        editText[0].setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    CyriptoApi infoApi  = retrofit.create(CyriptoApi.class);

                    Call<List<CryptoModels>> getInfo = infoApi.getModel();

                    getInfo.enqueue(new Callback<List<CryptoModels>>() {


                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(Call<List<CryptoModels>> call, Response<List<CryptoModels>> response) {

                            editText[0] = findViewById(R.id.coinSearchText);
                            textView = findViewById(R.id.coinResultText);

                            if (response.isSuccessful()) {

                                List<CryptoModels> infoList = response.body();

                                String arama = editText[0].getText().toString();

                                assert infoList != null;
                                for (CryptoModels infoModel : infoList) {
                                    //check json data from retrofit
                                    String coinAdi = infoModel.currency;
                                    String coinFiyati = infoModel.price;

                                    if (arama.equals(""+coinAdi)) {
                                        textView.setText(coinAdi + "=" + coinFiyati + "$");
                                        Toast.makeText(getApplicationContext(), "" + coinAdi, Toast.LENGTH_SHORT).show();


                                    }
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<List<CryptoModels>> call, Throwable t) {
                            t.printStackTrace();


                        }
                    });

                    return true;
                }
                return false;
            }
        });

        // Setup the recycler view
        recyclerView = findViewById(R.id.cryptoListRV);
        progressBar = findViewById(R.id.progressBar);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new RecyclerViewAdapter(topCryptoList, this);
        recyclerView.setAdapter(adapter);
        //Retrofit setup1
        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrolledOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems+scrolledOutItems == totalItems)) {
                    isScrolling = false;
                    fetchTopCryptoData(currentPage, itemsPerPage);
                }
            }
        });

        // Fetch the top 20 crptos from first page to populate recycler view
        fetchTopCryptoData(currentPage, itemsPerPage);
    }
    //search list
    public void aramaTarama (View view) {



    }

            //every 10 item setup controller****
            public void fetchTopCryptoData(int page, int itemsPerPage) {
        // Don't fetch when it reaches page 10
        if (page > 10) return;

        // Show the progress bar, while data is being fetched
        progressBar.setVisibility(View.VISIBLE);

        // Fetch itemsPerPage items from the respective page
        String url = String.format(
                Locale.US,
                "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=%d&page=%d&sparkline=true",
                itemsPerPage, page
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray topCryptoData = new JSONArray(response);
                        for (int i = 0; i < topCryptoData.length(); i++) {
                            topCryptoList.add(CryptoModels.parseData(topCryptoData.getJSONObject(i).toString()));
                        }
                        // Hide the progress bar after data has been fetched
                        progressBar.setVisibility(View.INVISIBLE);
                        // Notify the adapter that more data has been added;
                        adapter.notifyDataSetChanged();

                        // Increment the page, so that new results can be fetched from next page
                        // when called next time
                        currentPage += 1;

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

    protected void redirectToItemDetails(View v) {
        String url = String.format(
                "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=%s&order=market_cap_desc&per_page=100&page=1&sparkline=true",
                ((Button)v).getText()
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET, url,
                response -> {
                    Intent intent = new Intent(this, Elementals.class);
                    intent.putExtra("itemdata", response);
                    startActivity(intent);
                },
                error -> {
                    Intent intent;
                    // Redirect to error page
                    Toast.makeText(getApplicationContext(), "Hata", Toast.LENGTH_SHORT).show();
                    System.out.println("Error");
                }
        );

        queue.add(request);

    }


}
