package com.jlgconsulting.android.geoloc;

public interface Callback<T> {
    void run(Throwable throwable, T t);
}
