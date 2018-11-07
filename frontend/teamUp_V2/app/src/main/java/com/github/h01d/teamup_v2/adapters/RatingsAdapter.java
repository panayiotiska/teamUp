package com.github.h01d.teamup_v2.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.h01d.teamup_v2.R;
import com.github.h01d.teamup_v2.holders.RatingViewHolder;
import com.github.h01d.teamup_v2.models.Rating;

import java.util.ArrayList;

public class RatingsAdapter extends RecyclerView.Adapter<RatingViewHolder>
{
    private ArrayList<Rating> data;
    private Context context;

    public RatingsAdapter(ArrayList<Rating> data, Context context)
    {
        this.data = data;
        this.context = context;
    }

    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.players_rating, parent, false);

        return new RatingViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position)
    {
        holder.setView(data.get(position));
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }
}
