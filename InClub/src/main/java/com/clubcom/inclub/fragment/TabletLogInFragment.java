package com.clubcom.inclub.fragment;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clubcom.ccframework.FrameworkApplication;
import com.clubcom.ccframework.fragment.LogInFragment;
import com.clubcom.ccframework.util.ColorFilterUtility;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.R;
import com.clubcom.inclub.activity.TabletCreateAccountActivity;
import com.clubcom.inclub.activity.TabletLogInActivity;
import com.clubcom.inclub.receiver.NetworkStateReceiver;
import com.clubcom.inclub.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamwalter3 on 7/1/16.
 */
public class TabletLogInFragment extends LogInFragment implements View.OnClickListener {
    private WifiManager mWifiManager;
    private ArrayList<ScanResult> mWifiConfigurations = new ArrayList<>();
    private CheckBox vStayLoggedIn;
    private View vCreateAccountButton = null;

    private WifiScanResultReceiver mWifiScanResultReceiver;
    private IntentFilter mWifiScanResultIntentFilter;

    public TabletLogInFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final RelativeLayout v = (RelativeLayout) super.onCreateView(inflater, container, savedInstanceState);

        mWifiScanResultIntentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mWifiScanResultReceiver = new WifiScanResultReceiver();

        vStayLoggedIn = new CheckBox(mBaseActivity);
        vStayLoggedIn.setText(getString(R.string.log_in_stay_logged_in));
        vStayLoggedIn.setChecked(true);
        vStayLoggedIn.setTextColor(ContextCompat.getColor(mBaseActivity, android.R.color.white));
        vStayLoggedIn.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, R.id.number_pad);
        params.setMarginStart(getResources().getDimensionPixelSize(R.dimen.log_in_button_spacing));
        vStayLoggedIn.setLayoutParams(params);
        if (v != null) {
            v.addView(vStayLoggedIn);
        }

        return v;
    }

    @Override
    protected void addButtonsToAnimate() {
        super.addButtonsToAnimate();
        vCreateAccountButton = createLogInButton(getString(R.string.log_in_button_create_account), ContextCompat.getDrawable(mBaseActivity, R.drawable.create_account_user_id_drawable), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonsEnabled) {
                    createAccount();
                } else {
                    buttonNotEnabled();
                }
            }
        });

        mButtonsToAnimate.add(vCreateAccountButton);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void createAccount() {
        Intent intent = new Intent(mBaseActivity, TabletCreateAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void logIn() {
        if (vUsername.getText().length() < 4) {
            vUsername.setError(getString(R.string.log_in_username_length_error));
            return;
        }

        if (vPassword.getText().length() < 1) {
            vPassword.setError(getString(R.string.log_in_enter_password_error));
            return;
        }

        ((TabletLogInActivity) getActivity()).logIn(vUsername.getText().toString(), vPassword.getText().toString(), vStayLoggedIn.isChecked());
    }

    @Override
    protected void setLogo() {
        vLogo.setImageDrawable(ContextCompat.getDrawable(mBaseActivity, R.drawable.login_logo));
    }

    @Override
    protected int getMenuBackgroundColor() {
        return ContextCompat.getColor(mBaseActivity, R.color.menu_bg_color);
    }

    public boolean isAnimated() {
        return mLogInAnimated;
    }

    public boolean isAnimating() {
        return mIsAnimating;
    }

    private void scanWifi() {
        startScan();
    }

    private void connectToWifi(ScanResult scanResult) {
        mBaseActivity.showProgressDialog("Connecting...", "Connecting to " + scanResult.SSID);
        NetworkUtil.connectToWifi(getActivity(), scanResult.SSID);
    }

    @Override
    protected void onLogInAnimationDone() {
        super.onLogInAnimationDone();
        fadeIn(vStayLoggedIn);
    }

    @Override
    protected void buttonNotEnabled() {
        super.buttonNotEnabled();

        ((TabletLogInActivity) mBaseActivity).showNotConnectedDialog();
    }

    @Override
    public void clearAnimations() {
        super.clearAnimations();
        vStayLoggedIn.setVisibility(View.GONE);
    }

    private void startScan() {
        System.out.println("Starting Scan");
        mBaseActivity.showProgressDialog("Searching...", "Searching For WiFi");

        mWifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

        int changeNetworkPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CHANGE_WIFI_STATE);

        if (changeNetworkPermission == PackageManager.PERMISSION_GRANTED) {
            if (!mWifiManager.isWifiEnabled()) {
                System.out.println("Enabling");
                mWifiManager.setWifiEnabled(true);
            }

            getActivity().registerReceiver(mWifiScanResultReceiver, mWifiScanResultIntentFilter);
            mWifiManager.startScan();
        } else {
            //TODO request permission
        }
    }

    public class WifiAdapter extends ArrayAdapter<ScanResult> {
        private List<ScanResult> mWifiConfigurations;

        public WifiAdapter(Context context, int resource, List<ScanResult> wifiConfigs) {
            super(context, resource);
            mWifiConfigurations = wifiConfigs;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.wifi_simple_list_layout, null);
            }

            ScanResult i = mWifiConfigurations.get(position);

            if (i != null && !i.SSID.isEmpty()) {
                TextView textView = (TextView) view.findViewById(R.id.wifi_name);
                textView.setText(i.SSID);
            }

            return view;
        }

        @Override
        public ScanResult getItem(int position) {
            return mWifiConfigurations.get(position);
        }

        @Override
        public int getCount() {
            return mWifiConfigurations.size();
        }

        @Override
        public void clear() {
            super.clear();
            mWifiConfigurations.clear();
            notifyDataSetChanged();
        }
    }

    public class WifiScanResultReceiver extends BroadcastReceiver {
        public WifiScanResultReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                getActivity().unregisterReceiver(mWifiScanResultReceiver);

                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

                List<ScanResult> list = wifi.getScanResults();
                for (ScanResult i : list) {
                    if (i.SSID != null && i.SSID.toLowerCase().startsWith("clubcom")) {
                        if (FrameworkApplication.CURRENT_NETWORK != null && FrameworkApplication.CURRENT_NETWORK.contains(i.SSID)) {
                            continue;
                        }

                        int c = 0;
                        for (; c < mWifiConfigurations.size(); c++) {
                            if (i.SSID.equalsIgnoreCase(mWifiConfigurations.get(c).SSID)) {
                                break;
                            }
                        }

                        if (c == mWifiConfigurations.size()) {
                            mWifiConfigurations.add(i);
                        }
                    }
                }

                if (mWifiConfigurations.size() > 1) {
                    mBaseActivity.hideProgressDialog();
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setTitle("Select Wifi");
                    final WifiAdapter wifiAdapter = new WifiAdapter(getActivity(), -1, mWifiConfigurations);
                    alertDialog.setSingleChoiceItems(wifiAdapter, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ScanResult scanResult = wifiAdapter.getItem(which);
                            connectToWifi(scanResult);
                            dialog.dismiss();
                        }
                    });

                    alertDialog.show();
                } else if (mWifiConfigurations.size() == 1) {
                    final ScanResult scanResult = mWifiConfigurations.get(0);
                    connectToWifi(scanResult);
                } else {
                    //none found
                }
            }
        }
    }

    public void setPasswordText(String text) {
        vPassword.setText(text);
    }
}
