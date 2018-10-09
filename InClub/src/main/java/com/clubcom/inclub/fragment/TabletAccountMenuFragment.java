package com.clubcom.inclub.fragment;

import android.support.v4.content.ContextCompat;

import com.clubcom.ccframework.adapter.BaseRecyclerAdapter;
import com.clubcom.ccframework.fragment.AccountMenuFragment;
import com.clubcom.ccframework.model.MyAccountMenuItem;
import com.clubcom.communicationframework.model.apps.LayoutMap;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.adapter.TabletBaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamwalter3 on 9/30/16.
 */

public class TabletAccountMenuFragment extends AccountMenuFragment {
    @Override
    public BaseRecyclerAdapter getBaseRecyclerAdapter() {
        final String[] items = getResources().getStringArray(R.array.account_list_array);
        List<MyAccountMenuItem> menuItems = new ArrayList<>();
        for (String item : items) {
            menuItems.add(new MyAccountMenuItem(item));
        }

        return new TabletBaseRecyclerAdapter(mBaseActivity, menuItems, R.layout.menu_item_image_text);
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
    public boolean shouldShowDividerLine() {
        return mBaseActivity.isTablet();
    }

    @Override
    public void logIn() {

    }

    @Override
    public void createAccount() {

    }
}
