package com.gemini.block.geminitask.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gemini.block.geminitask.R;
import com.gemini.block.geminitask.model.Article;
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
        int position;
        Article current;

        public MyViewHolder(View itemView) {
            super(itemView);
            title       = (TextView)  itemView.findViewById(R.id.tvTitle);
            description = (TextView)  itemView.findViewById(R.id.tvDescription);
            imgHeader   = (ImageView) itemView.findViewById(R.id.img_row);
            imgSelect   = (ImageView) itemView.findViewById(R.id.img_row_select);

        }

        public void setData(Article current, int position) {
            this.title.setText(current.getAuthor());
            this.description.setText(current.getSource().getName());
            Picasso.with(context).load(current.getUrlToImage()).error(R.drawable.error)
                    .placeholder(R.drawable.placeholder).into(this.imgHeader);
            this.position = position;
            this.current = current;
        }
        public void setListeners() {
            imgSelect.setOnClickListener(MyViewHolder.this);
            imgHeader.setOnClickListener(MyViewHolder.this);


        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_row_select:
                    Timber.d("img_row_select ");

                    break;

                case R.id.img_row:
                    Timber.d("img_row ");
                    break;
            }
         //   Log.i("onClick after operation", mData.size() + " \n\n" + mData.toString());
        }
    }
}
