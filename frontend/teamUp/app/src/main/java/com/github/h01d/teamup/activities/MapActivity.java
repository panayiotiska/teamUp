package com.github.h01d.teamup.activities;

import android.content.Intent;
import android.os.Bundle;

import com.github.h01d.teamup.R;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback
{
    private GoogleMap mMap;
    String location;
    List<Address> addresses = null;

    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Επιλογή Τοποθεσίας");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(final String query)
            {
                location = query;

                mMap.clear();

                if(location.length() != 0)
                {
                    Geocoder geocoder = new Geocoder(getApplicationContext());

                    try
                    {
                        if(geocoder.getFromLocationName(location, 1).size() != 0)
                        {
                            Address address = geocoder.getFromLocationName(location, 1).get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                            mMap.addMarker(new MarkerOptions().position(latLng).title("Game Location"));
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                            Snackbar.make(findViewById(R.id.myCoordinatorLayout), query, Snackbar.LENGTH_INDEFINITE).setAction("ΕΒΙΒΕΒΑΙΩΣΗ", new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    Intent i = new Intent(MapActivity.this, CreateGameDetails.class);
                                    i.putExtra("SelectedAddress", query);
                                    startActivity(i);
                                }
                            }).show();
                        }
                        else
                        {
                            Toast.makeText(getBaseContext(), "ΔΕΝ ΥΠΑΡΧΕΙ ΔΙΕΥΘΥΝΣΗ", Toast.LENGTH_LONG).show();
                        }
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }

                searchView.closeSearch();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                //Do some magic
                return false;
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onBackPressed()
    {
        if(searchView.isSearchOpen())
        {
            searchView.closeSearch();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.m_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        mMap = googleMap;

        LatLng thessaloniki = new LatLng(40.6401, 22.9444);
        //mMap.addMarker(new MarkerOptions().position(thessaloniki).title("Game Location").icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker())));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(thessaloniki));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
        {
            @Override
            public void onMapLongClick(LatLng latLng)
            {
                mMap.clear();

                Geocoder geocoder = new Geocoder(getApplicationContext());

                try
                {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

                if(addresses.size() != 0)
                {
                    mMap.addMarker(new MarkerOptions().position(latLng));

                    String whole_address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    location = whole_address.substring(0, whole_address.indexOf(",")); //returns the text before first comma

                    Snackbar.make(findViewById(R.id.myCoordinatorLayout), location, Snackbar.LENGTH_INDEFINITE).setAction("ΕΒΙΒΕΒΑΙΩΣΗ", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Toast.makeText(getBaseContext(), location.toUpperCase(), Toast.LENGTH_LONG).show();

                            Intent i = new Intent(MapActivity.this, CreateGameDetails.class);
                            i.putExtra("SelectedAddress", location);
                            startActivity(i);
                        }
                    }).show();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "ΔΕΝ ΥΠΑΡΧΕΙ ΔΙΕΥΘΥΝΣΗ", Toast.LENGTH_LONG).show();
                }
            }

        });
    }
}