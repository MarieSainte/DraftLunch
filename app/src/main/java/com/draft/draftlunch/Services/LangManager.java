package com.draft.draftlunch.Services;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LangManager {

    Context ctx;

    public LangManager(Context context){
        ctx = context;
    }

    public void setLang(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = ctx.getResources();
        Configuration config  = resources.getConfiguration();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
