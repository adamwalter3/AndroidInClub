package com.clubcom.inclub.fragment;

import android.support.v4.content.ContextCompat;

import com.android.volley.VolleyError;
import com.clubcom.ccframework.adapter.BaseRecyclerAdapter;
import com.clubcom.ccframework.fragment.TvMenuFragment;
import com.clubcom.ccframework.model.TvGridViewItem;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.ccframework.util.GsonSingleton;
import com.clubcom.communicationframework.model.apps.LayoutMap;
import com.clubcom.communicationframework.model.tv.TvChannel;
import com.clubcom.communicationframework.model.tv.TvChannelList;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.adapter.TabletBaseRecyclerAdapter;
import com.clubcom.projectile.JsonElementListener;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamwalter3 on 9/29/16.
 */

public class TabletTvMenuFragment extends TvMenuFragment {
    @Override
    public void getTvChannels() {
        BackEnd.getTvChannels(mBaseActivity, new JsonElementListener() {
            @Override
            public void onResponse(JsonElement jsonElement) {
                List<String> keysToRemove = new ArrayList<>();
                TvChannelList tvChannelList = GsonSingleton.getInstance().getGson().fromJson(jsonElement, TvChannelList.class);
                for (String key : tvChannelList.getTVChannels().keySet()) {
                    if (tvChannelList.getTVChannels().get(key).getURL() == null) {
                        keysToRemove.add(key);
                    }
                }

                for (String key : keysToRemove) {
                    tvChannelList.getTVChannels().remove(key);
                }

                onTvChannelsReceived(tvChannelList);
            }

            @Override
            public void onError(VolleyError volleyError) {
                onTvChannelsReceived(null);
            }
        });
    }

    @Override
    public BaseRecyclerAdapter makeRecyclerAdapter() {
        List<TvGridViewItem> gridViewItems = new ArrayList<>();
        for (TvChannel channel : new ArrayList<>(mTvChannels.getTVChannels().values())) {
            gridViewItems.add(new TvGridViewItem(channel));
        }

        return new TabletBaseRecyclerAdapter(mBaseActivity, gridViewItems, R.layout.menu_item_image_text);
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
