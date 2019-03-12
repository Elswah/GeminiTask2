package com.gemini.block.geminitask.utils;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by paulodichone on 3/26/15.
 */
public class Prefs {

    SharedPreferences preferences;

    public Prefs(Activity activity) {
        preferences = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    //If user has n ot chose a city, return default!
    public String getTitle() {
        return preferences.getString("title", "bitcoin");
    }

    public void setTitle(String title) {
        preferences.edit().putString("title", title).commit();
    }
}
