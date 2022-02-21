package com.akilliajans.KriptoTakipv1.adapter;

import com.robinhood.spark.SparkAdapter;

import java.util.ArrayList;

public class SparklineAdapter extends SparkAdapter {
    public ArrayList<Float> sparkDt;

    public SparklineAdapter(ArrayList<Float> sparkDt) {
        this.sparkDt = sparkDt;
    }

    public void update(ArrayList<Float> sparkDt) {
        this.sparkDt = sparkDt;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return sparkDt.size();
    }

    @Override
    public Object getItem(int index) {
        return sparkDt.get(index);
    }

    @Override
    public float getY(int index) {
        return sparkDt.get(index);
    }
}