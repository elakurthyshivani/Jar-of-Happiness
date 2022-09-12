package com.jarofhappiness.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jarofhappiness.R;
import com.jarofhappiness.ViewAllActivity;
import com.jarofhappiness.adapter.OthersAdapter;
import com.jarofhappiness.database.JOHDatabase;
import com.jarofhappiness.database.OthersTuple;

import java.util.List;
import java.util.Objects;

public class OthersListFragment extends Fragment {
    private int userID, listType;
    private RecyclerView listContainer;
    private JOHDatabase database;
    private OthersAdapter adapter;

    public OthersListFragment(int userID, int listType) {
        super();

        this.userID=userID;
        this.listType=listType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final RecyclerView view=(RecyclerView)inflater.inflate(R.layout.fragment_list, container,
                false);


        database=JOHDatabase.getInstance(getContext());

        listContainer=view;
        listContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter();

        return view;
    }

    private void setAdapter()   {
        switch(listType)    {
            default: case OthersAdapter.WHEN:
                final LiveData<List<OthersTuple>> whenLD=database.memoryDao().getAllWhens(userID);
                whenLD.observe(this, new Observer<List<OthersTuple>>() {
                    @Override
                    public void onChanged(List<OthersTuple> others) {
                        setAdapter(others, OthersAdapter.WHEN);
                    }
                });
                break;
            case OthersAdapter.LOCATION:
                final LiveData<List<OthersTuple>> locationLD=database.memoryDao().getAllLocations(userID);
                locationLD.observe(this, new Observer<List<OthersTuple>>() {
                    @Override
                    public void onChanged(List<OthersTuple> others) {
                        setAdapter(others, OthersAdapter.LOCATION);
                    }
                });
                break;
            case OthersAdapter.TAG:
                final LiveData<List<OthersTuple>> tagLD=database.tagDao().getAllTags(userID);
                tagLD.observe(this, new Observer<List<OthersTuple>>() {
                    @Override
                    public void onChanged(List<OthersTuple> others) {
                        setAdapter(others, OthersAdapter.TAG);
                    }
                });
                break;
            case OthersAdapter.LINK:
                final LiveData<List<OthersTuple>> linkLD=database.linkDao().getAllLinks(userID);
                linkLD.observe(this, new Observer<List<OthersTuple>>() {
                    @Override
                    public void onChanged(List<OthersTuple> others) {
                        setAdapter(others, OthersAdapter.LINK);
                    }
                });
                break;
        }
    }

    private void setAdapter(List<OthersTuple> others, int type)   {
        if(others.size()==0)    {
            switch(type)    {
                default: case OthersAdapter.WHEN: case OthersAdapter.LINK:
                case OthersAdapter.LOCATION: case OthersAdapter.TAG:
                    ((ViewAllActivity)Objects.requireNonNull(getActivity())).changeToEmptyFragment();
                    break;
            }
        }
        else   {
            adapter=new OthersAdapter(others, type, userID);
            listContainer.setAdapter(adapter);
        }
    }
}
