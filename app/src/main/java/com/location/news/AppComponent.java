package com.location.news;

import dagger.Component;

@Component(modules = {NewsModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
}
