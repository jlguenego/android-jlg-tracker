package com.jlgconsulting.android.geoloc;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.jlgconsulting.android.geoloc.parcelable.DeviceInfo;
import com.jlgconsulting.android.geoloc.parcelable.GeolocInfo;

public class GeolocForegroundService extends Service {
    public static final String TAG = "JLGModule.MyGPSForegroundService";

    private static final int ONGOING_NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "ForegroundServiceChannel";

    private static GeolocForegroundService instance = null;

    private GPSTrackerForService gpsTrackerForService;

    private final ActivityRecognitionService activityRecognitionService =
            new ActivityRecognitionService(this);

    private DeviceInfo deviceInfo;

    private SoundPool soundPool;
    private int debugSound;

    private PowerManager.WakeLock wakeLock;

    final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "service location changed");
            if (Config.getInstance().isDebugMode()) {
                soundPool.play(debugSound, 1, 1, 1, 0, 1);
            }

            BatteryService battery = new BatteryService(GeolocForegroundService.this);
            GeolocInfo geolocInfo = new GeolocInfo(locationResult, activityRecognitionService.getDetectedActivity(), battery.getBatteryInfo(), deviceInfo);

            // send event before http call.
            if (Config.getInstance().getBroadCastUri() != null) {
                Intent intent = new Intent();
                intent.setAction(Config.getInstance().getBroadCastUri());
                intent.putExtra("geolocInfo", geolocInfo);
                intent.putExtra("eventName", "beforeGeoloc");
                sendBroadcast(intent);
            }


            Api.getInstance().sendGeoloc(geolocInfo, (err, result) -> {
                boolean httpResult;
                if (err != null) {
                    Log.e(TAG, "err" + err);
                    httpResult = false;
                } else {
                    Log.d(TAG, "result = " + result);
                    httpResult = true;
                }
                // in all case we send a geoloc event an after http call
                if (Config.getInstance().getBroadCastUri() != null) {
                    Intent intent = new Intent();
                    intent.setAction(Config.getInstance().getBroadCastUri());
                    intent.putExtra("geolocInfo", geolocInfo);
                    intent.putExtra("eventName", "afterGeoloc");
                    intent.putExtra("httpResult", httpResult);
                    sendBroadcast(intent);
                }
            });

        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        NotificationChannel serviceChannel = new NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(serviceChannel);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand service.id = " + this);
        Log.d(TAG, "onStartCommand start with intent" + intent.toString());
        String action = intent.getAction();
        Log.d(TAG, "onStartCommand intent action = " + action);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.LOCATION_MODE_NO_CHANGE | PowerManager.PARTIAL_WAKE_LOCK,
                "MyGPSForegroundService::MyWakelockTag");
        wakeLock.acquire(12 * 60 * 60 * 1000L /* 12 hours */);

        DeviceInfo di = intent.getParcelableExtra("deviceInfo");
        if (di != null) {
            deviceInfo = di;
        }


        if (action != null && action.equals(ActivityRecognitionService.TRANSITIONS_RECEIVER_ACTION)) {
            Log.d(TAG, "new detected activity received");
            activityRecognitionService.update(intent);

            // send the activity to the receiver
            if (Config.getInstance().getBroadCastUri() != null) {
                Intent activityIntent = new Intent();
                activityIntent.setAction(Config.getInstance().getBroadCastUri());
                activityIntent.putExtra("detectedActivity", activityRecognitionService.getDetectedActivity());
                sendBroadcast(activityIntent);
            }

            return START_NOT_STICKY;
        }

        this.soundInit();

        createNotificationChannel();

        Intent notificationIntent = new Intent(this, Config.getInstance().getActivityClass());
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE);

        Notification notification =
                new Notification.Builder(this, CHANNEL_ID)
                        .setContentTitle(Config.getInstance().getNotificationTitle())
                        .setContentText(Config.getInstance().getNotificationText())
                        .setSmallIcon(Config.getInstance().getNotificationIcon())
                        .setContentIntent(pendingIntent)
                        .setTicker(Config.getInstance().getNotificationTicker())
                        .build();

        startForeground(ONGOING_NOTIFICATION_ID, notification);
        GeolocForegroundService.instance = this;

        // geoloc
        gpsTrackerForService = new GPSTrackerForService(this, this.locationCallback);
        gpsTrackerForService.startListeningForLocation();

        // activities
        activityRecognitionService.start();

        // returns the status
        // of the program
        return START_NOT_STICKY;
    }

    // execution of the service will
    // stop on calling this method
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        gpsTrackerForService.stop();
        activityRecognitionService.stop();
        stopForeground(true);
        stopSelf();
        if (GeolocForegroundService.instance == null) {
            return;
        }
        GeolocForegroundService.instance = null;
        wakeLock.release();
    }

    private void soundInit() {
        Context context = this.getApplicationContext();
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
        debugSound = soundPool.load(context, Config.getInstance().getSound(), 1);
    }

    public static boolean isMyServiceRunning() {
        return instance != null;
    }
}