package com.example.news.newsmain;


import android.annotation.SuppressLint;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.apiservice.pojos.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainNewsAdapter extends ListAdapter <Article, MainNewsAdapter.NewsHolder> {

    public MainNewsAdapter() {
        super(DIFF_CALLBACK);
    }

    private static DiffUtil.ItemCallback <Article> DIFF_CALLBACK = new DiffUtil.ItemCallback <Article>() {
        @Override
        public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDescription().equals(newItem.getDescription());
        }
    };

    @NonNull
    @Override
    public MainNewsAdapter.NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new NewsHolder(layoutInflater.inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainNewsAdapter.NewsHolder viewHolder, int position) {
        viewHolder.tvDescription.setText(getItem(getItemCount() - 1 - position).getDescription());
        viewHolder.tvTitle.setText(getItem(getItemCount() - 1 - position).getTitle());
        try {
            Picasso.get().load(Uri.parse(getItem(getItemCount() - 1 - position).getUrlToImage())).placeholder(R.drawable.ic_launcher_background).into(viewHolder.ivPhoto);
        } catch (Exception noImg) {
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class NewsHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvDescription;
        ImageView ivPhoto;

        NewsHolder(View view) {
            super(view);
            tvDescription = view.findViewById(R.id.tvDescription);
            tvTitle = view.findViewById(R.id.tvTitle);
            ivPhoto = view.findViewById(R.id.ivPhoto);
        }
    }
}
