package com.jarofhappiness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.jarofhappiness.adapter.LinksAdapter;
import com.jarofhappiness.adapter.TagsAdapter;
import com.jarofhappiness.database.AppExecutors;
import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.database.LinkEntity;
import com.jarofhappiness.database.MemoryEntity;
import com.jarofhappiness.database.TagEntity;
import com.jarofhappiness.dialog.DatePickerDialog;
import com.jarofhappiness.dialog.LockPasswordDialog;
import com.jarofhappiness.util.Themes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddEditActivity extends AppCompatActivity implements LinksAdapter.LinksListener,
        TagsAdapter.TagsListener    {
    private RecyclerView linksContainer, tagsContainer;
    private JOHDatabase database;
    private int userID, memID;
    private String addOrEdit;
    private MemoryEntity memoryE;
    private LinksAdapter linksAdapter;
    private TagsAdapter tagsAdapter;
    private LinearLayout newLinkView, newTagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        Bundle b=getIntent().getExtras();
        assert b!=null;
        userID=b.getInt("userID");
        addOrEdit=b.getString("addOrEdit");

        database=JOHDatabase.getInstance(getApplicationContext());

        tagsContainer=findViewById(R.id.tags_container);
        linksContainer=findViewById(R.id.links_container);
        newLinkView=findViewById(R.id.new_link_view);
        newTagView=findViewById(R.id.new_tag_view);

        linksAdapter=new LinksAdapter(new ArrayList<String>(), this, LinksAdapter.EDIT);
        linksContainer.setAdapter(linksAdapter);
        linksContainer.setLayoutManager(new LinearLayoutManager(this));
        tagsAdapter=new TagsAdapter(new ArrayList<String>(), this, TagsAdapter.EDIT);
        tagsContainer.setAdapter(tagsAdapter);
        tagsContainer.setLayoutManager(new FlexboxLayoutManager(this));

        Themes.setTheme(findViewById(R.id.memory_details_container), this, database,
                userID);

        enterPressed();

        if(addOrEdit.equals("edit")) {
            memID=b.getInt("memID");
            fillWithMemoryDetails();
        }
    }

    public void saveMemory(View v)  {
        String title=((EditText)findViewById(R.id.title_input)).getText().toString().trim();
        String when=((EditText)findViewById(R.id.when_input)).getText().toString().trim();
        String location=((EditText)findViewById(R.id.location_input)).getText().toString().trim();
        String memory=((EditText)findViewById(R.id.memory_input)).getText().toString().trim();
        String lock=(String)findViewById(R.id.lock).getTag();
        if(memory.equals(""))
            return;

        if(addOrEdit.equals("add"))
           memoryE=new MemoryEntity(userID, title, when, location, memory, getSelectedMoods(),
                   Integer.parseInt(lock));
        else
            memoryE=new MemoryEntity(userID, memID, title, when, location, memory, getSelectedMoods(),
                    Integer.parseInt(lock));
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(addOrEdit.equals("add"))
                    memID=database.memoryDao().getMemIDAtRowID(database.memoryDao().insert(memoryE));
                else {
                    database.memoryDao().update(memoryE);
                    database.tagDao().deleteAllTags(memID);
                    database.linkDao().deleteAllLinks(memID);
                }

                for(int i=1; i<tagsContainer.getChildCount()-1; i++)
                    database.tagDao().insert(new TagEntity(memID, ((TextView)tagsContainer.
                            getChildAt(i).findViewById(R.id.tag_view)).getText().toString()));

                for(int i=1; i<linksContainer.getChildCount()-1; i++)
                    database.linkDao().insert(new LinkEntity(memID, ((TextView)linksContainer
                            .getChildAt(i).findViewById(R.id.link_view)).getText().toString()));

                Intent i=new Intent(getApplicationContext(), ViewActivity.class);
                i.putExtra("userID", userID);
                i.putExtra("memID", memID);
                i.putExtra("randomize", false);
                i.putExtra("toast", addOrEdit.equals("add")?"add":"edit");
                startActivity(i);
                finish();
            }
        });
    }

    private void fillWithMemoryDetails()    {
        final LiveData<MemoryEntity> memLD=database.memoryDao().getMemoryDetails(memID);
        memLD.observe(this, new Observer<MemoryEntity>() {
            @Override
            public void onChanged(MemoryEntity memoryEntity) {
                memLD.removeObserver(this);
                ((EditText)findViewById(R.id.title_input)).setText(memoryEntity.getTitle());
                ((EditText)findViewById(R.id.when_input)).setText(memoryEntity.getWhen());
                ((EditText)findViewById(R.id.location_input)).setText(memoryEntity.getLocation());
                ((EditText)findViewById(R.id.memory_input)).setText(memoryEntity.getMemory());

                if(memoryEntity.getMoods().contains(MemoryEntity.LAUGH))
                    toggleMood_inner((ImageView)findViewById(R.id.laugh_icon), R.drawable.laugh_s,
                            R.drawable.laugh_us);
                if(memoryEntity.getMoods().contains(MemoryEntity.LOL))
                    toggleMood_inner((ImageView)findViewById(R.id.lol_icon), R.drawable.lol_s,
                            R.drawable.lol_us);
                if(memoryEntity.getMoods().contains(MemoryEntity.LOVE))
                    toggleMood_inner((ImageView)findViewById(R.id.love_icon), R.drawable.love_s,
                            R.drawable.laugh_us);
                if(memoryEntity.getMoods().contains(MemoryEntity.TONGUE))
                    toggleMood_inner((ImageView)findViewById(R.id.tongue_icon), R.drawable.tongue_s,
                            R.drawable.tongue_us);
                if(memoryEntity.getMoods().contains(MemoryEntity.WINK))
                    toggleMood_inner((ImageView)findViewById(R.id.wink_icon), R.drawable.wink_s,
                            R.drawable.wink_us);
            }
        });

        final LiveData<List<String>> tagsLD=database.tagDao().getTags(memID);
        tagsLD.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> tags) {
                tagsLD.removeObserver(this);
                for(int i=0; i<tags.size(); i++)
                    addTag(tags.get(i).substring(2));
            }
        });

        final LiveData<List<String>> linksLD=database.linkDao().getLinks(memID);
        linksLD.observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> links) {
                linksLD.removeObserver(this);
                for(int i=0; i<links.size(); i++)
                    addLink(links.get(i));
            }
        });
    }

    private String getSelectedMoods()  {
        StringBuilder moods=new StringBuilder();
        if(!findViewById(R.id.laugh_icon).getTag().toString().contains("us"))
            moods.append(MemoryEntity.LAUGH);
        if(!findViewById(R.id.lol_icon).getTag().toString().contains("us"))
            moods.append(MemoryEntity.LOL);
        if(!findViewById(R.id.love_icon).getTag().toString().contains("us"))
            moods.append(MemoryEntity.LOVE);
        if(!findViewById(R.id.tongue_icon).getTag().toString().contains("us"))
            moods.append(MemoryEntity.TONGUE);
        if(!findViewById(R.id.wink_icon).getTag().toString().contains("us"))
            moods.append(MemoryEntity.WINK);
        return moods.toString();
    }

    public void toggleLock(View v)  {
        final ImageView lock=(ImageView)v;
        Drawable img=ContextCompat.getDrawable(this, R.drawable.lock);
        if(lock.getTag().equals(MemoryEntity.NO+"")) {
            LockPasswordDialog lockPassword=new LockPasswordDialog(this, userID);
            Objects.requireNonNull(lockPassword.getWindow()).setBackgroundDrawableResource(
                    R.color.transparent);
            lockPassword.setLockResult(new LockPasswordDialog.LockResult() {
                @Override
                public void isLockedOrNot(boolean isLocked) {
                    if(isLocked) {
                        lock.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.unlock));
                        lock.setTag(MemoryEntity.YES+"");
                        Toast.makeText(getApplicationContext(), "This memory is locked",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            lockPassword.show();
        }
        else {
            lock.setImageDrawable(img);
            lock.setTag(MemoryEntity.NO+"");
            Toast.makeText(this, "This memory is unlocked", Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoMenu(View v)    {
        Intent i=new Intent(this, MenuActivity.class);
        i.putExtra("userID", userID);
        startActivity(i);
    }

    public void toggleMood(View v)  {
        ImageView mood=(ImageView)v;
        switch(mood.getId())   {
            case R.id.laugh_icon:
                toggleMood_inner(mood, R.drawable.laugh_s, R.drawable.laugh_us);
                break;
            case R.id.lol_icon:
                toggleMood_inner(mood, R.drawable.lol_s, R.drawable.lol_us);
                break;
            case R.id.love_icon:
                toggleMood_inner(mood, R.drawable.love_s, R.drawable.love_us);
                break;
            case R.id.tongue_icon:
                toggleMood_inner(mood, R.drawable.tongue_s, R.drawable.tongue_us);
                break;
            case R.id.wink_icon:
                toggleMood_inner(mood, R.drawable.wink_s, R.drawable.wink_us);
                break;
            default:
        }
    }

    private void toggleMood_inner(ImageView mood, int s, int us) {
        if(mood.getTag().toString().contains("us")) {
            mood.setImageDrawable(ContextCompat.getDrawable(this, s));
            mood.setTag("s");
        }
        else    {
            mood.setImageDrawable(ContextCompat.getDrawable(this, us));
            mood.setTag("us");
        }
    }

    public void openDatePicker(View v)  {
        DatePickerDialog datepicker=new DatePickerDialog(this);
        Objects.requireNonNull(datepicker.getWindow()).setBackgroundDrawableResource(
                R.color.transparent);
        datepicker.setDateResult(new DatePickerDialog.DateResult() {
            @Override
            public void getSelectedDate(String date) {
                ((EditText)findViewById(R.id.when_input)).setText(date);
            }
        });
        datepicker.show();
    }

    private void enterPressed() {
        findViewById(R.id.tag_input).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_ENTER)
                    addTag(view);
                return true;
            }
        });
        findViewById(R.id.link_input).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction()==KeyEvent.ACTION_DOWN && keyCode==KeyEvent.KEYCODE_ENTER)
                addLink(view);
                return true;
            }
        });
    }

    public void addTag(View v)  {
        EditText tagInput=findViewById(R.id.tag_input);
        String tag=tagInput.getText().toString().trim().replace(" ", "");
        if(tag.equals(""))
            return;
        addTag(tag);
        tagInput.setText("");
    }

    private void addTag(String tag) {
        tagsAdapter.addTag(tag);

        if(tagsAdapter.getItemCount()==TagsAdapter.TAGS_LIMIT)
            newTagView.setVisibility(View.GONE);
    }

    public void showNewTagView()    {
        if(tagsAdapter.getItemCount()<TagsAdapter.TAGS_LIMIT && newTagView.getVisibility()
                ==View.GONE)
            newTagView.setVisibility(View.VISIBLE);
    }

    public void addLink(View v) {
        EditText linkInput=findViewById(R.id.link_input);
        String link=linkInput.getText().toString().trim();
        if(link.equals(""))
            return;
        addLink(link);
        linkInput.setText("");

        if(linksAdapter.getItemCount()==LinksAdapter.LINKS_LIMIT)
            newLinkView.setVisibility(View.GONE);
    }

    public void addLink(String link) {
        linksAdapter.addLink(link);
    }

    public void showNewLinkView()    {
        if(linksAdapter.getItemCount()<LinksAdapter.LINKS_LIMIT && newLinkView.getVisibility()
                ==View.GONE)
            newLinkView.setVisibility(View.VISIBLE);
    }
}