package com.gemini.block.geminitask.service;

import com.gemini.block.geminitask.model.New;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

 public interface NewsService {
    @GET("everything")
    Call<New> getNews(@QueryMap HashMap<String, String> filters);
}
