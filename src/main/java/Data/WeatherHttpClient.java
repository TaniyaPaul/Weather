package Data;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import Util.Utils;

/**
 * Created by Tapajita on 28-07-2017.
 */

abstract public class WeatherHttpClient {
    int MAX_RETRY = 4;

    abstract public void error(String message);

    public String getWeatherData(String place) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        int RetryCount = 1;
        StringBuffer stringBuffer = new StringBuffer();
        try {
            connection = (HttpURLConnection) (new URL(Utils.BASE_URL + place)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            while (RetryCount <= MAX_RETRY) {
                try {
                    connection.connect();
                    //Read the response
                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line + "\r\n");
                    }
                    inputStream.close();
                    connection.disconnect();
                } catch (IOException e) {
                    connection.disconnect();
                    error(e.getLocalizedMessage());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    Log.e("Connection error: ", "Retrying " + RetryCount);
                }
                RetryCount++;
            }
        } catch (ProtocolException e) {
            error(e.getLocalizedMessage());
        } catch (IOException e) {
            error(e.getLocalizedMessage());
        }
        return stringBuffer.toString();
    }
}
