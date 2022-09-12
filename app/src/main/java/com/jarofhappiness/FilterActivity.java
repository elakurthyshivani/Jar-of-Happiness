package com.jarofhappiness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.fragment.LockFragment;
import com.jarofhappiness.fragment.SearchByFragment;
import com.jarofhappiness.fragment.SearchInFragment;
import com.jarofhappiness.fragment.ViewByFragment;
import com.jarofhappiness.util.Themes;

public class FilterActivity extends AppCompatActivity {
    private final int[] filterTypesStrings=new int[]{R.string.search_by, R.string.view_by,
            R.string.lock, R.string.search_in};
    private final int[] filterIDs=new int[]{R.id.search_by_option, R.id.view_by_option,
            R.id.lock_option, R.id.search_in_option};
    private final int SEARCH_BY=0, VIEW_BY=1, LOCK=2, SEARCH_IN=3;
    private final Fragment[] filterFragments=new Fragment[4];

    public static final int VIEW=0, SEARCH=1;

    private int[] indices;
    private int filtersFor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        Bundle b=getIntent().getExtras();
        assert b!=null;
        filtersFor=b.getInt("filtersFor");

        Themes.setTheme(findViewById(R.id.fragment_container), this,
                JOHDatabase.getInstance(this), b.getInt("userID"));

        indices=filtersFor==VIEW?new int[]{1, 2}:new int[]{0, 2, 3};
        showFilterTypeButtons();
        createFragments(b);
        showFragment(findViewById(filterIDs[indices[0]]));
    }

    private void showFilterTypeButtons()  {
        LinearLayout filterTypesLL=findViewById(R.id.filter_types);
        for (int index : indices) {
            TextView filterType=(TextView)getLayoutInflater().inflate(R.layout.item_filter_types,
                    filterTypesLL, false);
            filterType.setText(filterTypesStrings[index]);
            filterType.setId(filterIDs[index]);
            filterType.setTag(index);
            filterTypesLL.addView(filterType);
        }
    }

    private void createFragments(Bundle b)  {
        for(int index : indices)    {
            switch(index)   {
                default: case SEARCH_BY:
                    filterFragments[SEARCH_BY]=new SearchByFragment(b.getInt("searchByOption"));
                    break;
                case VIEW_BY:
                    filterFragments[VIEW_BY]=new ViewByFragment(b.getInt("viewByOption"));
                    break;
                case LOCK:
                    filterFragments[LOCK]=new LockFragment(b.getBooleanArray("lockOptions"));
                    break;
                case SEARCH_IN:
                    filterFragments[SEARCH_IN]=new SearchInFragment(b.getBooleanArray(
                            "searchInOptions"));
                    break;
            }
        }
    }

    public void showFragment(View v)    {
        int index=(Integer)v.getTag();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                filterFragments[index]).commit();
        setBGForFilterTypes(index);
    }

    private void setBGForFilterTypes(int index)    {
        for (int value : indices)
            findViewById(filterIDs[value]).setBackgroundResource(R.color.white);
        findViewById(filterIDs[index]).setBackgroundResource(R.color.peach_puff);
    }

    public void back(View v)  {
        setResult(RESULT_CANCELED, new Intent());
        finish();
    }

    public void saveFiltersAndBack(View v)  {
        Intent i=new Intent();
        if(filtersFor==SEARCH) {
            i.putExtra("searchByOption", ((SearchByFragment)filterFragments[SEARCH_BY]).
                    getChecked());
            i.putExtra("searchInOptions", ((SearchInFragment)filterFragments[SEARCH_IN]).
                    getChecked());
        }
        else
            i.putExtra("viewByOption", ((ViewByFragment)filterFragments[VIEW_BY]).getChecked());
        i.putExtra("lockOptions", ((LockFragment)filterFragments[LOCK]).getChecked());
        setResult(RESULT_OK, i);
        finish();
    }
}