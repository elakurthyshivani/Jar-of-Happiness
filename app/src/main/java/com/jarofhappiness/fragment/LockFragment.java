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

public class LockFragment extends Fragment {
    public static final int LOCKED=0, UNLOCKED=1;
    private boolean[] checked;

    public LockFragment(boolean[] checked)  {
        this.checked=checked;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final LinearLayout view=(LinearLayout)inflater.inflate(R.layout.fragment_lock, container,
                false);


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
