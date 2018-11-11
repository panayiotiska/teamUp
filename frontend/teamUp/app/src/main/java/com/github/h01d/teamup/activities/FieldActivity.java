package com.github.h01d.teamup.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.h01d.teamup.R;
import com.github.h01d.teamup.adapters.FieldRatingsAdapter;
import com.github.h01d.teamup.models.Field;
import com.github.h01d.teamup.models.FieldRating;
import com.github.h01d.teamup.network.NetworkManager;
import com.github.h01d.teamup.network.NetworkManagerListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FieldActivity extends AppCompatActivity
{
    private final String TAG = "FieldActivity";

    private TextView address, workingDays, workingHours;
    private TextView overallRating, totalRating, ratingText;
    private RatingBar overallRatingBar;
    private RecyclerView recyclerView;
    private ImageView fieldImage;

    private int fieldId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);

        Field field = (Field) getIntent().getExtras().getSerializable("field");

        fieldId = field.getFieldId();

        // Setting up ActionBar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(field.getName());

        // Initialize views

        address = findViewById(R.id.a_field_address);
        workingDays = findViewById(R.id.a_field_days);
        workingHours = findViewById(R.id.a_field_hours);
        overallRating = findViewById(R.id.a_field_overall);
        totalRating = findViewById(R.id.a_field_total);
        overallRatingBar = findViewById(R.id.a_field_overallbar);
        ratingText = findViewById(R.id.a_field_rating_norating);
        fieldImage = findViewById(R.id.a_field_image);

        recyclerView = findViewById(R.id.a_field_rating_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        loadData();
    }

    private void loadData()
    {
        //shimmerFrameLayout.startShimmer();

        NetworkManager.getInstance(getApplicationContext()).getData("http://104.223.87.94:3000/api/v1/games/"/* + fieldId + "/"*/, new NetworkManagerListener()
        {
            @Override
            public void getResult(JSONArray result1)
            {
                try
                {
                    // Extract data from result

                    JSONArray result = new JSONArray("[\n" +
                            "\t{\n" +
                            "\t\t\"id\":1,\n" +
                            "\t\t\"name\":\"Toumba Stadium\",\n" +
                            "\t\t\"type\":0,\n" +
                            "\t\t\"imageUrl\":\"https://upload.wikimedia.org/wikipedia/commons/thumb/d/de/Thessalonioki%2C_Stadium_of_PAOK_-_panoramio.jpg/1280px-Thessalonioki%2C_Stadium_of_PAOK_-_panoramio.jpg\",\n" +
                            "\t\t\"sponsored\":true,\n" +
                            "\t\t\"contactPhone\":\"6969696969\",\n" +
                            "\t\t\"location\":\n" +
                            "\t\t{\n" +
                            "\t\t\t\"city\":\"Thessaloniki\",\n" +
                            "\t\t\t\"address\":\"Mikras Asias 6\",\n" +
                            "\t\t\t\"countryCode\":\"GR\",\n" +
                            "\t\t\t\"postalCode\":\"544 54\",\n" +
                            "\t\t\t\"latitude\":40.613858,\n" +
                            "\t\t\t\"longitude\":22.9722408\n" +
                            "\t\t},\n" +
                            "\t\t\"rating\":\n" +
                            "\t\t{\n" +
                            "\t\t\t\"averageRating\":3.2,\n" +
                            "\t\t\t\"ratingsCount\":4\n" +
                            "\t\t}\n" +
                            "\t}\n" +
                            "]");

                    JSONObject tmp = result.getJSONObject(0);

                    int id = tmp.getInt("id");
                    String name = tmp.getString("name");
                    int type = tmp.getInt("type");
                    String image = tmp.getString("imageUrl");
                    boolean sponsored = tmp.getBoolean("sponsored");
                    String phone = tmp.getString("contactPhone");
                    String city = tmp.getJSONObject("location").getString("city");
                    String address = tmp.getJSONObject("location").getString("address");
                    String countryCode = tmp.getJSONObject("location").getString("countryCode");
                    String postalCode = tmp.getJSONObject("location").getString("postalCode");
                    Double locLat = tmp.getJSONObject("location").getDouble("latitude");
                    Double locLong = tmp.getJSONObject("location").getDouble("longitude");
                    float averageRating = Float.parseFloat(tmp.getJSONObject("rating").getString("averageRating"));
                    int ratingsCount = tmp.getJSONObject("rating").getInt("ratingsCount");

                    // Update UI based on data

                    updateUI(new Field(id, name, type, image, sponsored, phone, city, address, countryCode, postalCode, locLat, locLong, averageRating, ratingsCount));

                    // Load User ratings

                    loadRatings();
                }
                catch(JSONException e)
                {
                    Log.d(TAG, e.getMessage());

                    //shimmerFrameLayout.stopShimmer();
                    //shimmerFrameLayout.setVisibility(View.GONE);

                    //TODO: kill the process, show error message something went wrong

                    e.printStackTrace();
                }
            }

            @Override
            public void getError(String error, int code)
            {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

                //shimmerFrameLayout.stopShimmer();
                //shimmerFrameLayout.setVisibility(View.GONE);

                //TODO: kill the process, show error message something went wrong
            }
        });
    }

    private void loadRatings()
    {
        NetworkManager.getInstance(getApplicationContext()).getData("http://104.223.87.94:3000/api/v1/games/" /*+ fieldId + "/ratings/"*/, new NetworkManagerListener()
        {
            @Override
            public void getResult(JSONArray result1)
            {
                try
                {
                    // Extract data from result

                    JSONArray result = new JSONArray("[\n" +
                            "\t{\n" +
                            "\t\t\"id\":1,\n" +
                            "\t\t\"createdBy\":\n" +
                            "\t\t{\n" +
                            "\t\t\t\"id\":\"100000273909010\",\n" +
                            "\t\t\t\"firstName\":\"Rafaellos\",\n" +
                            "\t\t\t\"lastName\":\"Monoyios\"\n" +
                            "\t\t},\n" +
                            "\t\t\"createdAt\":\"2018-11-20T21:00:00.000Z\",\n" +
                            "\t\t\"comment\":\"\",\n" +
                            "\t\t\"rating\":3.1\n" +
                            "\t},\n" +
                            "\t{\n" +
                            "\t\t\"id\":2,\n" +
                            "\t\t\"createdBy\":\n" +
                            "\t\t{\n" +
                            "\t\t\t\"id\":\"100000273909010\",\n" +
                            "\t\t\t\"firstName\":\"Rafaellos\",\n" +
                            "\t\t\t\"lastName\":\"Monoyios\"\n" +
                            "\t\t},\n" +
                            "\t\t\"createdAt\":\"2018-11-20T21:00:00.000Z\",\n" +
                            "\t\t\"comment\":\"lol\",\n" +
                            "\t\t\"rating\":4.2\n" +
                            "\t}\n" +
                            "]");

                    ArrayList<FieldRating> ratings = new ArrayList<>();

                    for(int i = 0; i < result.length(); i++)
                    {
                        JSONObject tmp = result.getJSONObject(i);

                        int ratingId = tmp.getInt("id");
                        String createdById = tmp.getJSONObject("createdBy").getString("id");
                        String createdFirstName = tmp.getJSONObject("createdBy").getString("firstName");
                        String createdLastName = tmp.getJSONObject("createdBy").getString("lastName");
                        String createAt = tmp.getString("createdAt");
                        String comment = tmp.getString("comment");
                        float rating = Float.parseFloat(tmp.getString("rating"));

                        ratings.add(new FieldRating(ratingId, createdById, createdFirstName, createdLastName, createAt, comment, rating));
                    }

                    // Update UI based on data

                    updateUI(ratings);

                }
                catch(JSONException e)
                {
                    Log.d(TAG, e.getMessage());

                    //shimmerFrameLayout.stopShimmer();
                    //shimmerFrameLayout.setVisibility(View.GONE);

                    //TODO: show error message something went wrong

                    e.printStackTrace();
                }
            }

            @Override
            public void getError(String error, int code)
            {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

                //shimmerFrameLayout.stopShimmer();
                //shimmerFrameLayout.setVisibility(View.GONE);

                //TODO: show error message something went wrong
            }
        });
    }

    private void updateUI(Field field)
    {
        address.setText(field.getAddress());
        workingDays.setText("Monday - Sunday");
        workingHours.setText("08:00 - 22:00");
        overallRating.setText("" + field.getAverageRating());
        totalRating.setText("(" + field.getTotalRatings() + ")");
        overallRatingBar.setRating(field.getAverageRating());

        if(!field.getImage().isEmpty())
        {
            Picasso.with(getApplicationContext())
                    .load(field.getImage())
                    .placeholder(R.drawable.background_solid)
                    .error(R.drawable.background_solid)
                    .into(fieldImage);
        }
    }

    private void updateUI(ArrayList<FieldRating> ratings)
    {
        // Update rating with their values

        if(ratings.size() == 0)
        {
            ratingText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else
        {
            ratingText.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            recyclerView.setAdapter(new FieldRatingsAdapter(ratings, getApplicationContext()));
        }

        //shimmerFrameLayout.stopShimmer();
        //shimmerFrameLayout.setVisibility(View.GONE);
    }
}
