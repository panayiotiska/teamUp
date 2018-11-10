package com.github.h01d.teamup.activities;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
    private boolean isOwner = false;
    private int teamId = -1;

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
        toolbar.setTitle(getIntent().getExtras().getInt("type") == 0 ? "Ποδόσφαιρο" : "Μπάσκετ");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.game_player_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.m_game_quit:
                if(isOwner)
                {
                    AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(getApplicationContext());
                    deleteBuilder.setTitle("ΔΙΑΓΡΑΦΗ");
                    deleteBuilder.setMessage("Είσαι σίγουρος ότι θέλεις να διαγράψεις το παιχνίδι;");
                    deleteBuilder.setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {

                        }
                    });
                    deleteBuilder.setNegativeButton("ΟΧΙ", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog deleteDialog = deleteBuilder.create();
                    deleteDialog.show();
                }
                else
                {
                    if(teamId == -1)
                    {
                        Toast.makeText(getApplicationContext(), "Δεν είσαι σε κάποια ομάδα.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(getApplicationContext());
                        deleteBuilder.setTitle("ΕΞΟΔΟΣ");
                        deleteBuilder.setMessage("Είσαι σίγουρος ότι θέλεις να βγεις από το παιχνίδι;");
                        deleteBuilder.setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                JSONObject tmp = new JSONObject();

                                try
                                {
                                    tmp.put("userId", "100000273909010");
                                }
                                catch(JSONException e)
                                {
                                    e.printStackTrace();

                                    Log.d(TAG, e.getMessage());
                                }

                                NetworkManager.getInstance(getApplicationContext()).deleteData("http://104.223.87.94:3000/api/v1/games/" + gameID + "/teams/" + teamId, tmp, new NetworkManagerListener()
                                {
                                    @Override
                                    public void getResult(JSONArray result)
                                    {
                                        loadData();

                                        Toast.makeText(getApplicationContext(), "Βγήκες από το παιχνίδι.", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void getError(String error, int code)
                                    {
                                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

                                        Log.d(TAG, error);
                                    }
                                });
                            }
                        });
                        deleteBuilder.setNegativeButton("ΟΧΙ", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog deleteDialog = deleteBuilder.create();
                        deleteDialog.show();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * loadData will load game data from the API
     */

    private void loadData()
    {
        teamId = -1;

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
                    String userid = tmp.getString("createdBy");
                    String createdDate = tmp.getString("createdAt");
                    String name = tmp.getString("name");
                    int type = tmp.getInt("type");
                    int size = tmp.getInt("size");
                    int firstTeamId = tmp.getInt("firstTeamId");
                    int secondTeamId = tmp.getInt("secondTeamId");
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

                    if(userid.equals("100000273909010")) // cached
                    {
                        isOwner = true;
                    }

                    // Update UI based on data

                    updateUI(new Game(gameid, userid, name, type, size, opponents, city, address, countryCode, locLat, locLong, gameDate, phone, comments, status, createdDate));

                    // Extract teams

                    if(!tmp.isNull("teams"))
                    {
                        ArrayList<User> team1 = new ArrayList<>();
                        ArrayList<User> team2 = new ArrayList<>();

                        JSONArray teams = tmp.getJSONArray("teams");

                        if(!teams.isNull(0)) // If team 1 exists
                        {
                            JSONArray team_1 = teams.getJSONArray(0);

                            for(int i = 0; i < team_1.length(); i++)
                            {
                                JSONObject player = team_1.getJSONObject(i);

                                String id = player.getString("id");
                                String firstName = player.getString("firstName");
                                String lastName = player.getString("lastName");

                                if(id.equals("100000273909010")) // cached
                                {
                                    teamId = firstTeamId;
                                }

                                team1.add(new User(id, firstName, lastName, "1"));
                            }
                        }

                        if(!teams.isNull(1)) // If team 2 exists
                        {
                            JSONArray team_2 = teams.getJSONArray(1);

                            for(int i = 0; i < team_2.length(); i++)
                            {
                                JSONObject player = team_2.getJSONObject(i);

                                String id = player.getString("id");
                                String firstName = player.getString("firstName");
                                String lastName = player.getString("lastName");

                                if(id.equals("100000273909010")) // cached
                                {
                                    teamId = secondTeamId;
                                }

                                team2.add(new User(id, firstName, lastName, "1"));
                            }
                        }

                        updateUI(size, team1, team2, firstTeamId, secondTeamId);
                    }
                }
                catch(JSONException e)
                {
                    Log.d(TAG, e.getMessage());

                    Toast.makeText(getApplicationContext(), "Πρόβλημα κατα την επεξεργασία.\nΠαρακαλόυμε δοκιμάστε ξανα.", Toast.LENGTH_SHORT).show();
                    finish();

                    e.printStackTrace();
                }
            }

            @Override
            public void getError(String error, int code)
            {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * updateUI (game)  will update all basic components values
     *
     * @param game the object we are getting the data from
     */

    private void updateUI(Game game)
    {
        // Update all components with their values

        if(game.getType() == 0)
        {
            getSupportActionBar().setTitle("Ποδόσφαιρο " + game.getSize() + "x" + game.getSize());
        }
        else
        {
            getSupportActionBar().setTitle("Μπάσκετ " + game.getSize() + "x" + game.getSize());
        }

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

    /**
     * UpdateUI (teams) will update all teams
     *
     * @param size  the game size
     * @param team1 the first team
     * @param team2 the second teeam
     */

    private void updateUI(int size, ArrayList<User> team1, final ArrayList<User> team2, final int firstTeamId, final int secondTeamId)
    {
        team1Recycler.setAdapter(new TeamsAdapter(team1, getApplicationContext()));

        if(team1.size() < size && !team1.contains(new User("100000273909010", "Rafaellos", "Monoyios", "1")))
        {
            team1Button.setVisibility(View.VISIBLE);

            if(team2.contains(new User("100000273909010", "Rafaellos", "Monoyios", "")))
            {
                team1Button.setText("ΑΛΛΑΓΗ");
            }
            else
            {
                team1Button.setText("ΕΙΣΟΔΟΣ");
            }

            // In case their is a difference in teams add margin to fill the void of buttons

            if(team1.size() < team2.size())
            {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) team1Button.getLayoutParams();

                if(team2.size() == size || team2.size() == 1)
                {
                    params.topMargin = (int) (38 * ((team2.size() - team1.size()) - 1) * getResources().getDisplayMetrics().density) + 2;
                }
                else
                {
                    params.topMargin = (int) (38 * (team2.size() - team1.size()) * getResources().getDisplayMetrics().density) + 2;
                }

                team1Button.requestLayout();
            }

            team1Button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    NetworkManager.getInstance().postData("http://104.223.87.94:3000/api/v1/games/" + gameID + "/teams/" + firstTeamId, new JSONObject(), new NetworkManagerListener()
                    {
                        @Override
                        public void getResult(JSONArray result)
                        {
                            loadData();

                            if(team1Button.getText().equals("ΑΛΛΑΓΗ"))
                            {
                                Toast.makeText(getApplicationContext(), "Άλλαξες ομάδα", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Μπήκες στο παιχνίδι", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void getError(String error, int code)
                        {
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else
        {
            team1Button.setVisibility(View.GONE);
        }

        team2Recycler.setAdapter(new TeamsAdapter(team2, getApplicationContext()));

        if(team2.size() < size && !team2.contains(new User("100000273909010", "Rafaellos", "Monoyios", "")))
        {
            team2Button.setVisibility(View.VISIBLE);

            if(team1.contains(new User("100000273909010", "Rafaellos", "Monoyios", "")))
            {
                team2Button.setText("ΑΛΛΑΓΗ");
            }
            else
            {
                team2Button.setText("ΕΙΣΟΔΟΣ");
            }

            if(team2.size() < team1.size())
            {
                // In case their is a difference in teams add margin to fill the void of buttons

                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) team2Button.getLayoutParams();

                if(team1.size() == size || team1.size() == 1)
                {
                    params.topMargin = (int) (38 * ((team1.size() - team2.size()) - 1) * getResources().getDisplayMetrics().density) + 2;
                }
                else
                {
                    params.topMargin = (int) (38 * (team1.size() - team2.size()) * getResources().getDisplayMetrics().density) + 2;
                }

                team2Button.requestLayout();
            }

            team2Button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    NetworkManager.getInstance().postData("http://104.223.87.94:3000/api/v1/games/" + gameID + "/teams/" + secondTeamId, new JSONObject(), new NetworkManagerListener()
                    {
                        @Override
                        public void getResult(JSONArray result)
                        {
                            loadData();

                            if(team2Button.getText().equals("ΑΛΛΑΓΗ"))
                            {
                                Toast.makeText(getApplicationContext(), "Άλλαξες ομάδα", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Μπήκες στο παιχνίδι", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void getError(String error, int code)
                        {
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        else
        {
            team2Button.setVisibility(View.GONE);
        }
    }
}