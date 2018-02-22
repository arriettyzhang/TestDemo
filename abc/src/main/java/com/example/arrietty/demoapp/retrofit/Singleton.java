package com.example.arrietty.demoapp.retrofit;

/**
 * Created by asus on 2017/12/18.
 */

public abstract class Singleton<T, P> {
    private volatile T mInstance;
    protected abstract T create(P p);
    public final T get(P p) {
        if (mInstance == null) {
            synchronized (this) {
                if (mInstance == null) {
                    mInstance = create(p);
                }
            }
        }
        return mInstance;
    }
}
