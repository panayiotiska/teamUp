package com.github.h01d.teamup.holders;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.h01d.teamup.R;
import com.github.h01d.teamup.activities.ProfileActivity;
import com.github.h01d.teamup.models.FieldRating;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FieldRatingViewHolder extends RecyclerView.ViewHolder
{
    private View view;
    private Context context;

    public FieldRatingViewHolder(@NonNull View itemView, Context context)
    {
        super(itemView);

        view = itemView;
        this.context = context;
    }

    public void setView(final FieldRating rating)
    {
        //ImageView pic = view.findViewById(R.id.l_rating_image);
        TextView name = view.findViewById(R.id.l_fieldrating_name);
        TextView date = view.findViewById(R.id.l_fieldrating_date);
        TextView comment = view.findViewById(R.id.l_fieldrating_comment);
        TextView overallRating = view.findViewById(R.id.l_fieldrating_overall_text);
        RatingBar ratingBar = view.findViewById(R.id.l_fieldrating_ratingbar);

        /*Picasso.with(context)
                .load("https://graph.facebook.com/" + dataHandler.getUser(data.get(position).getUserRate()).getId() + "/picture?type=large")
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(pic);*/

        name.setText(rating.getCreatedByFirstName() + " " + rating.getCreatedByLastName());
        date.setText(rating.getCreatedAt());
        comment.setText(rating.getComment().length() == 0 ? "Δεν υπάρχουν σχόλια" : rating.getComment());
        overallRating.setText("(" + rating.getRating() + ")");
        ratingBar.setRating(rating.getRating());

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
                Intent profile = new Intent(context, ProfileActivity.class);
                profile.putExtra("userID", rating.getCreatedById());
                profile.putExtra("name", rating.getCreatedByFirstName() + " " + rating.getCreatedByLastName());
                profile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK); //Start the same actiivty without affecting the current
                context.startActivity(profile);
            }
        });
    }
}
