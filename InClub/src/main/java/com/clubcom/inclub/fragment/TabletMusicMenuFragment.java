package com.clubcom.inclub.fragment;

import android.support.v4.content.ContextCompat;

import com.android.volley.VolleyError;
import com.clubcom.ccframework.adapter.BaseRecyclerAdapter;
import com.clubcom.ccframework.fragment.MusicMenuFragment;
import com.clubcom.ccframework.model.MusicGridViewItem;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.ccframework.util.GsonSingleton;
import com.clubcom.communicationframework.model.apps.LayoutMap;
import com.clubcom.communicationframework.model.musicvideo.Channel;
import com.clubcom.communicationframework.model.musicvideo.ChannelList;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.adapter.TabletBaseRecyclerAdapter;
import com.clubcom.projectile.JsonElementListener;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamwalter3 on 9/30/16.
 */

public class TabletMusicMenuFragment extends MusicMenuFragment {
    @Override
    public void getMusicChannels() {
        BackEnd.getMusicChannels(mBaseActivity, new JsonElementListener() {
            @Override
            public void onResponse(JsonElement jsonElement) {
                try {
                    ChannelList channelList = GsonSingleton.getInstance().getGson().fromJson(jsonElement, ChannelList.class);
                    onMusicChannelsReceived(channelList);
                    doneCaching();
                } catch (JsonSyntaxException ex) {
                    mBaseActivity.showActivityNotAvailable();
                    mCacheTimeoutHandler.removeCallbacks(mCacheTimeoutRunnable);
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                mBaseActivity.showActivityNotAvailable();
                mCacheTimeoutHandler.removeCallbacks(mCacheTimeoutRunnable);
            }
        });
    }

    @Override
    public BaseRecyclerAdapter getBaseRecyclerAdapter() {
        List<MusicGridViewItem> gridViewItems = new ArrayList<>();
        for (Channel channel : new ArrayList<>(mChannelList.getChannels().values())) {
            gridViewItems.add(new MusicGridViewItem(channel));
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
