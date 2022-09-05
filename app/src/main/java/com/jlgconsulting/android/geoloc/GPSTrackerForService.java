package com.jlgconsulting.android.geoloc;

import android.app.Service;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.util.ArrayList;
import java.util.List;

public class GPSTrackerForService {
    final private static String TAG = "JLGModule.GPSTrackerForService";

    final private LocationCallback locationCallback;
    final private FusedLocationProviderClient fusedLocationProviderClient;
    final private LocationRequest locationRequest;
    final private Handler handler = new Handler(Looper.getMainLooper());

    private Location lastLocation;

    final private LocationCallback listener = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "listener started");
            lastLocation = locationResult.getLastLocation();
        }
    };

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Do the task...
            if (lastLocation != null) {
                List<Location> list = new ArrayList<>();
                list.add(lastLocation);
                LocationResult locationResult = LocationResult.create(list);
                locationCallback.onLocationResult(locationResult);
            }
            // setInterval
            handler.postDelayed(this, Config.getInstance().getDelay());
        }
    };

    public GPSTrackerForService(Service service, LocationCallback locationCallback) {
        this.locationCallback = locationCallback;

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(service);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(Config.getInstance().getDelay());
        locationRequest.setFastestInterval(Config.getInstance().getDelay());
        locationRequest.setMaxWaitTime(Config.getInstance().getDelay());
    }

    public void startListeningForLocation() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, listener, null);
        } catch (SecurityException e) {
            Log.d(TAG, "e = " + e);
        }

        // now run a setInterval, starting after 2sec.
        handler.postDelayed(runnable, 2000);
    }

    public void stop() {
        fusedLocationProviderClient.removeLocationUpdates(listener);
        handler.removeCallbacks(runnable);
    }
}
