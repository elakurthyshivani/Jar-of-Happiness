package com.jarofhappiness.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jarofhappiness.R;

import java.util.Objects;

public class SearchByFragment extends Fragment {
    public static final int MEMORY=0, WHEN=1, LOCATION=2, MOOD=3, TAG=4;
    private int checked;

    public SearchByFragment(int checked)    {
        this.checked=checked;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final RadioGroup view=(RadioGroup)inflater.inflate(R.layout.fragment_search_by, container,
                false);

        for(int i=0; i<view.getChildCount(); i++) {
            final int cur=i;
            final RadioButton radioButton=(RadioButton)view.getChildAt(i);
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked)
                        checked=cur;
                    Objects.requireNonNull(getActivity()).findViewById(R.id.lock_option).
                            setVisibility(checked==MEMORY?View.VISIBLE:View.GONE);
                    Objects.requireNonNull(getActivity()).findViewById(R.id.search_in_option).
                            setVisibility(checked==MEMORY?View.VISIBLE:View.GONE);
                }
            });
            if(i==checked)
                radioButton.setChecked(true);
        }

        return view;
    }

    public int getChecked() {
        return checked;
    }
}
