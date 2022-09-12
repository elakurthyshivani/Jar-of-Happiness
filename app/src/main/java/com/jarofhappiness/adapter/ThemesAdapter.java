package com.jarofhappiness.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jarofhappiness.R;

import java.util.List;

public class ThemesAdapter extends RecyclerView.Adapter<ThemesAdapter.ThemesViewHolder> {
    private List<Integer> themes;
    private ThemesListener themesListener;
    private int curThemePosition;

    public ThemesAdapter(List<Integer> themes, ThemesListener themesListener, int curTheme)  {
        this.themes=themes;
        this.themesListener=themesListener;
        this.curThemePosition=themes.indexOf(curTheme);
    }

    class ThemesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView selected;
        private TextView themeView;

        public ThemesViewHolder(View parent)    {
            super(parent);

            themeView=parent.findViewById(R.id.theme_view);
            selected=parent.findViewById(R.id.selected);
            themeView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            themesListener.changeTheme(themes.get(getAdapterPosition()), getAdapterPosition(),
                    curThemePosition);
            curThemePosition=getAdapterPosition();
        }
    }

    public interface ThemesListener {
        void changeTheme(int themeID, int index, int prevCurThemePosition);
    }

    public int getCurThemePosition() {
        return curThemePosition;
    }

    @NonNull
    @Override
    public ThemesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ThemesViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_theme, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ThemesViewHolder holder, final int position) {
        holder.themeView.setBackgroundResource(themes.get(position));
        if(themes.get(position).equals(themes.get(curThemePosition)))
            holder.selected.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return themes.size();
    }
}
