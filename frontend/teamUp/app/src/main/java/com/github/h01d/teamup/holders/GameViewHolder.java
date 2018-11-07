package com.github.h01d.teamup.holders;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.h01d.teamup.R;
import com.github.h01d.teamup.activities.GameActivity;
import com.github.h01d.teamup.models.Game;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GameViewHolder extends RecyclerView.ViewHolder
{
    private View view;
    private Context context;

    public GameViewHolder(@NonNull View itemView, Context context)
    {
        super(itemView);

        view = itemView;
        this.context = context;
    }

    public void setView(final Game game)
    {
        ImageView image = view.findViewById(R.id.l_game_image);
        TextView name = view.findViewById(R.id.l_game_name);
        TextView size = view.findViewById(R.id.l_game_size);
        TextView players = view.findViewById(R.id.l_game_players);
        TextView date = view.findViewById(R.id.l_game_date);
        TextView time = view.findViewById(R.id.l_game_time);
        TextView location = view.findViewById(R.id.l_game_location);

        image.setImageResource(game.getType() == 0 ? R.drawable.icon_soccer : R.drawable.icon_basket);
        name.setText(game.getName());
        size.setText(game.getSize() + "x" + game.getSize());
        players.setText("0/" + (game.getSize() * 2));
        location.setText(game.getLocationCity());

        try
        {
            Date test = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(game.getGameDate());

            date.setText(new SimpleDateFormat("d MMM", Locale.getDefault()).format(test));
            time.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(test));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent gameIntent = new Intent(context, GameActivity.class);
                gameIntent.putExtra("gameid", game.getGameID());
                gameIntent.putExtra("type", game.getType());
                context.startActivity(gameIntent);
            }
        });
    }

    public View getView()
    {
        return view;
    }
}
