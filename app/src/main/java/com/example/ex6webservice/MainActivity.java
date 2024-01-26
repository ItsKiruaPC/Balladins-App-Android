package com.example.ex6webservice;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.frameLayout1);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.item_1) {
                    loadFragment(new PageHotel(), false);
                } else if (itemId == R.id.item_2) {
                    loadFragment(new PageReserv(), false);
                } else if (itemId == R.id.item_3) {
                    loadFragment(new PageSupprimer(), false);
                } else {
                    Toast.makeText(getApplicationContext(), "La page demandé n'est pas disponible", Toast.LENGTH_SHORT).show();
                }
                return true;

            }
        });
        loadFragment(new PageHotel(), true);
    }

    public void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialized) {
            fragmentTransaction.add(R.id.frameLayout1, fragment);
        } else {
            fragmentTransaction.replace(R.id.frameLayout1, fragment);
        }
        fragmentTransaction.commit();
    }

}