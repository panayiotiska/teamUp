package com.github.h01d.teamup.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.h01d.teamup.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder
{
    private View view;

    public HeaderViewHolder(@NonNull View itemView)
    {
        super(itemView);

        view = itemView;
    }

    public void setView(String title)
    {
        TextView test = view.findViewById(R.id.header_title);

        test.setText(title);
    }
}
