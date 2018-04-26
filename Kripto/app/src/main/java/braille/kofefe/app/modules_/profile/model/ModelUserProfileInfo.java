package braille.kofefe.app.modules_.profile.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Snow-Dell-05 on 12/27/2017.
 */

public class ModelUserProfileInfo implements Parcelable {

    private String uuid;
    private String name;
    private String username;
    private String bio;
    private String profilePic;
    private String coverPic;
    private Integer profileViewCount;
    private Integer totalPostViewCount;
    private String twitter;
    private String facebook;
    private String instagram;
    private String snapchat;
    private String followersCount;
    private String followingCount;
    private String totalPosts;
    private ModelLocation modelLocation;
    private String joined;
    private Boolean verified;
    private String relationship;

    private ModelTopFollowers mTopFollowers;
    private ModelTopFollowing mTopFollowing;
    private ModelRecentPosts[] mRecentPosts;

    public ModelUserProfileInfo(String uuid, String name, String username, String bio, String profilePic, String coverPic,
                                Integer profileViewCount, Integer totalPostViewCount, String twitter, String facebook,
                                String instagram, String snapchat, String followersCount, String followingCount,
                                String totalPosts, ModelLocation modelLocation, String joined, Boolean verified,
                                String relationship, ModelTopFollowers mTopFollowers, ModelTopFollowing mTopFollowing,
                                ModelRecentPosts[] mRecentPosts) {
        this.uuid = uuid;
        this.name = name;
        this.username = username;
        this.bio = bio;
        this.profilePic = profilePic;
        this.coverPic = coverPic;
        this.profileViewCount = profileViewCount;
        this.totalPostViewCount = totalPostViewCount;
        this.twitter = twitter;
        this.facebook = facebook;
        this.instagram = instagram;
        this.snapchat = snapchat;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.totalPosts = totalPosts;
        this.modelLocation = modelLocation;
        this.joined = joined;
        this.verified = verified;
        this.relationship = relationship;
        this.mTopFollowers = mTopFollowers;
        this.mTopFollowing = mTopFollowing;
        this.mRecentPosts = mRecentPosts;
    }

    protected ModelUserProfileInfo(Parcel in) {
        uuid = in.readString();
        name = in.readString();
        username = in.readString();
        bio = in.readString();
        profilePic = in.readString();
        coverPic = in.readString();
        twitter = in.readString();
        facebook = in.readString();
        instagram = in.readString();
        snapchat = in.readString();
        relationship = in.readString();
    }

    public static final Creator<ModelUserProfileInfo> CREATOR = new Creator<ModelUserProfileInfo>() {
        @Override
        public ModelUserProfileInfo createFromParcel(Parcel in) {
            return new ModelUserProfileInfo(in);
        }

        @Override
        public ModelUserProfileInfo[] newArray(int size) {
            return new ModelUserProfileInfo[size];
        }
    };

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public Integer getProfileViewCount() {
        return profileViewCount;
    }

    public void setProfileViewCount(Integer profileViewCount) {
        this.profileViewCount = profileViewCount;
    }

    public Integer getTotalPostViewCount() {
        return totalPostViewCount;
    }

    public void setTotalPostViewCount(Integer totalPostViewCount) {
        this.totalPostViewCount = totalPostViewCount;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getSnapchat() {
        return snapchat;
    }

    public void setSnapchat(String snapchat) {
        this.snapchat = snapchat;
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

    public String getTotalPosts() {
        return totalPosts;
    }

    public void setTotalPosts(String totalPosts) {
        this.totalPosts = totalPosts;
    }

    public ModelLocation getModelLocation() {
        return modelLocation;
    }

    public void setModelLocation(ModelLocation modelLocation) {
        this.modelLocation = modelLocation;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public ModelTopFollowers getmTopFollowers() {
        return mTopFollowers;
    }

    public void setmTopFollowers(ModelTopFollowers mTopFollowers) {
        this.mTopFollowers = mTopFollowers;
    }

    public ModelTopFollowing getmTopFollowing() {
        return mTopFollowing;
    }

    public void setmTopFollowing(ModelTopFollowing mTopFollowing) {
        this.mTopFollowing = mTopFollowing;
    }

    public ModelRecentPosts[] getmRecentPosts() {
        return mRecentPosts;
    }

    public void setmRecentPosts(ModelRecentPosts[] mRecentPosts) {
        this.mRecentPosts = mRecentPosts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(uuid);
        parcel.writeString(name);
        parcel.writeString(username);
        parcel.writeString(bio);
        parcel.writeString(profilePic);
        parcel.writeString(coverPic);
        parcel.writeString(twitter);
        parcel.writeString(facebook);
        parcel.writeString(instagram);
        parcel.writeString(snapchat);
        parcel.writeString(relationship);
    }
}