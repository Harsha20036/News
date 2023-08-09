package com.location.news;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;


@Module
public class NewsModule {

    @Provides
    public NewsRepository provideNewsRepository() {
        return new NewsRepository(); // You can initialize the repository here
    }

    @Provides
    public NewsAdapter provideNewsAdapter() {
        return new NewsAdapter(new ArrayList<>());
    }
}
