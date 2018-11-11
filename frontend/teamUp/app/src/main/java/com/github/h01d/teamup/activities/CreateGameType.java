package com.github.h01d.teamup.activities;

import com.github.h01d.teamup.R;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CreateGameType extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game_type);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Επιλογή παιχνιδιού");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CardView football = findViewById(R.id.a_create_type_football);
        CardView basket = findViewById(R.id.a_create_type_basketball);

        football.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(CreateGameType.this, CreateGameField.class);
                i.putExtra("type", 0); //value 0 for football game
                startActivity(i);
            }
        });

        basket.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(CreateGameType.this, CreateGameField.class);
                i.putExtra("type", 1);
                startActivity(i);
            }
        });
    }
}
