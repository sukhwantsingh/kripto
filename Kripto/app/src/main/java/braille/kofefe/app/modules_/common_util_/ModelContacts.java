package braille.kofefe.app.modules_.common_util_;

/**
 * Created by Snow-Dell-05 on 10-Jan-18.
 */

public class ModelContacts {
    private String mName;
    private String mPhoneNumber;
    private String mCntactId;

    public ModelContacts(String mName, String mPhoneNumber, String mCntactId) {
        this.mName = mName;
        this.mPhoneNumber = mPhoneNumber;
        this.mCntactId = mCntactId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmCntactId() {
        return mCntactId;
    }

    public void setmCntactId(String mCntactId) {
        this.mCntactId = mCntactId;
    }
}
