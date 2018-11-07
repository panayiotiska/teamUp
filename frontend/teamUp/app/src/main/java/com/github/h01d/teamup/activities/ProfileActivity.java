package com.github.h01d.teamup.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.h01d.teamup.R;
import com.github.h01d.teamup.adapters.RatingsAdapter;
import com.github.h01d.teamup.models.Rating;
import com.github.h01d.teamup.models.User;
import com.github.h01d.teamup.network.NetworkManager;
import com.github.h01d.teamup.network.NetworkManagerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity
{
    private final String TAG = "ProfileActivity";

    // Current user ID

    private String userID;

    // Layout views

    private RecyclerView ratingsList;
    private RatingBar onTimeRating, skillRating, behaviorRating, overallRatingBar;
    private TextView overallText, totalText, profileSince, profileGames, ratingText;

    private ShimmerFrameLayout shimmerFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Setting up ActionBar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getExtras().getString("name"));

        // Initialize game ID

        userID = getIntent().getExtras().getString("userID");

        // Initialize views

        onTimeRating = findViewById(R.id.a_profile_tupikotita_ratingbar);
        skillRating = findViewById(R.id.a_profile_ikanotita_ratingbar);
        behaviorRating = findViewById(R.id.a_profile_sumperifora_ratingbar);
        overallRatingBar = findViewById(R.id.a_profile_overall_ratingbar);
        overallText = findViewById(R.id.a_profile_overall_text);
        totalText = findViewById(R.id.a_profile_total_text);
        profileSince = findViewById(R.id.a_profile_since);
        profileGames = findViewById(R.id.a_profile_games);
        ratingText = findViewById(R.id.a_profile_rating_norating);

        ratingsList = findViewById(R.id.a_profile_rating_recycler);
        ratingsList.setLayoutManager(new LinearLayoutManager(this));
        ratingsList.setHasFixedSize(true);
        ratingsList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        shimmerFrameLayout = findViewById(R.id.ghost_profile_shimmer);

        // Load data for a first time

        loadData();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        shimmerFrameLayout.startShimmer();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
    }

    /**
     * loadData will load profile data from the API and update the profile UI
     */

    private void loadData()
    {
        shimmerFrameLayout.startShimmer();

        NetworkManager.getInstance(getApplicationContext()).getData("http://104.223.87.94:3000/api/v1/users/" + userID + "/", new NetworkManagerListener()
        {
            @Override
            public void getResult(JSONArray result)
            {
                try
                {
                    // Extract data from result

                    JSONObject tmp = result.getJSONObject(0);

                    String userid = tmp.getString("id");
                    String firstName = tmp.getString("firstName");
                    String lastName = tmp.getString("lastName");
                    String createdAt = tmp.getString("createdAt");

                    // Update UI based on data

                    updateUI(new User(userid, firstName, lastName, createdAt));

                    // Load User ratings

                    loadRatings();
                }
                catch(JSONException e)
                {
                    Log.d(TAG, e.getMessage());

                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    //TODO: kill the process, show error message something went wrong

                    e.printStackTrace();
                }
            }

            @Override
            public void getError(String error, int code)
            {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                //TODO: kill the process, show error message something went wrong
            }
        });
    }

    /**
     * loadRatings will load ratings data of user and update the ratings UI
     */

    private void loadRatings()
    {
        NetworkManager.getInstance(getApplicationContext()).getData("http://104.223.87.94:3000/api/v1/users/" + userID + "/ratings/", new NetworkManagerListener()
        {
            @Override
            public void getResult(JSONArray result)
            {
                try
                {
                    // Extract data from result

                    ArrayList<Rating> ratings = new ArrayList<>();

                    for(int i = 0; i < result.length(); i++)
                    {
                        JSONObject tmp = result.getJSONObject(i);

                        int ratingId = tmp.getInt("id");
                        String createdById = tmp.getJSONObject("createdBy").getString("id");
                        String createdFirstName = tmp.getJSONObject("createdBy").getString("firstName");
                        String createdLastName = tmp.getJSONObject("createdBy").getString("lastName");
                        String createAt = tmp.getString("createdAt");
                        String comment = tmp.getString("comment");
                        int onTime = tmp.getInt("onTime");
                        int skills = tmp.getInt("skills");
                        int behavior = tmp.getInt("behavior");

                        ratings.add(new Rating(ratingId, createdById, createdFirstName, createdLastName, createAt, comment, onTime, skills, behavior));
                    }

                    // Update UI based on data

                    updateUI(ratings);

                }
                catch(JSONException e)
                {
                    Log.d(TAG, e.getMessage());

                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    //TODO: show error message something went wrong

                    e.printStackTrace();
                }
            }

            @Override
            public void getError(String error, int code)
            {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                //TODO: show error message something went wrong
            }
        });
    }

    /**
     * updateUI (USER) will update the profile part
     *
     * @param user data
     */

    private void updateUI(User user)
    {
        // Update profile data with their values

        try
        {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(user.getCreatedAt());

            profileSince.setText("Μέλος από\n" + new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(date));
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }

        profileGames.setText("Έπαιξε\n" + 0 + " παιχνίδια");
    }

    /**
     * updateUI (RATINGS) will update ratings part
     *
     * @param ratings list
     */

    private void updateUI(ArrayList<Rating> ratings)
    {
        // Update rating with their values

        if(ratings.size() == 0)
        {
            ratingText.setVisibility(View.VISIBLE);
            ratingsList.setVisibility(View.GONE);
        }
        else
        {
            ratingText.setVisibility(View.GONE);
            ratingsList.setVisibility(View.VISIBLE);

            int totalOnTime = 0, totalSkill = 0, totalBehavior = 0;

            for(Rating rate : ratings)
            {
                totalOnTime += rate.getOnTime();
                totalSkill += rate.getSkills();
                totalBehavior += rate.getBehavior();
            }

            onTimeRating.setRating(totalOnTime / ratings.size());
            skillRating.setRating(totalSkill / ratings.size());
            behaviorRating.setRating(totalBehavior / ratings.size());
            overallRatingBar.setRating(((totalOnTime / ratings.size()) + (totalSkill / ratings.size()) + (totalBehavior / ratings.size())) / 3.0f);
            overallText.setText(String.format(Locale.getDefault(), "%.1f", ((totalOnTime / ratings.size()) + (totalSkill / ratings.size()) + (totalBehavior / ratings.size())) / 3.0));

            totalText.setText("(" + ratings.size() + ")");

            ratingsList.setAdapter(new RatingsAdapter(ratings, getApplicationContext()));
        }

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
    }
}