package com.github.h01d.teamup.activities;
import com.github.h01d.teamup.R;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CreateGameDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    Button dateTimeButton;
    private Spinner spinnerFor;

    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    boolean mustCheckTime = false; //have to check for past time

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game_details);

        dateTimeButton = findViewById(R.id.dateTimeButton);

        /* ADD ITEMS ON SPINNERS*/
        addItemsOnSpinners();

        /* NAVIGATE TO DATE/TIME FRAGMENT*/
        dateTimeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateGameDetails.this, CreateGameDetails.this,
                        year, month, day);

                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
    {
        yearFinal = year;
        monthFinal = month + 1;
        dayFinal = dayOfMonth;

        /* CHECKS IF DAY IS TODAY */
        Calendar today = Calendar.getInstance();
        if (yearFinal == today.get(Calendar.YEAR) && monthFinal == today.get(Calendar.MONTH) + 1 && dayFinal == today.get(Calendar.DAY_OF_MONTH))
        {
            Toast.makeText(getBaseContext(), "day is today",Toast.LENGTH_LONG).show();
            mustCheckTime = true;
        }

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);



        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateGameDetails.this, CreateGameDetails.this,
                hour, minute, DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        hourFinal = hourOfDay;
        minuteFinal = minute;

        Calendar today = Calendar.getInstance();
        Calendar chosenTime = Calendar.getInstance();
        chosenTime.set(Calendar.HOUR_OF_DAY, hourFinal);
        chosenTime.set(Calendar.MINUTE, minuteFinal);

        /*CHECKS FOR PAST TIME*/
        if (mustCheckTime && chosenTime.getTimeInMillis() <= today.getTimeInMillis())
        {
            Toast.makeText(getBaseContext(), "INSERT A FUTURE TIME!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getBaseContext(), "Year: " + yearFinal + "\n" +
                    "Month: " + monthFinal + "\n" +
                    "Day: " + dayFinal + "\n" +
                    "Hour: " + hourFinal + "\n" +
                    "Minute: " + minuteFinal + "\n", Toast.LENGTH_LONG).show();

            dateTimeButton.setText(dayFinal + "/" + monthFinal + "/" + yearFinal + " στις " + hourFinal + ":" + minuteFinal);
            dateTimeButton.setTypeface(dateTimeButton.getTypeface(), Typeface.BOLD_ITALIC);
        }
    }

    public void addItemsOnSpinners() {
        spinnerFor = findViewById(R.id.d_opponents_teammates_spinner);

        List<String> list = new ArrayList<>();
        list.add("Αντίπαλους&Συμπαίκτες");
        list.add("Αντίπαλους");
        list.add("Συμπαίκτες");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFor.setAdapter(dataAdapter);
    }
}
