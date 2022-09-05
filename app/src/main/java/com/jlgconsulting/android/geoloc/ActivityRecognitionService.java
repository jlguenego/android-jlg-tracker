package com.jlgconsulting.android.geoloc;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.Task;

public class ActivityRecognitionService {

    final static private String TAG = "JLGModule.ActivityRecognitionService";

    final static public String TRANSITIONS_RECEIVER_ACTION =
            "com.jlgconsulting.android.geoloc.event.TRANSITIONS_RECEIVER_ACTION";

    final private Context context;

    private PendingIntent pendingIntent;
    private DetectedActivity detectedActivity = new DetectedActivity(DetectedActivity.UNKNOWN, 0);

    public ActivityRecognitionService(Context context) {
        this.context = context;
        Log.d(TAG, "new ActivityRecognitionService with context = " + context);
    }

    public void start() {
        try {
            Intent intent = new Intent(TRANSITIONS_RECEIVER_ACTION);
            intent.setClass(context, GeolocForegroundService.class);
            pendingIntent = PendingIntent.getForegroundService(context, 0, intent, 0);

            Task<Void> task =
                    ActivityRecognition.getClient(context)
                            .requestActivityUpdates(5000, pendingIntent);
            task.addOnSuccessListener(
                    result -> Log.d(TAG, "Activity Detector Api was successfully registered."));
            task.addOnFailureListener(
                    e -> Log.e(TAG, "Activity Detector Api could NOT be registered: " + e));
        } catch (SecurityException e) {
            Log.e(TAG, "e = " + e);
        }
    }

    public void stop() {
        Log.d(TAG, "Stopping activity detector");
        try {
            ActivityRecognition.getClient(context).removeActivityUpdates(pendingIntent)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Activity Detector Api was successfully unregistered."))
                    .addOnFailureListener(e -> Log.e(TAG, "Transitions could not be unregistered: " + e));
        } catch (SecurityException e) {
            Log.e(TAG, "e: " + e);
        }
    }

    public static String toActivityString(int activity) {
        switch (activity) {
            case DetectedActivity.STILL:
                return "still";
            case DetectedActivity.IN_VEHICLE:
                return "in_vehicle";
            case DetectedActivity.ON_BICYCLE:
                return "on_bicycle";
            case DetectedActivity.WALKING:
                return "walking";
            case DetectedActivity.ON_FOOT:
                return "on_foot";
            case DetectedActivity.RUNNING:
                return "running";
            case DetectedActivity.TILTING:
                return "tilting";
            case DetectedActivity.UNKNOWN:
                return "unknown";
            default:
                return "other";
        }
    }

    public void update(Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action = " + action);

        ActivityRecognitionResult activityRecognitionResult = ActivityRecognitionResult.extractResult(intent);
        if (activityRecognitionResult == null) {
            Log.d(TAG, "no result in intent.");
        } else {
            this.detectedActivity = activityRecognitionResult.getMostProbableActivity();
            Log.d(TAG, "activityStr = " + toActivityString(this.detectedActivity.getType()));
        }
    }

    public DetectedActivity getDetectedActivity() {
        return this.detectedActivity;
    }
}
