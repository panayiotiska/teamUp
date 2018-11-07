package com.github.h01d.teamup_v2.holders;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.h01d.teamup_v2.R;
import com.github.h01d.teamup_v2.activities.ProfileActivity;
import com.github.h01d.teamup_v2.models.Rating;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RatingViewHolder extends  RecyclerView.ViewHolder
{
    private View view;
    private Context context;

    public RatingViewHolder(@NonNull View itemView, Context context)
    {
        super(itemView);

        view = itemView;
        this.context = context;
    }

    public void setView(final Rating rating)
    {
        //ImageView pic = view.findViewById(R.id.l_rating_image);
        TextView name = view.findViewById(R.id.l_rating_name);
        TextView date = view.findViewById(R.id.l_rating_date);
        TextView tipikotita = view.findViewById(R.id.l_rating_tupikotita_text);
        TextView ikanotita = view.findViewById(R.id.l_rating_ikanotita_text);
        TextView simperifora = view.findViewById(R.id.l_rating_sumperifora_text);
        TextView comment = view.findViewById(R.id.l_rating_comment);
        TextView overallRating = view.findViewById(R.id.l_rating_overall_text);
        RatingBar ratingBar = view.findViewById(R.id.l_rating_ratingbar);

        /*Picasso.with(context)
                .load("https://graph.facebook.com/" + dataHandler.getUser(data.get(position).getUserRate()).getId() + "/picture?type=large")
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(pic);*/

        name.setText(rating.getCreatedByFirstName() + " " + rating.getCreatedByLastName());
        date.setText(rating.getCreatedAt());
        tipikotita.setText("Τυπικότητα: " + rating.getOnTime());
        ikanotita.setText("Ικανότητα: " + rating.getSkills());
        simperifora.setText("Συμπεριφορά: " + rating.getBehavior());
        comment.setText(rating.getComment().length() == 0 ? "Δεν υπάρχουν σχόλια" : rating.getComment());
        float mo = (rating.getOnTime() + rating.getSkills() + rating.getBehavior()) / 3;
        overallRating.setText(String.format("(%.1f)", mo));
        ratingBar.setRating(mo);

        try
        {
            Date test = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(rating.getCreatedAt());

            date.setText(new SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(test));
        }
        catch(ParseException e)
        {
            Log.d("TEST", e.getMessage());
            e.printStackTrace();
        }

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu menu = new PopupMenu(context, v);
                menu.getMenu().add(Menu.NONE, 1, 1, "Προβολή Προφίλ");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem)
                    {
                        switch(menuItem.getItemId())
                        {
                            case 1:
                                Intent profile = new Intent(context, ProfileActivity.class);
                                profile.putExtra("userID", rating.getCreatedById());
                                profile.putExtra("name", rating.getCreatedByFirstName() + " " + rating.getCreatedByLastName());
                                profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK); //Start the same actiivty without affecting the current
                                context.startActivity(profile);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                menu.show();
            }
        });
    }
}
