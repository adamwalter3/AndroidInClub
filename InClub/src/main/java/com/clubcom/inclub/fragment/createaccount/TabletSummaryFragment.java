package com.clubcom.inclub.fragment.createaccount;

import android.support.v4.content.ContextCompat;

import com.clubcom.ccframework.fragment.createaccount.SummaryFragment;
import com.clubcom.inclub.R;

/**
 * Created by adamwalter3 on 9/28/16.
 */

public class TabletSummaryFragment extends SummaryFragment {
    @Override
    public int getNavBarBgColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.nav_bar_bg_color);
    }
}
