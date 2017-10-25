package Data;

import android.content.Context;
import android.widget.Toast;

import com.mon.taan.weather_app.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.Place;
import Model.Weather;
import Util.Utils;

/**
 * Created by Tapajita on 28-07-2017.
 */

public class JSONWeatherParser {

    public static Weather getWeather(String data) {

        if(data == null || data.trim().isEmpty()) return null;

            Weather weather = new Weather();
            //create Jsonobject from data
            try {
                JSONObject jsonObject = new JSONObject(data);
                Place place = new Place();

                JSONObject coordObj = Utils.getObject("coord", jsonObject);
                place.setLat(Utils.getFloat("lat", coordObj));
                place.setLon(Utils.getFloat("lon", coordObj));

                //Get the weather ARRay
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject jsonWeather = jsonArray.getJSONObject(0);
                weather.currentCondition.setWeatherId(Utils.getInt("id", jsonWeather));
                weather.currentCondition.setDescription(Utils.getString("description", jsonWeather));
                weather.currentCondition.setCondition(Utils.getString("main", jsonWeather));
                weather.currentCondition.setIcon(Utils.getString("icon", jsonWeather));

                JSONObject mainObj=Utils.getObject("main",jsonObject);
                weather.currentCondition.setHumidity((Utils.getInt("humidity",mainObj)));
                weather.currentCondition.setPressure(Utils.getInt("pressure",mainObj));
                weather.currentCondition.setMinTemp(Utils.getFloat("temp_min",mainObj));
                weather.currentCondition.setMaxTemp(Utils.getFloat("temp_max",mainObj));
                weather.currentCondition.setTemperature(Utils.getDouble("temp",mainObj));

                // Get the wind obj
                JSONObject windObj = Utils.getObject("wind", jsonObject);
                weather.wind.setSpeed(Utils.getFloat("speed", windObj));
                weather.wind.setDeg(Utils.getFloat("deg", windObj));

                //get the sys obj
                JSONObject sysObj = Utils.getObject("sys", jsonObject);
                place.setCountry(Utils.getString("country", sysObj));
                place.setLastpdate(Utils.getInt("dt", jsonObject));
                place.setSunrise(Utils.getInt("sunrise", sysObj));
                place.setSunset(Utils.getInt("sunset", sysObj));
                place.setCity(Utils.getString("name", jsonObject));
                weather.place = place;

                //get the clouds obj
                JSONObject cloudObj = Utils.getObject("clouds", jsonObject);
                weather.clouds.setPercipitation(77);

                return weather;

            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }



    }
}
