package com.gemini.block.geminitask.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gemini.block.geminitask.R;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private Unbinder unbinder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        Timber.d(Locale.getDefault().getLanguage());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
