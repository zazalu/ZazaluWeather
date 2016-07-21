package util;

import org.json.JSONException;

/**
 * Created by zazalu on 7/19/16.
 */
public interface HttpCallbackListener {

    void onFinish(String response) throws JSONException;
    void onError(Exception e);

}
