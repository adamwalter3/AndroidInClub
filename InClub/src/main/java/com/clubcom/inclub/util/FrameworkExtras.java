package com.clubcom.inclub.util;

/**
 * Created by adamwalter3 on 7/11/16.
 */
public class FrameworkExtras {
    public static String SYSTEM_SERVICE_PACKAGE_NAME = "com.clubcom.contentmanagementservice";
    public static String SYSTEM_SERVICE_COMPONENT_NAME = "com.clubcom.contentmanagementservice.service.MainService";

    public static String UPDATE_SERVICE_PACKAGE_NAME = "com.clubcom.updateservice";
    public static String UPDATE_SERVICE_COMPONENT_NAME = "com.clubcom.updateservice.MainService";

    public static String TV_SERVICE_PACKAGE_NAME = "com.clubcom.tvservice";
    public static String TV_SERVICE_COMPONENT_NAME = "com.clubcom.tvservice.service.MainService";

    public static String MAINTENANCE_MODE_COMPONENT_NAME = "com.clubcom.contentmanagementservice.activity.MaintenanceModeActivity";

    public static String SYSTEM_SETTINGS_PACKAGE_NAME = "com.android.settings";
    public static String SYSTEM_SETTINGS_COMPONENT_NAME = "com.android.settings.Settings";

    public static String DEFAULT_LAUNCHER_PACKAGE_NAME = "com.clubcom.launcher";
    public static String DEFAULT_LAUNCH_SCREEN_COMPONENT_NAME = ".activity.LaunchScreenActivity";
    public static String DEFAULT_START_SCREEN_COMPONENT_NAME = ".activity.StartScreenActivity";

    public static String GROUP_X_PACKAGE_NAME = "com.clubcom.groupx";
    public static String GROUP_X_CLASS_SCHEDULE_ACTIVITY = "activity.ClassScheduleActivity";

    public static String EXTRA_SESSION_INFO = "com.clubcom.launcher.session.info";
    public static String EXTRA_DEMO_MODE = "com.clubcom.launcher.demo.mode";
    public static String EXTRA_CHANNEL_LIST = "com.clubcom.launcher.channel.list";
    public static String EXTRA_GROUP_X = "com.clubcom.launcher.group.x";
    public static String EXTRA_PERSONAL_TRAINER_DATA = "com.clubcom.launcher.personal.trainer.data";
    public static String EXTRA_LAYOUT_DATA = "com.clubcom.launcher.layout.data";
    public static String EXTRA_ACTIVITY_NAME = "com.clubcom.launcher.activity.name";

    public static String ACTION_LOG_OUT = "com.clubcom.pvsframework.action.log.out";
    public static String ACTION_END_WORKOUT = "com.clubcom.android.systemui.action.END_WORKOUT";
    public static String ACTION_HOME = "com.clubcom.android.systemui.action.HOME";


    public static String ACTION_SESSION_UPDATED = "com.clubcom.pvsframework.action.session.updated";
    public static String EXTRA_SESSION_JSON = "com.clubcom.pvsframework.extra.session.json";

    public static String ACTION_WEATHER_UPDATED = "com.clubcom.pvsframework.action.weather.updated";
    public static String ACTION_GROUPX_UPDATED = "com.clubcom.pvsframework.action.groupx.updated";

    public static String ACTION_SIMULATE_CLICK = "com.clubcom.pvsframework.action.SIMULATE_CLICK";
    public static String EXTRA_VIEW_ID = "com.clubcom.pvsframework.extra.VIEW_ID";

    public static final String EXTRA_RESULT_RECEIVER = "com.clubcom.systemservices.extra.RESULT_RECEIVER";

    //For login after session start
    public static final String EXTRA_RESUME_COMPONENT = "com.clubcom.pvsframework.extra.RESUME_COMPONENT";

