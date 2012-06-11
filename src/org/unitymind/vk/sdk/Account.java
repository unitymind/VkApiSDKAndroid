package org.unitymind.vk.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Account {
    public String access_token;
    public long user_id;

    private SharedPreferences preferences;

    public Account(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void save() {
        Editor editor = preferences.edit();
        editor.putString("access_token", access_token);
        editor.putLong("user_id", user_id);
        editor.commit();
    }

    public void restore() {
        access_token = preferences.getString("access_token", null);
        user_id = preferences.getLong("user_id", 0);
    }

    public void clear() {
        user_id = 0;
        access_token = null;
        save();
    }

    @Override
    public String toString() {
        return String.format("User is: %s. account_token: %s.", user_id, access_token);
    }
}