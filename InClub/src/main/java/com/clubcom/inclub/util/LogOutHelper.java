package com.clubcom.inclub.util;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.clubcom.ccframework.activity.BaseActivity;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.activity.TabletLogInActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by adamwalter3 on 9/29/16.
 */

public class LogOutHelper {
    public static void doLogOut(GoogleApiClient googleApiClient, BaseActivity baseActivity) {
        if (MainApplication.sUserCredential != null) {
            if (googleApiClient != null && googleApiClient.isConnected()) {
                Auth.CredentialsApi.delete(googleApiClient, MainApplication.sUserCredential).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status != null) {
                            if (status.isSuccess()) {
                                // Credential was deleted successfully

                            }
                        }
                    }
                });
            }
        }

        MainApplication.sTabletLogInObject = null;
        Intent i = new Intent(baseActivity, TabletLogInActivity.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        baseActivity.startActivity(i);
    }
}
