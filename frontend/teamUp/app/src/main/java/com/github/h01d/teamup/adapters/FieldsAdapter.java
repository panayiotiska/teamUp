package com.github.h01d.teamup.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.h01d.teamup.R;
import com.github.h01d.teamup.holders.FieldViewHolder;
import com.github.h01d.teamup.holders.HeaderViewHolder;
import com.github.h01d.teamup.models.Field;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

public class FieldsAdapter extends StatelessSection
{
    private ArrayList<Field> data;
    private String title;
    private Context context;

    public FieldsAdapter(Context context, ArrayList<Field> data, String title)
    {
        super(SectionParameters.builder().itemResourceId(R.layout.field_layout).headerResourceId(R.layout.header_layout).build());

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
        return new FieldViewHolder(view, context);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        FieldViewHolder itemViewHolder = (FieldViewHolder) holder;

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
