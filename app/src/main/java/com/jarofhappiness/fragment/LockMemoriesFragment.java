package com.jarofhappiness.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.jarofhappiness.R;
import com.jarofhappiness.SettingsActivity;
import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.database.UserEntity;

public class LockMemoriesFragment extends Fragment implements View.OnClickListener {
    private int userID, lockType;
    private SettingsActivity activity;

    public LockMemoriesFragment(int userID) {
        this.userID=userID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final ConstraintLayout view=(ConstraintLayout)inflater.inflate(
                R.layout.fragment_lock_memories, container, false);

        JOHDatabase database=JOHDatabase.getInstance(getContext());
        activity=(SettingsActivity)getActivity();
        assert activity!=null;
        activity.findViewById(R.id.back).setVisibility(View.VISIBLE);
        ((TextView)activity.findViewById(R.id.action_name)).setText(R.string.lock_memories);

        database.userDao().getLockType(userID).observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer lt) {
                lockType=lt;
                view.findViewById(R.id.selected_pin_option).setVisibility(lockType==UserEntity.PIN?
                        View.VISIBLE:View.GONE);
                view.findViewById(R.id.selected_password_option).setVisibility(
                        lockType==UserEntity.PIN?View.GONE:View.VISIBLE);
            }
        });

        view.findViewById(R.id.pin_option).setOnClickListener(this);
        view.findViewById(R.id.password_option).setOnClickListener(this);

        return view;
    }

    public void onClick(View v)    {
        activity.openSetPassword(v.getId()==R.id.password_option?UserEntity.PASSWORD:
                UserEntity.PIN, lockType);
    }
}
