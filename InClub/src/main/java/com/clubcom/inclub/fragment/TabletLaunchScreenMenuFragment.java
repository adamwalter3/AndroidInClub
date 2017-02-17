package com.clubcom.inclub.fragment;

import android.support.v4.content.ContextCompat;

import com.clubcom.ccframework.adapter.BaseRecyclerAdapter;
import com.clubcom.ccframework.fragment.LaunchScreenMenuFragment;
import com.clubcom.ccframework.model.LaunchMenuGridViewObject;
import com.clubcom.communicationframework.model.apps.LayoutMap;
import com.clubcom.communicationframework.model.apps.LayoutObject;
import com.clubcom.communicationframework.model.apps.MenuObject;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.adapter.TabletBaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by adamwalter3 on 9/29/16.
 */

public class TabletLaunchScreenMenuFragment extends LaunchScreenMenuFragment {
    @Override
    public BaseRecyclerAdapter makeRecyclerAdapter() {
        return new TabletBaseRecyclerAdapter(mBaseActivity, getMenuObjects(), R.layout.menu_fragment_square_one_by_one);
    }

    private List<LaunchMenuGridViewObject> getMenuObjects() {
        List<LaunchMenuGridViewObject> menuGridViewObjectList = new ArrayList<>();
        if (MainApplication.sTabletLogInObject != null
                && MainApplication.sTabletLogInObject.getLayoutMap() != null
                && MainApplication.sTabletLogInObject.getLayoutMap().getLayouts() != null
                && !MainApplication.sTabletLogInObject.getLayoutMap().getLayouts().isEmpty()
                && MainApplication.sTabletLogInObject.getLayoutMap().getLayouts().containsKey(LayoutObject.MENU_TYPE_START_SCREEN)) {
            LayoutObject layoutObject = MainApplication.sTabletLogInObject.getLayoutMap().getLayouts().get(LayoutObject.MENU_TYPE_START_SCREEN);

            for (MenuObject menuObject : layoutObject.getMenu().values()) {
                menuGridViewObjectList.add(new LaunchMenuGridViewObject(menuObject));
            }

            Collections.sort(menuGridViewObjectList, new LaunchMenuGridViewObject.LaunchMenuGridViewObjectComparator());
            menuGridViewObjectList = fillUpMenu(menuGridViewObjectList);
        }

        return menuGridViewObjectList;
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
