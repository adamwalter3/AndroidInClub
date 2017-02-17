package com.clubcom.inclub.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.clubcom.ccframework.fragment.PersonalTrainerInfoFragment;
import com.clubcom.ccframework.util.CCDisplay;
import com.clubcom.ccframework.util.UrlFactory;
import com.clubcom.inclub.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by adamwalter3 on 9/30/16.
 */

public class TabletPersonalTrainerInfoFragment extends PersonalTrainerInfoFragment {
    @Override
    public int getMenuBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_bg_color);
    }

    @Override
    public int getMenuIconColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_icon_color);
    }

    @Override
    public int getContentLinkColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.content_link_color);
    }

    @Override
    public int getContentBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.content_bg_color);
    }

    @Override
    public int getContentTextColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.content_text_color);
    }

    @Override
    public void loadImage(Context context, final ImageView view, String filename) {
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
}
