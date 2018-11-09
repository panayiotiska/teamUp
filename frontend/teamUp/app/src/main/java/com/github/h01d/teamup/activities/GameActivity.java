package com.github.h01d.teamup.activities;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.h01d.teamup.R;
import com.github.h01d.teamup.adapters.TeamsAdapter;
import com.github.h01d.teamup.models.Game;
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

public class GameActivity extends AppCompatActivity
{
    private final String TAG = "GameActivity";

    // Current game ID

    private int gameID;

    // Layout views

    private TextView gameDate, gamePhone, gameLocation, gameComment;
    private Button team1Button, team2Button;
    private RecyclerView team1Recycler, team2Recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Setting up ActionBar

        int barColor, statusColor;

        if(getIntent().getExtras().getInt("type") == 0)
        {
            barColor = getResources().getColor(R.color.colorFootball);
            statusColor = getResources().getColor(R.color.colorFootballDark);
        }
        else
        {
            barColor = getResources().getColor(R.color.colorBasket);
            statusColor = getResources().getColor(R.color.colorBasketDark);
        }

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(statusColor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(barColor));

        // Initialize game ID

        gameID = getIntent().getExtras().getInt("gameid");

        // Initialize views

        gameLocation = findViewById(R.id.a_game_location);
        gameDate = findViewById(R.id.a_game_date);
        gamePhone = findViewById(R.id.a_game_phone);
        gameComment = findViewById(R.id.a_game_comment);
        team1Button = findViewById(R.id.a_game_team1_button);
        team2Button = findViewById(R.id.a_game_team2_button);

        team1Recycler = findViewById(R.id.a_game_team1);
        team1Recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        team1Recycler.setHasFixedSize(true);
        team1Recycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        team2Recycler = findViewById(R.id.a_game_team2);
        team2Recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        team2Recycler.setHasFixedSize(true);
        team2Recycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        // Load data for a first time

        loadData();
    }

    /**
     * loadData will load game data from the API
     */

    private void loadData()
    {
        NetworkManager.getInstance(getApplicationContext()).getData("http://104.223.87.94:3000/api/v1/games/" + gameID, new NetworkManagerListener()
        {
            @Override
            public void getResult(JSONArray result)
            {
                try
                {
                    // Extract data from result

                    JSONObject tmp = result.getJSONObject(0);

                    int gameid = tmp.getInt("id");
                    String userid = tmp.getString("id");
                    String createdDate = tmp.getString("createdAt");
                    String name = tmp.getString("name");
                    int type = tmp.getInt("type");
                    int size = tmp.getInt("size");
                    boolean opponents = tmp.getBoolean("opponents");
                    String gameDate = tmp.getString("eventDate");
                    String comments = tmp.getString("description");
                    String phone = tmp.getString("contact");
                    String status = tmp.getString("status");
                    String city = tmp.getJSONObject("location").getString("city");
                    String address = tmp.getJSONObject("location").getString("address");
                    String countryCode = tmp.getJSONObject("location").getString("countryCode");
                    Double locLong = tmp.getJSONObject("location").getDouble("longitude");
                    Double locLat = tmp.getJSONObject("location").getDouble("latitude");

                    // Update UI based on data

                    updateUI(new Game(gameid, userid, name, type, size, opponents, city, address, countryCode, locLat, locLong, gameDate, phone, comments, status, createdDate));

                    //

                    if(!tmp.isNull("teams"))
                    {
                        ArrayList<User> team1 = new ArrayList<>();
                        ArrayList<User> team2 = new ArrayList<>();

                        JSONArray teams = tmp.getJSONArray("teams");

                        if(!teams.isNull(0))
                        {
                            JSONArray team_1 = teams.getJSONArray(0);

                            for(int i = 0; i < team_1.length(); i++)
                            {
                                JSONObject player = team_1.getJSONObject(i);

                                String id = player.getString("id");
                                String firstName = player.getString("firstName");
                                String lastName = player.getString("lastName");

                                team1.add(new User(id, firstName, lastName, ""));
                            }
                        }

                        if(!teams.isNull(1))
                        {
                            JSONArray team_2 = teams.getJSONArray(1);

                            for(int i = 0; i < team_2.length(); i++)
                            {
                                JSONObject player = team_2.getJSONObject(i);

                                String id = player.getString("id");
                                String firstName = player.getString("firstName");
                                String lastName = player.getString("lastName");

                                team2.add(new User(id, firstName, lastName, ""));
                            }
                        }

                        updateUI(size, team1, team2);
                    }
                }
                catch(JSONException e)
                {
                    Log.d(TAG, e.getMessage());

                    //TODO: kill the process, show error message something went wrong

                    e.printStackTrace();
                }
            }

            @Override
            public void getError(String error, int code)
            {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

                //TODO: kill the process, show error message something went wrong
            }
        });
    }

    /**
     * updateUI will update all components values
     *
     * @param game the object we are getting the data from
     */

    private void updateUI(Game game)
    {
        // Update all components with their values

        getSupportActionBar().setTitle(game.getName() + " (" + game.getSize() + "x" + game.getSize() + ")");

        gameLocation.setText(game.getLocationAddress() + ", " + game.getLocationCity());
        gamePhone.setText(game.getPhone());
        gameComment.setText(game.getComment());

        try
        {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(game.getGameDate());

            gameDate.setText(new SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.getDefault()).format(date));
        }
        catch(ParseException e)
        {
            e.printStackTrace();
        }
    }

    private void updateUI(int size, ArrayList<User> team1, ArrayList<User> team2)
    {
        team1Recycler.setAdapter(new TeamsAdapter(team1, getApplicationContext()));

        if(team1.size() < size)
        {
            team1Button.setVisibility(View.VISIBLE);

            if(team1.size() < team2.size())
            {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) team1Button.getLayoutParams();
                params.topMargin = (int) (38 * (team2.size() - team1.size()) * getResources().getDisplayMetrics().density) + 2;
                team1Button.requestLayout();
            }
        }
        else
        {
            team1Button.setVisibility(View.GONE);
        }

        team2Recycler.setAdapter(new TeamsAdapter(team2, getApplicationContext()));

        if(team2.size() < size)
        {
            team2Button.setVisibility(View.VISIBLE);

            if(team2.size() < team1.size())
            {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) team2Button.getLayoutParams();
                params.topMargin = (int) (38 * (team1.size() - team2.size()) * getResources().getDisplayMetrics().density) + 2;
                team1Button.requestLayout();
            }
        }
        else
        {
            team2Button.setVisibility(View.GONE);
        }
    }
}