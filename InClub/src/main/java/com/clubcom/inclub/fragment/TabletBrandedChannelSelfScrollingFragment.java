package com.clubcom.inclub.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.clubcom.ccframework.fragment.brandedchannel.BrandedChannelSelfScrollingMenuFragment;
import com.clubcom.ccframework.util.CCDisplay;
import com.clubcom.ccframework.util.UrlFactory;
import com.clubcom.communicationframework.model.apps.LayoutMap;
import com.clubcom.inclub.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by adamwalter3 on 1/30/17.
 */

public class TabletBrandedChannelSelfScrollingFragment extends BrandedChannelSelfScrollingMenuFragment {
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

        System.out.println("Loading Image: " + UrlFactory.getRemotePath(filename, CCDisplay.getScreenDensity(mBaseActivity)));
        Picasso.with(mBaseActivity)
                .load(UrlFactory.getRemotePath(filename, CCDisplay.getScreenDensity(mBaseActivity)))
                .into(target);
    }

    @Override
    public int getMenuBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_bg_color);
    }

    @Override
    public int getMenuTextColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_text_color);
    }

    @Override
    public LayoutMap getLayoutMap() {
        return null;
    }

    @Override
    public void logIn() {

    }

    @Override
    public void createAccount() {

    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return null;
    }
}
