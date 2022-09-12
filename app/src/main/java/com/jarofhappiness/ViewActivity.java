package com.jarofhappiness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.jarofhappiness.adapter.LinksAdapter;
import com.jarofhappiness.adapter.TagsAdapter;
import com.jarofhappiness.database.AppExecutors;
import com.jarofhappiness.database.BinEntity;
import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.database.MemoryEntity;
import com.jarofhappiness.dialog.LockPasswordDialog;
import com.jarofhappiness.dialog.SimpleMessageDialog;
import com.jarofhappiness.util.Themes;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ViewActivity extends AppCompatActivity {
    private JOHDatabase database;
    private int userID, memID;
    private LinearLayout viewActivityScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        database=JOHDatabase.getInstance(getApplicationContext());

        final Bundle b=getIntent().getExtras();
        assert b!=null;
        userID=b.getInt("userID");

        viewActivityScreen=findViewById(R.id.view_activity_screen);

        Themes.setTheme(findViewById(R.id.memory_details_container), this,
                database, userID);
        Themes.setTheme(findViewById(R.id.memory_details), this, database, userID);

        if(!b.getBoolean("randomize")) {
            memID = b.getInt("memID");
            displayMemoryDetails();

            final LiveData<Integer> binLD=database.binDao().presentInBin(memID);
            binLD.observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer isPresent) {
                    Log.d("shivani", isPresent+"");
                    showEditDeleteDownloadIcons(isPresent<=0);
                }
            });

            String toast=b.getString("toast");
            assert toast!=null;
            if(toast.equals("add"))
                Toast.makeText(this, "Memory added!", Toast.LENGTH_SHORT).show();
            else if(toast.equals("edit"))
                Toast.makeText(this, "Memory edited!", Toast.LENGTH_SHORT).show();
        }
        else {
            findViewById(R.id.randomize).setVisibility(View.VISIBLE);
            getAnotherRandomMemory(null);
        }
    }

    private void displayMemoryDetails() {
        final ViewActivity viewActivity=this;
        final LiveData<MemoryEntity> memoryLD=database.memoryDao().getMemoryDetails(memID);
        memoryLD.observe(this, new Observer<MemoryEntity>() {
            @Override
            public void onChanged(MemoryEntity memory) {
                memoryLD.removeObserver(this);
                TextView titleView=findViewById(R.id.title_view),
                        whenView=findViewById(R.id.when_view),
                        locationView=findViewById(R.id.location_view);
                LinearLayout moodsContainer=findViewById(R.id.moods_container);
                int[] allMoods={0, R.drawable.laugh_s, R.drawable.lol_s, R.drawable.love_s,
                        R.drawable.tongue_s, R.drawable.wink_s};

                if (memory.getIsLocked()==MemoryEntity.NO)
                    findViewById(R.id.lock_view).setVisibility(View.GONE);
                else    {
                    LockPasswordDialog lockPasswordDialog=new LockPasswordDialog(viewActivity, userID);
                    Objects.requireNonNull(lockPasswordDialog.getWindow()).setBackgroundDrawableResource(
                            R.color.transparent);
                    lockPasswordDialog.setLockResult(new LockPasswordDialog.LockResult() {
                        @Override
                        public void isLockedOrNot(boolean isLocked) {
                            viewActivityScreen.setVisibility(View.VISIBLE);
                        }
                    });
                    lockPasswordDialog.show();
                    viewActivityScreen.setVisibility(View.GONE);
                }

                ((TextView)findViewById(R.id.memory_id_view)).setText(String.format(Locale.ENGLISH,
                        "@Memory%06d", memID));

                if (memory.getTitle()!=null && !memory.getTitle().equals("")) {
                    titleView.setVisibility(View.VISIBLE);
                    findViewById(R.id.title_mini_icon).setVisibility(View.VISIBLE);
                    titleView.setText(memory.getTitle());
                }
                else {
                    titleView.setVisibility(View.GONE);
                    findViewById(R.id.title_mini_icon).setVisibility(View.GONE);
                }

                if (memory.getWhen()!=null && !memory.getWhen().equals(""))   {
                    whenView.setVisibility(View.VISIBLE);
                    findViewById(R.id.when_mini_icon).setVisibility(View.VISIBLE);
                    whenView.setText(memory.getWhen());
                }
                else {
                    whenView.setVisibility(View.GONE);
                    findViewById(R.id.when_mini_icon).setVisibility(View.GONE);
                }

                if(memory.getLocation()!=null && !memory.getLocation().equals("")) {
                    locationView.setVisibility(View.VISIBLE);
                    findViewById(R.id.location_mini_icon).setVisibility(View.VISIBLE);
                    locationView.setText(memory.getLocation());
                }
                else {
                    locationView.setVisibility(View.GONE);
                    findViewById(R.id.location_mini_icon).setVisibility(View.GONE);
                }

                ((TextView)findViewById(R.id.memory_view)).setText(memory.getMemory());

                if(memory.getMoods()!=null && !memory.getMoods().equals(""))    {
                    moodsContainer.setVisibility(View.VISIBLE);
                    findViewById(R.id.mood_mini_icon).setVisibility(View.VISIBLE);
                    moodsContainer.removeAllViews();
                    for(int i=0; i<memory.getMoods().length(); i++) {
                        ImageView mood=(ImageView)getLayoutInflater().inflate(R.layout.item_selected_mood,
                                moodsContainer, false);
                        mood.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                                allMoods[Integer.parseInt(memory.getMoods().charAt(i)+"")]));
                        moodsContainer.addView(mood);
                    }
                }
                else    {
                    moodsContainer.setVisibility(View.GONE);
                    findViewById(R.id.mood_mini_icon).setVisibility(View.GONE);
                }
            }
        });

        final LiveData<List<String>> tagsLD=database.tagDao().getTags(memID);
        tagsLD.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> tags) {
                tagsLD.removeObserver(this);
                RecyclerView tagsContainer=findViewById(R.id.tags_container);
                if(tags.size()!=0)  {
                    tagsContainer.setVisibility(View.VISIBLE);
                    findViewById(R.id.tag_mini_icon).setVisibility(View.VISIBLE);
                    tagsContainer.setAdapter(new TagsAdapter(tags, TagsAdapter.VIEW));
                    tagsContainer.setLayoutManager(new FlexboxLayoutManager(getApplicationContext()));
                }
                else {
                    tagsContainer.setVisibility(View.GONE);
                    findViewById(R.id.tag_mini_icon).setVisibility(View.GONE);
                }
            }
        });

        final LiveData<List<String>> linksLD=database.linkDao().getLinks(memID);
        linksLD.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> links) {
                linksLD.removeObserver(this);
                RecyclerView linksContainer=findViewById(R.id.links_container);
                if(links.size()!=0)  {
                    linksContainer.setVisibility(View.VISIBLE);
                    findViewById(R.id.link_mini_icon).setVisibility(View.VISIBLE);
                    linksContainer.setAdapter(new LinksAdapter(links, LinksAdapter.VIEW));
                    linksContainer.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                }
                else {
                    linksContainer.setVisibility(View.GONE);
                    findViewById(R.id.link_mini_icon).setVisibility(View.GONE);
                }
            }
        });
    }

    public void getAnotherRandomMemory(View v)  {
        final LiveData<Integer> memLD=database.memoryDao().getRandomMemID(userID);
        memLD.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                memLD.removeObserver(this);
                memID=integer;
                displayMemoryDetails();
                if(findViewById(R.id.edit_icon).getVisibility()==View.GONE)
                    showEditDeleteDownloadIcons(true);
                Toast.makeText(getApplicationContext(), "Random memory displayed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDeleteDownloadIcons(boolean show)  {
        int visibility=show?View.VISIBLE:View.GONE;
        Log.d("shivani visibility", visibility+"");
        findViewById(R.id.edit_icon).setVisibility(visibility);
        findViewById(R.id.delete_icon).setVisibility(visibility);
        findViewById(R.id.download_icon).setVisibility(visibility);
        findViewById(R.id.delete_view).setVisibility(show?View.GONE:View.VISIBLE);
    }

    public void editMemory(View v)  {
        Intent i=new Intent(this, AddEditActivity.class);
        i.putExtra("userID", userID);
        i.putExtra("memID", memID);
        i.putExtra("addOrEdit", "edit");
        startActivity(i);
        finish();
    }

    public void addToRecycleBin(View v) {
        SimpleMessageDialog simpleMessageDialog=new SimpleMessageDialog(this,
                R.string.add_to_bin_message);
        simpleMessageDialog.setResult(new SimpleMessageDialog.Result() {
            @Override
            public void getResult() {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.binDao().insert(new BinEntity(memID));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Added to recycle bin",
                                        Toast.LENGTH_SHORT).show();
                                showEditDeleteDownloadIcons(false);
                            }
                        });
                    }
                });
            }
        });
        simpleMessageDialog.show();
    }

    public void downloadMemory(View v)  {
        LinearLayout memoryDetails=findViewById(R.id.memory_details);

        final Bitmap memBit=Bitmap.createBitmap(memoryDetails.getWidth(), memoryDetails.getHeight(),
                Bitmap.Config.ARGB_8888);
        memoryDetails.draw(new Canvas(memBit));

        LiveData<String> gmailLD=database.userDao().getGmail(userID);
        gmailLD.observe(this, new Observer<String>() {
            @SuppressLint("InlinedApi")
            @Override
            public void onChanged(String gmail) {
                String folderName="Memories_"+gmail, fileName="Mem-"+memID+".png";
                boolean errorOccurred=false;

                File directory=new File(getApplicationContext().getExternalFilesDir(null),
                        folderName);
                if(!directory.exists())
                    directory.mkdirs();
                File memFile=new File(directory, fileName);
                if(memFile.exists())
                    memFile.delete();
                try {
                    FileOutputStream out=new FileOutputStream(memFile);
                    memBit.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                }catch(Exception e) {
                    Toast.makeText(getApplicationContext(), "Error occurred while saving the memory",
                            Toast.LENGTH_SHORT).show();
                    errorOccurred=true;
                }

                if(!errorOccurred)   {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                    values.put(MediaStore.Images.Media.DESCRIPTION, fileName);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.DATA, memFile.getAbsolutePath());
                    getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Toast.makeText(getApplicationContext(), "Memory saved as an image. Check your gallery",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void gotoMenu(View v)    {
        Intent i=new Intent(this, MenuActivity.class);
        i.putExtra("userID", userID);
        startActivity(i);
    }
}