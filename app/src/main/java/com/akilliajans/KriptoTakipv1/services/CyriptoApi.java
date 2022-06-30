package com.akilliajans.KriptoTakipv1.services;

import com.akilliajans.KriptoTakipv1.model.CryptoModels;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CyriptoApi {

    //https://api.nomics.com/v1/prices?key=xx


    @GET ("prices?key=xxxx")
    Call<List<CryptoModels>> getModel() ;

}
