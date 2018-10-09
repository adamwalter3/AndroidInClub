package com.clubcom.inclub.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.clubcom.ccframework.FrameworkApplication;
import com.clubcom.ccframework.activity.*;
import com.clubcom.ccframework.fragment.BaseFragment;
import com.clubcom.ccframework.fragment.MenuFragment;
import com.clubcom.ccframework.fragment.TvMenuFragment;
import com.clubcom.ccframework.fragment.video.ExoPlayerVideoFragment;
import com.clubcom.ccframework.util.ActionHandler;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.communicationframework.model.ContentAction;
import com.clubcom.communicationframework.model.account.UserNetworkObject;
import com.clubcom.communicationframework.model.tv.TvChannel;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.fragment.TabletTvMenuFragment;
import com.clubcom.inclub.util.BannerPlayer;
import com.clubcom.inclub.util.GoogleApiHelper;
import com.clubcom.inclub.util.LogOutHelper;
import com.clubcom.inclub.util.LogReporter;
import com.clubcom.inclub.util.NetworkUtil;
import com.clubcom.inclub.util.TabletActionHandler;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

/**
 * Created by adamwalter3 on 9/29/16.
 */

public class TabletTvActivity extends TvActivity {
    private GoogleApiClient mGoogleApiClient;
    private BaseFragment.ActionCallback mActionCallback;

    @Override
    protected void superOnCreateCalled() {
        super.superOnCreateCalled();
        mGoogleApiClient = GoogleApiHelper.getGoogleAPIClient(mBaseActivity);
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        mActionCallback = new BaseFragment.ActionCallback() {
            @Override
            public void doAction(BaseActivity baseActivity, List<ContentAction> actions, ExoPlayerVideoFragment exoPlayerVideoFragment, MenuFragment menuFragment, Bundle b, String logo, ActionHandler.ActionListener listener) {
                new TabletActionHandler().doAction(baseActivity, actions, exoPlayerVideoFragment, null, menuFragment, b, logo, listener);
            }
        };

        mExoPlayerVideoFragment.setActionCallback(mActionCallback);
//        mVLCVideoFragment.setActionCallback(mActionCallback);
    }

    @Override
    public void saveUser(UserNetworkObject userNetworkObject) {
        BackEnd.saveUser(mBaseActivity, userNetworkObject, null);
    }

    @Override
    public TvMenuFragment getNewTvMenuFragment() {
        return new TabletTvMenuFragment();
    }

    @Override
    public int getDarkerBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.nav_bar_bg_darker);
    }

    @Override
    public void playTvChannel(TvChannel tvChannel, ExoPlayerVideoFragment exoPlayerVideoFragment) {
        exoPlayerVideoFragment.playHttpStream(null, tvChannel.getURL());
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
        Intent intent = new Intent(mBaseActivity, TabletMyAccountActivity.class);
        startActivity(intent);
    }

    @Override
    protected void showNextBanner() {
        if ((mTablet)) {
            BannerPlayer.showNextBanner(mBaseActivity, mCurrentFragment);
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                BannerPlayer.showNextBanner(mBaseActivity, vBannerView, null, mExoPlayerVideoFragment, mMenuFragment, mActionCallback);
            } else {
                vBannerView.setImageDrawable(null);
            }
        }
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
                    LogOutHelper.doLogOut(mGoogleApiClient, mBaseActivity);
                }
            }
        });
    }
}
