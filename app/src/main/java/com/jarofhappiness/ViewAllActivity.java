package com.jarofhappiness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.jarofhappiness.adapter.OthersAdapter;
import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.fragment.EmptyFragment;
import com.jarofhappiness.fragment.LockFragment;
import com.jarofhappiness.fragment.MemoriesListFragment;
import com.jarofhappiness.fragment.OthersListFragment;
import com.jarofhappiness.fragment.ViewByFragment;
import com.jarofhappiness.util.Themes;

public class ViewAllActivity extends AppCompatActivity {
    private static final int MLF=0, OLF=1;

    private int userID, curFrag, viewByOption;
    private boolean[] lockOptions;
    private LinearLayout longClickAppBar;
    private MemoriesListFragment mlf, mlf_l, mlf_ul;
    private OthersListFragment olf_w, olf_l, olf_t, olf_el;
    private EmptyFragment ef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        final Bundle b=getIntent().getExtras();
        assert b!=null;
        userID=b.getInt("userID");

        Themes.setTheme(findViewById(R.id.fragment_container), this,
                JOHDatabase.getInstance(this), userID);

        viewByOption=ViewByFragment.MEMORY;
        lockOptions=new boolean[]{true, true};

        longClickAppBar=findViewById(R.id.long_click_app_bar);
        longClickAppBar.addView(getLayoutInflater().inflate(R.layout.include_delete, longClickAppBar
                , false));

        mlf=new MemoriesListFragment(userID, MemoriesListFragment.MEM_ALL);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mlf).commit();
        curFrag=MLF;
    }

    public void changeToEmptyFragment() {
        if(ef==null)
            ef=new EmptyFragment(userID);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ef).
                commit();
    }

    @Override
    public void onBackPressed() {
        if(longClickAppBar.getVisibility()==View.GONE)
            super.onBackPressed();
        else if(curFrag==MLF)
            mlf.onBackPressed();
    }

    public void openFilters(View v) {
        Intent i=new Intent(this, FilterActivity.class);
        i.putExtra("userID", userID);
        i.putExtra("filtersFor", FilterActivity.VIEW);
        i.putExtra("viewByOption", viewByOption);
        i.putExtra("lockOptions", lockOptions);
        startActivityForResult(i, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK) {
            viewByOption=data.getIntExtra("viewByOption", ViewByFragment.MEMORY);
            lockOptions=data.getBooleanArrayExtra("lockOptions");
            Fragment cur;
            switch(viewByOption)    {
                default: case ViewByFragment.MEMORY:
                    assert lockOptions!=null;
                    if(lockOptions[LockFragment.LOCKED] && lockOptions[LockFragment.UNLOCKED])
                        cur=mlf;
                    else if(lockOptions[LockFragment.LOCKED])   {
                        if(mlf_l==null)
                            mlf_l=new MemoriesListFragment(userID, MemoriesListFragment.MEM_LKD);
                        cur=mlf_l;
                    }
                    else if(lockOptions[LockFragment.UNLOCKED]) {
                        if(mlf_ul==null)
                            mlf_ul=new MemoriesListFragment(userID,
                                    MemoriesListFragment.MEM_UNLKD);
                        cur=mlf_ul;
                    }
                    else
                        cur=mlf;
                    curFrag=MLF;
                    break;
                case ViewByFragment.WHEN:
                    if(olf_w==null)
                        olf_w=new OthersListFragment(userID, OthersAdapter.WHEN);
                    cur=olf_w;
                    curFrag=OLF;
                    break;
                case ViewByFragment.LOCATION:
                    if(olf_l==null)
                        olf_l=new OthersListFragment(userID, OthersAdapter.LOCATION);
                    cur=olf_l;
                    curFrag=OLF;
                    break;
                case ViewByFragment.TAG:
                    if(olf_t==null)
                        olf_t=new OthersListFragment(userID, OthersAdapter.TAG);
                    cur=olf_t;
                    curFrag=OLF;
                    break;
                case ViewByFragment.LINK:
                    if(olf_el==null)
                        olf_el=new OthersListFragment(userID, OthersAdapter.LINK);
                    cur=olf_el;
                    curFrag=OLF;
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    cur).commit();
        }
    }

    public void addToRecycleBin(View v) {
        if(curFrag==MLF)
            mlf.addToRecycleBin();
    }

    public void gotoMenu(View v)    {
        Intent i=new Intent(this, MenuActivity.class);
        i.putExtra("userID", userID);
        startActivity(i);
    }
}