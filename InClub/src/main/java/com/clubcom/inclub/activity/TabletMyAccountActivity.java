package com.clubcom.inclub.activity;

import android.support.v4.content.ContextCompat;

import com.clubcom.ccframework.activity.MyAccountActivity;
import com.clubcom.ccframework.fragment.AccountMenuFragment;
import com.clubcom.ccframework.fragment.AccountSettingsFragment;
import com.clubcom.ccframework.fragment.PrivacyPolicyFragment;
import com.clubcom.ccframework.fragment.WorkoutMenuFragment;
import com.clubcom.ccframework.util.DemographicsFactory;
import com.clubcom.communicationframework.model.account.UserNetworkObject;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.fragment.TabletAccountMenuFragment;
import com.clubcom.inclub.fragment.TabletAccountSettingsFragment;
import com.clubcom.inclub.fragment.TabletPrivacyPolicyFragment;
import com.clubcom.inclub.fragment.TabletWorkoutMenuFragment;
import com.clubcom.inclub.util.GoogleApiHelper;
import com.clubcom.inclub.util.LogOutHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by adamwalter3 on 9/30/16.
 */

public class TabletMyAccountActivity extends MyAccountActivity {
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void superOnCreateCalled() {
        super.superOnCreateCalled();
        mGoogleApiClient = GoogleApiHelper.getGoogleAPIClient(mBaseActivity);
    }

    @Override
    public int getMenuBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_bg_color);
    }

    @Override
    public PrivacyPolicyFragment getNewPrivacyPolicyFragment() {
        return new TabletPrivacyPolicyFragment();
    }

    @Override
    public AccountSettingsFragment getNewAccountSettingsFragment() {
        return new TabletAccountSettingsFragment();
    }

    @Override
    public AccountMenuFragment getNewAccountMenuFragment() {
        return new TabletAccountMenuFragment();
    }

    @Override
    public WorkoutMenuFragment getNewWorkoutMenuFragment() {
        return new TabletWorkoutMenuFragment();
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
                && MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation() != null) {
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

    }

    @Override
    protected void updateUserObject(UserNetworkObject userNetworkObject) {
        MainApplication.sTabletLogInObject.getLogInNetworkObject().setUserInformation(userNetworkObject);
    }

    @Override
    public void writeLogEntry(String log) {

    }
}
