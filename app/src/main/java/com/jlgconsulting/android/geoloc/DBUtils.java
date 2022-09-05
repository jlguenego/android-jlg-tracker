package com.jlgconsulting.android.geoloc;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class DBUtils {
    private static DBUtils instance;
    public static DBUtils getInstance() {
        if (instance == null) {
            instance = new DBUtils();
        }
        return instance;
    }

    private List<Location> locations = new ArrayList<>();
    public  List<Location> getLocations() {
        return locations;
    }

    public  void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
