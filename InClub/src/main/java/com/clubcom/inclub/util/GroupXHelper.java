package com.clubcom.inclub.util;

import com.clubcom.communicationframework.model.club.GroupX;
import com.clubcom.inclub.R;

/**
 * Created by adamwalter3 on 8/8/16.
 */
public class GroupXHelper {
    public static int getResourceForClassType(String classType, boolean foreground) {
        if (foreground) {
            switch (classType.toLowerCase()) {
                case GroupX.CLASS_NAME_LES_MILLES:
                    return R.drawable.default_les_milles_icon;
                case GroupX.CLASS_NAME_BTS:
                    return R.drawable.default_bts_icon;
                case GroupX.CLASS_NAME_ZUMBA:
                case "zumbaÂ®":
                    return R.drawable.default_zumba_icon;
                case GroupX.CLASS_NAME_SPINNING:
                    return R.drawable.default_spinning_icon;
                case GroupX.CLASS_NAME_KINESIS:
                    return R.drawable.default_kinesis_icon;
                case GroupX.CLASS_NAME_AEROBICS:
                    return R.drawable.default_aerobics_icon;
                case GroupX.CLASS_NAME_STEP:
                    return R.drawable.default_step_icon;
                case GroupX.CLASS_NAME_YOGA:
                    return R.drawable.default_yoga_icon;
                case GroupX.CLASS_NAME_PILATES:
                    return R.drawable.default_pilates_icon;
                case GroupX.CLASS_NAME_STRENGTH:
                    return R.drawable.default_strength_icon;
                case GroupX.CLASS_NAME_AQUA:
                    return R.drawable.default_aqua_icon;
                case GroupX.CLASS_NAME_KICKBOXING:
                    return R.drawable.default_kick_boxing_icon;
                case GroupX.CLASS_NAME_CYCLING:
                    return R.drawable.default_cycling_icon;
                case GroupX.CLASS_NAME_SENIOR:
                    return R.drawable.default_senior_icon;
                case GroupX.CLASS_NAME_HIP_HOP:
                    return R.drawable.default_hip_hop_icon;
                case GroupX.CLASS_NAME_LATIN_DANCE:
                    return R.drawable.default_latin_dance_icon;
                case GroupX.CLASS_NAME_BOOT_CAMP:
                    return R.drawable.default_boot_camp_icon;
                case GroupX.CLASS_NAME_BOXING:
                    return R.drawable.default_boxing_icon;
                case GroupX.CLASS_NAME_DANCE:
                    return R.drawable.default_dance_icon;
                case GroupX.CLASS_NAME_CROSS_TRAINING:
                    return R.drawable.default_cross_training_icon;
                case GroupX.CLASS_NAME_POWDER_BLUE:
                case GroupX.CLASS_NAME_GENERAL:
                default:
                    return R.drawable.default_general_icon;
            }
        } else {
            switch (classType.toLowerCase()) {
                case GroupX.CLASS_NAME_LES_MILLES:
                    return R.drawable.default_les_milles_bg;
                case GroupX.CLASS_NAME_BTS:
                    return R.drawable.default_bts_bg;
                case GroupX.CLASS_NAME_ZUMBA:
                case "zumbaÂ®":
                    return R.drawable.default_zumba_bg;
                case GroupX.CLASS_NAME_SPINNING:
                    return R.drawable.default_spinning_bg;
                case GroupX.CLASS_NAME_KINESIS:
                    return R.drawable.default_kinesis_bg;
                case GroupX.CLASS_NAME_POWDER_BLUE:
                    //TODO Replace with actual powder blue
                    return R.drawable.default_class_schedule_activity_schedule_background;
                case GroupX.CLASS_NAME_GENERAL:
                    //TODO replcae with actual general bg
                    return R.drawable.default_class_schedule_activity_schedule_background;
                case GroupX.CLASS_NAME_AEROBICS:
                    return R.drawable.default_aerobics_bg;
                case GroupX.CLASS_NAME_STEP:
                    return R.drawable.default_step_bg;
                case GroupX.CLASS_NAME_YOGA:
                    return R.drawable.default_yoga_bg;
                case GroupX.CLASS_NAME_PILATES:
                    return R.drawable.default_pilates_bg;
                case GroupX.CLASS_NAME_STRENGTH:
                    return R.drawable.default_strength_bg;
                case GroupX.CLASS_NAME_AQUA:
                    return R.drawable.default_aqua_bg;
                case GroupX.CLASS_NAME_KICKBOXING:
                    return R.drawable.default_kick_boxing_bg;
                case GroupX.CLASS_NAME_CYCLING:
                    return R.drawable.default_cycling_bg;
                case GroupX.CLASS_NAME_SENIOR:
                    return R.drawable.default_senior_bg;
                case GroupX.CLASS_NAME_HIP_HOP:
                    return R.drawable.default_hip_hop_bg;
                case GroupX.CLASS_NAME_LATIN_DANCE:
                    return R.drawable.default_latin_dance_bg;
                case GroupX.CLASS_NAME_BOOT_CAMP:
                    return R.drawable.default_boot_camp_bg;
                case GroupX.CLASS_NAME_BOXING:
                    return R.drawable.default_boxing_bg;
                case GroupX.CLASS_NAME_DANCE:
                    return R.drawable.default_dance_bg;
                case GroupX.CLASS_NAME_CROSS_TRAINING:
                    return R.drawable.default_cross_training_bg;
                default:
                    return R.drawable.default_class_schedule_activity_schedule_background;
            }
        }
    }
}