package com.jarofhappiness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.database.MemoryEntity;
import com.jarofhappiness.util.Themes;

import java.util.Locale;
import java.util.Objects;

public class StatisticsActivity extends AppCompatActivity {
    private int userID;
    private JOHDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        userID= Objects.requireNonNull(getIntent().getExtras()).getInt("userID");

        database=JOHDatabase.getInstance(this);

        Themes.setTheme(findViewById(R.id.no_s_container), this, database, userID);

        populateCounts();
    }

    private void populateCounts()   {
        final LiveData<Integer> titleLD=database.memoryDao().countOfTitles(userID);
        titleLD.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                titleLD.removeObserver(this);
                ((TextView)findViewById(R.id.title_count)).setText(String.format(Locale.ENGLISH,
                        "%d", count));
            }
        });

        final LiveData<Integer> whenLD=database.memoryDao().countOfWhens(userID);
        whenLD.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                whenLD.removeObserver(this);
                ((TextView)findViewById(R.id.when_count)).setText(String.format(Locale.ENGLISH,
                        "%d", count));
            }
        });

        final LiveData<Integer> locationLD=database.memoryDao().countOfLocations(userID);
        locationLD.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                locationLD.removeObserver(this);
                ((TextView)findViewById(R.id.location_count)).setText(String.format(Locale.ENGLISH,
                        "%d", count));
            }
        });

        final  LiveData<Integer> memoryLD=database.memoryDao().countOfMemories(userID);
        memoryLD.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                memoryLD.removeObserver(this);
                ((TextView)findViewById(R.id.memory_count)).setText(String.format(Locale.ENGLISH,
                        "%d", count));
            }
        });

        final LiveData<Integer> lockedMemoryLD=database.memoryDao().countOfLockedMemories(userID);
        lockedMemoryLD.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                lockedMemoryLD.removeObserver(this);
                ((TextView)findViewById(R.id.locked_memories_count)).setText(String.format(
                        Locale.ENGLISH, "%d", count));
            }
        });

        final LiveData<Integer> deletedMemoryLD=database.binDao().countOfDeletedMemories(userID);
        deletedMemoryLD.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                deletedMemoryLD.removeObserver(this);
                ((TextView)findViewById(R.id.deleted_memories_count)).setText(String.format(
                        Locale.ENGLISH, "%d", count));
            }
        });

        final int[] moodIDs=new int[]{R.id.laugh_count, R.id.lol_count, R.id.love_count,
            R.id.tongue_count, R.id.wink_count};
        for(int i=Integer.parseInt(MemoryEntity.LAUGH); i<=Integer.parseInt(MemoryEntity.WINK);
                i++)    {
            final LiveData<Integer> moodLD=database.memoryDao().countOfMemoriesWithMood(userID,
                    "%"+i+"%");
            final int moodID=i;
            moodLD.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer count) {
                    moodLD.removeObserver(this);
                    ((TextView)findViewById(moodIDs[moodID-1])).setText(String.format(
                            Locale.ENGLISH, "%d", count));
                }
            });
        }

        final LiveData<Integer> tagLD=database.tagDao().countOfTags(userID);
        tagLD.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                tagLD.removeObserver(this);
                ((TextView)findViewById(R.id.tag_count)).setText(String.format(
                        Locale.ENGLISH, "%d", count));
            }
        });

        final LiveData<Integer> linkLD=database.linkDao().countOfLinks(userID);
        linkLD.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                linkLD.removeObserver(this);
                ((TextView)findViewById(R.id.link_count)).setText(String.format(
                        Locale.ENGLISH, "%d", count));
            }
        });
    }

    public void gotoMenu(View v)    {
        Intent i=new Intent(this, MenuActivity.class);
        i.putExtra("userID", userID);
        startActivity(i);
    }
}