    public static final String EMAIL_REGEX =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    //For NavBar
    public static final String ACTION_NAVBAR_CLICK = "com.clubcom.android.systemui.action.NAVBAR_CLICK";
    public static final String EXTRA_NAVBAR_SELECTION = "com.clubcom.android.systemui.extra.NAVBAR_SELECTION";
    public static final String NAVBAR_HOME = "com.clubcom.android.systemui.action.HOME";
    public static final String NAVBAR_BACK = "com.clubcom.android.systemui.action.BACK";
    public static final String NAVBAR_GUEST_LOGIN = "com.clubcom.android.systemui.action.GUEST_LOGIN";
    public static final String NAVBAR_CREATE_ACCOUNT = "com.clubcom.android.systemui.action.CREATE_ACCOUNT";
    public static final String NAVBAR_FORGOT_PASSWORD = "com.clubcom.android.systemui.action.FORGOT_PASSWORD";
    public static final String NAVBAR_PREV_SCREEN = "com.clubcom.android.systemui.action.PREVIOUS_SCREEN";
    public static final String NAVBAR_NEXT_SCREEN = "com.clubcom.android.systemui.action.NEXT_SCREEN";
    public static final String NAVBAR_ACCOUNT_PANEL = "com.clubcom.android.systemui.action.ACCOUNT_PANEL";
    public static final String NAVBAR_END_WORKOUT = "com.clubcom.android.systemui.action.END_WORKOUT";
    public static final String NAVBAR_BANNER = "com.clubcom.android.systemui.action.BANNER";

    //For keyboard
    public static final String ACTION_CONFIG_KEYBOARD = "com.android.inputmethod.latin.CONFIG_KEYBOARD";
    public static final String EXTRA_BACKGROUND_COLOR = "com.android.inputmethod.latin.BACKGROUND_COLOR";
    public static final String EXTRA_KEY_COLOR = "com.android.inputmethod.latin.KEY_COLOR";
    public static final String EXTRA_KEY_PRESSED_COLOR = "com.android.inputmethod.latin.KEY_PRESSED_COLOR";

    //For UpdateService
    public static final String EXTRA_MODEL_NUMBER = "com.clubcom.systemservices.extra.MODEL_NUMBER";//Behavior of rom updates may change depending on device type
    public static final int ARG_TASK_SUCCESS = 1;
    public static final int ARG_TASK_FAILED = -1;

    public static final String KEY_STORE_PASSWORD = "Dr0wssap";

    public static final String INTENT_SHOW_START_SCREEN_CLASS = "com.clubcom.androidlauncher.SHOW_START_SCREEN";

    public static final String EXTRA_START_SCREEN_USER_ID = "com.clubcom.androidlauncher.start_screen_user_id";

    public static final String INTENT_START_BRANDED_CHANNEL = "com.clubcom.brandedchannel.START_CHANNEL";
    public static final String EXTRA_BRANDED_CHANNEL_INFO = "com.clubcom.brandedchannel.CHANNEL_INFO";

    public static final String INTENT_START_MY_ACCOUNT = "com.clubcom.myaccount.START_ACCOUNT";

    public static final String INTENT_UPDATE_THEME = "com.clubcom.launcher.startscreenactivity.UPDATE_THEME";
    public final static String EXTRA_TRAINER = "Trainer";

    public final static String EXTRA_CREATE_ACCOUNT_SHOULD_RESUME_LAUNCHER = "com.clubcom.pvsframework.create.account.RESUME_LAUNCHER";
    public final static String INTENT_FORGOT_SOMETHING = "com.clubcom.androidaccountmanager.activity.account.FORGOT_SOMETHING";

    public final static String INTENT_SHOW_REBOOT_DIALOG = "com.clubcom.reboot.Broadcast";

    public static final String EXTRA_SCREEN_ID_TAG = "com.clubcom.androidlauncher.screen_id_tag";

    public static final String INTENT_START_SCREEN_LOADED = "com.clubcom.launcher.start_screen_loaded";
}
