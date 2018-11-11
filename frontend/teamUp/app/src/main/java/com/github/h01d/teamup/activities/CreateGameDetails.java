package com.github.h01d.teamup.activities;

import com.github.h01d.teamup.R;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateGameDetails extends AppCompatActivity
{
    private EditText gameType, gameField, gameSize, gamePhone, gameDate, gameComments;

    private int id, type;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game_details);

        id = getIntent().getIntExtra("id", -1);
        type = getIntent().getIntExtra("id", -1);
        name = getIntent().getStringExtra("name");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Στοιχεία παιχνιδιού");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gameType = findViewById(R.id.a_create_details_type);
        gameField = findViewById(R.id.a_create_details_field);
        gameSize = findViewById(R.id.a_create_details_size);
        gamePhone = findViewById(R.id.a_create_details_phone);
        gameComments = findViewById(R.id.a_create_details_comments);

        gameType.setText(type == 0 ? "Ποδόσφαιρο" : "Καλαθόσφαιρα");
        gameField.setText(name);

        gameDate = findViewById(R.id.a_create_details_date);
        gameDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar c = Calendar.getInstance();

                final int todayYear = c.get(Calendar.YEAR);
                final int todayMonth = c.get(Calendar.MONTH);
                final int todayDay = c.get(Calendar.DAY_OF_MONTH);
                final int todayHour = c.get(Calendar.HOUR_OF_DAY);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateGameDetails.this, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, final int year, final int month, final int dayOfMonth)
                    {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateGameDetails.this, new TimePickerDialog.OnTimeSetListener()
                        {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                            {
                                Calendar today = Calendar.getInstance();

                                Calendar chosenTime = Calendar.getInstance();
                                chosenTime.set(year, month, dayOfMonth, hourOfDay, minute);

                                if(chosenTime.getTimeInMillis() <= today.getTimeInMillis())
                                {
                                    Toast.makeText(getBaseContext(), "INSERT A FUTURE TIME!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    String timestamp = year + "-" + (month + 1) + "-" + dayOfMonth + "T" + hourOfDay + ":" + minute + ":00.000Z"; //2018-11-18T18:00:00.000Z

                                    try
                                    {
                                        Date test = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(timestamp);

                                        gameDate.setText(new SimpleDateFormat("d MMMM yyyy HH:mm", Locale.getDefault()).format(test));
                                    }
                                    catch(ParseException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, todayHour, 0, true);

                        timePickerDialog.show();
                    }
                }, todayYear, todayMonth, todayDay);

                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
        });
    }
}
