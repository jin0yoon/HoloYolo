package com.example.user.holoyolo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class PlacePickerActivity extends AppCompatActivity {
    private Button btngetplace;
    private TextView get_place;
    int PLACE_PICKER_REQUEST=1;
    double locationLat;
    double locationLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsselect);

        btngetplace=(Button)findViewById(R.id.btngetgps);
        get_place=(TextView)findViewById(R.id.tvPlace);
        btngetplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    Intent intent= builder.build(PlacePickerActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                }catch (GooglePlayServicesRepairableException e){
                    e.printStackTrace();
                }catch (GooglePlayServicesNotAvailableException e){
                    e.printStackTrace();
                }

            }
        });

    }


    public void googlePlacePicker(View view){
        //place picker 함수를 부르는 부분

        PlacePicker.IntentBuilder builder= new PlacePicker.IntentBuilder();

        try{

            startActivityForResult(builder.build(PlacePickerActivity.this), PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesRepairableException e){
            e.printStackTrace();
        }catch(GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode ==PLACE_PICKER_REQUEST){
            if(resultCode== RESULT_OK){
                //Place place = PlacePicker.getPlace(PlacePickerActivity.this, data);
                Place place = PlacePicker.getPlace(this, data);
                StringBuilder stBuilder = new StringBuilder();

                String placename = String.format("%s", place.getName());
                String latitude = String.valueOf(place.getLatLng().latitude);
                String longitude = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());

                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);

                get_place.setText(stBuilder.toString());
            }
        }
    }



}