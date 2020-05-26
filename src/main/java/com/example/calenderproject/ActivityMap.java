package com.example.calenderproject;

import androidx.fragment.app.FragmentActivity;
import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class  ActivityMap extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    Button buttonMap1;
    double lat;
    double lng;
    MyDatabase db = new MyDatabase(this);
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        buttonMap1 = (Button)findViewById(R.id.buttonMap1);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this);
        name =  getIntent().getStringExtra("map_name");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng istanbul = new LatLng(41.015137, 28.979530);
        mMap.addMarker(new MarkerOptions().position(istanbul).title("Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(istanbul));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                Marker marker = null;
                List<Address> addresses = null;
                //double lat = latLng.latitude;
                //double lng = latLng.longitude;
                setLat(latLng.latitude);
                setLng(latLng.longitude);

                if (marker != null) {
                    marker.remove();
                }
                final Geocoder geocoder = new Geocoder(ActivityMap.this, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(lat, lng, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                Toast.makeText(getApplicationContext(), cityName + stateName + countryName, Toast.LENGTH_SHORT).show();
                marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mylocation))
                        .snippet(cityName + " " + stateName + " " + countryName));

                buttonMap1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", 28.43242324,77.8977673);
                        //Intent it = new Intent(Intent.ACTION_SEND);
                        //it.setType("text/plain");
                        //it.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri));
                        //it.putExtra(Intent.EXTRA_TEXT, uri);
                        //if (it.resolveActivity(getPackageManager()) != null) {
                         //   startActivity(it);
                        //}
                        //Intent intent=new Intent();
                        //intent.putExtra("l1", getLat());
                        //intent.putExtra("l2",getLng());
                        //setResult(Activity.RESULT_OK, intent);

                        db.updateLocationDatabase(name,  getLat(),  getLng());

                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.custom_toast_container));
                        TextView text = (TextView)layout.findViewById(R.id.text);
                        text.setText("the location has been saved");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 400);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                        finish();

                    }
                });
            }
        });
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}



