package com.clubcom.inclub;

import android.app.Application;

import com.clubcom.communicationframework.model.ContentAction;
import com.clubcom.communicationframework.model.account.TabletLogInObject;
import com.clubcom.communicationframework.model.account.UserNetworkObject;
import com.clubcom.communicationframework.model.ads.AdOrder;
import com.clubcom.communicationframework.model.ads.ContentItem;
import com.clubcom.communicationframework.model.ads.PlaybackListItem;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by adamwalter3 on 7/1/16.
 */
public class MainApplication extends Application {
    public static TabletLogInObject sTabletLogInObject = null;
    public static Credential sUserCredential;
    public static boolean sIsDemoMode = false;
    public static long sPrerollPlayedTime = 0;

    public static boolean isDemo() {
        return sIsDemoMode;
    }

    public static String sBaseUrl;
    public static String sStreamUrl;

    public static List<ContentItem> sMidrolls;
    public static int sMidrollIndex = 0;

    public static HashMap<Integer, AdOrder> sBrandedChannels = new HashMap<>();

    public static List<ContentItem> sBanners;
    public static GoogleApiClient sGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();

        setBaseUrl();
    }

    protected void setBaseUrl() {
        sBaseUrl = getBaseContext().getResources().getString(R.string.api_url);
        sStreamUrl = getBaseContext().getResources().getString(R.string.transfer_url);
    }

    public static void incrementMidrollIndex() {
        sMidrollIndex++;
        if (sMidrollIndex >= sMidrolls.size()) {
            sMidrollIndex = 0;
        }
    }
}
