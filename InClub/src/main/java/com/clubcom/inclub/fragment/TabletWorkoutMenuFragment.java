package com.clubcom.inclub.fragment;

import android.support.v4.content.ContextCompat;

import com.clubcom.ccframework.fragment.WorkoutMenuFragment;
import com.clubcom.inclub.R;

/**
 * Created by adamwalter3 on 1/17/17.
 */

public class TabletWorkoutMenuFragment extends WorkoutMenuFragment {
    @Override
    public int getMenuTextColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_text_color);
    }
}
