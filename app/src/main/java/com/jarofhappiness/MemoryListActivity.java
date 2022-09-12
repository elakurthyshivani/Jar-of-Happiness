package com.jarofhappiness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.fragment.MemoriesListFragment;
import com.jarofhappiness.util.Themes;

public class MemoryListActivity extends AppCompatActivity {
    private MemoriesListFragment mlf;
    private LinearLayout longClickAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_list);

        final Bundle b=getIntent().getExtras();
        assert b!=null;
        int userID=b.getInt("userID");
        int type=b.getInt("type");
        String text=b.getString("text");

        Themes.setTheme(findViewById(R.id.fragment_container), this,
                JOHDatabase.getInstance(this), userID);

        longClickAppBar=findViewById(R.id.long_click_app_bar);
        longClickAppBar.addView(getLayoutInflater().inflate(R.layout.include_delete, longClickAppBar
                , false));

        mlf=new MemoriesListFragment(userID, type, text);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mlf).commit();
    }

    public void back(View v)    {
        finish();
    }

    @Override
    public void onBackPressed() {
        if(longClickAppBar.getVisibility()==View.GONE)
            super.onBackPressed();
        else
            mlf.onBackPressed();
    }

    public void addToRecycleBin(View v) {
        mlf.addToRecycleBin();
    }
}