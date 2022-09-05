package com.jlgconsulting.android.geoloc.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DeviceInfo implements Parcelable {
    public String deviceName;
    public List<String> imeiList;
    public String model;
    public String display;
    public String uniqueId;
    public String systemName;
    public String systemVersion;
    public String deviceId;
    public String readableVersion;
    public String macAddress;
    public String serialNumber;
    public String manufacturer;

    public DeviceInfo() {}

    protected DeviceInfo(Parcel in) {
        deviceName = in.readString();
        imeiList = in.createStringArrayList();
        model = in.readString();
        display = in.readString();
        uniqueId = in.readString();
        systemName = in.readString();
        systemVersion = in.readString();
        deviceId = in.readString();
        readableVersion = in.readString();
        macAddress = in.readString();
        serialNumber = in.readString();
        manufacturer = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceName);
        dest.writeStringList(imeiList);
        dest.writeString(model);
        dest.writeString(display);
        dest.writeString(uniqueId);
        dest.writeString(systemName);
        dest.writeString(systemVersion);
        dest.writeString(deviceId);
        dest.writeString(readableVersion);
        dest.writeString(macAddress);
        dest.writeString(serialNumber);
        dest.writeString(manufacturer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DeviceInfo> CREATOR = new Creator<DeviceInfo>() {
        @Override
        public DeviceInfo createFromParcel(Parcel in) {
            return new DeviceInfo(in);
        }

        @Override
        public DeviceInfo[] newArray(int size) {
            return new DeviceInfo[size];
        }
    };
}
