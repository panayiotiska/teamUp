package com.github.h01d.teamup.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.h01d.teamup.activities.CreateActivity;
import com.github.h01d.teamup.adapters.GamesAdapter;
import com.github.h01d.teamup.R;
import com.github.h01d.teamup.models.Game;
import com.github.h01d.teamup.network.NetworkManager;
import com.github.h01d.teamup.network.NetworkManagerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class GamesFragment extends Fragment
{
    private final String TAG = "GamesFragment";

    // Layout views

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout relativeLayout;
    private ImageView errorImage;
    private TextView errorText;

    private ShimmerFrameLayout shimmerFrameLayout;

    public GamesFragment()
    {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        setHasOptionsMenu(true);

        // Initialize views

        recyclerView = view.findViewById(R.id.f_games_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        relativeLayout = view.findViewById(R.id.f_games_relative);
        errorImage = view.findViewById(R.id.f_games_icon);
        errorText = view.findViewById(R.id.f_games_error);

        swipeRefreshLayout = view.findViewById(R.id.f_games_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadData();
            }
        });

        shimmerFrameLayout = view.findViewById(R.id.f_games_shimmer);

        return view;
    }

    @Override
    public void onPause()
    {
        super.onPause();

        swipeRefreshLayout.setRefreshing(false);

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Reload data in case of leaving the game fragment

        loadData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.create_game_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.m_create:
                startActivity(new Intent(getContext(), CreateActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * loadData will load the data from the API using GET request
     */

    private void loadData()
    {
        swipeRefreshLayout.setRefreshing(true);
        shimmerFrameLayout.startShimmer();

        NetworkManager.getInstance(getContext()).getData("http://104.223.87.94:3000/api/v1/games/", new NetworkManagerListener()
        {
            @Override
            public void getResult(JSONArray result)
            {
                try
                {
                    // Extract data from result

                    ArrayList<Game> games = new ArrayList<>();

                    for(int i = 0; i < result.length(); i++)
                    {
                        JSONObject tmp = result.getJSONObject(i);

                        Log.d(TAG, tmp.toString());

                        int gameid = tmp.getInt("id");
                        String name = tmp.getString("name");
                        String gameDate = tmp.getString("eventDate");
                        int type = tmp.getInt("type");
                        int size = tmp.getInt("size");
                        String city = tmp.getJSONObject("Location").getString("city");

                        games.add(new Game(gameid, "", name, type, size, true, city, "", "", 0, 0, gameDate, "", "", "", ""));
                    }

                    // Update UI based on data

                    updateUI(games);
                }
                catch(JSONException e)
                {
                    Log.d(TAG, e.getMessage());
                    e.printStackTrace();

                    swipeRefreshLayout.setRefreshing(false);

                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    // Show the error message
                    // TODO: make a method to take care of it

                    recyclerView.setVisibility(View.INVISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);

                    errorImage.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                    errorText.setText("Πρόβλημα κατα την επεξεργασία.\nΠαρακαλόυμε δοκιμάστε ξανα.");
                }
            }

            @Override
            public void getError(String error, int code)
            {
                swipeRefreshLayout.setRefreshing(false);

                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                // Show the error message
                // TODO: make a method to take care of it

                recyclerView.setVisibility(View.INVISIBLE);
                relativeLayout.setVisibility(View.VISIBLE);

                if(code == 0)
                {
                    errorImage.setImageResource(R.drawable.ic_cloud_off_black_24dp);
                }
                else
                {
                    errorImage.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                }

                errorText.setText(error);
            }
        });
    }

    /**
     * updateUI will update all components values
     *
     * @param games the object we are getting the data from
     */

    private void updateUI(ArrayList<Game> games)
    {
        // Update all components with their values

        swipeRefreshLayout.setRefreshing(false);

        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);

        if(games.size() == 0)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);

            errorImage.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
            errorText.setText("Δεν υπάρχουν διαθέσιμα παιχνίδια.");
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.INVISIBLE);

            SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();

            GamesAdapter today = new GamesAdapter(getContext(), games, "Διαθέσιμα παιχνίδια");
            sectionedRecyclerViewAdapter.addSection(today);

            recyclerView.setAdapter(sectionedRecyclerViewAdapter);
        }
    }
}