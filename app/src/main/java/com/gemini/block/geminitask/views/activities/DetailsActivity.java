package com.gemini.block.geminitask.views.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gemini.block.geminitask.R;
import com.gemini.block.geminitask.model.Article;
import com.gemini.block.geminitask.utils.RoundedCornersTransformation;
import com.gemini.block.geminitask.utils.Util;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    TextView txt_source, txt_author, txt_title, txt_content, txt_data_author, txt_data_title, txt_data_content;
    ImageView imageView;
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setUpReference();
        getDataObject();

    }

    private void setUpReference() {
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt_source = (TextView) findViewById(R.id.txt_source);
        txt_author = (TextView) findViewById(R.id.txt_author);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_content = (TextView) findViewById(R.id.txt_content);
        txt_data_author = (TextView) findViewById(R.id.txt_data_author);
        txt_data_title = (TextView) findViewById(R.id.txt_data_title);
        txt_data_content = (TextView) findViewById(R.id.txt_data_content);
        imageView = (ImageView) findViewById(R.id.detsBandImage);

        Util.setFont(txt_source, this, 0);
        Util.setFont(txt_content, this, 0);
        Util.setFont(txt_title, this, 0);
        Util.setFont(txt_author, this, 0);
        Util.setFont(txt_data_author, this, 0);
        Util.setFont(txt_data_title, this, 0);
        Util.setFont(txt_data_content, this, 0);

    }

    private void getDataObject() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            article = bundle.getParcelable("currentitem");
            setDataToView(article);
        }
    }

    private void setDataToView(Article article) {
        Picasso.with(getApplicationContext()).load(article.getUrlToImage()).centerCrop().
                fit().error(R.drawable.error).placeholder(R.drawable.placeholder).
                transform(new RoundedCornersTransformation(22, 0)).into(imageView);
        txt_data_author.setText(article.getAuthor());
        txt_data_content.setText(article.getContent());
        txt_data_title.setText(article.getTitle());
        txt_source.setText(article.getSource().getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_event_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_websiteId) {

            String url = article.getUrl();

            if (!url.equals("")) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "Website not available", Toast.LENGTH_LONG).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

}
