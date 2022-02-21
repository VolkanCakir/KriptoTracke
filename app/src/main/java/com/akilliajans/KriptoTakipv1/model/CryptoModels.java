package com.akilliajans.KriptoTakipv1.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class CryptoModels implements Serializable {


    @SerializedName("currency")
    public String currency;
    @SerializedName("price")
    public String price;

    String id;
    String symbol;
    String name;
    String currentPrice;
    String priceChange;
    String marketCap;
    String marketCapRank;
    String circulatingSupply;
    String totalSupply;
    String ath;
    String athDate;
    String marketCapChange;
    String marketCapChangePercent;
    String totalVolume;
    String low24h;
    String high24;
    ArrayList<Float> sparklineData7D;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(String priceChange) {
        this.priceChange = priceChange;
    }

    public String getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(String marketCap) {
        this.marketCap = marketCap;
    }

    public String getMarketCapRank() {
        return marketCapRank;
    }

    public void setMarketCapRank(String marketCapRank) {
        this.marketCapRank = marketCapRank;
    }

    public String getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(String circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

    public String getAth() {
        return ath;
    }

    public void setAth(String ath) {
        this.ath = ath;
    }

    public String getAthDate() {
        return athDate;
    }

    public void setAthDate(String athDate) {
        this.athDate = athDate;
    }

    public String getMarketCapChange() {
        return marketCapChange;
    }

    public void setMarketCapChange(String marketCapChange) {
        this.marketCapChange = marketCapChange;
    }

    public String getMarketCapChangePercent() {
        return marketCapChangePercent;
    }

    public void setMarketCapChangePercent(String marketCapChangePercent) {
        this.marketCapChangePercent = marketCapChangePercent;
    }

    public String getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(String totalVolume) {
        this.totalVolume = totalVolume;
    }

    public String getLow24h() {
        return low24h;
    }

    public void setLow24h(String low24h) {
        this.low24h = low24h;
    }

    public String getHigh24() {
        return high24;
    }

    public void setHigh24(String high24) {
        this.high24 = high24;
    }

    public ArrayList<Float> getSparklineData7D() {
        return sparklineData7D;
    }

    public void setSparklineData7D(ArrayList<Float> sparklineData7D) {
        this.sparklineData7D = sparklineData7D;
    }

    public CryptoModels(String id, String symbol, String name, String currentPrice,
                        String priceChange, String marketCap, String marketCapRank,
                        String circulatingSupply, String totalSupply, String ath,
                        String athDate, String marketCapChange, String marketCapChangePercent,
                        String totalVolume, String low24h, String high24, ArrayList<Float> sparklineData7D) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.priceChange = priceChange;
        this.marketCap = marketCap;
        this.marketCapRank = marketCapRank;
        this.circulatingSupply = circulatingSupply;
        this.totalSupply = totalSupply;
        this.ath = ath;
        this.athDate = athDate;
        this.marketCapChange = marketCapChange;
        this.marketCapChangePercent = marketCapChangePercent;
        this.totalVolume = totalVolume;
        this.low24h = low24h;
        this.high24 = high24;
        this.sparklineData7D = sparklineData7D;
    }

    public static CryptoModels parseData(String response) throws JSONException {
        JSONObject cryptodata = new JSONObject(response);
        JSONArray sparkline = cryptodata.getJSONObject("sparkline_in_7d").getJSONArray("price");
        ArrayList<Float> sparklineData = new ArrayList<>();
        for (int i = 0; i < sparkline.length(); i++) {
            sparklineData.add(Float.parseFloat(sparkline.getString(i)));
        }

        String id = cryptodata.getString("id");
        String symbol = cryptodata.getString("symbol");
        String name = cryptodata.getString("name");
        String currentPrice = cryptodata.getString("current_price");
        String priceChangePercent = cryptodata.getString("price_change_24h");
        String marketCap = cryptodata.getString("market_cap");
        String marketCapRank = cryptodata.getString("market_cap_rank");
        String circulatingSupply = cryptodata.getString("circulating_supply");
        String totalSupply = cryptodata.getString("total_supply");
        String ath = cryptodata.getString("ath");
        String athDate = cryptodata.getString("ath_date");
        String marketCapChange = cryptodata.getString("market_cap_change_24h");
        String marketCapChangePercent = cryptodata.getString("market_cap_change_percentage_24h");
        String totalvolume = cryptodata.getString("total_volume");
        String low24h = cryptodata.getString("low_24h");
        String high24h = cryptodata.getString("high_24h");

        return new CryptoModels(
                id,
                symbol,
                name,
                currentPrice,
                priceChangePercent,
                NumberS.formatLargeNumber(marketCap),
                marketCapRank,
                NumberS.formatLargeNumber(circulatingSupply),
                NumberS.formatLargeNumber(totalSupply),
                NumberS.formatPrice(ath),
                NumberS.formatDate(athDate),
                NumberS.formatPrice(marketCapChange),
                NumberS.formatPercentChange(marketCapChangePercent),
                NumberS.formatPrice(totalvolume),
                NumberS.formatPrice(low24h),
                NumberS.formatPrice(high24h),
                sparklineData
        );
    }

    public class CryptoModel {







    }
}





