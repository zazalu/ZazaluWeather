package util;

/**
 * Created by zazalu on 7/19/16.
 */
public interface HttpCallbackListener {

    void onFinish(String response);
    void onError(Exception e);

}
