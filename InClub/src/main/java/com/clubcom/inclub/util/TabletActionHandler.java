package com.clubcom.inclub.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.clubcom.ccframework.activity.BaseActivity;
import com.clubcom.ccframework.activity.BrandedChannelActivity;
import com.clubcom.ccframework.activity.GroupXActivity;
import com.clubcom.ccframework.fragment.video.ExoPlayerVideoFragment;
import com.clubcom.ccframework.util.ActionHandler;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.ccframework.util.CCDisplay;
import com.clubcom.ccframework.util.PVSImageLoader;
import com.clubcom.ccframework.util.UrlFactory;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.activity.TabletBrandedChannelActivity;
import com.clubcom.inclub.activity.TabletGroupXActivity;
import com.clubcom.inclub.activity.TabletMusicActivity;
import com.clubcom.inclub.activity.TabletPersonalTrainerActivity;
import com.clubcom.inclub.activity.TabletTvActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by adamwalter3 on 10/3/16.
 */

public class TabletActionHandler extends ActionHandler {
    @Override
    protected void launchClubComApp(BaseActivity baseActivity, String action, String actionExtra, String logo, ActionListener listener) {
        Intent intent = null;
        if (action.endsWith("tv")) {
            intent = new Intent(baseActivity, TabletTvActivity.class);
        } else if (action.endsWith("musicplayer")) {
            intent = new Intent(baseActivity, TabletMusicActivity.class);
        } else if (action.endsWith("personaltrainer")) {
            intent = new Intent(baseActivity, TabletPersonalTrainerActivity.class);
        } else if (action.endsWith("clubapp")) {
            //ignore
            System.out.println("call to clubapp should be removed");
        } else if (action.endsWith("groupx")) {
            intent = new Intent(baseActivity, TabletGroupXActivity.class);
            intent.putExtra(GroupXActivity.EXTRA_CLASS_TYPE, actionExtra);
        }

        if (intent != null) {
            startActivity(baseActivity, intent, null, listener);
            if (listener != null) {
                listener.actionComplete();
            }
        } else {
            if (listener != null) {
                listener.actionError();
            }
        }
    }
    @Override
    protected void playVideo(ExoPlayerVideoFragment fragment, String action, ExoPlayerVideoFragment.VideoPlaybackListener videoPlaybackListener, ActionListener listener) {
        if (fragment != null) {
            if (action.contains(".")) {
                action = action.substring(0, action.indexOf("."));
            }

            //TODO maybe make this item?
            fragment.playHttpStream(null, UrlFactory.getMediaUrl(action));
            if (listener != null) {
                listener.actionComplete();
            }
        } else {
            if (listener != null) {
                listener.actionError();
            }
        }
    }

    @Override
    protected void launchBrandedChannel(BaseActivity baseActivity, String orderId, String logo, ActionListener actionListener) {
        Intent intent = new Intent(baseActivity, TabletBrandedChannelActivity.class);
        intent.putExtra(BrandedChannelActivity.EXTRA_BRANDED_CHANNEL_INFO, orderId);
        startActivity(baseActivity, intent, null, actionListener);
    }

    @Override
    protected void showImage(final BaseActivity baseActivity, ExoPlayerVideoFragment exoPlayerVideoFragment, String fileName) {
        exoPlayerVideoFragment.hideBanner();
        if (fileName.equalsIgnoreCase("clubapphomescreen")) {
            exoPlayerVideoFragment.showStatic(R.drawable.launch_screen);
            exoPlayerVideoFragment.showBanner(ContextCompat.getDrawable(baseActivity, R.drawable.logo_banner));
        } else {
            exoPlayerVideoFragment.stopPlayer();
            exoPlayerVideoFragment.getvStaticView().setVisibility(View.VISIBLE);
            exoPlayerVideoFragment.getvStaticView().bringToFront();
            final View view = exoPlayerVideoFragment.getvStaticView();
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    if (view instanceof ImageView) {
                        ((ImageView) view).setImageBitmap(bitmap);
                    } else {
                        view.setBackground(new BitmapDrawable(baseActivity.getResources(), bitmap));
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
            Picasso.with(baseActivity).load(UrlFactory.getRemotePath(fileName, CCDisplay.getScreenDensity(baseActivity))).into(target);

        }
    }

    @Override
    protected void sendEmail(BaseActivity baseActivity, int type, int contentId, String optionalJson) {
        BackEnd.sendEmailTask(baseActivity, MainApplication.sTabletLogInObject.getLogInNetworkObject().getSessionGUID(),
                String.valueOf(contentId), type, optionalJson, null);
    }
}
