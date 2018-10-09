package com.clubcom.inclub.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.clubcom.ccframework.FrameworkApplication;
import com.clubcom.ccframework.activity.LogInActivity;
import com.clubcom.ccframework.fragment.LogInFragment;
import com.clubcom.ccframework.util.BackEnd;
import com.clubcom.ccframework.util.ClubcomLog;
import com.clubcom.ccframework.util.GsonSingleton;
import com.clubcom.ccframework.util.HTMLDecoder;
import com.clubcom.ccframework.util.PermissionHelper;
import com.clubcom.communicationframework.message.ContentManagementMessageSender;
import com.clubcom.communicationframework.model.ContentAction;
import com.clubcom.communicationframework.model.account.ForgotSomethingResponse;
import com.clubcom.communicationframework.model.account.LogInNetworkObject;
import com.clubcom.communicationframework.model.account.LogInUserObject;
import com.clubcom.communicationframework.model.account.TabletLogInObject;
import com.clubcom.communicationframework.model.account.UserNetworkObject;
import com.clubcom.communicationframework.model.ads.AdList;
import com.clubcom.communicationframework.model.ads.OrderGroupMap;
import com.clubcom.communicationframework.model.apps.LayoutMap;
import com.clubcom.communicationframework.model.apps.LayoutObject;
import com.clubcom.communicationframework.model.apps.MenuContent;
import com.clubcom.communicationframework.model.apps.MenuObject;
import com.clubcom.communicationframework.model.apps.TemplateObject;
import com.clubcom.communicationframework.model.apps.TemplateObjectMap;
import com.clubcom.inclub.BuildConfig;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.fragment.TabletLogInFragment;
import com.clubcom.inclub.receiver.NetworkStateReceiver;
import com.clubcom.inclub.util.BannerPlayer;
import com.clubcom.inclub.util.GoogleApiHelper;
import com.clubcom.inclub.util.GroupXHelper;
import com.clubcom.inclub.util.LogReporter;
import com.clubcom.inclub.util.MidrollPlayer;
import com.clubcom.inclub.util.NetworkUtil;
import com.clubcom.projectile.JsonElementListener;
import com.clubcom.projectile.StringListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by adamwalter3 on 4/1/16.
 */
public class TabletLogInActivity extends LogInActivity  {
    FirebaseAnalytics mFirebaseAnalytics;
    private WifiManager mWifiManager;
    private CredentialRequest mCredentialRequest;
    protected GoogleApiClient mGoogleApiClient;
    private NetworkStateReceiver.OnNetworkStateUpdated mNetworkStateReceiver;
    private BroadcastReceiver mWifiScanReceiver = null;
    private boolean mCredentialsRequested = false;

