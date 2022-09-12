package com.jarofhappiness.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jarofhappiness.R;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.TagsViewHolder> {
    public static final int EDIT=0, VIEW=1, TAGS_LIMIT=5;
    private List<String> tags;
    private int tagType;
    private TagsListener tagsListener;


    public TagsAdapter(List<String> tags, TagsListener tagsListener, int tagType)    {
        this.tags=tags;
        this.tagsListener=tagsListener;
        this.tagType=tagType;
    }

    public TagsAdapter(List<String> tags, int tagType)    {
        this.tags=tags;
        this.tagType=tagType;
    }

    class TagsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener    {
        private TextView tagView;

        public TagsViewHolder(View parent)  {
            super(parent);

            tagView=parent.findViewById(R.id.tag_view);
            if(tagType==EDIT)
                parent.findViewById(R.id.close_button).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            tags.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());

            tagsListener.showNewTagView();
        }
    }

    public interface TagsListener  {
        void showNewTagView();
    }

    public void addTag(String tag)    {
        tags.add(tag);
        notifyItemInserted(tags.size()-1);
    }


    @NonNull
    @Override
    public TagsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagsViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                tagType==EDIT?R.layout.item_tag:R.layout.item_readonly_tag,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagsViewHolder holder, int position) {
        holder.tagView.setText(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }
}
