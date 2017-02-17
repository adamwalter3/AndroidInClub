package com.clubcom.inclub.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;

import com.android.volley.VolleyError;
import com.clubcom.ccframework.activity.GroupXActivity;
import com.clubcom.ccframework.fragment.GroupXScheduleFragment;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.ccframework.util.GsonSingleton;
import com.clubcom.ccframework.util.HTMLDecoder;
import com.clubcom.communicationframework.model.account.UserNetworkObject;
import com.clubcom.communicationframework.model.club.GroupXClassDayObject;
import com.clubcom.communicationframework.model.club.PersonalTrainerData;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.fragment.TabletGroupXScheduleFragment;
import com.clubcom.inclub.util.GoogleApiHelper;
import com.clubcom.inclub.util.LogOutHelper;
import com.clubcom.projectile.JsonElementListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by adamwalter3 on 9/30/16.
 */

public class TabletGroupXActivity extends GroupXActivity {
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void superOnCreateCalled() {
        super.superOnCreateCalled();
        mGoogleApiClient = GoogleApiHelper.getGoogleAPIClient(mBaseActivity);
    }

    @Override
    public GroupXScheduleFragment getNewGroupXScheduleFragment() {
        return new TabletGroupXScheduleFragment();
    }

    @Override
    public void getGroupX(String classType) {
        BackEnd.getGroupX(mBaseActivity, getIntent().getExtras().getString(EXTRA_CLASS_TYPE), new JsonElementListener() {
            @Override
            public void onResponse(JsonElement jsonElement) {
                String decodedText = HTMLDecoder.decodeHTML(jsonElement.toString());
                List<GroupXClassDayObject> classes = GsonSingleton.getInstance().getGson().fromJson(decodedText, new TypeToken<List<GroupXClassDayObject>>() {
                }.getType());
                onGroupXReceived(classes);
            }

            @Override
            public void onError(VolleyError volleyError) {
                onGroupXReceived(null);
            }
        });
    }

    @Override
    public void getPersonalTrainers() {
        BackEnd.getPersonalTrainers(mBaseActivity, new JsonElementListener() {
            @Override
            public void onResponse(JsonElement jsonElement) {
                PersonalTrainerData personalTrainerData = GsonSingleton.getInstance().getGson().fromJson(jsonElement, PersonalTrainerData.class);
                onPersonalTrainersReceived(personalTrainerData);
            }

            @Override
            public void onError(VolleyError volleyError) {
                onPersonalTrainersReceived(null);
            }
        });
    }

    @Override
    protected int getNavBarBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.nav_bar_bg_color);
    }

    @Override
    protected int getNavBarIconColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.nav_bar_icon_color);
    }

    @Override
    protected int getNavBarTextColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.nav_bar_text_color);
    }

    @Override
    public UserNetworkObject getUserObject() {
        if (MainApplication.sTabletLogInObject != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject() != null
                && MainApplication.sTabletLogInObject.getLogInNetworkObject() != null) {
            return MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation();
        } else {
            return null;
        }
    }

    @Override
    protected void logOut() {
        LogOutHelper.doLogOut(mGoogleApiClient, mBaseActivity);
    }

    @Override
    protected void launchMyAccount() {
        Intent intent = new Intent(mBaseActivity, TabletMyAccountActivity.class);
        startActivity(intent);
    }

    @Override
    protected void updateUserObject(UserNetworkObject userNetworkObject) {
        MainApplication.sTabletLogInObject.getLogInNetworkObject().setUserInformation(userNetworkObject);
    }

    @Override
    public void writeLogEntry(String log) {

    }
}
