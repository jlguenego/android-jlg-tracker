package com.jlgconsulting.android.geoloc;

import android.app.Activity;

public class Config {
    private String geolocUrl = "http://localhost:3000/api/geoloc";
    private String testUrl = "http://localhost:3000/api/test";
    private long delay = 5000;
    private boolean debugMode = true;
    private String notificationTitle = "JLG Tracker";
    private String notificationText = "A great app to send geoloc!";
    private String notificationTicker = "JLG Tracker: a great app to send geoloc!";
    private int notificationIcon = -1;
    private Class<? extends Activity> activityClass = null;
    private String authenticationToken = "123soleil!";

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }



    public Class<? extends Activity> getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class<? extends Activity> activityClass) {
        this.activityClass = activityClass;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    private int sound = -1;

    public int getNotificationIcon() {
        return notificationIcon;
    }

    public void setNotificationIcon(int notificationIcon) {
        this.notificationIcon = notificationIcon;
    }

    public String getBroadCastUri() {
        return broadCastUri;
    }

    public void setBroadCastUri(String broadCastUri) {
        this.broadCastUri = broadCastUri;
    }

    private String broadCastUri = null;

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getNotificationTicker() {
        return notificationTicker;
    }

    public void setNotificationTicker(String notificationTicker) {
        this.notificationTicker = notificationTicker;
    }

    private static Config instance = null;

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public String getGeolocUrl() {
        return geolocUrl;
    }

    public void setGeolocUrl(String geolocUrl) {
        this.geolocUrl = geolocUrl;
    }

    public String getTestUrl() {
        return testUrl;
    }

    public void setTestUrl(String testUrl) {
        this.testUrl = testUrl;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
}
