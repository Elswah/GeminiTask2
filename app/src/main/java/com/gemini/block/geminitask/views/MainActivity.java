package com.gemini.block.geminitask.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gemini.block.geminitask.R;
import com.gemini.block.geminitask.model.New;
import com.gemini.block.geminitask.service.NewsService;
import com.gemini.block.geminitask.service.ServiceBuilder;
import com.gemini.block.geminitask.utils.Constants;

import java.util.HashMap;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        Timber.d(Locale.getDefault().getLanguage());
        HashMap<String, String> filterMap = new HashMap<>();
        filterMap.put("q", "bitcoin");
        filterMap.put("apiKey", Constants.token);
        NewsService newsService = ServiceBuilder.buildService(NewsService.class);
        Call<New> request = newsService.getNews(filterMap);
       request.enqueue(new Callback<New>() {
           @Override
           public void onResponse(Call<New> call, Response<New> response) {
               Timber.d(response.toString());

           }

           @Override
           public void onFailure(Call<New> call, Throwable t) {

           }
       });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
