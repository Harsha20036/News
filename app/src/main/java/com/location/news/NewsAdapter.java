package com.location.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.location.newsapp.R;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<News> newsList;
    private List<News> bookmarkedList;
    private boolean isLoading = false;
    private boolean isError = false;
    private OnLoadMoreListener onLoadMoreListener;

    private static final int ITEM_TYPE_NEWS = 0;
    private static final int ITEM_TYPE_LOADING = 1;
    private static final int ITEM_TYPE_ERROR = 2;

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
        this.bookmarkedList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ITEM_TYPE_NEWS) {
            View itemView = inflater.inflate(R.layout.item_news, parent, false);
            return new NewsViewHolder(itemView);
        } else if (viewType == ITEM_TYPE_LOADING) {
            View loadingView = inflater.inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(loadingView);
        } else { // ITEM_TYPE_ERROR
            View errorView = inflater.inflate(R.layout.item_error, parent, false);
            return new ErrorViewHolder(errorView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsViewHolder) {
            NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
            News news = newsList.get(position);

            newsViewHolder.titleTextView.setText(news.getTitle());
            newsViewHolder.contentTextView.setText(news.getContent());

            // Handle bookmark click
            newsViewHolder.bookmarkImageView.setOnClickListener(v -> {
                if (bookmarkedList.contains(news)) {
                    bookmarkedList.remove(news);
                    newsViewHolder.bookmarkImageView.setImageResource(R.drawable.ic_baseline_error_24);
                } else {
                    bookmarkedList.add(news);
                    newsViewHolder.bookmarkImageView.setImageResource(R.drawable.ic_baseline_message_24);
                }
            });

            if (bookmarkedList.contains(news)) {
                newsViewHolder.bookmarkImageView.setImageResource(R.drawable.ic_baseline_message_24);
            } else {
                newsViewHolder.bookmarkImageView.setImageResource(R.drawable.ic_baseline_error_24);
            }
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = newsList.size();
        if (isLoading) {
            itemCount++;
        }
        if (isError) {
            itemCount++;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (isError && position == getItemCount() - 1) {
            return ITEM_TYPE_ERROR;
        } else if (isLoading && position == getItemCount() - 1) {
            return ITEM_TYPE_LOADING;
        } else {
            return ITEM_TYPE_NEWS;
        }
    }

    public void setError(boolean isError) {
        this.isError = isError;
        notifyDataSetChanged();
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
        ImageView bookmarkImageView;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            bookmarkImageView = itemView.findViewById(R.id.bookmarkImageView);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        // Initialize loading indicator views here if needed

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    static class ErrorViewHolder extends RecyclerView.ViewHolder {
        // Initialize error views here if needed

        ErrorViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
