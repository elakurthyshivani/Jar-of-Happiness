package com.jarofhappiness.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jarofhappiness.R;
import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.util.Themes;

public class EmptyFragment extends Fragment {
    private int userID;

    public EmptyFragment(int userID)    {
        super();

        this.userID=userID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_empty, container, false);
        Themes.setTheme(view, this, JOHDatabase.getInstance(getContext()), userID);
        return view;
    }
}
