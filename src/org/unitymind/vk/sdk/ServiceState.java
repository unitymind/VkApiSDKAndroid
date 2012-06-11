package org.unitymind.vk.sdk;

import android.os.Bundle;
import android.util.Log;

public class ServiceState {
    private final String TAG = ServiceState.class.getName();

    public boolean isInit = false;
    public boolean registered;
    public boolean hasNetwork;

    public void update(Bundle state) {
        registered = state.getBoolean("registered", false);
        hasNetwork = state.getBoolean("hasNetwork", false);
        isInit = true;

        Log.d(TAG, "Service state update: " + toString());
    }

    @Override
    public String toString() {
        return String.format("IsInit: %s. registered: %s. hasNetwork: %s", isInit, registered, hasNetwork);
    }

}
