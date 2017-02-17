package com.clubcom.inclub.fragment.createaccount;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.RemoteException;

import com.android.volley.VolleyError;
import com.clubcom.ccframework.fragment.createaccount.EmailFragment;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.ccframework.util.ClubcomLog;
import com.clubcom.ccframework.util.GsonSingleton;
import com.clubcom.communicationframework.message.ContentManagementMessageSender;
import com.clubcom.communicationframework.model.account.ForgotSomethingResponse;
import com.clubcom.inclub.activity.TabletCreateAccountActivity;
import com.clubcom.projectile.StringListener;

/**
 * Created by adamwalter on 11/4/14.
 */
public class TabletEmailFragment extends EmailFragment {
    @Override
    public void validateEmail(final String email) {
        BackEnd.validateEmail(mBaseActivity, email, new StringListener() {
            @Override
            public void onResponse(String s) {
                mBaseActivity.hideProgressDialog();
                if (s.equals("1")) {
                    ((TabletCreateAccountActivity) getActivity()).stepForward(false);
                } else {
                    emailTaken(email);
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                onServerError();
            }
        });
    }

    @Override
    public void requestPasswordResetEmail(String email) {
        BackEnd.retrievePasswordTask(mBaseActivity, email, null, new StringListener() {
            @Override
            public void onResponse(String s) {
                ForgotSomethingResponse forgotSomethingResponse = GsonSingleton.getInstance().getGson().fromJson(s, ForgotSomethingResponse.class);
                switch (forgotSomethingResponse.getSuccess()) {
                    case 1:
                        //success
                        onEmailSent(forgotSomethingResponse.getEmail());
                        break;
                    case 2:
                        //failure
                        onEmailNotSent();
                        break;
                    default:
                        onEmailNotSent();
                        break;
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                onEmailNotSent();
            }
        });
    }
}
