package com.jarofhappiness;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.fragment.LockMemoriesFragment;
import com.jarofhappiness.fragment.MyAccountFragment;
import com.jarofhappiness.fragment.SetPasswordFragment;
import com.jarofhappiness.fragment.ThemesGridFragment;
import com.jarofhappiness.util.Themes;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    public static final int CHANGE_THEMES=0, LOCK_MEMORIES=1, SET_PASSWORD=2, MY_ACCOUNT=3;

    private int userID, curOption;
    private String newGmail;
    private Fragment fragment;
    private LinearLayout mainSettingsContainer;
    private FrameLayout subSettingsContainer;
    private ImageView back, ok;
    private TextView actionName;
    private JOHDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        database=JOHDatabase.getInstance(this);

        userID=Objects.requireNonNull(getIntent().getExtras()).getInt("userID");

        mainSettingsContainer=findViewById(R.id.main_settings_container);
        subSettingsContainer=findViewById(R.id.sub_settings_container);
        actionName=findViewById(R.id.action_name);
        back=findViewById(R.id.back);
        ok=findViewById(R.id.ok);

        Themes.setTheme(mainSettingsContainer, this, database, userID);
        Themes.setTheme(subSettingsContainer, this, database, userID);
    }

    public void openChangeThemes(View v)    {
        showSubSettings();
        curOption=CHANGE_THEMES;

        fragment=new ThemesGridFragment(userID);
        getSupportFragmentManager().beginTransaction().replace(R.id.sub_settings_container,
                fragment).commit();
    }

    public void openLockMemories(View v)    {
        showSubSettings();
        curOption=LOCK_MEMORIES;

        fragment=new LockMemoriesFragment(userID);
        getSupportFragmentManager().beginTransaction().replace(R.id.sub_settings_container,
                fragment).commit();
    }

    public void openSetPassword(int lockType, int curLockType) {
        curOption=SET_PASSWORD;

        fragment=new SetPasswordFragment(userID, lockType, curLockType);
        getSupportFragmentManager().beginTransaction().replace(R.id.sub_settings_container,
                fragment).commit();
    }

    public void openMyAccount(View v)   {
        showSubSettings();
        curOption=MY_ACCOUNT;

        fragment=new MyAccountFragment(userID);
        getSupportFragmentManager().beginTransaction().replace(R.id.sub_settings_container,
                fragment).commit();
    }

    private void showSubSettings()  {
        mainSettingsContainer.setVisibility(View.GONE);
        subSettingsContainer.setVisibility(View.VISIBLE);
    }

    private void backToMainSettings()  {
        switch(curOption)   {
            default:
                getSupportFragmentManager().beginTransaction().remove(fragment);
                mainSettingsContainer.setVisibility(View.VISIBLE);
                subSettingsContainer.setVisibility(View.GONE);
                actionName.setText(R.string.settings);
                back.setVisibility(View.GONE);
                ok.setVisibility(View.GONE);
                break;
            case SET_PASSWORD:
                openLockMemories(null);
                break;
        }
    }

    public void gotoMenu(View v)    {
        Intent i=new Intent(this, MenuActivity.class);
        i.putExtra("userID", userID);
        startActivity(i);
    }

    public void back(View v)    {
        backToMainSettings();
    }

    @Override
    public void onBackPressed() {
        if(mainSettingsContainer.getVisibility()==View.VISIBLE)
            super.onBackPressed();
        else
            backToMainSettings();
    }

    public void ok(View v)  {
        switch(curOption)   {
            default: case CHANGE_THEMES:
                ((ThemesGridFragment)fragment).saveTheme();
                Themes.setTheme(mainSettingsContainer, this, database, userID);
                break;
            case SET_PASSWORD:

                break;
            case MY_ACCOUNT:
                GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.
                        DEFAULT_SIGN_IN).requestEmail().build();
                GoogleSignInClient mGoogleSignInClient= GoogleSignIn.getClient(this, gso);

                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                getSupportFragmentManager().beginTransaction().remove(fragment);

                                Intent i=new Intent(SettingsActivity.this,
                                        LoginActivity.class);
                                //i.putExtra("toast", "Account is successfully deleted!");
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        });
                break;
        }
    }

    public void selectNewGoogleAccount()    {
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        GoogleSignInClient mGoogleSignInClient=GoogleSignIn.getClient(this, gso);
    }

    public String getNewGmail() {
        return newGmail;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1) {
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                newGmail=Objects.requireNonNull(task.getResult(ApiException.class)).getEmail();
            }
            catch (ApiException e)  {
                Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
            }
        }
    }
}