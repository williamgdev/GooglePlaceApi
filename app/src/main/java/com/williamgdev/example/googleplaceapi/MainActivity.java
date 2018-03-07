package com.williamgdev.example.googleplaceapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends AppCompatActivity {


    int PLACE_PICKER_REQUEST = 1;
    private GPAInteractor gpaInteractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gpaInteractor = GPAInteractor.getInstance();
        gpaInteractor.initializeGoogleApi(this);

        launchMapFragment();

        launchPlacePicker();
    }

    private void launchPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            showText(e.getMessage());
        }
    }

    private void launchMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(gpaInteractor);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(placeSelectionListener);
        autocompleteFragment.setFilter(gpaInteractor.getFilterByCountry("US"));
    }

    private PlaceSelectionListener placeSelectionListener = new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(Place place) {
            gpaInteractor.setSelectedPlace(place);
            gpaInteractor.moveCameraToSelectedPlace();
        }

        @Override
        public void onError(Status status) {
            // TODO: Handle the error.
            showText("An error occurred: " + status);
        }
    };


    public void showText(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                gpaInteractor.setSelectedPlace(PlacePicker.getPlace(data, this));
                gpaInteractor.moveCameraToSelectedPlace();
            }
        }
    }

}
