package com.clubcom.inclub.util;

import android.support.v4.content.ContextCompat;

import com.android.volley.VolleyError;
import com.clubcom.ccframework.activity.BaseActivity;
import com.clubcom.ccframework.fragment.video.ExoPlayerVideoFragment;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.ccframework.util.CCDisplay;
import com.clubcom.ccframework.util.GsonSingleton;
import com.clubcom.ccframework.util.UrlFactory;
import com.clubcom.communicationframework.model.ads.ContentItem;
import com.clubcom.communicationframework.model.ads.OrderGroup;
import com.clubcom.communicationframework.model.ads.OrderGroupMap;
import com.clubcom.communicationframework.model.ads.PlaybackListItem;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.activity.TabletLaunchMenuActivity;
import com.clubcom.projectile.JsonElementListener;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by adamwalter3 on 8/25/16.
 */

public class MidrollPlayer {
    public static void populateMidrolls() {
        if (MainApplication.sTabletLogInObject != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject() != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject().getAdListData() != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject().getAdListData().containsKey(OrderGroupMap.KEY_MID_ROLL)) {
            OrderGroup midrollGroup = MainApplication.sTabletLogInObject.getLogInNetworkObject().getAdListData().getMidRollGroup();
            for (String key : midrollGroup.getPlaybackList().keySet()) {
                midrollGroup.getPlaybackList().get(key).setKey(key);
            }

            MainApplication.sMidrolls = new ArrayList<>(midrollGroup.getPlaybackList().values());

            Collections.sort(MainApplication.sMidrolls, new ContentItem.ContentItemComparator());
        }
    }

    public static ContentItem playNextMidroll(final BaseActivity tabletBaseActivity, final ExoPlayerVideoFragment exoPlayerVideoFragment, boolean increment) {
        if (increment) {
            MainApplication.incrementMidrollIndex();
        }

        final ContentItem playbackListItem = MainApplication.sMidrolls.get(MainApplication.sMidrollIndex);

        if (playbackListItem.getAssociatedContent(ContentItem.CONTENT_TYPE_BANNER) != null) {
            showBannerFromContent(tabletBaseActivity, playbackListItem.getAssociatedContent(), exoPlayerVideoFragment);
        } else {
            exoPlayerVideoFragment.showBanner(ContextCompat.getDrawable(tabletBaseActivity, R.drawable.logo_banner));
        }

        exoPlayerVideoFragment.playHttpStream(UrlFactory.getMediaUrl(MainApplication.sMidrolls.get(MainApplication.sMidrollIndex)));
        return playbackListItem;
    }

    private static void showBannerFromContent(BaseActivity tabletBaseActivity, HashMap<String, ContentItem> associatedContent, ExoPlayerVideoFragment exoPlayerVideoFragment) {
        boolean bannerSet = false;
        for (ContentItem associatedContentItem : associatedContent.values()) {
            if (associatedContentItem.getContentTypeID() == ContentItem.CONTENT_TYPE_BANNER) {
                if (associatedContentItem.getActions() != null && !associatedContentItem.getActions().isEmpty()) {
                    exoPlayerVideoFragment.showBanner(UrlFactory.getRemotePath(associatedContentItem.getFileName(), CCDisplay.getScreenDensity(tabletBaseActivity)), new ArrayList<>(associatedContentItem.getActions().values()));
                } else {
                    exoPlayerVideoFragment.showBanner(UrlFactory.getRemotePath(associatedContentItem.getFileName(), CCDisplay.getScreenDensity(tabletBaseActivity)), null);
                }
                bannerSet = true;
                break;
            }
        }

        if (!bannerSet) {
            exoPlayerVideoFragment.showBanner(ContextCompat.getDrawable(tabletBaseActivity, R.drawable.logo_banner));
        }
    }
}
