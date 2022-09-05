package com.jlgconsulting.android.geoloc.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class BatteryInfo implements Parcelable {
    public boolean isCharging;
    public float level;

    public BatteryInfo(boolean isCharging, float level) {
        this.isCharging = isCharging;
        this.level = level;
    }

    protected BatteryInfo(Parcel in) {
        isCharging = in.readByte() != 0;
        level = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isCharging ? 1 : 0));
        dest.writeFloat(level);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BatteryInfo> CREATOR = new Creator<BatteryInfo>() {
        @Override
        public BatteryInfo createFromParcel(Parcel in) {
            return new BatteryInfo(in);
        }

        @Override
        public BatteryInfo[] newArray(int size) {
            return new BatteryInfo[size];
        }
    };
}
