package com.clubcom.inclub.util;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.VolleyError;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.ccframework.util.ClubcomLog;
import com.clubcom.inclub.MainApplication;
import com.clubcom.projectile.StringListener;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by adamwalter3 on 5/16/17.
 */

public class LogReporter {
    private final static int LOG_LENGTH = 10;
    public static void reportLog(Context ctx, String log) {
        System.out.println("Reporting Log " + log.split("\\|").length + ": " + log);
        String sessionId = "0000-0000-0000-0000-0000-0000-0000-0000-0000";
        if (MainApplication.sTabletLogInObject != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject() != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation() != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation().getSessionGuid() != null) {
            sessionId = MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation().getSessionGuid();
        }

        if (log.split("\\|").length == LOG_LENGTH) {
            BackEnd.reportInstantLog(ctx, sessionId, log, new StringListener() {
                @Override
                public void onResponse(String s) {
                    System.out.println("Log Send Response 200: " +s);
                }

                @Override
                public void onError(VolleyError volleyError) {

                }
            });
        }
    }

    private static String getContentId(String[] parts) {
        return parts[3];
    }

    private static String getOrderId(String[] parts) {
        return parts[4];
    }

    private static String getPlaybackTime(String[] parts) {
        return parts[5];
    }

    private static String getAdLength(String[] parts) {
        return parts[6];
    }

}
