package com.jarofhappiness.util;

import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.jarofhappiness.R;
import com.jarofhappiness.database.JOHDatabase;

import java.util.ArrayList;

public class Themes {
    public static final ArrayList<Integer> themes=new ArrayList<>();

    static  {
        themes.add(R.drawable.theme_1);
        themes.add(R.drawable.theme_2);
        themes.add(R.drawable.theme_3);
        themes.add(R.drawable.theme_4);
        themes.add(R.drawable.theme_5);
        themes.add(R.drawable.theme_6);
        themes.add(R.drawable.theme_7);
        themes.add(R.drawable.theme_8);

    }

    public static void setTheme(final View view, LifecycleOwner context, JOHDatabase database,
                                int userID)   {
        LiveData<Integer> themeLD=database.userDao().getThemeID(userID);
        themeLD.observe(context, new Observer<Integer>() {
            @Override
            public void onChanged(Integer themeID) {
                try {
                    view.setBackgroundResource(themeID);
                }
                catch(NullPointerException ignored) {

                }
            }
        });
    }
}
