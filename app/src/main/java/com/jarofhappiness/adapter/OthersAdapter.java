package com.jarofhappiness.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jarofhappiness.MemoryListActivity;
import com.jarofhappiness.R;
import com.jarofhappiness.database.OthersTuple;
import com.jarofhappiness.fragment.MemoriesListFragment;

import java.util.List;
import java.util.Locale;

public class OthersAdapter extends RecyclerView.Adapter<OthersAdapter.OthersListViewHolder> {
    public static final int WHEN=0, LOCATION=1, TAG=2, LINK=3;
    private List<OthersTuple> others;
    private int type, userID;


    public OthersAdapter(List<OthersTuple> others, int type, int userID) {
        this.others=others;
        this.type=type;
        this.userID=userID;
    }

    class OthersListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private TextView othersView, memCountView;
        public OthersListViewHolder(View parent) {
            super(parent);

            othersView=parent.findViewById(R.id.others_view);
            memCountView=parent.findViewById(R.id.mem_count_view);
            parent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int memType;
            switch(type)    {
                default: case WHEN:
                    memType=MemoriesListFragment.MEM_WHN;
                    break;
                case LOCATION:
                    memType=MemoriesListFragment.MEM_LOC;
                    break;
                case TAG:
                    memType=MemoriesListFragment.MEM_TAG;
                    break;
                case LINK:
                    memType=MemoriesListFragment.MEM_LNK;
                    break;
            }

            Intent i=new Intent(view.getContext(), MemoryListActivity.class);
            i.putExtra("userID", userID);
            i.putExtra("type", memType);
            i.putExtra("text", others.get(getAdapterPosition()).text);
            view.getContext().startActivity(i);
        }
    }

    @NonNull
    @Override
    public OthersListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OthersListViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_others_tuple, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OthersListViewHolder holder, int position) {
        OthersTuple other=others.get(position);
        Context context=holder.othersView.getContext();

        switch(type)    {
            default: case WHEN:
                holder.othersView.setTextColor(ContextCompat.getColor(context, R.color.blue_violet));
                break;
            case LOCATION:
                holder.othersView.setTextColor(ContextCompat.getColor(context,
                        R.color.light_sea_green));
                break;
            case TAG:
                holder.othersView.setTextColor(ContextCompat.getColor(context, R.color.deep_pink));
                break;
            case LINK:
                holder.othersView.setTextColor(ContextCompat.getColor(context, R.color.dodger_blue));
        }
        holder.othersView.setText(other.text);
        holder.memCountView.setText(String.format(Locale.ENGLISH, "%06d", other.count));
    }

    @Override
    public int getItemCount() {
        return others.size();
    }
}
