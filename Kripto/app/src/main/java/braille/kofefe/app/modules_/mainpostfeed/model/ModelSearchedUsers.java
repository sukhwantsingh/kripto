package braille.kofefe.app.modules_.mainpostfeed.model;

/**
 * Created by Snow-Dell-05 on 04-Jan-18.
 */

public class ModelSearchedUsers {

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

    private boolean mFlagCheckPost = false;

    private String postUserName;
    private String postTime;
    private String postContent;

    private String content;

    private String location_name;
    private String location_city;
    private String location_state;
    private String location_country;

    private ModelFeedMedia[] media;
    private ModelScrambledMedia[] scrambledMedia;
    private String postLockStatus;

    public ModelSearchedUsers(String name, String userName, String profilePic, String relationship, String uuid, String bio, String followersCount, String followingCount,
                              String joined, String loc_name, String loc_city, String loc_state, String loc_country,
                              boolean mFlagCheckPost, String postUserName, String postTime, String postContent,
                              String content, String location_name, String location_city, String location_state,
                              String location_country, ModelFeedMedia[] media, ModelScrambledMedia[] scrambledMedia,
                              String postLockStatus) {

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
        this.mFlagCheckPost = mFlagCheckPost;
        this.postUserName = postUserName;
        this.postTime = postTime;
        this.postContent = postContent;
        this.content = content;
        this.location_name = location_name;
        this.location_city = location_city;
        this.location_state = location_state;
        this.location_country = location_country;
        this.media = media;
        this.scrambledMedia = scrambledMedia;
        this.postLockStatus = postLockStatus;
    }

    public String getContent() {
        return content;
    }

    public String getLocation_name() {
        return location_name;
    }

    public String getLocation_city() {
        return location_city;
    }

    public String getLocation_state() {
        return location_state;
    }

    public String getLocation_country() {
        return location_country;
    }

    public ModelFeedMedia[] getMedia() {
        return media;
    }

    public ModelScrambledMedia[] getScrambledMedia() {
        return scrambledMedia;
    }

    public String getPostLockStatus() {
        return postLockStatus;
    }

    public String getPostUserName() {
        return postUserName;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getPostContent() {
        return postContent;
    }

    public boolean ismFlagCheckPost() {
        return mFlagCheckPost;
    }

    public String getBio() {
        return bio;
    }

    public String getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(String followersCount) {
        this.followersCount = followersCount;
    }

    public String getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(String followingCount) {
        this.followingCount = followingCount;
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

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getUuid() {
        return uuid;
    }
}
