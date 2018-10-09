package com.clubcom.inclub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.android.volley.VolleyError;
import com.clubcom.ccframework.FrameworkApplication;
import com.clubcom.ccframework.activity.BaseActivity;
import com.clubcom.ccframework.activity.MusicActivity;
import com.clubcom.ccframework.fragment.BaseFragment;
import com.clubcom.ccframework.fragment.MenuFragment;
import com.clubcom.ccframework.fragment.MusicAlbumArtFragment;
import com.clubcom.ccframework.fragment.MusicMenuFragment;
import com.clubcom.ccframework.fragment.video.ExoPlayerVideoFragment;
import com.clubcom.ccframework.util.ActionHandler;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.ccframework.util.GsonSingleton;
import com.clubcom.ccframework.util.HTMLDecoder;
import com.clubcom.ccframework.util.UrlFactory;
import com.clubcom.communicationframework.model.ContentAction;
import com.clubcom.communicationframework.model.account.UserNetworkObject;
import com.clubcom.communicationframework.model.ads.AdListPlayedMessageObject;
import com.clubcom.communicationframework.model.ads.ContentItem;
import com.clubcom.communicationframework.model.ads.PlaybackListItem;
import com.clubcom.communicationframework.model.musicvideo.Channel;
import com.clubcom.communicationframework.model.musicvideo.PlaylistItem;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.fragment.TabletMusicMenuFragment;
import com.clubcom.inclub.fragment.TabletMusicWidgetFragment;
import com.clubcom.inclub.util.BannerPlayer;
import com.clubcom.inclub.util.GoogleApiHelper;
import com.clubcom.inclub.util.LogOutHelper;
import com.clubcom.inclub.util.LogReporter;
import com.clubcom.inclub.util.MidrollPlayer;
import com.clubcom.inclub.util.NetworkUtil;
import com.clubcom.inclub.util.TabletActionHandler;
import com.clubcom.projectile.JsonElementListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;

/**
 * Created by adamwalter3 on 9/30/16.
 */

public class TabletMusicActivity extends MusicActivity {
    protected GoogleApiClient mGoogleApiClient;
    protected BaseFragment.ActionCallback mActionCallback;

    @Override
    public MusicMenuFragment getNewMusicMenuFragment() {
        return new TabletMusicMenuFragment();
    }

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
                new TabletActionHandler().doAction(baseActivity, actions, exoPlayerVideoFragment, mVideoPlaybackListener, menuFragment, b, logo, listener);
            }
        };

        mExoPlayerVideoFragment.setActionCallback(mActionCallback);
    }

    @Override
    public void saveUser(UserNetworkObject userNetworkObject) {
        BackEnd.saveUser(mBaseActivity, userNetworkObject, null);
    }

    @Override
    public int getDarkerBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.nav_bar_bg_darker);
    }

    @Override
    public void getChannelInfo(final Channel channel) {
        BackEnd.getChannelInfo(mBaseActivity, String.valueOf(channel.getChannelID()), new JsonElementListener() {
            @Override
            public void onResponse(JsonElement jsonElement) {
                HashMap<String, PlaylistItem> playlist = GsonSingleton.getInstance().getGson().fromJson(jsonElement.getAsJsonObject().get("PlaylistItems"), new TypeToken<HashMap<String, PlaylistItem>>() {
                }.getType());
                channel.setPlaylist(playlist);
                onChannelInfoReceived(channel);
            }

            @Override
            public void onError(VolleyError volleyError) {
                showNoChannelAlertDialog();
            }
        });
    }

    @Override
    public void playMusicVideo(String channelId, int index, long position, ExoPlayerVideoFragment exoPlayerVideoFragment) {
        //TODO Maybe populate more
        ContentItem contentItem = new ContentItem();
        contentItem.setContentID(Integer.valueOf(mPlaylistItems.get(index).getContentID()));
        exoPlayerVideoFragment.playHttpStream(contentItem, UrlFactory.getMediaUrl(mPlaylistItems.get(index)), position);
    }

    @Override
    public MusicAlbumArtFragment getMusicWidgetFragment() {
        return new TabletMusicWidgetFragment();
    }

    @Override
    protected boolean shouldShowAlbumWidget() {
        return true;
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
    protected int getNavBarBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.nav_bar_bg_color);
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
    public ContentItem playNextMidroll(boolean increment) {
        return MidrollPlayer.playNextMidroll(mBaseActivity, mExoPlayerVideoFragment, true);
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
    public void setPriority(ContentItem contentItem, double newPriority, AdListPlayedMessageObject adListPlayedMessageObject) {
        MainApplication.sTabletLogInObject.getLogInNetworkObject().getAdListData().getMidRollGroup().getPlaybackList().get(contentItem.getKey())
                .setPriority(newPriority);
    }

    @Override
    public void adSkipped(AdListPlayedMessageObject adListPlayedMessageObject) {

    }

    @Override
    protected void showNextBanner() {
        if ((mTablet)) {
            BannerPlayer.showNextBanner(mBaseActivity, mExoPlayerVideoFragment);
        } else {
            BannerPlayer.showNextBanner(mBaseActivity, vBannerView, null, mExoPlayerVideoFragment, mMenuFragment, mActionCallback);
            System.out.println("Show Banner");
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
