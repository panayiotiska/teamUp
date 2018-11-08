package com.github.h01d.teamup.holders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.h01d.teamup.R;
import com.github.h01d.teamup.models.User;

public class PlayerViewHolder extends RecyclerView.ViewHolder
{
    private View view;
    private Context context;

    public PlayerViewHolder(@NonNull View itemView, Context context)
    {
        super(itemView);

        view = itemView;
        this.context = context;
    }

    public void setView(User user)
    {
        //ImageView pic = view.findViewById(R.id.l_team_image);
        TextView name = view.findViewById(R.id.l_team_name);

        name.setText(user.getFirstName() + " " + user.getLastName());
    }
}
