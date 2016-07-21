package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zazalu on 7/19/16.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader br = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream inputStream = connection.getInputStream();
                    StringBuilder response = new StringBuilder();
                    br = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ( (line = br.readLine()) != null){
                        response.append(line);
                    }
                    if(listener != null){
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if(listener != null){
                        listener.onError(e);
                    }
                    e.printStackTrace();
                }finally {
                    if(br != null)
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    if(connection != null)
                    connection.disconnect();
                }

            }
        }).start();
    }
}


