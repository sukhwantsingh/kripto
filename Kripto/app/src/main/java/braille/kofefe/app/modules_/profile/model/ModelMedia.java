package braille.kofefe.app.modules_.profile.model;

/**
 * Created by Snow-Dell-05 on 06-Jan-18.
 */

public class ModelMedia {
    private String mediaUrl;
    private String mediaType;

    public ModelMedia(String mediaUrl, String mediaType) {
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }


    public String getMediaType() {
        return mediaType;
    }

    @Override
    public String toString() {
        return "ClassPojo [mediaUrl = " + mediaUrl + ", mediaType = " + mediaType + "]";
    }
}
