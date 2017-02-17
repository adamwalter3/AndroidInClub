package com.clubcom.inclub.fragment;

import android.support.v4.content.ContextCompat;

import com.clubcom.ccframework.fragment.AccountSettingsFragment;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.ccframework.util.UnitHelper;
import com.clubcom.communicationframework.model.account.UserNetworkObject;
import com.clubcom.inclub.R;

/**
 * Created by adamwalter3 on 9/30/16.
 */

public class TabletAccountSettingsFragment extends AccountSettingsFragment {
    @Override
    public int getMenuBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_bg_color);
    }

    @Override
    public int getMenuTextColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_text_color);
    }

    @Override
    public int getContentLinkColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.content_link_color);
    }

    @Override
    public int getContentActivtLinkColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.content_active_link_color);
    }

    @Override
    public void saveUser(UserNetworkObject userNetworkObject) {
        BackEnd.saveUser(mBaseActivity, userNetworkObject, null);
    }

    @Override
    public boolean isImperial() {
        return UnitHelper.isImperial();
    }
}
