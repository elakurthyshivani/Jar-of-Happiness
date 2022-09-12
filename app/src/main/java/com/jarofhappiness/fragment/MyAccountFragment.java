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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.jarofhappiness.R;
import com.jarofhappiness.SettingsActivity;
import com.jarofhappiness.database.AppExecutors;
import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.dialog.SimpleMessageDialog;

public class MyAccountFragment extends Fragment implements View.OnClickListener {
    private JOHDatabase database;
    private int userID;
    private SettingsActivity activity;

    public MyAccountFragment(int userID) {
        this.userID=userID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final LinearLayout view=(LinearLayout) inflater.inflate(
                R.layout.fragment_my_account, container, false);

        database=JOHDatabase.getInstance(getContext());
        activity=(SettingsActivity)getActivity();
        assert activity!=null;
        activity.findViewById(R.id.back).setVisibility(View.VISIBLE);
        ((TextView)activity.findViewById(R.id.action_name)).setText(R.string.my_account);

        view.findViewById(R.id.change_gmail).setOnClickListener(this);
        view.findViewById(R.id.delete_my_account).setOnClickListener(this);

        return view;
    }

    public void onClick(View v) {
        if(v.getId()==R.id.delete_my_account)   {
            final SimpleMessageDialog simpleMessageDialog=new SimpleMessageDialog(activity,
                    R.string.delete_my_account_message);
            simpleMessageDialog.setResult(new SimpleMessageDialog.Result() {
                @Override
                public void getResult() {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            database.binDao().emptyBin(userID);
                            database.linkDao().emptyLinks(userID);
                            database.tagDao().emptyTags(userID);
                            database.memoryDao().emptyMemories(userID);
                            database.userDao().deleteAccount(userID);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    activity.ok(null);
                                }
                            });
                        }
                    });
                }
            });
            simpleMessageDialog.show();
        }
        else    {
            SimpleMessageDialog simpleMessageDialog=new SimpleMessageDialog(activity,
                    R.string.change_gmail_message);
            simpleMessageDialog.setResult(new SimpleMessageDialog.Result() {
                @Override
                public void getResult() {
                    /*
                    1. Check if the new gmail is already present.
                    2. If not present, simply change the gmail and log out.
                    3. If present, another dialog message if they want to merge memories
                    4. If yes, change the userID for these memIDs
                    5. If no, just dismiss.
                     */
                    activity.selectNewGoogleAccount();
                    String gmail=activity.getNewGmail();
                    database.userDao().isUserPresent(gmail).observe(activity, new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer isPresent) {
                            if(isPresent==0)    {
                                
                            }
                            else    {

                            }
                        }
                    });
                }
            });
            simpleMessageDialog.show();
        }
    }
}