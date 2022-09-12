package com.jarofhappiness.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.jarofhappiness.R;
import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.database.UserEntity;

public class LockPasswordDialog extends Dialog implements android.view.View.OnClickListener {
    private LockResult lockResult;
    private JOHDatabase database;
    private int userID;
    private Activity activity;

    public LockPasswordDialog(Activity activity, int userID)  {
        super(activity);
        this.activity=activity;
        this.userID=userID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_lock_password);
        findViewById(R.id.dialog_back).setOnClickListener(this);
        findViewById(R.id.dialog_select).setOnClickListener(this);
        findViewById(R.id.dialog_close).setOnClickListener(this);

        database=JOHDatabase.getInstance(getContext());
        database.userDao().getLockType(userID).observe((LifecycleOwner)activity,
                new Observer<Integer>() {
            @Override
            public void onChanged(Integer lockType) {
                if(lockType==UserEntity.PIN)
                    ((EditText)findViewById(R.id.password_input)).setInputType(
                            InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                Log.d("Shivani", lockType+"");
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.dialog_select)
            lockResult.isLockedOrNot(doPasswordsMatch(((EditText)findViewById(R.id.password_input))
                    .getText().toString()));
        dismiss();
    }

    private boolean doPasswordsMatch(String password1)  {
        // DATABASE CALL
        return true;
    }

    public void setLockResult(LockResult lockResult)    {
        this.lockResult=lockResult;
    }

    public interface LockResult {
        void isLockedOrNot(boolean isLocked);
    }

}
