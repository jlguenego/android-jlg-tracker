package com.jlgconsulting.android.geoloc;

import android.location.Location;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationResult;
import com.jlgconsulting.android.geoloc.parcelable.BatteryInfo;
import com.jlgconsulting.android.geoloc.parcelable.DeviceInfo;
import com.jlgconsulting.android.geoloc.parcelable.GeolocInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class JSONUtils {

    public static JSONObject GeolocInfoToJson(GeolocInfo geolocInfo) throws JSONException {

        DetectedActivity detectedActivity = geolocInfo.detectedActivity;
        LocationResult locationResult = geolocInfo.locationResult;
        BatteryInfo batteryInfo = geolocInfo.batteryInfo;
        DeviceInfo deviceInfo = geolocInfo.deviceInfo;

        JSONObject geolocJson = new JSONObject();
        JSONArray locationJsonArray = new JSONArray();
        for (Location location : locationResult.getLocations()) {
            JSONObject locationJson = new JSONObject();
            Instant now = Instant.ofEpochMilli(location.getTime());
            locationJson.put("timestamp", now.toString());
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());
            locationJson.put("odometer", 1234);

            JSONObject activityJson = new JSONObject();
            activityJson.put("type", ActivityRecognitionService.toActivityString(detectedActivity.getType()));
            activityJson.put("confidence", detectedActivity.getConfidence());
            locationJson.put("activity", activityJson);

            JSONObject batteryJson = new JSONObject();
            batteryJson.put("is_charging", batteryInfo.isCharging);
            batteryJson.put("level", batteryInfo.level);
            locationJson.put("battery", batteryJson);

            JSONObject coordsJson = new JSONObject();
            coordsJson.put("latitude", location.getLatitude());
            coordsJson.put("longitude", location.getLongitude());
            coordsJson.put("accuracy", location.getAccuracy());
            coordsJson.put("speed", location.getSpeed());
            coordsJson.put("speed_accuracy", location.getSpeedAccuracyMetersPerSecond());
            coordsJson.put("heading", location.getBearing());
            coordsJson.put("heading_accuracy", location.getBearingAccuracyDegrees());
            coordsJson.put("altitude", location.getAltitude());
            coordsJson.put("altitude_accuracy", location.getVerticalAccuracyMeters());


            locationJson.put("coords", coordsJson);
            locationJson.put("localTimestamp", formatter.format(now) + " Paris");

            locationJsonArray.put(locationJson);
        }
        geolocJson.put("location", locationJsonArray);

        JSONObject deviceInfoJson = new JSONObject();
        deviceInfoJson.put("deviceName", deviceInfo.deviceName);
        deviceInfoJson.put("imeiList", new JSONArray(deviceInfo.imeiList));
        deviceInfoJson.put("model", deviceInfo.model);
        deviceInfoJson.put("display", deviceInfo.display);
        deviceInfoJson.put("uniqueId", deviceInfo.uniqueId);
        deviceInfoJson.put("systemName", deviceInfo.systemName);
        deviceInfoJson.put("systemVersion", deviceInfo.systemVersion);
        deviceInfoJson.put("deviceId", deviceInfo.deviceId);
        deviceInfoJson.put("readableVersion", deviceInfo.readableVersion);
        deviceInfoJson.put("macAddress", deviceInfo.macAddress);
        deviceInfoJson.put("serialNumber", deviceInfo.serialNumber);
        deviceInfoJson.put("manufacturer", deviceInfo.manufacturer);
        geolocJson.put("deviceInfo", deviceInfoJson);

        geolocJson.put("id", deviceInfo.deviceName.replaceAll(" ", "-"));
        geolocJson.put("auth_token", Config.getInstance().getAuthenticationToken());

        return geolocJson;
    }
}
