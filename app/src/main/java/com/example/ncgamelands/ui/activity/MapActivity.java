package com.example.ncgamelands.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.example.ncgamelands.R;
import com.example.ncgamelands.model.CustomInfo;
import com.example.ncgamelands.util.AppUtil;
import com.example.ncgamelands.util.IntentUtil;
import com.example.ncgamelands.util.KLog;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapActivity extends BaseActivity implements
        OnMapReadyCallback,
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MapActivity.class.getSimpleName();

    @BindView(R.id.ivback)
    ImageView ivback;
    @BindView(R.id.tvtitle)
    TextView tvtitle;
    @BindView(R.id.ivicon)
    ImageView ivicon;

    @BindView(R.id.lldirections)
    LinearLayout lldirections;
    @BindView(R.id.llregulations)
    LinearLayout llregulations;
    @BindView(R.id.llgpstracking)
    LinearLayout llgpstracking;

    private GoogleMap mMap;
    private LocationManager locationManager;

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1990;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    long interval = 10 * 1000;   // 10 seconds, in milliseconds
    long fastestInterval = 1 * 1000;  // 1 second, in milliseconds
    float minDisplacement = 0;

    private double mCurrentLatitude = 0;
    private double mCurrentLongitude = 0;

    private double mDstLatitude = 0;
    private double mDstLongitude = 0;

    CustomInfo item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_map);
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

        ivicon.setVisibility(View.VISIBLE);
        ivicon.setImageResource(R.drawable.ic_navigation_white);

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
                mDstLatitude = Double.parseDouble(item.getLatitude());
            }
            if (!AppUtil.getValue(item.getLongitude()).isEmpty()) {
                mDstLongitude = Double.parseDouble(item.getLongitude());
            }

        }
    }

    @OnClick(R.id.ivback)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.ivicon)
    public void onNavigation() {
        if (mDstLatitude != 0 && mDstLongitude != 0) {
            try {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + mDstLatitude + "," + mDstLongitude + "");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            } catch (Exception e) {
                KLog.e(e.getMessage());
            }
        }
    }

    @OnClick(R.id.llregulations)
    public void onRegulations() {
        IntentUtil.startRegulationActivity(this);
    }

    @OnClick(R.id.llgpstracking)
    public void onGPSTracking() {
        IntentUtil.startGPSTrackingActivity(this);
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
        KLog.d(location.toString());

//        mCurrentLatitude = mDstLatitude + 0.2;
//        mCurrentLongitude = mDstLongitude + 0.2;

        mCurrentLatitude = location.getLatitude();
        mCurrentLongitude = location.getLongitude();

        //place marker at current position
        mMap.clear();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(mCurrentLatitude, mCurrentLongitude));
        markerOptions.title("You");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mMap.addMarker(markerOptions);

        markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(mDstLatitude, mDstLongitude));
        markerOptions.title(item.getLabel());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(markerOptions);

        LatLng origin = new LatLng(mCurrentLatitude, mCurrentLongitude);
        LatLng destination = new LatLng(mDstLatitude, mDstLongitude);

        DrawRouteMaps.getInstance(this).draw(origin, destination, mMap);
//        DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.ic_marker_green, "You");
//        DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.ic_marker_red, item.getLabel());

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();

        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));

        //zoom to current position:
/*        CameraPosition aCameraPosition = new CameraPosition.Builder().target(
                new LatLng(mCurrentLatitude, mCurrentLongitude)).zoom(18).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(aCameraPosition));
        mMap.addMarker(markerOptions);*/

        //If you only need one location, unregister the listener
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}