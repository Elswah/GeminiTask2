package com.gemini.block.geminitask.views.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gemini.block.geminitask.R;
import com.gemini.block.geminitask.adapter.RecyclerAdapter;
import com.gemini.block.geminitask.model.Article;
import com.gemini.block.geminitask.model.New;
import com.gemini.block.geminitask.service.NewsService;
import com.gemini.block.geminitask.service.ServiceBuilder;
import com.gemini.block.geminitask.utils.Constants;
import com.gemini.block.geminitask.utils.Prefs;
import com.gemini.block.geminitask.utils.Util;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private Unbinder unbinder;
    private Snackbar snackbar;
    private String title;
    private ArrayList<Article> articlesList = new ArrayList<>();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.p)
    ProgressBar p;
    @BindView(R.id.nointernet)
    TextView tvNoInternet;
    @BindView(R.id.layout)
    ConstraintLayout constraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpRecyclerView();
        unbinder = ButterKnife.bind(this);
        Prefs prefs = new Prefs(MainActivity.this);
        title = prefs.getTitle();
        if (Util.checkInternetConnection(this)) {
            tvNoInternet.setVisibility(View.GONE);
            getNews(title);
        } else {
            showSnackBar();
        }
        // getNews(title);

    }
    private void showSnackBar() {
        p.setIndeterminate(false);
        p.setVisibility(View.GONE);
        tvNoInternet.setVisibility(View.VISIBLE);
        snackbar = Snackbar.make(constraintLayout, "No Internet Connection !", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Util.checkInternetConnection(getApplicationContext())) {
                            recyclerView.setVisibility(View.VISIBLE);
                            p.setIndeterminate(true);
                            p.setVisibility(View.VISIBLE);
                            tvNoInternet.setVisibility(View.GONE);
                            snackbar.dismiss();

                            getNews(title);
                        } else {
                            showSnackBar();
                        }
                    }
                });
        snackbar.setActionTextColor(ResourcesCompat.getColor(getResources(), R.color.snackBarActionTxtColor, null));
        snackbar.show();
    }

    private void getNews(String title) {
        articlesList.clear();
        p.setIndeterminate(true);
        p.setVisibility(View.VISIBLE);
        HashMap<String, String> filterMap = new HashMap<>();
        filterMap.put("q", title);
        filterMap.put("apiKey", Constants.token);
        NewsService newsService = ServiceBuilder.buildService(NewsService.class);
        Call<New> request = newsService.getNews(filterMap);
        request.enqueue(new Callback<New>() {
            @Override
            public void onResponse(Call<New> call, Response<New> response) {
                //Timber.d(response.toString());
                handleResponse(response);


            }

            @Override
            public void onFailure(Call<New> call, Throwable t) {

            }
        });
    }
    private void handleResponse(Response<New> response){
        if(response!=null ) {
            if(response.isSuccessful()) {
                if(response.body().getStatus().equals("ok")&& response.body().getTotalResults()>0) {
                    articlesList = (ArrayList<Article>) response.body().getArticles();
                    RecyclerAdapter adapter = new RecyclerAdapter(getApplicationContext(), articlesList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    p.setIndeterminate(false);
                    p.setVisibility(View.GONE);
                }else {
                    Toast.makeText(this, "No Data with this input", Toast.LENGTH_SHORT).show();
                    p.setVisibility(View.GONE);
                    p.setIndeterminate(false);
                    //get this value as default
                    getNews("bitcoin");

                }
            }else {
                // error case
                switch (response.code()) {
                    case 404:
                        Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
                        break;
                    case 500:
                        Toast.makeText(this, "server broken", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(this, "unknown error", Toast.LENGTH_SHORT).show();
                        p.setIndeterminate(false);
                        p.setVisibility(View.GONE);
                        break;
                }
            }
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_title) {

            showInputDialog();

        }

        return super.onOptionsItemSelected(item);
    }

    private void showInputDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Change key word of Search");

        final EditText titleInput = new EditText(MainActivity.this);
        titleInput.setInputType(InputType.TYPE_CLASS_TEXT);
        titleInput.setHint("enter key word to filter like (bitcoin)");
        builder.setView(titleInput);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Prefs titlePreference = new Prefs(MainActivity.this);
                titlePreference.setTitle(titleInput.getText().toString());

                String title = titlePreference.getTitle();
                if (title.equals("") && title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "enter your field correctly", Toast.LENGTH_SHORT).show();
                } else {
                    if (Util.checkInternetConnection(getApplicationContext())) {
                        //re-render everything again
                        recyclerView.setVisibility(View.VISIBLE);
                        tvNoInternet.setVisibility(View.GONE);
                        ////////
                        if (snackbar != null) {
                            snackbar.dismiss();
                        }

                        getNews(title);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        showSnackBar();
                    }
                }

            }
        });
        builder.show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
