package com.jlgconsulting.android.geoloc.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationResult;

public class GeolocInfo implements Parcelable {
    public LocationResult locationResult;
    public DetectedActivity detectedActivity;
    public BatteryInfo batteryInfo;
    public DeviceInfo deviceInfo;

    public GeolocInfo(LocationResult locationResult, DetectedActivity detectedActivity, BatteryInfo batteryInfo, DeviceInfo deviceInfo) {
        this.locationResult = locationResult;
        this.detectedActivity = detectedActivity;
        this.batteryInfo = batteryInfo;
        this.deviceInfo = deviceInfo;
    }

    protected GeolocInfo(Parcel in) {
        locationResult = in.readParcelable(LocationResult.class.getClassLoader());
        detectedActivity = in.readParcelable(DetectedActivity.class.getClassLoader());
        batteryInfo = in.readParcelable(BatteryInfo.class.getClassLoader());
        deviceInfo = in.readParcelable(DeviceInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(locationResult, flags);
        dest.writeParcelable(detectedActivity, flags);
        dest.writeParcelable(batteryInfo, flags);
        dest.writeParcelable(deviceInfo, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GeolocInfo> CREATOR = new Creator<GeolocInfo>() {
        @Override
        public GeolocInfo createFromParcel(Parcel in) {
            return new GeolocInfo(in);
        }

        @Override
        public GeolocInfo[] newArray(int size) {
            return new GeolocInfo[size];
        }
    };
}
