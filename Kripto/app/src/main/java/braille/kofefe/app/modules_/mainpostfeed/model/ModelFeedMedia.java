package braille.kofefe.app.modules_.mainpostfeed.model;

/**
 * Created by Snow-Dell-05 on 06-Jan-18.
 */

public class ModelFeedMedia {
    private String mediaUrl;
    private String mediaType;
    private String thumbnailUrl;

    public ModelFeedMedia(String mediaUrl, String mediaType, String thumbnailUrl) {
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }


    public String getMediaType() {
        return mediaType;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public String toString() {
        return "ClassPojo [mediaUrl = " + mediaUrl + ", mediaType = " + mediaType + "]";
    }
}
