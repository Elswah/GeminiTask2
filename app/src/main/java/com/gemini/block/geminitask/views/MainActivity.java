package com.gemini.block.geminitask.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gemini.block.geminitask.R;
import com.gemini.block.geminitask.adapter.RecyclerAdapter;
import com.gemini.block.geminitask.model.New;
import com.gemini.block.geminitask.service.NewsService;
import com.gemini.block.geminitask.service.ServiceBuilder;
import com.gemini.block.geminitask.utils.Constants;
import com.gemini.block.geminitask.utils.Util;

import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private Unbinder unbinder;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.p)
    ProgressBar p;
    @BindView(R.id.nointernet)
    TextView tvNoInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpRecyclerView();
        unbinder = ButterKnife.bind(this);
        HashMap<String, String> filterMap = new HashMap<>();
        filterMap.put("q", "bitcoin");
        filterMap.put("apiKey", Constants.token);
        NewsService newsService = ServiceBuilder.buildService(NewsService.class);
        Call<New> request = newsService.getNews(filterMap);
       request.enqueue(new Callback<New>() {
           @Override
           public void onResponse(Call<New> call, Response<New> response) {
               //Timber.d(response.toString());
               RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), response.body().getArticles());
               recyclerView.setAdapter(adapter);

           }

           @Override
           public void onFailure(Call<New> call, Throwable t) {

           }
       });
    }
    private void setUpRecyclerView() {
        unbinder = ButterKnife.bind(this);
        // (Context context, int spanCount)
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
        // Even if we dont use it then also our items shows default animation. #Check Docs
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        p.setIndeterminate(true);
        Util.setFont(tvNoInternet, this, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
