package com.github.h01d.teamup.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.h01d.teamup.R;
import com.github.h01d.teamup.holders.FieldRatingViewHolder;
import com.github.h01d.teamup.models.FieldRating;

import java.util.ArrayList;

public class FieldRatingsAdapter extends RecyclerView.Adapter<FieldRatingViewHolder>
{
    private ArrayList<FieldRating> data;
    private Context context;

    public FieldRatingsAdapter(ArrayList<FieldRating> data, Context context)
    {
        this.data = data;
        this.context = context;
    }

    @Override
    public FieldRatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.field_rating, parent, false);

        return new FieldRatingViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull FieldRatingViewHolder holder, int position)
    {
        holder.setView(data.get(position));
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }
}