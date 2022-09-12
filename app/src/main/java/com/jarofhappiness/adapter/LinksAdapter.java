package com.jarofhappiness.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jarofhappiness.R;

import java.util.List;

public class LinksAdapter extends RecyclerView.Adapter<LinksAdapter.LinksViewHolder>    {
    public static final int EDIT=0, VIEW=1, LINKS_LIMIT=10;
    private List<String> links;
    private int linkType;
    private LinksListener linksListener;

    public LinksAdapter(List<String> links, LinksListener linksListener, int linkType) {
        this.links=links;
        this.linkType=linkType;
        this.linksListener=linksListener;
    }

    public LinksAdapter(List<String> links, int lintType)   {
        this.links=links;
        this.linkType=lintType;
    }

    class LinksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener   {
        private TextView linkView;
        public LinksViewHolder(View parent)   {
            super(parent);

            linkView=parent.findViewById(R.id.link_view);
            if(linkType==EDIT)
                parent.findViewById(R.id.close_button).setOnClickListener(this);
        }

        public void onClick(View view)  {
            links.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());

            linksListener.showNewLinkView();
        }
    }

    public interface LinksListener  {
        void showNewLinkView();
    }

    public void addLink(String link)    {
        links.add(link);
        notifyItemInserted(links.size()-1);
    }


    @NonNull
    @Override
    public LinksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinksViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                linkType==EDIT?R.layout.item_link:R.layout.item_readonly_link,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinksViewHolder holder, int position) {
        holder.linkView.setText(Html.fromHtml("<u>"+links.get(position)+"</u>"));
    }

    @Override
    public int getItemCount() {
        return links.size();
    }
}
