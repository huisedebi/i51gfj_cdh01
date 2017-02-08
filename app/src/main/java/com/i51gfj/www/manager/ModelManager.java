package com.i51gfj.www.manager;

import android.content.Context;

public class ModelManager {

    private static ModelManager instance;
    private Context context;
    public static boolean mNetWorkState;

    static {
        instance = new ModelManager();
    }

    private ModelManager() {}

    public static ModelManager getInstance() {
        return instance;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public synchronized void initialize(Context context) {
        if (this.context == null) {
            this.context = context;
        }
    }

}
