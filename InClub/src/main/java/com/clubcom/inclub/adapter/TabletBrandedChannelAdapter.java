package com.clubcom.inclub.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.clubcom.ccframework.activity.BaseActivity;
import com.clubcom.ccframework.adapter.AbstractRecyclerAdapter;
import com.clubcom.ccframework.adapter.BrandedChannelRecyclerAdapter;
import com.clubcom.ccframework.fragment.MenuFragment;
import com.clubcom.ccframework.model.GridViewItem;
import com.clubcom.ccframework.util.CCDisplay;
import com.clubcom.ccframework.util.UrlFactory;
import com.clubcom.inclub.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by adamwalter3 on 9/29/16.
 */

public class TabletBrandedChannelAdapter extends BrandedChannelRecyclerAdapter {
    public TabletBrandedChannelAdapter(BaseActivity baseActivity, List<? extends GridViewItem> items, int layout, MenuFragment.OnItemClickListener listener) {
        super(baseActivity, items, layout, listener);
    }

    public TabletBrandedChannelAdapter(BaseActivity baseActivity, List<? extends GridViewItem> items, int layout) {
        this(baseActivity, items, layout, null);
    }

    @Override
    public void loadImage(final Context ctx, final View view, String filename, final ImageLoadInterface imageLoadInterface) {
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (view instanceof ImageView) {
                    ((ImageView) view).setImageBitmap(bitmap);
                } else {
                    view.setBackground(new BitmapDrawable(ctx.getResources(), bitmap));
                }

                if (imageLoadInterface != null) {
                    imageLoadInterface.onImageLoadSuccess(bitmap);
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
        Picasso.with(ctx).load(UrlFactory.getRemotePath(filename, CCDisplay.getScreenDensity(ctx))).into(target);
    }

    @Override
    public int getContentActiveLinkColor(Context ctx) {
        return ContextCompat.getColor(ctx, R.color.content_active_link_color);
    }

    @Override
    public int getMenuTextColor(Context ctx) {
        return ContextCompat.getColor(ctx, R.color.menu_text_color);
    }

    @Override
    public int getMenuIconColor(Context ctx) {
        return ContextCompat.getColor(ctx, R.color.menu_icon_color);
    }
}
