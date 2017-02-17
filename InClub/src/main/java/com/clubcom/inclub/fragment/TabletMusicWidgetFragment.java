package com.clubcom.inclub.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.clubcom.ccframework.fragment.MusicAlbumArtFragment;
import com.clubcom.ccframework.util.CCDisplay;
import com.clubcom.ccframework.util.UrlFactory;
import com.clubcom.inclub.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by adamwalter3 on 10/19/16.
 */

public class TabletMusicWidgetFragment extends MusicAlbumArtFragment {
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
    public int getMenuBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_bg_color);
    }
}
