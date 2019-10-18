package com.sjani.searchrecyclerviewapp.data;

import com.sjani.searchrecyclerviewapp.model.Pizza;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EndpointInterface {
    String API_ENDPOINT = "https://api.myjson.com";

    @GET("/bins/snyji")
    Call<List<Pizza>> getPizzas();

}
