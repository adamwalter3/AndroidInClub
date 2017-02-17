package com.clubcom.inclub.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.clubcom.ccframework.activity.PersonalTrainerActivity;
import com.clubcom.ccframework.fragment.PersonalTrainerInfoFragment;
import com.clubcom.ccframework.fragment.PersonalTrainerMenuFragment;
import com.clubcom.communicationframework.model.account.UserNetworkObject;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.fragment.TabletPersonalTrainerInfoFragment;
import com.clubcom.inclub.fragment.TabletPersonalTrainerMenuFragment;
import com.clubcom.inclub.util.GoogleApiHelper;
import com.clubcom.inclub.util.LogOutHelper;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by adamwalter3 on 9/30/16.
 */

public class TabletPersonalTrainerActivity extends PersonalTrainerActivity {
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void superOnCreateCalled() {
        super.superOnCreateCalled();
        mGoogleApiClient = GoogleApiHelper.getGoogleAPIClient(mBaseActivity);
    }

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
        if (MainApplication.sTabletLogInObject != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject() != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject() != null) {
            return MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation();
        } else {
            return null;
        }
    }

    @Override
    protected void logOut() {
        LogOutHelper.doLogOut(mGoogleApiClient, mBaseActivity);
    }

    @Override
    protected void launchMyAccount() {
        Intent intent = new Intent(mBaseActivity, TabletMyAccountActivity.class);
        startActivity(intent);
    }

    @Override
    protected void updateUserObject(UserNetworkObject userNetworkObject) {
        MainApplication.sTabletLogInObject.getLogInNetworkObject().setUserInformation(userNetworkObject);
    }

    @Override
    public void writeLogEntry(String log) {

    }

    @Override
    public PersonalTrainerMenuFragment getNewPersonalTrainerMenuFragment() {
        return new TabletPersonalTrainerMenuFragment();
    }

    @Override
    public PersonalTrainerInfoFragment getNewPersonalTrainerInfoFragment() {
        return new TabletPersonalTrainerInfoFragment();
    }
}
