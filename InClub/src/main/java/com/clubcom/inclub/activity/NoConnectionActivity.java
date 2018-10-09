package com.clubcom.inclub.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.clubcom.ccframework.activity.BaseActivity;
import com.clubcom.communicationframework.model.account.UserNetworkObject;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.util.LogReporter;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by adamwalter3 on 7/1/16.
 */
public class NoConnectionActivity extends BaseActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected int getNavBarBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.nav_bar_bg_color);
    }

    @Override
    protected int getNavBarIconColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.nav_bar_icon_color);
    }

    @Override
    protected int getNavBarTextColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.nav_bar_text_color);
    }

    @Override
    public UserNetworkObject getUserObject() {
        return MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation();
    }

    @Override
    protected void logOut() {

    }

    @Override
    protected void launchMyAccount() {

    }

    @Override
    protected void updateUserObject(UserNetworkObject userNetworkObject) {

    }

    @Override
    public void writeLogEntry(String log) {
        LogReporter.reportLog(mBaseActivity, log);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        View view = LayoutInflater.from(this).inflate(R.layout.no_connection_activity_layout, vContentArea, false);
        vContentArea.addView(view);
    }
}
