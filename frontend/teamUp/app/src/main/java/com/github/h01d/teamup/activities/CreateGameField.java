package com.github.h01d.teamup.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.h01d.teamup.R;

public class CreateGameField extends AppCompatActivity
{

    FloatingActionButton customLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game_field);

        customLocation = findViewById(R.id.fb_custom_location);

        String selectedAddress = getIntent().getStringExtra("MyData"); //maybe put inside onResume
        //Toast.makeText(getBaseContext(), selectedAddress, Toast.LENGTH_LONG).show();

        customLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Intent intent;
                intent = new Intent(CreateGameField.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }
}
