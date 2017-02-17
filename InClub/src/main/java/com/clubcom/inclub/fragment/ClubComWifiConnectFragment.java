package com.clubcom.inclub.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clubcom.ccframework.fragment.BaseFragment;
import com.clubcom.inclub.R;
import com.clubcom.inclub.activity.SplashScreenActivity;
import com.clubcom.inclub.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamwalter3 on 7/1/16.
 */
public class ClubComWifiConnectFragment extends BaseFragment {
    private ListView vWifiList;
    private TextView vWifiTitle;
    private ProgressBar vWifiLoadingIndicator;
    private ImageView vWifiRefresh;

    private WifiScanResultReceiver mWifiScanResultReceiver;
    private IntentFilter mWifiScanResultIntentFilter;

    private ConnectionChangeReceiver mConnectionChangeResultReceiver;
    private IntentFilter mConnectionChangeIntentFilter;

    private ArrayList<ScanResult> mWifiConfigurations = new ArrayList<>();

    private WifiManager mWifiManager;

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mConnectionChangeResultReceiver, mConnectionChangeIntentFilter);
        startScan();
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(mConnectionChangeResultReceiver);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWifiScanResultIntentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        mWifiScanResultReceiver = new WifiScanResultReceiver();

        mConnectionChangeIntentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        mConnectionChangeResultReceiver = new ConnectionChangeReceiver();
    }

    private void startScan() {
        System.out.println("Starting Scan");

        ((WifiAdapter) vWifiList.getAdapter()).clear();
        vWifiLoadingIndicator.setVisibility(View.VISIBLE);

        mWifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

        int changeNetworkPermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CHANGE_WIFI_STATE);

        if (changeNetworkPermission == PackageManager.PERMISSION_GRANTED) {
            if (!mWifiManager.isWifiEnabled()) {
                mWifiManager.setWifiEnabled(true);
            }

            getActivity().registerReceiver(mWifiScanResultReceiver, mWifiScanResultIntentFilter);
            mWifiManager.startScan();
        } else {
            //TODO request permission
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clubcom_wifi_connection_layout, container, false);
        vWifiList = (ListView) view.findViewById(R.id.wifi_list_view);
        vWifiTitle = (TextView) view.findViewById(R.id.wifi_list_view_title);
        vWifiLoadingIndicator = (ProgressBar) view.findViewById(R.id.wifi_loading);
        vWifiRefresh = (ImageView) view.findViewById(R.id.wifi_refresh);

        vWifiList.setAdapter(new WifiAdapter(getActivity(), -1, mWifiConfigurations));

        vWifiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO add dialog about always connecting
                final ScanResult scanResult = (ScanResult) parent.getAdapter().getItem(position);
                NetworkUtil.connectToWifi(getActivity(), scanResult.SSID);
            }
        });

        vWifiRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vWifiLoadingIndicator.getVisibility() == View.VISIBLE) {
                    vWifiRefresh.animate().rotationBy(180).start();
                } else {
                    vWifiRefresh.animate().rotationBy(180).start();
                    startScan();
                }
            }
        });
        return view;
    }

    public class ConnectionChangeReceiver extends BroadcastReceiver {
        public ConnectionChangeReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println(intent.getAction());
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                if (intent.hasExtra(WifiManager.EXTRA_NETWORK_INFO)) {
                    NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    System.out.println(networkInfo.toString());
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        //send us back to the splash to intialize all the club info... maybe a different transition screen
                        Intent splashScreen = new Intent(context, SplashScreenActivity.class);
                        splashScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(splashScreen);
                    } else if(networkInfo.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                        //ignored... delete for release probably
                    }
                }
            }
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
                    if (i.SSID != null && i.SSID.toLowerCase().startsWith("clubco")) {
                        int c = 0;
                        for (; c < mWifiConfigurations.size(); c++) {
                            if (i.SSID.equalsIgnoreCase(mWifiConfigurations.get(c).SSID)) {
                                break;
                            }
                        }

                        if (c == mWifiConfigurations.size()) {
                            mWifiConfigurations.add(i);
                            ((WifiAdapter) vWifiList.getAdapter()).notifyDataSetChanged();
                        }
                    }
                }

                if (mWifiConfigurations.size() > 0) {
                    vWifiLoadingIndicator.setVisibility(View.GONE);
                } else {

                }
            }
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
}
