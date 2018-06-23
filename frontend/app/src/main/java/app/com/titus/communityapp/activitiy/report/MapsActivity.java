package app.com.titus.communityapp.activitiy.report;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import app.com.titus.communityapp.R;
import app.com.titus.communityapp.util.ParametersTags;
import app.com.titus.communityapp.util.factory.DialogFactory;
import app.com.titus.communityapp.util.DistanceCalculator;
import app.com.titus.communityapp.util.constant.ConstantUtils;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private LocationManager locationManager;
    private LatLng setPosition;

    private final String dialogMessage = "Do you confirm the coordinates?";
    private final String confirmMessage = "Confirm";
    private final String cancelMessage = "Cancel";


    private Runnable confirmLocation;
    private Runnable cancelLocation;
    private static final String TAG = MapsActivity.class.getName();
    private LatLng startPosition;
    private LocationListener locationListener;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Toolbar toolbar = findViewById(R.id.back);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setDialogRunnables();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> Log.v(TAG, "Location found:" + location));
    }


    private void setDialogRunnables() {
        confirmLocation = () -> {
            Intent intent = new Intent();
            String address = getAddresses(startPosition, this);
            if (!address.equals(ConstantUtils.EMPTY_STRING)) {
                intent.putExtra(ParametersTags.ADDRESS_TAG, address);
            }
            intent.putExtra(ParametersTags.LATITUDE_TAG, setPosition.latitude);
            intent.putExtra(ParametersTags.LONGITUDE_TAG, setPosition.longitude);
            Log.v(TAG, "Coord:" + setPosition);
            setResult(RESULT_OK, intent);
            finish();
        };
        cancelLocation = () -> {

        };
    }

    private void setLocationToCluj() {
        Log.v(TAG, "Location set to Cluj center");
        startPosition = new LatLng(ConstantUtils.clujLatitude, ConstantUtils.clujLongitude);
    }

    private void initLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.v(TAG, "location changed:" + location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }

    public static String getAddresses(LatLng position, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                Log.v(TAG, "address:" + returnedAddress.getAddressLine(0));
                return returnedAddress.getAddressLine(0);
            } else {
                Log.v(TAG, "No address returned");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "Cannot get address");
        }
        return ConstantUtils.EMPTY_STRING;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        initLocationListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, ConstantUtils.FIFTY_THOUSAND, ConstantUtils.ONE_THOUSAND, locationListener);
        if (locationManager != null) {
            Log.v(TAG, "get Current Location");
            Location location = getLastKnownLocation();
            if (location != null) {
                startPosition = new LatLng(location.getLatitude(), location.getLongitude());
                Log.v(TAG, "location:" + startPosition);
            } else {
                setLocationToCluj();
            }

        }
        if (locationManager == null ||
                (DistanceCalculator.distance(startPosition.latitude, ConstantUtils.clujLatitude, startPosition.longitude, ConstantUtils.clujLongitude)) > 40)
            setLocationToCluj();
        Log.v(TAG, "position:" + startPosition.toString());
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(startPosition));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(ConstantUtils.ZOOM));
        googleMap.setOnMapClickListener(v -> {
            setPosition = new LatLng(v.latitude, v.longitude);
            Dialog dialog = DialogFactory.createDialog(dialogMessage, confirmMessage, cancelMessage,
                    this, confirmLocation, cancelLocation);
            dialog.show();
        });
    }

    @SuppressLint("MissingPermission")
    private Location getLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            Log.v(TAG, "last known location:" + l);

            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                Log.v(TAG, "found best last known location:" + l);
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }

}
