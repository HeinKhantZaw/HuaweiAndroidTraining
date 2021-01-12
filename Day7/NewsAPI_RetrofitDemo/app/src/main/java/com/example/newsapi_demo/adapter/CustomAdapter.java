package com.example.newsapi_demo.adapter;


import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapi_demo.R;
import com.example.newsapi_demo.model.Article;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<Article> articleArrayList;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle, tvDate, tvDesc;
        private LinearLayout layout;

        public ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.articleTitle);
            tvDesc = view.findViewById(R.id.articleDesc);
            layout = view.findViewById(R.id.itemLayout);
            tvDate = view.findViewById(R.id.articlePublish);
        }

    }

    public CustomAdapter(List<Article> articleArrayList) {
        this.articleArrayList = articleArrayList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.articles_items, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final Article articleModel = articleArrayList.get(position);
        if (!TextUtils.isEmpty(articleModel.getTitle())) {
            viewHolder.tvTitle.setText(articleModel.getTitle());
        }
        if (!TextUtils.isEmpty(articleModel.getPublishedAt())) {
            viewHolder.tvDate.setText(articleModel.getPublishedAt());
        }
        if (!TextUtils.isEmpty(articleModel.getDescription())) {
            viewHolder.tvDesc.setText(articleModel.getDescription());
        }
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), " clicked", Toast.LENGTH_SHORT).show();
                Intent openURL = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(articleArrayList.get(position).getUrl()));
                view.getContext().startActivity(openURL);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }
}
