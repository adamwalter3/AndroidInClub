package com.clubcom.inclub.util;

import com.clubcom.communicationframework.model.ads.AdOrder;
import com.clubcom.communicationframework.model.ads.BrandedChannelObject;
import com.clubcom.communicationframework.model.ads.ContentItem;
import com.clubcom.communicationframework.model.ads.VideoCollectionVideo;
import com.clubcom.communicationframework.model.apps.MenuContent;

import java.util.ArrayList;

/**
 * Created by adamwalter3 on 8/1/16.
 */
public class AdOrderHelper {
    public static BrandedChannelObject getBrandedChannelFromAdOrder(AdOrder order) {
        ArrayList<VideoCollectionVideo> videos = new ArrayList<>();
        BrandedChannelObject brandedChannelObject = new BrandedChannelObject();

        for (ContentItem item : new ArrayList<>(order.getContentItems().values())) {
            if (item.getXmlContent() != null && item.getName().endsWith("VC")) {
                if (item.getXmlContent().getTemplate() != null) {
                    MenuContent menuContent = item.getXmlContent();
                    brandedChannelObject = new BrandedChannelObject(order.getOrderName(), menuContent);
                    System.out.println("Adding Branding: " + brandedChannelObject.getTitle());
                } else {
                    //ClubcomLog.appendErrorEntry(ClubcomLog.ERROR_TYPE.JSON_FILE_INVALID, "Received Video Collection with empty branding template", null);
                }
            } else {
                if (item.getXmlContent() != null && item.getXmlContent().getTemplate() != null) {
                    MenuContent menuContent = item.getXmlContent();
                    VideoCollectionVideo videoObject = new VideoCollectionVideo(menuContent);
                    videoObject.setSequence(item.getSequence());
                    if (videoObject.getContentId() != null) {
                        videos.add(videoObject);
                    } else {
                        //ClubcomLog.appendErrorEntry(ClubcomLog.ERROR_TYPE.JSON_FILE_INVALID, "Received Video but video does not exist", null);
                    }
                } else {
                    //ClubcomLog.appendErrorEntry(ClubcomLog.ERROR_TYPE.JSON_FILE_INVALID, "Received Video but video has no template", null);
                }
            }
        }

        brandedChannelObject.setVideoCollectionVideos(videos);
        return brandedChannelObject;
    }
}
