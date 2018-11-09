package com.github.h01d.teamup.holders;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.h01d.teamup.R;
import com.github.h01d.teamup.activities.ProfileActivity;
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

    public void setView(final User user)
    {
        //ImageView pic = view.findViewById(R.id.l_team_image);
        TextView name = view.findViewById(R.id.l_team_name);

        name.setText(user.getFirstName() + " " + user.getLastName());

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent profile = new Intent(context, ProfileActivity.class);
                profile.putExtra("userID", user.getUserID());
                profile.putExtra("name", user.getFirstName() + " " + user.getLastName());
                profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK); //Start the same actiivty without affecting the current
                context.startActivity(profile);
            }
        });
    }
}
