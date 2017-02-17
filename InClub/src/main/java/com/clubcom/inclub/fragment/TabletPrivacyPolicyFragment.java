package com.clubcom.inclub.fragment;

import android.support.v4.content.ContextCompat;

import com.clubcom.ccframework.fragment.PrivacyPolicyFragment;
import com.clubcom.inclub.R;

/**
 * Created by adamwalter3 on 9/30/16.
 */

public class TabletPrivacyPolicyFragment extends PrivacyPolicyFragment {
    @Override
    public int getMenuTextColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_text_color);
    }

    @Override
    public int getMenuBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_bg_color);
    }
}
