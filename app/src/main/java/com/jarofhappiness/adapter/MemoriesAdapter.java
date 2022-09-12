package com.jarofhappiness.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jarofhappiness.R;
import com.jarofhappiness.ViewActivity;
import com.jarofhappiness.database.MemoryEntity;
import com.jarofhappiness.database.MemoryTuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MemoriesAdapter extends RecyclerView.Adapter<MemoriesAdapter.MemoriesViewHolder> {
    private List<MemoryTuple> memories;
    private ArrayList<Integer> selectedMemIDs;
    private MemoriesListener memoriesListener;
    private int userID;
    private boolean areAllChecksVisible;

    public MemoriesAdapter(List<MemoryTuple> memories, MemoriesListener memoriesListener,
                           int userID)   {
        this.memories=memories;
        this.memoriesListener=memoriesListener;
        this.userID=userID;
        selectedMemIDs=new ArrayList<>();
    }

    class MemoriesViewHolder extends RecyclerView.ViewHolder implements CompoundButton.
            OnCheckedChangeListener, View.OnLongClickListener, View.OnClickListener {
        private CheckBox checkBox;
        private TextView titleView, memIDView, memoryView;
        private ImageView lockView;

        MemoriesViewHolder(View parent)    {
            super(parent);

            checkBox=parent.findViewById(R.id.checkbox);
            titleView=parent.findViewById(R.id.title_view);
            memIDView=parent.findViewById(R.id.mem_count_view);
            memoryView=parent.findViewById(R.id.memory_view);
            lockView=parent.findViewById(R.id.lock_view);

            parent.setOnLongClickListener(this);
            parent.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            MemoryTuple memory=memories.get(getAdapterPosition());
            if(memory.isChecked!=isChecked) {
                memory.isChecked=isChecked;
                if(isChecked)
                    selectedMemIDs.add(memory.memID);
                else
                    selectedMemIDs.remove((Integer)(memory.memID));
            }
            memoriesListener.onAllCheckboxesChecked(selectedMemIDs.size());
        }

        @Override
        public boolean onLongClick(View view) {
            if(!areAllChecksVisible)    {
                areAllChecksVisible=true;
                notifyDataSetChanged();
                memoriesListener.onMemoryLongClick();
                memoriesListener.onAllCheckboxesChecked(selectedMemIDs.size());
            }
            return true;
        }

        @Override
        public void onClick(View view) {
            MemoryTuple memory=memories.get(getAdapterPosition());

            Intent i=new Intent(view.getContext(), ViewActivity.class);
            i.putExtra("userID", userID);
            i.putExtra("memID", memory.memID);
            i.putExtra("randomize", false);
            i.putExtra("toast", "");
            view.getContext().startActivity(i);
        }
    }

    public interface MemoriesListener  {
        void onMemoryLongClick();
        void onAllCheckboxesChecked(int selectedCount);
    }

    public void removeAllChecksVisible()    {
        areAllChecksVisible=false;
        setAllChecksChecked(false);
    }

    public void setAllChecksChecked(boolean isChecked)    {
        for(MemoryTuple memory : memories)
            memory.isChecked=isChecked;
        if(isChecked)
            fillSelectedMemIDs();
        else
            emptySelectedMemIDs();
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getSelectedMemIDs() {
        return selectedMemIDs;
    }

    public void emptySelectedMemIDs()    {
        selectedMemIDs.clear();
    }

    public void fillSelectedMemIDs()    {
        emptySelectedMemIDs();
        for(MemoryTuple memory : memories)
            selectedMemIDs.add(memory.memID);
    }

    @NonNull
    @Override
    public MemoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemoriesViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_memory_tuple, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemoriesViewHolder holder, int position) {
        MemoryTuple memory=memories.get(position);

        holder.memIDView.setText(String.format(Locale.ENGLISH, "%06d", memory.memID));

        if(memory.title!=null && !memory.title.equals(""))
            holder.titleView.setText(memory.title);
        else
            holder.titleView.setText(R.string.no_title);


        if(memory.isLocked!=MemoryEntity.YES) {
            holder.memoryView.setText(memory.memory);
            holder.memoryView.setVisibility(View.VISIBLE);
            holder.lockView.setVisibility(View.GONE);
        }
        else {
            holder.memoryView.setVisibility(View.GONE);
            holder.lockView.setVisibility(View.VISIBLE);
        }

        holder.checkBox.setVisibility(areAllChecksVisible?View.VISIBLE:View.GONE);

        holder.checkBox.setChecked(memory.isChecked);
    }

    @Override
    public int getItemCount() {
        return memories.size();
    }
}
