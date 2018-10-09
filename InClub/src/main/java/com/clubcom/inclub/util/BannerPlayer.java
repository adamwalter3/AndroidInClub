package com.clubcom.inclub.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.clubcom.ccframework.activity.BaseActivity;
import com.clubcom.ccframework.fragment.BaseFragment;
import com.clubcom.ccframework.fragment.MenuFragment;
import com.clubcom.ccframework.fragment.video.ExoPlayerVideoFragment;
import com.clubcom.ccframework.util.ActionHandler;
import com.clubcom.ccframework.util.CCDisplay;
import com.clubcom.ccframework.util.UrlFactory;
import com.clubcom.communicationframework.model.ads.ContentItem;
import com.clubcom.communicationframework.model.ads.OrderGroup;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by adamwalter3 on 9/30/16.
 */

public class BannerPlayer {
    public static List<ContentItem> sOrderedBanners;
    public static int sCurrentBannerIndex;

    public static void setBanners(Context ctx, OrderGroup bannerGroup) {
        sOrderedBanners = new ArrayList<>(bannerGroup.getPlaybackList().values());
        Collections.sort(sOrderedBanners, new ContentItem.ContentItemComparator());
    }

    public static ContentItem getNextBanner(boolean increment) {
        if (sOrderedBanners == null) {
            return null;
        }

        if (increment) {
            sCurrentBannerIndex++;
        }

        if (sCurrentBannerIndex >= sOrderedBanners.size()) {
            sCurrentBannerIndex = 0;
        }

        return sOrderedBanners.get(sCurrentBannerIndex);

    }

    public static void showNextBanner(Context ctx, final BaseFragment baseFragment) {
        ContentItem banner = getNextBanner(true);

        //todo figure out why base frag can be null
        if (banner != null && baseFragment != null) {
            baseFragment.showBanner(UrlFactory.getRemotePath(banner.getFileName(), CCDisplay.getScreenDensity(ctx)), new ArrayList<>(banner.getActions().values()));
        } else {
            if (baseFragment != null) {
                baseFragment.hideBanner();
            }
        }
    }

    public static void showNextBanner(final BaseActivity ctx, final ImageView imageView, final ActionHandler.ActionListener actionListener, final ExoPlayerVideoFragment exoPlayerVideoFragment, final MenuFragment menuFragment, final BaseFragment.ActionCallback actionCallback) {
        final ContentItem banner = getNextBanner(true);
        if (banner != null) {
            Picasso.with(ctx).load(UrlFactory.getRemotePath(banner.getFileName(), CCDisplay.getScreenDensity(ctx)))
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            imageView.setImageBitmap(bitmap);

                            if (banner.getActions() != null) {
                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (actionCallback != null) {
                                            actionCallback.doAction(ctx, new ArrayList<>(banner.getActions().values()), exoPlayerVideoFragment, menuFragment, null, null, actionListener);
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        } else {
            //hide banner
            if (imageView != null) {
                imageView.setVisibility(View.GONE);
                imageView.setImageBitmap(null);
            }
        }
    }
}
