package com.jarofhappiness.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jarofhappiness.R;
import com.jarofhappiness.SettingsActivity;
import com.jarofhappiness.adapter.ThemesAdapter;
import com.jarofhappiness.database.AppExecutors;
import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.util.Themes;

public class ThemesGridFragment extends Fragment implements ThemesAdapter.ThemesListener {
    private JOHDatabase database;
    private int userID;
    private ThemesAdapter adapter;
    private RecyclerView changeThemes;
    private SettingsActivity activity;
    private FrameLayout subSettingsContainer;

    public ThemesGridFragment(int userID) {
        this.userID=userID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final RecyclerView view=(RecyclerView)inflater.inflate(R.layout.fragment_change_theme,
                container, false);

        database=JOHDatabase.getInstance(getContext());

        activity=(SettingsActivity)getActivity();
        assert activity!=null;
        activity.findViewById(R.id.back).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.ok).setVisibility(View.VISIBLE);
        ((TextView)activity.findViewById(R.id.action_name)).setText(R.string.change_theme);
        subSettingsContainer=activity.findViewById(R.id.sub_settings_container);

        changeThemes=view;
        final ThemesAdapter.ThemesListener themesListener=this;
        LiveData<Integer> themeLD=database.userDao().getThemeID(userID);
        themeLD.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer themeID) {
                subSettingsContainer.setBackgroundResource(themeID);
                adapter=new ThemesAdapter(Themes.themes, themesListener, themeID);
                view.setAdapter(adapter);
            }
        });
        view.setLayoutManager(new GridLayoutManager(getContext(), 3));

        return view;
    }

    @Override
    public void changeTheme(int themeID, int index, int prevCurThemePosition) {
        Log.d("shivani", prevCurThemePosition+"");
        changeThemes.getChildAt(index).findViewById(R.id.selected).setVisibility(View.VISIBLE);
        changeThemes.getChildAt(prevCurThemePosition).findViewById(R.id.selected).
                setVisibility(View.GONE);
        subSettingsContainer.setBackgroundResource(themeID);
    }

    public void saveTheme() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.userDao().updateTheme(userID,
                        Themes.themes.get(adapter.getCurThemePosition()));
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Theme changed!", Toast.LENGTH_SHORT).
                                show();
                        activity.back(null);
                    }
                });
            }
        });
    }
}
