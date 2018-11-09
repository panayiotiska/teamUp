package com.github.h01d.teamup.activities;

import android.os.Bundle;

import com.github.h01d.teamup.R;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback
{
    private GoogleMap mMap;
    String location;
    List<Address> addresses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button lol = findViewById(R.id.searchButton);
        lol.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText location_tf = findViewById(R.id.tfAddress);
                TextView location_tv = findViewById(R.id.tvAddress);

                location = location_tf.getText().toString();
                location_tv.setText(location.toUpperCase());

                mMap.clear();

                /*SEARCH FOR GIVEN LOCATION*/


                if (location.length() != 0){

                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    try
                    {
                        Address address = geocoder.getFromLocationName(location,1).get(0);

                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        CreateActivity.latLong = latLng; //parsing value in CreateGame2 for marker
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Game Location"));
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });

        Button test = findViewById(R.id.doneButton);
        test.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                JSONObject locationObj = new JSONObject();
                try {
                    locationObj.put("city_name", addresses.get(0).getLocality());
                    locationObj.put("address", location);
                    locationObj.put("country", addresses.get(0).getCountryName());
                    locationObj.put("country_code", addresses.get(0).getCountryCode());
                    locationObj.put("postal_code", addresses.get(0).getPostalCode());
                    locationObj.put("latitude", addresses.get(0).getLatitude());
                    locationObj.put("longitude", addresses.get(0).getLongitude());

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                JSONArray jsonArray = new JSONArray();

                jsonArray.put(locationObj);

                Toast.makeText(getBaseContext(), location.toUpperCase(),Toast.LENGTH_LONG).show();

                TextView txtView = ((CreateActivity.addressTv).findViewById(R.id.tv_show_address));
                txtView.setTypeface(txtView.getTypeface(), Typeface.BOLD_ITALIC);
                txtView.setText(location);

                finish();
            }
        });
    }

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng thessaloniki = new LatLng(40.6401, 22.9444);
        //mMap.addMarker(new MarkerOptions().position(thessaloniki).title("Game Location").icon(BitmapDescriptorFactory.fromBitmap(createCustomMarker())));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(thessaloniki));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                mMap.clear();

                CreateActivity.latLong = latLng; //parse latitude and longitude to the static variable

                mMap.addMarker(new MarkerOptions().position(latLng));

                Geocoder geocoder = new Geocoder(getApplicationContext());

                try
                {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

                String whole_address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                location = whole_address.substring( 0, whole_address.indexOf(",")); //returns the text before first comma

                TextView location_tv = findViewById(R.id.tvAddress);
                location_tv.setText(location.toUpperCase());
            }

        });
    }

    /*
    public Bitmap createCustomMarker (){
        int height = 220;
        int width = 220;

        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.marker);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap customMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return customMarker;
    }
    */
}
