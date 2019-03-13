package com.gemini.block.geminitask.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gemini.block.geminitask.R;
import com.gemini.block.geminitask.model.Article;
import com.gemini.block.geminitask.utils.RoundedCornersTransformation;
import com.gemini.block.geminitask.views.activities.DetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import timber.log.Timber;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    List<Article> mData;
    private LayoutInflater inflater;
    private Context context;

    public RecyclerAdapter(Context context, List<Article> data) {
        this.context=context;
        inflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Timber.d("onCreateViewHolder");
        View view = inflater.inflate(R.layout.list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Log.i(TAG, "onBindViewHolder" + position);
        Timber.d("onBindViewHolder %d",position);
        Article current = mData.get(position);
        holder.setData(current, position);
        holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title,description;
        ImageView imgHeader, imgSelect;
        LinearLayout linearLayout;
        int position;
        Article current;

        public MyViewHolder(View itemView) {
            super(itemView);
            title       = (TextView)  itemView.findViewById(R.id.tvTitle);
            description = (TextView)  itemView.findViewById(R.id.tvDescription);
            imgHeader   = (ImageView) itemView.findViewById(R.id.img_row);
            imgSelect   = (ImageView) itemView.findViewById(R.id.img_row_select);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout);

        }

        public void setData(Article current, int position) {
            this.title.setText(current.getAuthor());
            this.description.setText(current.getSource().getName());
            if (!current.getUrlToImage().isEmpty() && current.getUrlToImage() != null && !current.getUrlToImage().equals("")) {
                Picasso.with(context).load(current.getUrlToImage()).error(R.drawable.error)
                        .placeholder(R.drawable.placeholder).transform(new RoundedCornersTransformation(22, 0))
                        .fit().centerCrop().into(this.imgHeader);
            } else {
                Picasso.with(context).load(R.drawable.placeholder).error(R.drawable.error)
                        .placeholder(R.drawable.placeholder).transform(new RoundedCornersTransformation(22, 0))
                        .fit().centerCrop().into(this.imgHeader);
            }
            this.position = position;
            this.current = current;
        }
        public void setListeners() {
            imgSelect.setOnClickListener(MyViewHolder.this);
            imgHeader.setOnClickListener(MyViewHolder.this);
            linearLayout.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_row_select:
                    // Timber.d("img_row_select ");
                    Intent intent2 = new Intent(context, DetailsActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putParcelable("currentitem", current);
                    intent2.putExtras(bundle2);
                    context.startActivity(intent2);
                    break;

                case R.id.img_row:
                    //  Timber.d("img_row ");

                    break;
                case R.id.layout:
                    //  Timber.d("layout clicked ");
                    Intent intent = new Intent(context, DetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("currentitem", current);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    break;
            }
         //   Log.i("onClick after operation", mData.size() + " \n\n" + mData.toString());
        }
    }
}
