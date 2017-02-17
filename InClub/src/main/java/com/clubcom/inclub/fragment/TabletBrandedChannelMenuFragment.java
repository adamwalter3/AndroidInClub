package com.clubcom.inclub.fragment;

import android.support.v4.content.ContextCompat;

import com.clubcom.ccframework.adapter.BaseRecyclerAdapter;
import com.clubcom.ccframework.fragment.brandedchannel.BrandedChannelMenuFragment;
import com.clubcom.communicationframework.model.apps.LayoutMap;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.adapter.TabletBrandedChannelAdapter;

/**
 * Created by adamwalter3 on 9/30/16.
 */

public class TabletBrandedChannelMenuFragment extends BrandedChannelMenuFragment {
    @Override
    public BaseRecyclerAdapter getBaseRecyclerAdapter() {
        return new TabletBrandedChannelAdapter(mBaseActivity, mVideos, R.layout.branded_channel_one_by_one);
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
        if (MainApplication.sTabletLogInObject != null
                && MainApplication.sTabletLogInObject.getLayoutMap() != null) {
            return MainApplication.sTabletLogInObject.getLayoutMap();
        } else {
            return null;
        }
    }

    @Override
    public void logIn() {

    }

    @Override
    public void createAccount() {

    }
}