    public final static int RC_SAVE = 11223;
    public final static int RC_READ = 11224;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    public void showNotConnectedDialog() {
        showActivityAlertDialog(getString(R.string.not_connected), getString(R.string.not_connected_message), null, null, getString(R.string.open_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openSettings();
            }
        }, null, null, true);
    }

    protected void openSettings() {
        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
    }

    @Override
    protected void logOut() {
        //ignored no menu
    }

    @Override
    protected void launchMyAccount() {
            //ignored no menu
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
        return MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation();
    }

    @Override
    protected void superOnCreateCalled() {
        super.superOnCreateCalled();

        mGoogleApiClient = GoogleApiHelper.getGoogleAPIClient(mBaseActivity);
        mCredentialRequest = new CredentialRequest.Builder()
                .setPasswordLoginSupported(true)
                .build();

        buildLayout();
    }

    @Override
    protected void onNoUserNameRetrieved() {
        requestCredentials();
    }

    @Override
    protected void onNoExtras() {
        requestCredentials();
    }

    private synchronized void requestCredentials() {
        if (!mCredentialsRequested) {
            mCredentialsRequested = true;
            if ((FrameworkApplication.NETWORK_STATE_CURRENT && FrameworkApplication.WIFI_CONNECTED_CLUBCOM) || (FrameworkApplication.NETWORK_STATE_CURRENT && FrameworkApplication.CORPORATE_CALLS_AVAILABLE && MainApplication.sIsDemoMode)) {
                Auth.CredentialsApi.request(mGoogleApiClient, mCredentialRequest).setResultCallback(new ResultCallback<CredentialRequestResult>() {
                    @Override
                    public void onResult(@NonNull CredentialRequestResult credentialRequestResult) {
                        if (credentialRequestResult.getStatus().isSuccess()) {
                            onCredentialRetrieved(credentialRequestResult.getCredential());
                        } else if (credentialRequestResult.getStatus().getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED) {
                            hideProgressDialog();
                            resolveResult(credentialRequestResult.getStatus());
                        } else {
                            hideProgressDialog();
                        }
                    }
                });
            } else if (!FrameworkApplication.NETWORK_STATE_CURRENT) {
                NetworkUtil.checkConnections(mBaseActivity, new NetworkUtil.ConnectionCheckComplete() {
                    @Override
                    public void complete() {
                        mCredentialsRequested = false;
                        requestCredentials();
                    }
                });
            } else {
                showNotConnectedDialog();
            }
        }
    }

    @Override
    protected void buildLayout() {
        super.buildLayout();

        if (vLogInImageView != null) {
            vLogInImageView.setImageDrawable(ContextCompat.getDrawable(mBaseActivity, R.drawable.login_big));
        }
    }

    private void resolveResult(Status status) {
        if (status.getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED) {
            // Prompt the user to choose a saved credential; do not show the hint selector.
            try {
                status.startResolutionForResult(this, RC_READ);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "STATUS: Failed to send resolution.", e);
            }
        } else {
            // The user must create an account or sign in manually.
            Log.e(TAG, "STATUS: Unsuccessful credential request.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SAVE) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "SAVE: OK");
                Toast.makeText(this, "Credentials saved", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "SAVE: Canceled by user");
            }

            startPreroll();
        }

        if (requestCode == RC_READ) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                onCredentialRetrieved(credential);
            } else {
                Log.e(TAG, "Credential Read: NOT OK");
                Toast.makeText(this, "Credential Read Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onCredentialRetrieved(Credential credential) {
        mUserName = credential.getId();
        mPassword = credential.getPassword();

        MainApplication.sUserCredential = credential;
        logIn(mUserName, mPassword, false);
    }

    @Override
    public void logIn(final String username, final String password, final boolean stayLoggedIn) {
        //TODO check for internet --
        showProgressDialog(getString(R.string.please_wait), getString(R.string.logging_in));
        BackEnd.logIn(this, username, password, -1, null, true, new JsonElementListener() {
            @Override
            public void onResponse(JsonElement jsonElement) {
                String decoded = HTMLDecoder.decodeHTML(jsonElement.toString());
                TabletLogInObject tabletLogInObject = GsonSingleton.getInstance().getGson().fromJson(decoded, TabletLogInObject.class);
                switch (tabletLogInObject.getLogInNetworkObject().getValidUser()) {
                    case LogInNetworkObject.RESPONSE_VALID_USER:
                        showProgressDialog(getString(R.string.please_wait), getString(R.string.retrieving_account));
                        MainApplication.sTabletLogInObject = tabletLogInObject;
                        if (tabletLogInObject != null
                                && tabletLogInObject.getLogInNetworkObject() != null
                                && tabletLogInObject.getLayoutMap() != null
                                && tabletLogInObject.getLayoutMap().getLayouts() != null
                                && !tabletLogInObject.getLayoutMap().getLayouts().isEmpty()) {

                            MidrollPlayer.populateMidrolls();
                            if (tabletLogInObject.getLogInNetworkObject().getAdListData() != null) {
                                for (String key : tabletLogInObject.getLogInNetworkObject().getAdListData().keySet()) {
                                    if (tabletLogInObject.getLogInNetworkObject().getAdListData().get(key).getPlaybackList() != null
                                        && !tabletLogInObject.getLogInNetworkObject().getAdListData().get(key).getPlaybackList().isEmpty()) {
                                        for (String playbackKey : tabletLogInObject.getLogInNetworkObject().getAdListData().get(key).getPlaybackList().keySet()) {
                                            tabletLogInObject.getLogInNetworkObject().getAdListData().get(key).getPlaybackList().get(playbackKey).setPriority(Double.valueOf(playbackKey));
                                            tabletLogInObject.getLogInNetworkObject().getAdListData().get(key).getPlaybackList().get(playbackKey).setKey(playbackKey);
                                        }
                                    }
                                }

                                if (tabletLogInObject.getLogInNetworkObject().getAdListData().containsKey(OrderGroupMap.KEY_BANNER_ONLY)
                                        && tabletLogInObject.getLogInNetworkObject().getAdListData().getBannerGroup().getPlaybackList() != null
                                        && tabletLogInObject.getLogInNetworkObject().getAdListData().getBannerGroup().getPlaybackList().size() > 0) {
                                    BannerPlayer.setBanners(mBaseActivity, tabletLogInObject.getLogInNetworkObject().getAdListData().getBannerGroup());
                                }
                            }

                            if (tabletLogInObject.getLayoutMap() != null
                                    && tabletLogInObject.getLayoutMap().getLayouts() != null
                                    && !tabletLogInObject.getLayoutMap().getLayouts().isEmpty()
                                    && tabletLogInObject.getLayoutMap().getLayouts().containsKey(LayoutObject.MENU_TYPE_GROUP_X)) {
                                for (MenuObject menuObject : tabletLogInObject.getLayoutMap().getLayouts().get(LayoutObject.MENU_TYPE_GROUP_X).getMenu().values()) {
                                    String title = menuObject.getContent().getTemplate().getFields().get("textlabel").getData();
                                    TemplateObject templateObject = menuObject.getContent().getTemplate().getFields().get("foregroundimage");
                                    templateObject.setTemplateID(GroupXHelper.getResourceForClassType(title, true));
                                    templateObject.setData("local");
                                }
                            }

                            if (!PermissionHelper.isAdminUser(MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation().getPermissions())) {
                                removeDemoButtons();
                            }

                            if (tabletLogInObject.getLogInNetworkObject().getUserInformation().isPasswordNeedsReset()) {
                                resetPassword(mBaseActivity, username, stayLoggedIn);
                            } else {
                                logOnSucceeded(stayLoggedIn, username, password);
                            }
                        } else {
                            showActivityAlertDialog("Error" , "There was an error logging you in.  Please, try again at a later time.");
                        }
                        break;
                    case LogInNetworkObject.RESPONSE_REQUEST_CREATE_ACCOUNT:
                        createAccount(mUserName);
                        break;
                    case LogInNetworkObject.RESPONSE_BAD_PASSWORD:
                        onWrongPassword(username);
                        break;
                    case LogInNetworkObject.RESPONSE_BAD_USERNAME:
                        onWrongUserName(username);
                        break;
                    case LogInNetworkObject.RESPONSE_IN_USE:
                    case LogInNetworkObject.RESPONSE_NO_DATABASE_CONNECTION:
                    case LogInNetworkObject.RESPONSE_UNKNOWN:
                    case LogInNetworkObject.RESPONSE_NONE:
                    default:
                        break;
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                mBaseActivity.hideProgressDialog();
                String message = getString(R.string.log_in_error_message);
                if (volleyError != null && volleyError.networkResponse != null) {
                    message = message + " Network Response: " + volleyError.networkResponse.statusCode;
                }
                showActivityAlertDialog(getString(R.string.error), message, getString(R.string.ok), null, null, null, false);
                buildLayout();
            }
        });
    }

    public void removeDemoButtons() {
        LayoutMap layoutMap = MainApplication.sTabletLogInObject.getLayoutMap();


        for (Integer key : layoutMap.getLayouts().keySet()) {
            LayoutObject layoutObject = layoutMap.getLayouts().get(key);
            List<Integer> keysToRemove = new ArrayList<>();
            for (Integer menuKey : layoutObject.getMenu().keySet()) {
                MenuObject menuObject = layoutObject.getMenu().get(menuKey);
                if (menuObject.getContent() != null
                    && menuObject.getContent().getActions() != null
                    && !menuObject.getContent().getActions().isEmpty()) {
                    List<ContentAction> actions = new ArrayList(menuObject.getContent().getActions().values());
                    boolean[] permissions = actions.get(0).getPermissions();
                    if (permissions != null) {
                        String build = BuildConfig.FLAVOR;
                        if (permissions[20] || permissions[21] || permissions[22]
                                || permissions[23] || permissions[24] || permissions[25]
                                || permissions[26] || permissions[27] || permissions[28]) {
                            if (build.equals("planetfitness") && !permissions[27]) {
                                keysToRemove.add(menuKey);
                            } else if (build.equals("twentyfour") && !permissions[28]) {
                                keysToRemove.add(menuKey);
                            } else if (build.equals("nysc") && !permissions[26]) {
                                keysToRemove.add(menuKey);
                            } else if (build.equals("golds") && !permissions[25]) {
                                keysToRemove.add(menuKey);
                            } else if (build.equals("laf") && !permissions[24]) {
                                keysToRemove.add(menuKey);
                            }
                        }
                    }
                }
            }

            for (Integer removeKey : keysToRemove) {
                layoutObject.getMenu().remove(removeKey);
            }

            keysToRemove.clear();
        }

        MainApplication.sTabletLogInObject.setLayoutMap(layoutMap);
        if (BuildConfig.FLAVOR.equals("planetfitness")) {
            MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation().getPermissions()[27] = true;
        } else if (BuildConfig.FLAVOR.equals("twentyfour")) {
            MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation().getPermissions()[28] = true;
        } else if (BuildConfig.FLAVOR.equals("nysc")) {
            MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation().getPermissions()[26] = true;
        } else if (BuildConfig.FLAVOR.equals("golds")) {
            MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation().getPermissions()[25] = true;
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof LogInFragment) {
            if (NetworkUtil.isConnected(mBaseActivity)) {
                ((LogInFragment) fragment).enableButtons();
            } else {
                ((LogInFragment) fragment).disableButtons();
            }
        }
    }

    @Override
    public void createAccount(String userName) {
        Intent intent = new Intent(mBaseActivity, TabletCreateAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void requestPasswordReset(String userName) {
        BackEnd.retrievePasswordTask(mBaseActivity, null, userName, new StringListener() {
            @Override
            public void onResponse(String s) {
                ForgotSomethingResponse forgotSomethingResponse = GsonSingleton.getInstance().getGson().fromJson(s, ForgotSomethingResponse.class);
                switch (forgotSomethingResponse.getSuccess()) {
                    case 1:
                        onEmailSent(forgotSomethingResponse.getEmail());
                        break;
                    case 2:
                    default:
                        onEmailNotSent();
                        break;
                }
            }

            @Override
            public void onError(VolleyError volleyError) {
                onEmailNotSent();
                ClubcomLog.appendLog(ClubcomLog.appendErrorEntry(ClubcomLog.ERROR_TYPE.INVALID_SERVER_RESPONSE, "There was an error retrieving the account - " + volleyError.getMessage(), null));
            }
        });
    }

    @Override
    public void sendPasswordChangeRequest(LogInUserObject logInUserObject) {
        BackEnd.changePassword(mBaseActivity, logInUserObject, new JsonElementListener() {
            @Override
            public void onResponse(JsonElement jsonElement) {
                //ignored
            }

            @Override
            public void onError(VolleyError volleyError) {
                //ignored
            }
        });
    }

    private void saveCredentials(String username, String password) {
        Credential credential = new Credential.Builder(username)
                .setPassword(password)
                .build();

        MainApplication.sUserCredential = credential;

        Auth.CredentialsApi.save(mGoogleApiClient, credential).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    Toast.makeText(mBaseActivity, R.string.saved_credentials, Toast.LENGTH_SHORT).show();
                    startPreroll();
                } else {
                    if (status.hasResolution()) {
//                         Try to resolve the save request. This will prompt the user if the credential is new.
                        try {
                            status.startResolutionForResult(mBaseActivity, TabletLogInActivity.RC_SAVE);
                        } catch (IntentSender.SendIntentException e) {
                            // Could not resolve the request
                            Log.e(TAG, "STATUS: Failed to send resolution.", e);
                            Toast.makeText(mBaseActivity, R.string.credential_save_failed, Toast.LENGTH_SHORT).show();
                            startPreroll();
                        }
                    } else {
                        // Request has no resolution
                        Toast.makeText(mBaseActivity, R.string.credential_save_failed, Toast.LENGTH_SHORT).show();
                        startPreroll();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestCredentials();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mLogInFragment != null) {
            mLogInFragment.enableButtons();
        }

        mCredentialsRequested = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        NetworkStateReceiver.setListener(mNetworkStateReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();

        NetworkStateReceiver.clearListener();
    }

    @Override
    public void onBackPressed() {
        if (mLogInFragment != null && mLogInFragment.isAnimated()) {
            mLogInFragment.clearAnimations();
        } else if (mLogInFragment != null && mLogInFragment.isAnimating()) {
            //Ignore
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void logOnSucceeded(boolean stayLoggedIn, String username, String password) {
        if (stayLoggedIn) {
            saveCredentials(username, password);
        } else {
            startPreroll();
        }
    }

    @Override
    public LogInFragment getLogInFragment() {
        return new TabletLogInFragment();
    }

    protected void startPreroll() {
        Intent intent = new Intent(mBaseActivity, TabletPrerollActivity.class);
        startActivity(intent);
        hideProgressDialog();
        finish();
    }

    protected void logOnDemoMode() {
        //TODO is this needed? - I think CMS achieves this - maybe needed if CMS isn't running
        UserNetworkObject userNetworkObject = new UserNetworkObject();
        userNetworkObject.setGuestLogIn(false);
        MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation().setLoggedIn(true);
        MainApplication.sTabletLogInObject.getLogInNetworkObject().getUserInformation().setGuestLogIn(false);

        startPreroll();
    }

    @Override
    protected void updateUserObject(UserNetworkObject userNetworkObject) {
        MainApplication.sTabletLogInObject.getLogInNetworkObject().setUserInformation(userNetworkObject);
    }

    @Override
    public void writeLogEntry(String log) {
        LogReporter.reportLog(mBaseActivity, log);
    }
}
