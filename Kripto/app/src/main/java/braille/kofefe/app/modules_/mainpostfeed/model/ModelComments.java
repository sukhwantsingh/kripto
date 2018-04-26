package braille.kofefe.app.modules_.mainpostfeed.model;

/**
 * Created by Snow-Dell-05 on 30-Jan-18.
 */

public class ModelComments {

    private String postUuid;
    private String comment_id;


    private String name;
    private String userName;
    private String profilePic;
    private String relationship;
    private String uuid;

    private String bio;
    private String followersCount;
    private String followingCount;
    private String joined;

    private String loc_name;
    private String loc_city;
    private String loc_state;
    private String loc_country;

    private String comment_;
    private String commented_date;
    private boolean comment_deleted;

    private String media_url;
    private String media_type;
    private String media_thumbnailUrl;


    public ModelComments(String postUuid, String comment_id, String name, String userName,
                         String profilePic, String relationship, String uuid, String bio,
                         String followersCount, String followingCount, String joined, String loc_name,
                         String loc_city, String loc_state, String loc_country, String comment_,
                         String commented_date, boolean comment_deleted, String media_url,
                         String media_type, String media_thumbnailUrl) {

        this.postUuid = postUuid;
        this.comment_id = comment_id;
        this.name = name;
        this.userName = userName;
        this.profilePic = profilePic;
        this.relationship = relationship;
        this.uuid = uuid;
        this.bio = bio;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.joined = joined;
        this.loc_name = loc_name;
        this.loc_city = loc_city;
        this.loc_state = loc_state;
        this.loc_country = loc_country;
        this.comment_ = comment_;
        this.commented_date = commented_date;
        this.comment_deleted = comment_deleted;
        this.media_url = media_url;
        this.media_type = media_type;
        this.media_thumbnailUrl = media_thumbnailUrl;

    }

    public String getPostUuid() {
        return postUuid;
    }

    public String getComment_id() {
        return comment_id;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getUuid() {
        return uuid;
    }

    public String getBio() {
        return bio;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public String getJoined() {
        return joined;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public String getLoc_city() {
        return loc_city;
    }

    public String getLoc_state() {
        return loc_state;
    }

    public String getLoc_country() {
        return loc_country;
    }

    public String getComment_() {
        return comment_;
    }

    public String getCommented_date() {
        return commented_date;
    }

    public boolean isComment_deleted() {
        return comment_deleted;
    }

    public String getMedia_url() {
        return media_url;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getMedia_thumbnailUrl() {
        return media_thumbnailUrl;
    }


}
