package com.github.h01d.teamup.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import at.markushi.ui.CircleButton;

import com.github.h01d.teamup.R;

public class CreateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
{
    public static LatLng latLong = null;
    public static String addedAddress = null;

    Button dateTimeButton;
    private Spinner spinnerType, spinnerSize, spinnerFor,spinnerPlayground;
    ArrayAdapter<String> dataAdapterPlayground;
    int itemAddedTwice = 0; //

    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    boolean mustCheckTime = false; //have to check for past time

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

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

                DatePickerDialog datePickerDialog = new DatePickerDialog(CreateActivity.this, CreateActivity.this,
                        year, month, day);

                datePickerDialog.show();

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if (addedAddress != null)
        {
            itemAddedTwice++;
            if (itemAddedTwice == 2)
            {
                dataAdapterPlayground.remove(spinnerPlayground.getItemAtPosition(3).toString());
                itemAddedTwice--;
            }
            dataAdapterPlayground.add(addedAddress);
            dataAdapterPlayground.notifyDataSetChanged();
            spinnerPlayground.setSelection(3);
        }

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



        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateActivity.this, CreateActivity.this,
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

        spinnerType = findViewById(R.id.d_create_type_spinner);
        spinnerSize = findViewById(R.id.d_create_size_spinner);
        spinnerFor = findViewById(R.id.d_opponents_teammates_spinner);
        spinnerPlayground = findViewById(R.id.d_playground_spinner);

        spinnerPlayground.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onClick(View v) {
            }

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int position, long row_id) {
                final Intent intent;

                if(row_id == 0)
                {
                    intent = new Intent(CreateActivity.this, MapActivity.class);
                    startActivity(intent);
                }

                /*
                switch(row_id)
                {
                    case 0:
                        intent = new Intent(CreateActivity.this, MapActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(CurrentActivity.this, TargetActivity2.class);
                        break;
                }
                */
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        List<String> list = new ArrayList<>();
        list.add("Ποδόσφαιρο");
        list.add("Μπάσκετ");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(dataAdapter);

        List<String> list2 = new ArrayList<>();
        list2.add("5vs5");
        list2.add("11vs11");

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list2);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSize.setAdapter(dataAdapter2);

        List<String> list3 = new ArrayList<>();
        list3.add("Αντίπαλους&Συμπαίκτες");
        list3.add("Αντίπαλους");
        list3.add("Συμπαίκτες");

        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list3);
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFor.setAdapter(dataAdapter3);

        List<String> list4 = new ArrayList<>();
        list4.add("ΠΡΟΣΘΗΚΗ ΔΙΕΥΘΥΝΣΗΣ");
        list4.add("ΧΑΝΘ (Ν. Γερμανού 1)");
        list4.add("Top Fitness All Star (Αγίου Δημητρίου 159Α");

        dataAdapterPlayground = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list4);
        dataAdapterPlayground.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayground.setAdapter(dataAdapterPlayground);
    }
}
