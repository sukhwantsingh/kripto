package braille.kofefe.app.modules_.mainpostfeed.callback_;

import braille.kofefe.app.modules_.mainpostfeed.model.ModelComments;

/**
 * Created by Snow-Dell-05 on 31-Jan-18.
 */

public interface ICommentCallback {
    void getCommentsAfter(int position, ModelComments mModleComments);

}
