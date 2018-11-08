package com.github.h01d.teamup.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.h01d.teamup.R;
import com.github.h01d.teamup.holders.PlayerViewHolder;
import com.github.h01d.teamup.models.User;

import java.util.ArrayList;

public class TeamsAdapter extends RecyclerView.Adapter<PlayerViewHolder>
{
    private ArrayList<User> data;

    private Context context;

    public TeamsAdapter(ArrayList<User> data, Context context)
    {
        this.data = data;
        this.context = context;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.player_team, parent, false);

        return new PlayerViewHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position)
    {
        holder.setView(data.get(position));
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }
}
