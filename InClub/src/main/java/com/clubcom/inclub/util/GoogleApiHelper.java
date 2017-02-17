package com.clubcom.inclub.util;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.clubcom.ccframework.activity.BaseActivity;
import com.clubcom.inclub.MainApplication;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by adamwalter3 on 9/28/16.
 */

public class GoogleApiHelper {
    public static GoogleApiClient getGoogleAPIClient(final BaseActivity baseActivity) {
        return new GoogleApiClient.Builder(baseActivity)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        baseActivity.onGoogleApiConnected();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        baseActivity.onGoogleApiConnectionSuspended();
                    }
                })
                .enableAutoManage(baseActivity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        baseActivity.onGoogleApiConnectionFailed();
                    }
                })
                .addApi(Auth.CREDENTIALS_API)
                .build();
    }

}
