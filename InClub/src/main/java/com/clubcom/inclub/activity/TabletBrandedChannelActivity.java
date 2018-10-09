package com.clubcom.inclub.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.clubcom.ccframework.FrameworkApplication;
import com.clubcom.ccframework.activity.BrandedChannelActivity;
import com.clubcom.ccframework.fragment.MenuFragment;
import com.clubcom.ccframework.fragment.brandedchannel.BrandedChannelMenuFragment;
import com.clubcom.ccframework.fragment.brandedchannel.BrandedChannelSelfScrollingMenuFragment;
import com.clubcom.ccframework.fragment.video.ExoPlayerVideoFragment;
import com.clubcom.ccframework.model.VideoCollectionVideoObject;
import com.clubcom.ccframework.util.ActionHandler;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.ccframework.util.CCDisplay;
import com.clubcom.ccframework.util.GsonSingleton;
import com.clubcom.ccframework.util.UrlFactory;
import com.clubcom.communicationframework.model.ContentAction;
import com.clubcom.communicationframework.model.account.UserNetworkObject;
import com.clubcom.communicationframework.model.ads.AdOrder;
import com.clubcom.communicationframework.model.ads.ContentItem;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.fragment.TabletBrandedChannelMenuFragment;
import com.clubcom.inclub.fragment.TabletBrandedChannelSelfScrollingFragment;
import com.clubcom.inclub.util.AdOrderHelper;
import com.clubcom.inclub.util.GoogleApiHelper;
import com.clubcom.inclub.util.LogOutHelper;
import com.clubcom.inclub.util.LogReporter;
import com.clubcom.inclub.util.NetworkUtil;
import com.clubcom.inclub.util.TabletActionHandler;
import com.clubcom.projectile.JsonElementListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by adamwalter3 on 9/30/16.
 */

public class TabletBrandedChannelActivity extends BrandedChannelActivity {
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void superOnCreateCalled() {
        super.superOnCreateCalled();
        mGoogleApiClient = GoogleApiHelper.getGoogleAPIClient(mBaseActivity);
        showProgressDialog("Loading", "Loading");
    }

    @Override
    public void getBrandedChannelInfo() {
        BackEnd.getAdByOrder(mBaseActivity, mOrderId, new JsonElementListener() {
            @Override
            public void onResponse(JsonElement jsonElement) {

                AdOrder adOrder = GsonSingleton.getInstance().getGson().fromJson(jsonElement, AdOrder.class);
                if (adOrder != null) {
                    mBranding = AdOrderHelper.getBrandedChannelFromAdOrder(adOrder);
                    onBrandedChannelInfoReceived(mBranding);
                } else {
                    onBrandedChannelInfoReceived(null);
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                onBrandedChannelInfoReceived(null);
            }
        });
    }

    @Override
    public void loadImage(String filename, final View view) {
                final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if (view instanceof ImageView) {
                        ((ImageView) view).setImageBitmap(bitmap);
                    } else {
                        view.setBackground(new BitmapDrawable(mBaseActivity.getResources(), bitmap));
                    }
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            view.setTag(target);

            Picasso.with(mBaseActivity)
                    .load(UrlFactory.getRemotePath(filename, CCDisplay.getScreenDensity(mBaseActivity)))
                    .into(target);
    }

    @Override
    public void doAction(List<ContentAction> actionList, int contentId, ExoPlayerVideoFragment exoPlayerVideoFragment, MenuFragment menuFragment, ActionHandler.ActionListener listener) {
        new TabletActionHandler().doAction(mBaseActivity, actionList, exoPlayerVideoFragment, mVideoPlayBackListener, menuFragment, null, null, listener);
    }

    @Override
    public void saveUser(UserNetworkObject userNetworkObject) {
        BackEnd.saveUser(mBaseActivity, userNetworkObject, null);
    }

    @Override
    public void playVideo(VideoCollectionVideoObject videoCollectionVideoObject, ExoPlayerVideoFragment exoPlayerVideoFragment) {
        //TODO maybe populate more info
        ContentItem contentItem = new ContentItem();
        contentItem.setContentID(Integer.valueOf(videoCollectionVideoObject.getContentId()));
        exoPlayerVideoFragment.playHttpStream(contentItem, UrlFactory.getMediaUrl(videoCollectionVideoObject.getContentId()), mCurrentPosition);
    }

    @Override
    public BrandedChannelSelfScrollingMenuFragment getBrandedChannelOnDemandMenuFragment() {
        return new TabletBrandedChannelSelfScrollingFragment();
    }

    @Override
    public BrandedChannelMenuFragment getNewBrandedChannelMenuFragment() {
        return new TabletBrandedChannelMenuFragment();
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
