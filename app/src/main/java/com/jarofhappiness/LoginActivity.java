package com.jarofhappiness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.jarofhappiness.database.AppExecutors;
import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.database.UserEntity;
import com.jarofhappiness.util.Themes;

public class LoginActivity extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private JOHDatabase database;
    private int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Toast when signout or account deleted.

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        mGoogleSignInClient=GoogleSignIn.getClient(this, gso);

        database=JOHDatabase.getInstance(getApplicationContext());
    }

    @Override
    protected void onStart()    {
        super.onStart();
        processAccount(GoogleSignIn.getLastSignedInAccount(this));
    }

    public void signIn(View v)  {
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), 1);
    }

    private void processAccount(final GoogleSignInAccount account)    {
        if(account!=null) {
            final String gmail=account.getEmail();
            database.userDao().isUserPresent(gmail).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(final Integer isPresent) {
                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            if(isPresent==0)    {
                                assert gmail!=null;
                                database.userDao().insert(new UserEntity(gmail, Themes.themes.get(0)));
                            }
                            userID=database.userDao().getUser(gmail).getUserID();
                        }
                    });

                    Intent i=new Intent(getApplicationContext(), AddEditActivity.class);
                    i.putExtra("userID", userID);
                    i.putExtra("addOrEdit", "add");
                    startActivity(i);
                    finish();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1) {
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                processAccount(task.getResult(ApiException.class));
            }
            catch (ApiException e)  {
                processAccount(null);
            }
        }
    }
}