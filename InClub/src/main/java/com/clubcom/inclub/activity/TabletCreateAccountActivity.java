package com.clubcom.inclub.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.android.volley.VolleyError;
import com.clubcom.ccframework.FrameworkApplication;
import com.clubcom.ccframework.activity.CreateAccountActivity;
import com.clubcom.ccframework.fragment.createaccount.EmailFragment;
import com.clubcom.ccframework.fragment.createaccount.SummaryFragment;
import com.clubcom.ccframework.fragment.createaccount.UserIdFragment;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.communicationframework.model.account.UserNetworkObject;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.fragment.createaccount.TabletEmailFragment;
import com.clubcom.inclub.fragment.createaccount.TabletSummaryFragment;
import com.clubcom.inclub.fragment.createaccount.TabletUserIdFragment;
import com.clubcom.inclub.util.LogOutHelper;
import com.clubcom.inclub.util.LogReporter;
import com.clubcom.inclub.util.NetworkUtil;
import com.clubcom.projectile.StringListener;

/**
 * Created by adamwalter on 10/31/14.
 */
public class TabletCreateAccountActivity extends CreateAccountActivity {
    @Override
    protected int getNavBarBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.nav_bar_bg_color);
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
        //ignored - no Menu
    }

    @Override
    protected void launchMyAccount() {
        //ignored - no Menu
    }

    @Override
    public UserIdFragment getNewUserIdFragment() {
        return new TabletUserIdFragment();
    }

    @Override
    public EmailFragment getNewEmailFragment() {
        return new TabletEmailFragment();
    }

    @Override
    public SummaryFragment getNewSummaryFragment() {
        return new TabletSummaryFragment();
    }

    @Override
    public int getMenuBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_bg_color);
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
    public int getMenuTextColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_text_color);
    }

    @Override
    public void saveAccount(final UserNetworkObject userNetworkObject) {
        BackEnd.saveUser(mBaseActivity, userNetworkObject, new StringListener() {
            @Override
            public void onResponse(String s) {
                if (s.equals("{\"SaveUserResult\":1}")) {
                    showProgressDialog("Creating Account", "Logging In...");
                    Intent logInActivity = new Intent(mBaseActivity, TabletLogInActivity.class);
                    logInActivity.putExtra(TabletLogInActivity.EXTRA_USERNAME, userNetworkObject.getUserName());
                    logInActivity.putExtra(TabletLogInActivity.EXTRA_PASSWORD, userNetworkObject.getPassword());
                    mBaseActivity.startActivity(logInActivity);
                    finish();
                } else {
                    showActivityAlertDialog(getString(R.string.error), getString(R.string.unknown_error), null, null, getString(R.string.ok), null, true);
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                showActivityAlertDialog(getString(R.string.error), getString(R.string.unknown_error), null, null, getString(R.string.ok), null, true);
            }
        });
    }

    @Override
    protected void updateUserObject(UserNetworkObject userNetworkObject) {
        MainApplication.sTabletLogInObject.getLogInNetworkObject().setUserInformation(userNetworkObject);
    }

    @Override
    public void writeLogEntry(String log) {
        LogReporter.reportLog(mBaseActivity, log);
    }

    @Override
    protected void onAppToForeground() {
        super.onAppToForeground();

        NetworkUtil.checkConnections(mBaseActivity, new NetworkUtil.ConnectionCheckComplete() {
            @Override
            public void complete() {
                if ((FrameworkApplication.NETWORK_STATE_CURRENT && FrameworkApplication.WIFI_CONNECTED_CLUBCOM) || (FrameworkApplication.NETWORK_STATE_CURRENT && FrameworkApplication.CORPORATE_CALLS_AVAILABLE && MainApplication.sIsDemoMode)) {
                    //do nothing
                } else {
                    finish();
                }
            }
        });
    }
}
