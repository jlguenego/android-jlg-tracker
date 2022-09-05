package com.jlgconsulting.android.geoloc;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;
import com.jlgconsulting.android.geoloc.parcelable.GeolocInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Api {

    final static private String TAG = "JLGModule.Api";
    final private static  MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    static private Api instance = null;

    final private ExecutorService executor = Executors.newFixedThreadPool(10);
    final private OkHttpClient client = new OkHttpClient();

    public static Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }
        return instance;
    }

    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                return "";
            }
            return responseBody.string();
        }
    }

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                return "";
            }
            return responseBody.string();
        }
    }

    public void test(Callback<String> callback) {
        executor.execute(() -> {
            try {
                String msg = this.get(Config.getInstance().getTestUrl());
                callback.run(null, msg);
            } catch (IOException e) {
                Log.d(TAG, "e = " + e);
                callback.run(e, null);
            }
        });
    }

    public void sendGeoloc(GeolocInfo geolocInfo, Callback<String> callback) {
        try {
            GeolocInfo newGeolocInfo = ParcelUtils.clone(geolocInfo);
            // merge the locationResult.
            List<Location> locations = DBUtils.getInstance().getLocations();
            DBUtils.getInstance().setLocations(new ArrayList<>());

            locations.add(geolocInfo.locationResult.getLastLocation());
            newGeolocInfo.locationResult = LocationResult.create(locations);
            JSONObject geolocJson = JSONUtils.GeolocInfoToJson(newGeolocInfo);

            String json = geolocJson.toString();

            executor.execute(() -> {
                try {
                    String msg = this.post(Config.getInstance().getGeolocUrl(), json);
                    callback.run(null, msg);
                } catch (IOException e) {
                    Log.d(TAG, "post failed. e = " + e);
                    DBUtils.getInstance().setLocations(locations);
                    callback.run(e, null);
                }
            });
        } catch (JSONException e) {
            Log.e(TAG, "e = " + e.getMessage());
        }
    }
}
