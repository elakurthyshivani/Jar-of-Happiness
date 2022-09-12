package com.jarofhappiness.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jarofhappiness.BinActivity;
import com.jarofhappiness.R;
import com.jarofhappiness.ViewAllActivity;
import com.jarofhappiness.adapter.MemoriesAdapter;
import com.jarofhappiness.database.AppExecutors;
import com.jarofhappiness.database.BinEntity;
import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.database.MemoryEntity;
import com.jarofhappiness.database.MemoryTuple;
import com.jarofhappiness.dialog.SimpleMessageDialog;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MemoriesListFragment extends Fragment implements MemoriesAdapter.MemoriesListener {
    public final static int MEM_LKD=0, MEM_UNLKD=1, MEM_ALL=2, BIN=3, MEM_WHN=4, MEM_LOC=5,
            MEM_TAG=6, MEM_LNK=7;

    private boolean explicitlyClicked=true;
    private int userID, listType;
    private String text;
    private JOHDatabase database;
    private MemoriesAdapter adapter;
    private RecyclerView listContainer;
    private CheckBox selectAll;
    private LinearLayout appBar, longClickAppBar;
    private TextView noOfChecked;

    public MemoriesListFragment(int userID, int listType)  {
        super();

        this.userID=userID;
        this.listType=listType;
    }

    public MemoriesListFragment(int userID, int listType, String text)  {
        super();

        this.userID=userID;
        this.listType=listType;
        this.text=text;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RecyclerView view=(RecyclerView)inflater.inflate(R.layout.fragment_list, container,
                false);

        database=JOHDatabase.getInstance(getContext());

        appBar=Objects.requireNonNull(getActivity()).findViewById(R.id.app_bar);
        longClickAppBar=getActivity().findViewById(R.id.long_click_app_bar);
        noOfChecked=getActivity().findViewById(R.id.no_of_checked);
        selectAll=getActivity().findViewById(R.id.select_all);
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked && explicitlyClicked)   {
                    //setAllChecksChecked(true);
                    adapter.setAllChecksChecked(true);
                    explicitlyClicked=true;
                }
                else if(!isChecked && explicitlyClicked)    {
                    //setAllChecksChecked(false);
                    adapter.setAllChecksChecked(false);
                    explicitlyClicked=true;
                }
            }
        });
        listContainer=view;
        listContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter(this);

        return view;
    }

    private void setAdapter(final MemoriesAdapter.MemoriesListener memoriesListener)   {
        switch(listType)   {
            default:case MEM_ALL:
                final LiveData<List<MemoryTuple>> memLD1=database.memoryDao().getAllMemories(
                        userID);
                memLD1.observe(this, new Observer<List<MemoryTuple>>() {
                    @Override
                    public void onChanged(List<MemoryTuple> memoriesList) {
                        setAdapter(memoriesList, memoriesListener);
                    }
                });
            break;
            case MEM_LKD: case MEM_UNLKD:
                final LiveData<List<MemoryTuple>> memLD2=database.memoryDao().getAllMemories(
                        userID, listType==MEM_LKD?MemoryEntity.YES:MemoryEntity.NO);
                memLD2.observe(this, new Observer<List<MemoryTuple>>() {
                    @Override
                    public void onChanged(List<MemoryTuple> memoriesList) {
                        setAdapter(memoriesList, memoriesListener);
                    }
                });
            break;
            case BIN:
                final LiveData<List<MemoryTuple>> memLD3=database.memoryDao().getAllMemoriesInBin(
                        userID);
                memLD3.observe(this, new Observer<List<MemoryTuple>>() {
                    @Override
                    public void onChanged(List<MemoryTuple> memoriesList) {
                        setAdapter(memoriesList, memoriesListener);
                    }
                });
            break;
            case MEM_WHN:
                final LiveData<List<MemoryTuple>> memLD4=database.memoryDao().getAllMemoriesWhen(
                        userID, text);
                memLD4.observe(this, new Observer<List<MemoryTuple>>() {
                    @Override
                    public void onChanged(List<MemoryTuple> memoriesList) {
                        setAdapter(memoriesList, memoriesListener);
                    }
                });
                break;
            case MEM_LOC:
                final LiveData<List<MemoryTuple>> memLD5=database.memoryDao().getAllMemoriesLocation(
                        userID, text);
                memLD5.observe(this, new Observer<List<MemoryTuple>>() {
                    @Override
                    public void onChanged(List<MemoryTuple> memoriesList) {
                        setAdapter(memoriesList, memoriesListener);
                    }
                });
                break;
            case MEM_TAG:
                final LiveData<List<MemoryTuple>> memLD6=database.memoryDao().getAllMemoriesTag(
                        userID, text);
                memLD6.observe(this, new Observer<List<MemoryTuple>>() {
                    @Override
                    public void onChanged(List<MemoryTuple> memoriesList) {
                        setAdapter(memoriesList, memoriesListener);
                    }
                });
                break;
            case MEM_LNK:
                final LiveData<List<MemoryTuple>> memLD7=database.memoryDao().getAllMemoriesLink(
                        userID, text);
                memLD7.observe(this, new Observer<List<MemoryTuple>>() {
                    @Override
                    public void onChanged(List<MemoryTuple> memoriesList) {
                        setAdapter(memoriesList, memoriesListener);
                    }
                });
        }
    }

    private void setAdapter(List<MemoryTuple> memoriesList, MemoriesAdapter.MemoriesListener
                            memoriesListener)   {
        if(memoriesList.size()==0)  {
            switch(listType)    {
                case BIN:
                    ((BinActivity)Objects.requireNonNull(getActivity())).changeToEmptyFragment();
                    break;
                default: case MEM_ALL: case MEM_LKD: case MEM_UNLKD:
                    ((ViewAllActivity)Objects.requireNonNull(getActivity())).changeToEmptyFragment();
                    break;
            }
        }
        else    {
            adapter=new MemoriesAdapter(memoriesList, memoriesListener, userID);
            adapter.notifyDataSetChanged();
            listContainer.setAdapter(adapter);
        }
    }

    @Override
    public void onMemoryLongClick() {
        /*if(!areChecksAllVisible) {
            toggleAllChecksVisibility(true);
            areChecksAllVisible=true;
        }*/
        longClickAppBar.setVisibility(View.VISIBLE);
        appBar.setVisibility(View.GONE);
    }

    @Override
    public void onAllCheckboxesChecked(int selectedCount) {
        noOfChecked.setText(String.format(Locale.ENGLISH, "%d", selectedCount));
        if(selectedCount==adapter.getItemCount())  {
            explicitlyClicked=false;
            selectAll.setChecked(true);
            explicitlyClicked=true;
        }
        else if(selectAll.isChecked()) {
            explicitlyClicked=false;
            selectAll.setChecked(false);
            explicitlyClicked=true;
        }
    }

    public void onBackPressed() {
        longClickAppBar.setVisibility(View.GONE);
        appBar.setVisibility(View.VISIBLE);
        adapter.removeAllChecksVisible();
    }

    public void addToRecycleBin()   {
        // Dialog msg.
        final BinEntity[] bins=new BinEntity[adapter.getSelectedMemIDs().size()];
        for(int i=0; i<bins.length; i++)
            bins[i]=new BinEntity(adapter.getSelectedMemIDs().get(i));
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.binDao().insertMany(bins);
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            onBackPressed();
                        }
                        catch(Exception e)  {
                            getActivity().finish();
                            /*Intent i=new Intent(getContext(), ViewAllActivity.class);
                            i.putExtra("userID", userID);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);*/
                        }
                        Toast.makeText(getContext(), "Memories added to bin", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        });
    }

    public void deleteMemoriesFromBin(final boolean deleteAll)    {
        SimpleMessageDialog simpleMessageDialog=new SimpleMessageDialog(getActivity(),
                R.string.delete_from_bin_message);
        simpleMessageDialog.setResult(new SimpleMessageDialog.Result() {
            @Override
            public void getResult() {
                if(deleteAll)
                    adapter.setAllChecksChecked(true);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.memoryDao().deleteSelectedMemories(adapter.getSelectedMemIDs());
                        database.binDao().deleteSelectedMemIDs(adapter.getSelectedMemIDs());
                        database.tagDao().deleteSelectedMemIDs(adapter.getSelectedMemIDs());
                        database.linkDao().deleteSelectedMemIDs(adapter.getSelectedMemIDs());
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                                Toast.makeText(getContext(), "Memories deleted", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }
                });
            }
        });
        simpleMessageDialog.show();
    }

    public void restoreMemoriesFromBin(final boolean restoreAll)  {
        SimpleMessageDialog simpleMessageDialog=new SimpleMessageDialog(getActivity(),
                R.string.restore_memories_message);
        simpleMessageDialog.setResult(new SimpleMessageDialog.Result() {
            @Override
            public void getResult() {
                if(restoreAll)
                    adapter.setAllChecksChecked(true);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        database.binDao().deleteSelectedMemIDs(adapter.getSelectedMemIDs());
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                                Toast.makeText(getContext(), "Memories restored",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        simpleMessageDialog.show();
    }
}
