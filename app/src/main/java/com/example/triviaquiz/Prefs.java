package com.example.triviaquiz;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences sharedPrefs;

    public Prefs(Activity activity){
        this.sharedPrefs = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public int get_best_score(){
        return sharedPrefs.getInt("best_score", 0);
    }

    public int get_current_correct(){
        return sharedPrefs.getInt("answered_correct", 0);
    }

    public int get_counter_num(){
        return sharedPrefs.getInt("count_number", 0);
    }

    public boolean get_current_answered(int counter_num){
        return sharedPrefs.getBoolean("quest_object"+counter_num, false);
    }

    public void set_best_score(int best_score){
        sharedPrefs.edit().putInt("best_score", best_score).apply();
    }
    public void set_current_correct(int current_correct){
        sharedPrefs.edit().putInt("answered_correct", current_correct).apply();
    }
    public void set_counter_num(int counter_num){
        sharedPrefs.edit().putInt("count_number", counter_num).apply();
    }
    public void set_current_answered(int counter_num, boolean isAnswered){
        sharedPrefs.edit().putBoolean("quest_object"+counter_num, isAnswered).apply();
    }
}
