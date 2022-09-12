package com.jarofhappiness.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jarofhappiness.R;
import com.jarofhappiness.SettingsActivity;
import com.jarofhappiness.adapter.ThemesAdapter;
import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.database.UserEntity;

public class SetPasswordFragment extends Fragment {
    private JOHDatabase database;
    private int userID, lockType, curLockType;
    private ThemesAdapter adapter;
    private SettingsActivity activity;
    private TextView curTitle, newTitle, reTitle;

    public SetPasswordFragment(int userID, int lockType, int curLockType) {
        this.userID=userID;
        this.lockType=lockType;
        this.curLockType=curLockType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final LinearLayout view=(LinearLayout)inflater.inflate(
                R.layout.fragment_set_password, container, false);

        database=JOHDatabase.getInstance(getContext());
        activity=(SettingsActivity)getActivity();
        assert activity!=null;
        activity.findViewById(R.id.ok).setVisibility(View.VISIBLE);
        ((TextView)activity.findViewById(R.id.action_name)).setText(String.format("Set %s",
                getString(lockType==UserEntity.PASSWORD?R.string.password:R.string.pin)));

        curTitle=view.findViewById(R.id.current_password_title);
        newTitle=view.findViewById(R.id.new_password_title);
        reTitle=view.findViewById(R.id.reenter_password_title);

        if(lockType==UserEntity.PIN)    {
            newTitle.setText(R.string.new_pin);
            reTitle.setText(R.string.reenter_pin);
        }
        else    {
            newTitle.setText(R.string.new_password);
            reTitle.setText(R.string.reenter_password);
        }
        curTitle.setText(curLockType==UserEntity.PASSWORD?R.string.current_password:
                R.string.current_pin);

        return view;
    }
}
