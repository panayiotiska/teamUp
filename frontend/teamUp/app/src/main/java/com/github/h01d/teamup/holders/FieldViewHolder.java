package com.github.h01d.teamup.holders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.h01d.teamup.R;
import com.github.h01d.teamup.activities.FieldActivity;
import com.github.h01d.teamup.models.Field;

public class FieldViewHolder extends RecyclerView.ViewHolder
{
    private View view;
    private Context context;

    public FieldViewHolder(@NonNull View itemView, Context context)
    {
        super(itemView);

        view = itemView;
        this.context = context;
    }

    public void setView(final Field field)
    {
        ImageView image = view.findViewById(R.id.l_field_image);
        TextView name = view.findViewById(R.id.l_field_name);
        TextView type = view.findViewById(R.id.l_field_type);
        TextView address = view.findViewById(R.id.l_field_address);
        TextView average = view.findViewById(R.id.l_field_average);
        TextView total = view.findViewById(R.id.l_field_total);
        RatingBar ratingBar = view.findViewById(R.id.l_field_ratingbar);
        LinearLayout linearLayout = view.findViewById(R.id.l_field_layout);

        name.setText(field.getName());
        type.setText(field.getType() == 0 ? "Γήπεδο Ποδοσφαίρου" : "Γήπεδο Μπάσκετ");
        address.setText(field.getAddress() + ", " + field.getCity() + " " + field.getPostalCode());
        average.setText("" + field.getAverageRating());
        total.setText("(" + field.getTotalRatings() + " Αξιολογήσεις)");
        ratingBar.setRating(field.getAverageRating());

        linearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putSerializable("field", field);
                Intent intent = new Intent(context, FieldActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    public View getView()
    {
        return view;
    }
}
