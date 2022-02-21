package com.akilliajans.KriptoTakipv1.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.akilliajans.KriptoTakipv1.model.CryptoModels;
import com.akilliajans.KriptoTakipv1.R;
import com.akilliajans.KriptoTakipv1.model.Elementals;
import com.robinhood.spark.SparkView;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    public RecyclerViewAdapter(ArrayList<CryptoModels> cryptoModels) {
    }

    public void setData(ArrayList<CryptoModels> data) {
        this.data = data;
    }

    public ArrayList<CryptoModels> data;
    public Context context;

    public RecyclerViewAdapter(ArrayList<CryptoModels> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_view_row, parent, false);
        ViewHolder holder = new ViewHolder(rowItem, context);
        rowItem.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                CryptoModels cryptoModels = this.data.get(position);
                Intent intent = new Intent(context, Elementals.class);
                intent.putExtra("itemdata", cryptoModels);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {


        CryptoModels cryptoModels = this.data.get(position);
        TextView itemSymbol = holder.linearLayout.findViewById(R.id.itemSymbol);
        TextView itemName = holder.linearLayout.findViewById(R.id.itemName);
        SparkView sparkline = holder.linearLayout.findViewById(R.id.sparkline);
        TickerView tickerView = holder.linearLayout.findViewById(R.id.tickerView);
        tickerView.setCharacterLists(TickerUtils.provideNumberList());



        itemName.setText(cryptoModels.getName());
        itemSymbol.setText(cryptoModels.getSymbol().toUpperCase());
        tickerView.setText("$" + cryptoModels.getCurrentPrice());
        SparklineAdapter adapter = new SparklineAdapter(cryptoModels.getSparklineData7D());
        sparkline.setAdapter(adapter);

        int color = Float.parseFloat(cryptoModels.getPriceChange()) < 0 ?
                ContextCompat.getColor(context, R.color.sparkRed) :
                ContextCompat.getColor(context, R.color.sparkGreen);

        sparkline.setLineColor(color);
        tickerView.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout linearLayout;
        public Context context;

        public ViewHolder(View view, Context context) {
            super(view);
            this.linearLayout = view.findViewById(R.id.linearLayoutRow);
            this.context = context;
        }
    }
}
