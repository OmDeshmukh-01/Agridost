package com.example.dummy;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        // Attach saved locale before anything else
        String currentLang = LocaleHelper.getLanguage(base);
        android.util.Log.d("MyApplication", "attachBaseContext - Current language: " + currentLang);
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Ensure locale is applied on app process start
        String currentLang = LocaleHelper.getLanguage(this);
        android.util.Log.d("MyApplication", "onCreate - Current language: " + currentLang);
        LocaleHelper.onAttach(this);
    }
}
