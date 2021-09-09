package com.example.fetchrewards;



import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    
    @GET("hiring.json")
    Call<String> getRewards();


}