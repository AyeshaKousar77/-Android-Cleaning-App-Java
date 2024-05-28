package com.example.bmi.database;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bmi.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LatLng selectedLocation;
    private static final LatLngBounds ISLAMABAD_BOUNDS = new LatLngBounds(
            new LatLng(33.5, 72.8), // Southwest corner
            new LatLng(33.9, 73.3)  // Northeast corner
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Button to confirm location selection
        Button confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            if (selectedLocation != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", selectedLocation.latitude);
                resultIntent.putExtra("longitude", selectedLocation.longitude);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Please select a location first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Set the map's camera position to the defined bounds
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(ISLAMABAD_BOUNDS, 0));

        // Enable necessary gestures for user interaction within the bounds
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        // Set a click listener for the map
        mMap.setOnMapClickListener(latLng -> {
            // Ensure the clicked location is within the bounds
            if (ISLAMABAD_BOUNDS.contains(latLng)) {
                // Set the selected location
                selectedLocation = latLng;

                // Clear previous markers and add a marker for the new location
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));

                Toast.makeText(this, "Location set", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please select a location within the specified area", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

