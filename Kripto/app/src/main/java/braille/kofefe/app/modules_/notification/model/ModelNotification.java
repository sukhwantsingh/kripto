package braille.kofefe.app.modules_.notification.model;

/**
 * Created by Snow-Dell-05 on 31-Jan-18.
 */

public class ModelNotification {

    private String notificationId;
    private String notificationType;
    private String title;
    private String message;
    private String titleImage;
    private String bodyImage;
    private String notifiedDate;
    private Boolean viewed;
    private String uuid;

    public ModelNotification(String notificationId, String notificationType, String title, String message, String titleImage, String bodyImage, String notifiedDate, Boolean viewed, String uuid) {
        this.notificationId = notificationId;
        this.notificationType = notificationType;
        this.title = title;
        this.message = message;
        this.titleImage = titleImage;
        this.bodyImage = bodyImage;
        this.notifiedDate = notifiedDate;
        this.viewed = viewed;
        this.uuid = uuid;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getTitleImage() {
        return titleImage;
    }

    public String getBodyImage() {
        return bodyImage;
    }

    public String getNotifiedDate() {
        return notifiedDate;
    }

    public Boolean getViewed() {
        return viewed;
    }

    public String getUuid() {
        return uuid;
    }
}
