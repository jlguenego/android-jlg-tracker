package com.jlgconsulting.android.geoloc;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelUtils {

    public static <T extends Parcelable> T clone(T object) {
        Parcel p = Parcel.obtain();
        p.writeValue(object);
        p.setDataPosition(0);
        Class<? extends Parcelable> c = object.getClass();
        @SuppressWarnings("unchecked")
        T newObject = (T) p.readValue(c.getClassLoader());
        p.recycle();
        return newObject;
    }
}
