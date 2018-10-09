package com.clubcom.inclub.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.clubcom.ccframework.FrameworkApplication;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.util.NetworkUtil;

/**
 * Created by adamwalter3 on 7/1/16.
 */
public class SplashScreenActivity extends FragmentActivity {
    protected Handler mStartHandler;
    protected Runnable mStartRunnable;
    protected ConnectivityManager mConnectivityManager;
    protected WifiManager mWifiManager;

    protected boolean mWifiStateChecked = false;
    protected boolean mWifiInitialized = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        if (mStartHandler == null) {
            mStartHandler = new Handler();
        }

        if (mStartRunnable == null) {
            mStartRunnable = new Runnable() {
                @Override
                public void run() {
                    if (isReady()) {
                        startApp();
                        finish();
                    } else {
                        System.out.println("Waiting...");
                        new Handler().postDelayed(mStartRunnable, 500);
                    }
                }
            };
        }

        mStartHandler.post(mStartRunnable);
        boolean success = NetworkUtil.checkConnections(this, new NetworkUtil.ConnectionCheckComplete() {
            @Override
            public void complete() {
                mWifiInitialized = true;
            }
        });

        if (success) {
            mWifiStateChecked = true;
        } else {
            //TODO Request Permissions
        }
    }

    private void startApp() {
        if (FrameworkApplication.WIFI_CONNECTED_CLUBCOM) {
            System.out.println("Starting Log In");
            if (MainApplication.sTabletLogInObject == null
                    || MainApplication.sTabletLogInObject.getLogInNetworkObject() == null
                    || MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation() == null) {
                startActivity(new Intent(this, TabletLogInActivity.class));
            } else if (System.currentTimeMillis() - MainApplication.sPrerollPlayedTime > 90 * 60 * 1000) {
                startActivity(new Intent(this, TabletPrerollActivity.class));
            } else{
                startActivity(new Intent(this, TabletLaunchMenuActivity.class));
            }
        } else if (!FrameworkApplication.NETWORK_CONNECTED) {
            System.out.println("Starting No Conn");
            startActivity(new Intent(this, TabletLogInActivity.class));
            //startActivity(new Intent(this, NoConnectionActivity.class));
        } else {
            startActivity(new Intent(this, TabletLogInActivity.class));
            //TODO startActivity(new Intent(this, LimitedConnectivity.class));
        }

        finish();
    }

    public boolean isReady() {
        return mWifiStateChecked && mWifiInitialized;
    }
}
