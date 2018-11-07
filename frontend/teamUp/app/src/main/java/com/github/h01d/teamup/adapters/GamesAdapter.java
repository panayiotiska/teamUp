package com.github.h01d.teamup.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.h01d.teamup.R;
import com.github.h01d.teamup.holders.HeaderViewHolder;
import com.github.h01d.teamup.holders.GameViewHolder;
import com.github.h01d.teamup.models.Game;
import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class GamesAdapter extends StatelessSection
{
    private ArrayList<Game> data;
    private String title;
    private Context context;

    public GamesAdapter(Context context, ArrayList<Game> data, String title)
    {
        super(SectionParameters.builder().itemResourceId(R.layout.game_layout).headerResourceId(R.layout.header_layout).build());

        this.context = context;
        this.data = data;
        this.title = title;
    }


    @Override
    public int getContentItemsTotal()
    {
        return data.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view)
    {
        return new GameViewHolder(view, context);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        GameViewHolder itemViewHolder = (GameViewHolder) holder;

        itemViewHolder.setView(data.get(position));
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view)
    {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder)
    {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

        headerViewHolder.setView(title);
    }
}
