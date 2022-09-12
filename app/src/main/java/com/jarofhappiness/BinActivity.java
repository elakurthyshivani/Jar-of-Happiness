package com.jarofhappiness;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.fragment.EmptyFragment;
import com.jarofhappiness.fragment.MemoriesListFragment;
import com.jarofhappiness.util.Themes;

public class BinActivity extends AppCompatActivity {
    private int userID;
    private CheckBox selectAll;
    private MemoriesListFragment mlf;
    private EmptyFragment ef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bin);

        final Bundle b=getIntent().getExtras();
        assert b!=null;
        userID=b.getInt("userID");

        selectAll=findViewById(R.id.select_all);
        LinearLayout longClickAppBar=findViewById(R.id.long_click_app_bar);
        longClickAppBar.addView(getLayoutInflater().inflate(R.layout.include_empty_bin,
                longClickAppBar, false));
        longClickAppBar.addView(getLayoutInflater().inflate(R.layout.include_restore,
                longClickAppBar, false));

        Themes.setTheme(findViewById(R.id.fragment_container), this,
                JOHDatabase.getInstance(this), userID);

        mlf=new MemoriesListFragment(userID, MemoriesListFragment.BIN);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mlf).commit();
    }

    public void changeToEmptyFragment() {
        if(ef==null)
            ef=new EmptyFragment(userID);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ef).
                commit();
    }

    public void emptyBin(View v)    {
        mlf.deleteMemoriesFromBin(true);
    }

    public void restoreMemories(View v) {
        mlf.restoreMemoriesFromBin(true);
    }

    public void deleteSelectedFromBin(View v)   {
        mlf.deleteMemoriesFromBin(false);
    }

    public void restoreSelectedMemories(View v) {
        mlf.restoreMemoriesFromBin(false);
    }

    @Override
    public void onBackPressed() {
        if(selectAll.getVisibility()==View.GONE)
            super.onBackPressed();
        else
            mlf.onBackPressed();
    }

    public void gotoMenu(View v)    {
        Intent i=new Intent(this, MenuActivity.class);
        i.putExtra("userID", userID);
        startActivity(i);
    }
}