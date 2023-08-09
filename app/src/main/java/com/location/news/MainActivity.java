package com.location.news;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.location.newsapp.R;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements NewsAdapter.OnLoadMoreListener {

    private RecyclerView newsRecyclerView;
    private boolean isLoading = false;
    private boolean isError = false;

    @Inject
    NewsAdapter newsAdapter;

    private List<News> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NewsComponent newsComponent = DaggerNewsComponent.create();
        newsComponent.inject(this);

        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        newsAdapter = new NewsAdapter(newsList);
        newsAdapter.setOnLoadMoreListener(this);

        newsRecyclerView.setAdapter(newsAdapter);

        newsList.addAll(getInitialNewsData());
        newsAdapter.notifyDataSetChanged();

        setupSearchBar();
    }

    @Override
    public void onLoadMore() {
        if (!isLoading && !isError) {
            isLoading = true;
            new Handler().postDelayed(() -> {
                List<News> moreNews = getMoreNewsData();
                if (moreNews.isEmpty()) {
                    newsAdapter.setError(true);
                } else {
                    newsList.addAll(moreNews);
                    newsAdapter.setLoading(false);
                    isLoading = false;
                }
            }, 1500);
        }
    }

    private void handleFetchError() {
        isError = true;
        newsAdapter.setError(true);
    }

    private List<News> getInitialNewsData() {
        List<News> initialNews = new ArrayList<>();
        initialNews.add(new News("Headline 1", "News content 1..."));
        initialNews.add(new News("Headline 2", "News content 2..."));
        initialNews.add(new News("Headline 3", "News content 3..."));
        return initialNews;
    }

    private List<News> getMoreNewsData() {
        try {
            // Fetch news data
            return new ArrayList<>(); // Returned data
        } catch (Exception e) {
            handleFetchError();
            return new ArrayList<>(); // Empty data
        }
    }

    private void setupSearchBar() {
        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = searchEditText.getText().toString().trim();
                searchNews(query);
                return true;
            }
            return false;
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                searchNews(query);
            }
        });
    }

    private void searchNews(String query) {
        List<News> searchResults = new ArrayList<>();

        // Loop through the existing newsList and filter items based on the query
        for (News news : getInitialNewsData()) {
            if (news.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    news.getContent().toLowerCase().contains(query.toLowerCase())) {
                searchResults.add(news);
            }
        }

        if (searchResults.isEmpty()) {
            // Display a message indicating no search results were found
            Toast.makeText(this, "No search results found.", Toast.LENGTH_SHORT).show();
        } else {
            // Clear the newsList and add the search results
            newsList.clear();
            newsList.addAll(searchResults);

            // Notify the adapter of the data change
            newsAdapter.notifyDataSetChanged();
        }
    }


}
