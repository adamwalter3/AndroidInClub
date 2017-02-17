package com.clubcom.inclub.fragment.createaccount;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.android.volley.VolleyError;
import com.clubcom.ccframework.fragment.createaccount.UserIdFragment;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.inclub.activity.TabletCreateAccountActivity;
import com.clubcom.projectile.StringListener;

/**
 * Created by adamwalter on 11/4/14.
 */
public class TabletUserIdFragment extends UserIdFragment {

    @Override
    public void validateBackEndUser(final String userName) {
        BackEnd.validateUser(getActivity(), vInput.getText().toString(), new StringListener() {
            @Override
            public void onResponse(String s) {
                mBaseActivity.hideProgressDialog();
                if (s.equals("1")) {
                    ((TabletCreateAccountActivity) getActivity()).stepForward(false);
                } else {
                   userIdTaken();
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                onServerError();
            }
        });
    }
}
