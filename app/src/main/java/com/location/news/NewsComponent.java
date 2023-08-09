package com.location.news;

import dagger.Component;

@Component(modules = {NewsModule.class})
public interface NewsComponent {
    void inject(MainActivity activity);
}
