package com.example.ncgamelands.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.ncgamelands.R;
import com.example.ncgamelands.model.CustomInfo;
import com.example.ncgamelands.util.AppUtil;
import com.example.ncgamelands.util.KLog;
import com.example.ncgamelands.util.PreferenceUtil;
import com.example.ncgamelands.util.RuntimeData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class GPSTrackingActivity extends BaseActivity implements
        OnMapReadyCallback,
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = GPSTrackingActivity.class.getSimpleName();

    @BindView(R.id.ivback)
    ImageView ivback;
    @BindView(R.id.tvtitle)
    TextView tvtitle;
    @BindView(R.id.ivicon)
    ImageView ivicon;

    @BindView(R.id.switchTKOnOff)
    Switch switchTKOnOff;

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1990;

    private GoogleMap mMap;
    private LocationManager locationManager;

    private ArrayList<LatLng> points; //added
    Polyline line; //added

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    long interval = 10 * 1000;   // 10 seconds, in milliseconds
    long fastestInterval = 1 * 1000;  // 1 second, in milliseconds
    float minDisplacement = 0;

    private double mCurrentLatitude = 0;
    private double mCurrentLongitude = 0;

    CustomInfo item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_gps_tracking);
        ButterKnife.bind(this);

//        addFragmentToContainer(new Map());
//        onNewIntent(getIntent());

//        setUpMapIfNeeded();
//        buildGoogleApiClient();

//        onNewIntent(getIntent());
    }

    @Override
    public void onResume() {
        super.onResume();

        points = new ArrayList<LatLng>(); //added

        ivicon.setVisibility(View.VISIBLE);

        switchTKOnOff.setChecked(PreferenceUtil.isTrackingEnable().get());

        setUpMapIfNeeded();
        buildGoogleApiClient();

/*
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
*/

        item = RuntimeData.getInstance().getItem();
        if (item != null) {

            tvtitle.setText(item.getLabel());

            if (!AppUtil.getValue(item.getLatitude()).isEmpty()) {
//                mCurrentLatitude = Double.parseDouble(item.getLatitude());
            }
            if (!AppUtil.getValue(item.getLongitude()).isEmpty()) {
//                mCurrentLongitude = Double.parseDouble(item.getLongitude());
            }

        }

    }

    @OnCheckedChanged(R.id.switchTKOnOff)
    public void onTrackingCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        PreferenceUtil.isTrackingEnable().set(isChecked);
    }

    @OnClick(R.id.ivback)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.ivicon)
    public void onClearMap() {
        points.clear();
        mMap.clear();
    }

    private synchronized void buildGoogleApiClient() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void setUpMapIfNeeded() {
        // Google Play Services are not available
        if (checkPlayServices()) {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }

        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                return true;
            }
        });
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.application_requires_gps))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(interval)
                .setFastestInterval(fastestInterval)
                .setSmallestDisplacement(minDisplacement);

/*        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }*/

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void handleNewLocation(Location location) {
        KLog.d("onLocationChanged " + location.toString());

/*        if (mCurrentLatitude == 0)
            mCurrentLatitude = location.getLatitude();
        if (mCurrentLongitude == 0)
            mCurrentLongitude = location.getLongitude();*/

/*        if (mCurrentLatitude == 0) {
            mCurrentLatitude = location.getLatitude();
        } else {
            mCurrentLatitude += 0.0001;
        }
        if (mCurrentLongitude == 0) {
            mCurrentLongitude = location.getLongitude();
        } else {
            mCurrentLongitude += 0.0001;
        }*/

        mCurrentLatitude = location.getLatitude();
        mCurrentLongitude = location.getLongitude();

        //place marker at current position
        mMap.clear();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(mCurrentLatitude, mCurrentLongitude));
        markerOptions.title("You");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(markerOptions);

        //zoom to current position:
        CameraPosition aCameraPosition = new CameraPosition.Builder().target(
                new LatLng(mCurrentLatitude, mCurrentLongitude)).zoom(18).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(aCameraPosition));

        // redraws line
        if (PreferenceUtil.isTrackingEnable().get()) {
            LatLng latLng = new LatLng(mCurrentLatitude, mCurrentLongitude); //you already have this
            points.add(latLng); //added
        }

        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }

        line = mMap.addPolyline(options); //add Polyline

        //If you need only one location, unregister the listener
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}