package braille.kofefe.app.modules_.profile.model;

/**
 * Created by Snow-Dell-05 on 08-Jan-18.
 */

public class ModelFollowing {

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


    public ModelFollowing(String name, String userName, String profilePic, String relationship, String uuid, String bio, String followersCount, String followingCount, String joined, String loc_name, String loc_city, String loc_state, String loc_country) {

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
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
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
}
