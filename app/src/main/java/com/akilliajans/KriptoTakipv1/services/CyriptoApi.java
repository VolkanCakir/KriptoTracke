package com.akilliajans.KriptoTakipv1.services;

import com.akilliajans.KriptoTakipv1.model.CryptoModels;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CyriptoApi {

    //https://api.nomics.com/v1/prices?key=085f26fe482d2ebfd856ede279af7ef1b3ff8df0


    @GET ("prices?key=085f26fe482d2ebfd856ede279af7ef1b3ff8df0")
    Call<List<CryptoModels>> getModel() ;

}
