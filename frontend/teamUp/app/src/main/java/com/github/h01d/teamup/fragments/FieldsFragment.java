package com.github.h01d.teamup.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.h01d.teamup.R;
import com.github.h01d.teamup.adapters.FieldsAdapter;
import com.github.h01d.teamup.adapters.GamesAdapter;
import com.github.h01d.teamup.models.Field;
import com.github.h01d.teamup.models.Game;
import com.github.h01d.teamup.network.NetworkManager;
import com.github.h01d.teamup.network.NetworkManagerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class FieldsFragment extends Fragment
{
    private final String TAG = "FieldsFragment";

    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView errorImage;
    private TextView errorText;

    public FieldsFragment()
    {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_fields, container, false);

        recyclerView = view.findViewById(R.id.f_fields_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        relativeLayout = view.findViewById(R.id.f_fields_relative);
        errorImage = view.findViewById(R.id.f_fields_icon);
        errorText = view.findViewById(R.id.f_fields_error);

        swipeRefreshLayout = view.findViewById(R.id.f_fields_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadData();
            }
        });

        return view;
    }

    @Override
    public void onPause()
    {
        super.onPause();

        swipeRefreshLayout.setRefreshing(false);

        //shimmerFrameLayout.stopShimmer();
        //shimmerFrameLayout.setVisibility(View.GONE);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // Reload data in case of leaving the game fragment

        loadData();
    }

    private void loadData()
    {
        swipeRefreshLayout.setRefreshing(true);
        //shimmerFrameLayout.startShimmer();

        NetworkManager.getInstance(getContext()).getData("http://104.223.87.94:3000/api/v1/fields/", new NetworkManagerListener()
        {
            @Override
            public void getResult(JSONArray result1)
            {
                try
                {
                    JSONArray result = new JSONArray("[\n" +
                            "\t{\n" +
                            "\t\t\"id\":1,\n" +
                            "\t\t\"name\":\"Toumba Stadium\",\n" +
                            "\t\t\"type\":0,\n" +
                            "\t\t\"imageUrl\":\"\",\n" +
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

                    // Extract data from result

                    ArrayList<Field> sponsoredFields = new ArrayList<>();
                    ArrayList<Field> publicFields = new ArrayList<>();

                    for(int i = 0; i < result.length(); i++)
                    {
                        JSONObject tmp = result.getJSONObject(0);

                        Log.d(TAG, tmp.toString());

                        int fieldId = tmp.getInt("id");
                        String name = tmp.getString("name");
                        int type = tmp.getInt("type");
                        String image = tmp.getString("imageUrl");
                        boolean sponsored = tmp.getBoolean("sponsored");
                        String phone = tmp.getString("contactPhone");
                        String city = tmp.getJSONObject("location").getString("city");
                        String address = tmp.getJSONObject("location").getString("address");
                        String countryCode = tmp.getJSONObject("location").getString("countryCode");
                        String postalCode = tmp.getJSONObject("location").getString("postalCode");
                        Double locLong = tmp.getJSONObject("location").getDouble("longitude");
                        Double locLat = tmp.getJSONObject("location").getDouble("latitude");
                        float averageRating = Float.parseFloat(tmp.getJSONObject("rating").getString("averageRating"));
                        int ratingsCount = tmp.getJSONObject("rating").getInt("ratingsCount");

                        if(sponsored)
                        {
                            sponsoredFields.add(new Field(fieldId, name, type, image, sponsored, phone, city, address, countryCode, postalCode, locLat, locLong, averageRating, ratingsCount));
                        }
                        else
                        {
                            publicFields.add(new Field(fieldId, name, type, image, sponsored, phone, city, address, countryCode, postalCode, locLat, locLong, averageRating, ratingsCount));
                        }
                    }

                    // Update UI based on data

                    updateUI(sponsoredFields, publicFields);
                }
                catch(JSONException e)
                {
                    Log.d(TAG, e.getMessage());
                    e.printStackTrace();

                    swipeRefreshLayout.setRefreshing(false);

                    //shimmerFrameLayout.stopShimmer();
                    //shimmerFrameLayout.setVisibility(View.GONE);

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

                //shimmerFrameLayout.stopShimmer();
                //shimmerFrameLayout.setVisibility(View.GONE);

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

    private void updateUI(ArrayList<Field> sponsoredFields, ArrayList<Field> publicFields)
    {
        // Update all components with their values

        swipeRefreshLayout.setRefreshing(false);

        //shimmerFrameLayout.stopShimmer();
        //shimmerFrameLayout.setVisibility(View.GONE);

        SectionedRecyclerViewAdapter sectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();

        FieldsAdapter sponsored, publics;

        if(sponsoredFields.size() > 0)
        {
            sponsored = new FieldsAdapter(getContext(), sponsoredFields, "Προτεινόμενα γήπεδα");
            sectionedRecyclerViewAdapter.addSection(sponsored);
        }

        if(publicFields.size() > 0)
        {
            publics = new FieldsAdapter(getContext(), sponsoredFields, "Δημόσια γήπεδα");
            sectionedRecyclerViewAdapter.addSection(publics);
        }

        if(sponsoredFields.size() == 0 && publicFields.size() == 0)
        {
            recyclerView.setVisibility(View.INVISIBLE);
            relativeLayout.setVisibility(View.VISIBLE);

            errorImage.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
            errorText.setText("Δεν υπάρχουν διαθέσιμα γήπεδα.");
        }
        else
        {
            recyclerView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.INVISIBLE);

            recyclerView.setAdapter(sectionedRecyclerViewAdapter);
        }
    }
}
