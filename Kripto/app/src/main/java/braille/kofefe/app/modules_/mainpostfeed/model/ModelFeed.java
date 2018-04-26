package braille.kofefe.app.modules_.mainpostfeed.model;

/**
 * Created by Snow-Dell-05 on 12/13/2017.
 */

public class ModelFeed {
    private String user_name;
    private String user_userName;
    private String user_profilePic;
    private String user_relationship;
    private String user_uuid;

    private String user_bio;
    private String user_followersCount;
    private String user_followingCount;
    private String user_joined;

    private String user_loc_name;
    private String user_loc_city;
    private String user_loc_state;
    private String user_loc_country;


    private String content;
    private String defaultScrambledContent;
    private String postedDate;
    private String unlockInProgress;
    private String unlocked;

    private String liked;
    private String anger;
    private String sad;
    private String love;
    private String laugh;

    private String timerInSecs;
    private String commentsCount;
    private String lockExpiry;

    private String referenceUser;
    private String userReactionType;
    private String referenceReactionType;

    private String location_name;

    private String location_city;
    private String location_state;
    private String location_country;

    private String uuid;
    private ModelFeedMedia[] media;
    private ModelScrambledMedia[] scrambledMedia;
    private String postLockStatus;
    private String totalReactions;

    private boolean mFlagPlayAudio = false;

    public ModelFeed(String user_name, String user_userName, String user_profilePic, String user_relationship, String user_uuid,
                     String user_bio, String user_followersCount, String user_followingCount, String user_joined,
                     String user_loc_name, String user_loc_city, String user_loc_state, String user_loc_country,
                     String content, String defaultScrambledContent, String postedDate, String unlockInProgress,
                     String unlocked, String liked, String anger, String sad, String love, String laugh,
                     String timerInSecs, String commentsCount, String lockExpiry, String referenceUser,
                     String userReactionType, String referenceReactionType, String location_name, String location_city, String location_state, String location_country, String uuid, ModelFeedMedia[] media, ModelScrambledMedia[] scrambledMedia,
                     String postLockStatus, String totalReactions) {

        this.user_name = user_name;
        this.user_userName = user_userName;
        this.user_profilePic = user_profilePic;
        this.user_relationship = user_relationship;
        this.user_uuid = user_uuid;
        this.user_bio = user_bio;
        this.user_followersCount = user_followersCount;
        this.user_followingCount = user_followingCount;
        this.user_joined = user_joined;
        this.user_loc_name = user_loc_name;
        this.user_loc_city = user_loc_city;
        this.user_loc_state = user_loc_state;
        this.user_loc_country = user_loc_country;
        this.content = content;
        this.defaultScrambledContent = defaultScrambledContent;
        this.postedDate = postedDate;
        this.unlockInProgress = unlockInProgress;
        this.unlocked = unlocked;
        this.liked = liked;
        this.anger = anger;
        this.sad = sad;
        this.love = love;
        this.laugh = laugh;
        this.timerInSecs = timerInSecs;
        this.commentsCount = commentsCount;
        this.lockExpiry = lockExpiry;
        this.referenceUser = referenceUser;
        this.userReactionType = userReactionType;
        this.referenceReactionType = referenceReactionType;
        this.location_name = location_name;

        this.location_city = location_city;
        this.location_state = location_state;
        this.location_country = location_country;


        this.uuid = uuid;
        this.media = media;
        this.scrambledMedia = scrambledMedia;
        this.postLockStatus = postLockStatus;
        this.totalReactions = totalReactions;
    }

    public boolean isFlagPlayAudio() {
        return mFlagPlayAudio;
    }

    public void setFlagPlayAudio(boolean mFlagPlayAudio) {
        this.mFlagPlayAudio = mFlagPlayAudio;
    }

    public String getReferenceUser() {
        return referenceUser;
    }

    public String getUserReactionType() {
        return userReactionType;
    }

    public String getReferenceReactionType() {
        return referenceReactionType;
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

    public String getUser_name() {
        return user_name;
    }

    public String getUser_userName() {
        return user_userName;
    }

    public String getUser_profilePic() {
        return user_profilePic;
    }

    public String getUser_relationship() {
        return user_relationship;
    }

    public String getUser_uuid() {
        return user_uuid;
    }

    public String getUser_bio() {
        return user_bio;
    }

    public String getUser_followersCount() {
        return user_followersCount;
    }

    public String getUser_followingCount() {
        return user_followingCount;
    }

    public String getUser_joined() {
        return user_joined;
    }

    public String getUser_loc_name() {
        return user_loc_name;
    }

    public String getUser_loc_city() {
        return user_loc_city;
    }

    public String getUser_loc_state() {
        return user_loc_state;
    }

    public String getUser_loc_country() {
        return user_loc_country;
    }

    public String getContent() {
        return content;
    }

    public String getDefaultScrambledContent() {
        return defaultScrambledContent;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public String getUnlockInProgress() {
        return unlockInProgress;
    }

    public void setUnlockInProgress(String unlockInProgress) {
        this.unlockInProgress = unlockInProgress;
    }

    public String getUnlocked() {
        return unlocked;
    }

    public void setUnlocked(String unlocked) {
        this.unlocked = unlocked;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getAnger() {
        return anger;
    }

    public void setAnger(String anger) {
        this.anger = anger;
    }

    public String getSad() {
        return sad;
    }

    public void setSad(String sad) {
        this.sad = sad;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getLaugh() {
        return laugh;
    }

    public void setLaugh(String laugh) {
        this.laugh = laugh;
    }

    public String getTimerInSecs() {
        return timerInSecs;
    }

    public void setTimerInSecs(String timerInSecs) {
        this.timerInSecs = timerInSecs;
    }

    public String getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(String commentsCount) {
        this.commentsCount = commentsCount;
    }

    public String getLockExpiry() {
        return lockExpiry;
    }

    public String getUuid() {
        return uuid;
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

    public void setPostLockStatus(String postLockStatus) {
        this.postLockStatus = postLockStatus;
    }

    public String getTotalReactions() {
        return totalReactions;
    }

    public void setTotalReactions(String totalReactions) {
        this.totalReactions = totalReactions;
    }


}
