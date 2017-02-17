package com.clubcom.inclub.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.android.volley.VolleyError;
import com.clubcom.ccframework.activity.LaunchMenuActivity;
import com.clubcom.ccframework.activity.PrerollActivity;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.ccframework.util.GsonSingleton;
import com.clubcom.communicationframework.model.account.UserNetworkObject;
import com.clubcom.communicationframework.model.ads.ContentItem;
import com.clubcom.communicationframework.model.ads.OrderGroup;
import com.clubcom.communicationframework.model.ads.OrderGroupMap;
import com.clubcom.communicationframework.model.ads.PlaybackListItem;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.ccframework.util.UrlFactory;
import com.clubcom.projectile.JsonElementListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by adamwalter3 on 9/28/16.
 */

public class TabletPrerollActivity extends PrerollActivity {
    @Override
    public void getPreroll() {
        if (MainApplication.sTabletLogInObject != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject() != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject().getAdListData() != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject().getAdListData().containsKey(OrderGroupMap.KEY_PRE_ROLL_WITH_FOLLOWER)) {
            OrderGroup prerolls = MainApplication.sTabletLogInObject.getLogInNetworkObject().getAdListData().getPreRollWithFollowerGroup();

            if (prerolls.getPlaybackList() != null && !prerolls.getPlaybackList().isEmpty()) {
                ArrayList<ContentItem> prerollItems = new ArrayList<>(prerolls.getPlaybackList().values());
                Collections.sort(prerollItems, new ContentItem.ContentItemComparator());
                ContentItem contentItem = prerollItems.get(0);
                onPrerollReceived(contentItem);
            } else {
                onPrerollReceived(null);
            }
        } else {
            onPrerollReceived(null);
        }
    }

    @Override
    public void startNextActivity(ContentItem contentItem) {
        Intent intent = new Intent(mBaseActivity, TabletLaunchMenuActivity.class);
        if (contentItem != null) {
            intent.putExtra(LaunchMenuActivity.EXTRA_PREROLL_FOLLOWER, GsonSingleton.getInstance().getGson().toJson(contentItem));
        }

        startActivity(intent);
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
    public int getDarkerBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.nav_bar_bg_darker);
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
        //ignore no menu
    }

    @Override
    protected void launchMyAccount() {
        //ignore no menu
    }

    @Override
    public void playVideo(ContentItem contentItem) {
        mExoPlayerVideoFragment.playHttpStream(UrlFactory.getMediaUrl(contentItem), mCurrentPosition);
        MainApplication.sPrerollPlayedTime = System.currentTimeMillis();
    }

    @Override
    protected void updateUserObject(UserNetworkObject userNetworkObject) {
        MainApplication.sTabletLogInObject.getLogInNetworkObject().setUserInformation(userNetworkObject);
    }

    @Override
    public void writeLogEntry(String log) {

    }
}
