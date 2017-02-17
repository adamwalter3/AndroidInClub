package com.clubcom.inclub.fragment;

import android.support.v4.content.ContextCompat;

import com.android.volley.VolleyError;
import com.clubcom.ccframework.adapter.BaseRecyclerAdapter;
import com.clubcom.ccframework.fragment.PersonalTrainerMenuFragment;
import com.clubcom.ccframework.model.PersonalTrainerGridViewObject;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.ccframework.util.GsonSingleton;
import com.clubcom.communicationframework.model.apps.LayoutMap;
import com.clubcom.communicationframework.model.club.PersonalTrainerData;
import com.clubcom.communicationframework.model.club.PersonalTrainerObject;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.adapter.TabletBaseRecyclerAdapter;
import com.clubcom.projectile.JsonElementListener;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamwalter3 on 9/30/16.
 */

public class TabletPersonalTrainerMenuFragment extends PersonalTrainerMenuFragment {
    @Override
    public void getPersonalTrainers() {
        BackEnd.getPersonalTrainers(mBaseActivity, new JsonElementListener() {
            @Override
            public void onResponse(JsonElement jsonElement) {
                if (jsonElement != null) {
                    onPersonalTrainersReceived(GsonSingleton.getInstance().getGson().fromJson(jsonElement, PersonalTrainerData.class));
                } else {
                    onPersonalTrainersReceived(null);
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                onPersonalTrainersReceived(null);
            }
        });
    }

    @Override
    public BaseRecyclerAdapter makeRecyclerAdapter() {
        List<PersonalTrainerGridViewObject> gridViewItems = new ArrayList<>();
        for (PersonalTrainerObject trainer : new ArrayList<>(mTrainers.getPersonalTrainers().values())) {
            gridViewItems.add(new PersonalTrainerGridViewObject(trainer));
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
