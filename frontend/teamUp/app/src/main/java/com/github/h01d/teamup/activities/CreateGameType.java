package com.github.h01d.teamup.activities;
import com.github.h01d.teamup.R;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CreateGameType extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game_type);

        final View basketballLayout = findViewById(R.id.basketball_layout);
        final View footballLayout = findViewById(R.id.football_layout);

        basketballLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(CreateGameType.this, CreateGameField.class);
                i.putExtra("GameType", "1"); //value 1 for basketball game
                startActivity(i);
            }
        });

        footballLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(CreateGameType.this, CreateGameField.class);
                i.putExtra("GameType", "0"); //value 0 for football game
                startActivity(i);
            }
        });
    }
}
