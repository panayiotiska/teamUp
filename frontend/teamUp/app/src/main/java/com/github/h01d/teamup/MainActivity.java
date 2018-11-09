package com.github.h01d.teamup;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.widget.TextView;

import com.github.h01d.teamup.activities.ProfileActivity;
import com.github.h01d.teamup.fragments.GamesFragment;
import com.github.h01d.teamup.models.User;

public class MainActivity extends AppCompatActivity
{
    private final String TAG = "MainActivity";

    // Dummy user for now, will be cached in memory

    private final User user = new User("100000273909010", "Rafaellos", "Monoyios", "27-10-2018 10:42");

    // Layout views

    private FragmentTransaction fragmentTransaction;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Settings up ActionBar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Παιχνίδια");

        // Settings up Fragments

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_frame_holder, new GamesFragment());
        fragmentTransaction.commit();

        // Setting up Navigation

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_games);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch(menuItem.getItemId())
                {
                    case R.id.nav_games:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_frame_holder, new GamesFragment());
                        fragmentTransaction.commit();

                        getSupportActionBar().setTitle("Παιχνίδια");
                        menuItem.setChecked(true);
                        break;
                    case R.id.nav_profile:
                        Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
                        profile.putExtra("userID", user.getUserID());
                        profile.putExtra("name", user.getFirstName() + " " + user.getLastName());
                        startActivity(profile);
                        break;
                    case R.id.nav_logout:
                        AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(MainActivity.this);
                        logoutBuilder.setTitle("Αποσύνδεση");
                        logoutBuilder.setMessage("Είσαι σίγουρος ότι θέλεις να αποσυνδεθείς;");
                        logoutBuilder.setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                        logoutBuilder.setNegativeButton("ΟΧΙ", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog logoutDialog = logoutBuilder.create();
                        logoutDialog.show();
                        break;
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        View header = navigationView.getHeaderView(0);

        TextView headerName = header.findViewById(R.id.nav_header_name);
        //ImageView headerImage = header.findViewById(R.id.nav_header_image);

        headerName.setText(user.getFirstName() + " " + user.getLastName());
    }

    @Override
    public void onBackPressed()
    {
        // If drawer is open, on back press close the drawer instead of activity

        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            AlertDialog.Builder exitBuilder = new AlertDialog.Builder(MainActivity.this);
            exitBuilder.setTitle("Έξοδος");
            exitBuilder.setMessage("Είσαι σίγουρος ότι θέλεις να κλείσεις την εφαρμογή;");
            exitBuilder.setPositiveButton("ΝΑΙ", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    finish(); // Kill the app
                }
            });
            exitBuilder.setNegativeButton("ΟΧΙ", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.dismiss();
                }
            });
            AlertDialog exitDialog = exitBuilder.create();
            exitDialog.show();
        }
    }
}