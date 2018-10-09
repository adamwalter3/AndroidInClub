package com.clubcom.inclub.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;

import com.clubcom.ccframework.FrameworkApplication;
import com.clubcom.inclub.MainApplication;
import com.clubcom.inclub.activity.TabletCreateAccountActivity;
import com.clubcom.inclub.activity.TabletLogInActivity;
import com.clubcom.inclub.util.NetworkUtil;

/**
 * Created by adamwalter3 on 7/19/16.
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    private static OnNetworkStateUpdated mListener;

    //TODO update network states for different events
    @Override
    public void onReceive(final Context context, final Intent intent) {
        System.out.println("Network Changed: " + intent.toString());
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            if (intent.hasExtra(WifiManager.EXTRA_NETWORK_INFO)) {
                final NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                System.out.println(networkInfo.toString());
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    if (mListener != null) {
                        NetworkUtil.checkConnections(context, new NetworkUtil.ConnectionCheckComplete() {
                            @Override
                            public void complete() {
                                mListener.networkConnected(networkInfo.getExtraInfo());
                                if ((FrameworkApplication.NETWORK_STATE_CURRENT && FrameworkApplication.WIFI_CONNECTED_CLUBCOM) || (FrameworkApplication.NETWORK_STATE_CURRENT && FrameworkApplication.CORPORATE_CALLS_AVAILABLE && MainApplication.sIsDemoMode)) {
                                    //do nothing
                                } else {
                                    startLogInActivity(context);
                                }

                            }
                        });
                    } else {
                        System.out.println("Listener was null");
                    }
                } else {
                    if (mListener != null) {
                        NetworkUtil.checkConnections(context, new NetworkUtil.ConnectionCheckComplete() {
                            @Override
                            public void complete() {
                                mListener.networkStateChanged(networkInfo.getState().toString());
                                if ((FrameworkApplication.NETWORK_STATE_CURRENT && FrameworkApplication.WIFI_CONNECTED_CLUBCOM) || (FrameworkApplication.NETWORK_STATE_CURRENT && FrameworkApplication.CORPORATE_CALLS_AVAILABLE && MainApplication.sIsDemoMode)) {
                                    //do nothing
                                } else {
                                    startLogInActivity(context);
                                }
                            }
                        });
                    } else {
                        System.out.println("Listener was null");
                    }
                    System.out.println("State Changed: " + networkInfo.getState().toString());
                    //TODO notify state
                }
            }
        } if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            System.out.println("WifiReceiver" + ">>>>SUPPLICANT_STATE_CHANGED_ACTION<<<<<<");
            SupplicantState suppState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);

            switch (suppState) {
                case ASSOCIATED:
                    System.out.println("SupplicantState" + "ASSOCIATED");
                    break;
                case ASSOCIATING:
                    System.out.println("SupplicantState" + "ASSOCIATING");
                    break;
                case AUTHENTICATING:
                    System.out.println("SupplicantState" + "Authenticating...");
                    break;
                case COMPLETED:
                    System.out.println("SupplicantState" + "Connected");
                    break;
                case DISCONNECTED:
                    System.out.println("SupplicantState" + "Disconnected");
                    NetworkUtil.checkConnections(context, new NetworkUtil.ConnectionCheckComplete() {
                        @Override
                        public void complete() {
                            if (intent.getExtras().containsKey(WifiManager.EXTRA_SUPPLICANT_ERROR)) {
                                int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0);
                                if (error == 1) {
                                    if (mListener != null) {
                                        mListener.failedToConnect();
                                        if ((FrameworkApplication.NETWORK_STATE_CURRENT && FrameworkApplication.WIFI_CONNECTED_CLUBCOM) || (FrameworkApplication.NETWORK_STATE_CURRENT && FrameworkApplication.CORPORATE_CALLS_AVAILABLE && MainApplication.sIsDemoMode)) {
                                            //do nothing
                                        } else {
                                            startLogInActivity(context);
                                        }
                                    }
                                }
                            }
                        }
                    });
                    break;
                case DORMANT:
                    System.out.println("SupplicantState" + "DORMANT");
                    break;
                case FOUR_WAY_HANDSHAKE:
                    System.out.println("SupplicantState" + "FOUR_WAY_HANDSHAKE");
                    break;
                case GROUP_HANDSHAKE:
                    System.out.println("SupplicantState" + "GROUP_HANDSHAKE");
                    break;
                case INACTIVE:
                    System.out.println("SupplicantState" + "INACTIVE");
                    break;
                case INTERFACE_DISABLED:
                    System.out.println("SupplicantState" + "INTERFACE_DISABLED");
                    break;
                case INVALID:
                    System.out.println("SupplicantState" + "INVALID");
                    break;
                case SCANNING:
                    System.out.println("SupplicantState" + "SCANNING");
                    break;
                case UNINITIALIZED:
                    System.out.println("SupplicantState" + "UNINITIALIZED");
                    break;
                default:
                    System.out.println("SupplicantState" + "Unknown");
                    break;

            }
        }
    }

    public static void setListener(OnNetworkStateUpdated listener) {
        mListener = listener;
    }

    public static void clearListener() {
        mListener = null;
        System.out.println("Listener Clear Called");
    }

    public interface OnNetworkStateUpdated {
        void networkConnected(String name);
        void networkStateChanged(String details);
        void failedToConnect();
    }

    public static void startLogInActivity(Context context) {
        Intent i = new Intent(context, TabletLogInActivity.class);
        // set the new task and clear flags
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }
}
