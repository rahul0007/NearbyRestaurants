package com.nearby.task.utils;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.model.LatLng;
import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class LocationManager implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        ResultCallback<LocationSettingsResult> {


    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final int REQUEST_CHECK_SETTINGS = 111;
    private static final int REQUEST_CHECK_PERMISSION = 222;
    // Location updates intervals in sec
    private static final int UPDATE_INTERVAL = 3000; // 3 sec
    private static final int FATEST_INTERVAL = 3000; // 3 sec
    private static final int DISPLACEMENT = 5; // 1 meters
    public static LatLng latLng;
    private GoogleApiClient mGoogleApiClient;

    @Nullable
    private AppCompatActivity activity;

    private Context appContext;

    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private boolean isFreshLocation = true;

    private LocationListener locationListener;


    @Inject
    public LocationManager() {

    }


    @Nullable
    public Location getLastLocation() {
        return mLastLocation;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void triggerLocation(LocationListener locationListener) {
        this.locationListener = locationListener;
        init();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean requestPermission() {

        if (activity != null) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CHECK_PERMISSION);
            } else if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CHECK_PERMISSION);
            } else if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CHECK_PERMISSION);
            } else {
                return true;
            }
            return false;
        } else return false;
    }

    private void init() {
        if (checkPlayServices()) {

            if (Build.VERSION.SDK_INT >= 23 && requestPermission()) {
                connectToClient();
            } else {
                connectToClient();
            }

        } else {

            if (locationListener != null)
                locationListener.onFail(LocationListener.Status.NO_PLAY_SERVICE);
        }
    }

    private void connectToClient() {
        buildGoogleApiClient();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }


    /**
     * Creating google api client object
     */
    public synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(activity == null ? appContext : activity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApiIfAvailable(LocationServices.API).build();

    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters

    }


    /**
     * Starting the location updates
     */

    public void startLocationUpdates() {

        if (!checkPermission()) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }





    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }


    /**
     * Method to display the location on UI
     */

    public void getLocation() {


        if (isFreshLocation) {

            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            startLocationUpdates();


        } else {

            if (!checkPermission()) {

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (mLastLocation != null) {
                    double latitude = mLastLocation.getLatitude();
                    double longitude = mLastLocation.getLongitude();
                    Log.e("LAST Location ", +latitude + " : " + longitude);
                    if (locationListener != null)
                        locationListener.onLocationAvailable(new LatLng(latitude, longitude));
                } else {
                    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    startLocationUpdates();
                }
            }

        }


    }

    private boolean checkPermission() {
        if (activity != null && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return true;
        }
        return false;
    }


    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {

        if (activity != null) {
            GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
            int result = googleAPI.isGooglePlayServicesAvailable(activity);
            if (result != ConnectionResult.SUCCESS) {
                if (googleAPI.isUserResolvableError(result)) {
                    googleAPI.getErrorDialog(activity, result,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                }

                return false;
            }
            return true;
        } else return false;
    }

    public void checkLocationEnable() {

        LocationSettingsRequest.Builder locationSettingsRequestBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        locationSettingsRequestBuilder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequestBuilder.build());
        result.setResultCallback(this);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("Location Manager", "onConnected");
        createLocationRequest();
        checkLocationEnable();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("Location Manager", "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.e("Location Manager", "onConnectionFailed ");
    }

    @Override
    public void onLocationChanged(Location location) {

        // stopLocationUpdates();

        /// mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        mLastLocation = location;


        Log.e("Location Manager", "onLocationChanged " + location.getLatitude() + " : " + location.getLongitude());

        if (locationListener != null)

            locationListener.onLocationAvailable(new LatLng(location.getLatitude(), location.getLongitude()));

    }

    public void stop() {
        locationListener = null;
        stopLocationUpdates();
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }


    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

        Status status = locationSettingsResult.getStatus();
        Log.e("Location Manager", "Location Setting " + status.hasResolution());
        if (status.hasResolution()) {
            Toast.makeText(activity, "Please Enable the Location service ", Toast.LENGTH_SHORT).show();
            try {
                status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }

        } else {
            getLocation();
        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == Activity.RESULT_OK) {
                getLocation();
            } else {

                if (locationListener != null)
                    locationListener.onFail(LocationListener.Status.DENIED_LOCATION_SETTING);
            }


        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CHECK_PERMISSION) {

            // We have requested multiple permissions for contacts, so all of them need to be
            // checked.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                connectToClient();
            } else {

                if (locationListener != null)
                    locationListener.onFail(LocationListener.Status.PERMISSION_DENIED);

            }

        }
    }


    public interface LocationListener {
        void onLocationAvailable(LatLng latLng);

        void onFail(Status status);

        enum Status {
            PERMISSION_DENIED, NO_PLAY_SERVICE, DENIED_LOCATION_SETTING
        }
    }
}