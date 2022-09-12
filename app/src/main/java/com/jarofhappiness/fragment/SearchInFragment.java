package com.jarofhappiness.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jarofhappiness.R;

public class SearchInFragment extends Fragment {
    public static final int TITLE=0, WHEN=1, LOCATION=2, MEMORY=3, TAG=4;
    private boolean[] checked;

    public SearchInFragment(boolean[] checked)  {
        this.checked=checked;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final LinearLayout view=(LinearLayout)inflater.inflate(R.layout.fragment_search_in,
                container, false);

        for(int i=0; i<view.getChildCount(); i++)   {
            final int cur=i;
            final CheckBox checkBox=(CheckBox)view.getChildAt(i);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    checked[cur]=isChecked;
                }
            });
            if(checked[i])
                checkBox.setChecked(true);
        }

        return view;
    }

    public boolean[] getChecked() {
        return checked;
    }
}
