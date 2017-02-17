package com.clubcom.inclub.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.clubcom.ccframework.activity.BaseActivity;
import com.clubcom.ccframework.activity.LaunchMenuActivity;
import com.clubcom.ccframework.fragment.BaseFragment;
import com.clubcom.ccframework.fragment.LaunchScreenMenuFragment;
import com.clubcom.ccframework.fragment.MenuFragment;
import com.clubcom.ccframework.fragment.video.ExoPlayerVideoFragment;
import com.clubcom.ccframework.util.ActionHandler;
import com.clubcom.ccframework.util.CCDisplay;
import com.clubcom.ccframework.util.FileManager;
import com.clubcom.ccframework.util.UrlFactory;
import com.clubcom.communicationframework.model.ContentAction;
import com.clubcom.communicationframework.model.account.ScreenIdentity;
import com.clubcom.communicationframework.model.account.UserNetworkObject;
import com.clubcom.communicationframework.model.ads.AdListPlayedMessageObject;
import com.clubcom.communicationframework.model.ads.ContentItem;
import com.clubcom.communicationframework.model.ads.OrderGroupMap;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.fragment.TabletLaunchScreenMenuFragment;
import com.clubcom.inclub.util.GoogleApiHelper;
import com.clubcom.inclub.util.LogOutHelper;
import com.clubcom.inclub.util.MidrollPlayer;
import com.clubcom.inclub.util.TabletActionHandler;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.crash.FirebaseCrash;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by adamwalter3 on 9/29/16.
 */

public class TabletLaunchMenuActivity extends LaunchMenuActivity {
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void superOnCreateCalled() {
        super.superOnCreateCalled();
        mGoogleApiClient = GoogleApiHelper.getGoogleAPIClient(mBaseActivity);

        if (MainApplication.sTabletLogInObject != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject() != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject().getAdListData() != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject().getAdListData().containsKey(OrderGroupMap.KEY_MID_ROLL)) {

            if (MainApplication.sMidrolls == null || MainApplication.sMidrolls.isEmpty()) {
                MidrollPlayer.populateMidrolls();
            }
        }
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        if (mTablet) {
            mExoPlayerVideoFragment.setActionCallback(new BaseFragment.ActionCallback() {
                @Override
                public void doAction(BaseActivity baseActivity, List<ContentAction> actions, ExoPlayerVideoFragment exoPlayerVideoFragment, MenuFragment menuFragment, Bundle b, String logo, ActionHandler.ActionListener listener) {
                    new TabletActionHandler().doAction(baseActivity, actions, exoPlayerVideoFragment, mVideoPlaybackListener, menuFragment, b, logo, listener);
                    mPlaybackHandler.removeCallbacks(mPlaybackRunnable);
                }
            });
        }

        mMenuFragment.setActionCallback(new BaseFragment.ActionCallback() {
            @Override
            public void doAction(BaseActivity baseActivity, List<ContentAction> actions, ExoPlayerVideoFragment exoPlayerVideoFragment, MenuFragment menuFragment, Bundle b, String logo, ActionHandler.ActionListener listener) {
                new TabletActionHandler().doAction(baseActivity, actions, exoPlayerVideoFragment, mVideoPlaybackListener, menuFragment, b, logo, listener);
                mPlaybackHandler.removeCallbacks(mPlaybackRunnable);
            }
        });

        mLayoutBuilt = true;
    }

    @Override
    public LaunchScreenMenuFragment getNewLaunchScreenMenuFragment() {
        return new TabletLaunchScreenMenuFragment();
    }

    @Override
    public int getDarkerBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.nav_bar_bg_darker);
    }

    @Override
    public void loadImage(String filename, final View view) {
        Target target = new Target() {
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
        Picasso.with(mBaseActivity).load(UrlFactory.getRemotePath(filename, CCDisplay.getScreenDensity(mBaseActivity))).into(target);
    }

    @Override
    public void doAction(List<ContentAction> list, ExoPlayerVideoFragment exoPlayerVideoFragment, MenuFragment menuFragment, ActionHandler.ActionListener actionListener) {
        new TabletActionHandler().doAction(mBaseActivity, list, exoPlayerVideoFragment, mVideoPlaybackListener, menuFragment, null, null, actionListener);
    }

    @Override
    public ContentItem playNextMidroll(boolean increment) {
        return MidrollPlayer.playNextMidroll(mBaseActivity, mExoPlayerVideoFragment, increment);
    }

    @Override
    public void incrementMidrollIndex() {
        MainApplication.incrementMidrollIndex();
    }

    @Override
    public int getMidrollCount() {
        return MainApplication.sMidrolls.size();
    }

    @Override
    public void setPriority(ContentItem contentItem, double priority, AdListPlayedMessageObject adListPlayedMessageObject) {
        if (contentItem != null && contentItem.getKey() != null && MainApplication.sTabletLogInObject.getLogInNetworkObject().getAdListData().getMidRollGroup().getPlaybackList().containsKey(contentItem.getKey())) {
            MainApplication.sTabletLogInObject.getLogInNetworkObject().getAdListData().getMidRollGroup().getPlaybackList().get(contentItem.getKey())
                    .setPriority(priority);
        }
    }

    @Override
    public void hideBanner() {
        mExoPlayerVideoFragment.hideBanner();
    }

    @Override
    public void showDefaultStatic() {
        mExoPlayerVideoFragment.showStatic(R.drawable.launch_screen);
    }

    @Override
    public void showStatic(String fileName) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                mExoPlayerVideoFragment.showStatic(new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        mExoPlayerVideoFragment.getvStaticView().setTag(target);

        Picasso.with(mBaseActivity).load(UrlFactory.getRemotePath(fileName, CCDisplay.getScreenDensity(mBaseActivity))).into(target);
    }

    @Override
    public void showVideoPlaceholderStatic() {
        mExoPlayerVideoFragment.showStatic(R.drawable.launch_screen);
    }

    @Override
    public void startVideos() {
        if (MainApplication.sMidrolls != null) {
            if (mErrorCount < MainApplication.sMidrolls.size()) {
                mPlaybackHandler.postDelayed(mPlaybackRunnable, 5000);
            }
        }
    }

    protected Runnable mPlaybackRunnable = new Runnable() {
        @Override
        public void run() {
            playNextVideo(false);
            mExoPlayerVideoFragment.showBanner(ContextCompat.getDrawable(mBaseActivity, R.drawable.logo_banner));
        }
    };

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

    ExoPlayerVideoFragment.VideoPlaybackListener mVideoPlaybackListener = new ExoPlayerVideoFragment.VideoPlaybackListener() {
        @Override
        public void onVideoPrepared() {
            mExoPlayerVideoFragment.getvStaticView().setVisibility(View.GONE);
        }

        @Override
        public void onVideoCompleted() {
            mExoPlayerVideoFragment.setVideoPlaybackListener(null);
            mExoPlayerVideoFragment.setAdPlaybackListener(mAdPlaybackListener);
            playNextVideo(true);

        }

        @Override
        public void onPlaybackError(boolean wasMediaPlayerError) {
            mExoPlayerVideoFragment.setVideoPlaybackListener(null);
            mExoPlayerVideoFragment.setAdPlaybackListener(mAdPlaybackListener);
            playNextVideo(true);
        }
    };

    @Override
    public void adSkipped(AdListPlayedMessageObject adListPlayedMessageObject) {
        super.adSkipped(adListPlayedMessageObject);

        incrementMidrollIndex();
    }
}
