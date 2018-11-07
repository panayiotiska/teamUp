package com.github.h01d.teamup_v2.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.h01d.teamup_v2.R;

public class CreateActivity extends AppCompatActivity
{
    private Spinner typeSpinner, sizeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Δημιουργία παιχνιδιού");

        typeSpinner = findViewById(R.id.a_create_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item);
        adapter.add("Ποδόσφαιρο");
        adapter.add("Μπάσκετ");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setSelection(0);

        sizeSpinner = findViewById(R.id.a_create_size);
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item);
        for(int i = 1; i <= 11; i++)
        {
            adapter.add(i + "x" + i);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(adapter);
        sizeSpinner.setSelection(0);
    }
}
