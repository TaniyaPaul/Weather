package com.mon.taan.weather_app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.LogRecord;

import Data.JSONWeatherParser;
import Data.WeatherHttpClient;
import Model.Weather;


public class MainActivity extends AppCompatActivity {
    private TextView cityName;
    private TextView temp;
    private ImageView iconView;
    private TextView description;
    private TextView humididty;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;
    private Context ApplicationContext;
    Handler mHandler;
    Weather weather = new Weather();
    String Location = "moscow,BR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconView = (ImageView) findViewById(R.id.thumnailIcon);
        temp = (TextView) findViewById(R.id.temptext);
        cityName = (TextView) findViewById(R.id.citytext);
        description = (TextView) findViewById(R.id.cloudtext);
        humididty = (TextView) findViewById(R.id.humidtext);
        pressure = (TextView) findViewById(R.id.pressuretext);
        wind = (TextView) findViewById(R.id.windtext);
        sunrise = (TextView) findViewById(R.id.risetext);
        sunset = (TextView) findViewById(R.id.settext);
        updated = (TextView) findViewById(R.id.updatetext);

        ApplicationContext = getApplicationContext();
        createUiHandel();

        renderWeatherData();


    }

    private void createUiHandel() {
        // Set this up in the UI thread.

        mHandler = new Handler(Looper.getMainLooper()) {

            @Override
            public void handleMessage(Message message) {
                // This is where you do your work in the UI thread.
                // Your worker tells you in the message what to do.
                Toast.makeText(ApplicationContext, message.obj.toString(), Toast.LENGTH_LONG).show();
            }
        };


    }

    public void renderWeatherData() {
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute("q=" + Location + "&appid=f46c17dd5f352fbccb1db6115b893baf&units=metric");


    }

    private class Download extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return null;
        }

        private Bitmap downloadImage(String code) {
            // final DefaultHttpClient client = new DefaultHttpClient();
            return null;
        }
    }


    private class WeatherTask extends AsyncTask<String, Void, Weather> {
        @Override
        protected Weather doInBackground(String... params) {
            String data = (new WeatherHttpClient() {
                @Override
                public void error(String message) {
                    // And this is how you call it from the worker thread:
                    Message mssg = mHandler.obtainMessage(1, message);
                    mssg.sendToTarget();
                }
            }.getWeatherData(params[0]));
            Log.v("Data:", data);
            weather = JSONWeatherParser.getWeather(data);
            if (weather != null) {
                Log.v("Data:", data);

            } else {
                Log.e("Data:", "Not found");

            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
            DateFormat df = DateFormat.getDateInstance();

            String sunriseDate = df.format(new Date(weather.place.getSunrise()));
            String sunsetDate = df.format(new Date(weather.place.getSunset()));
            String updateDate = df.format(new Date(weather.place.getLastpdate()));

            DecimalFormat decimalFormat = new DecimalFormat("#.#");

            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());

            if (weather != null) {
                cityName.setText(weather.place.getCity() + "," + weather.place.getCountry());
                temp.setText("" + tempFormat + " C");
                humididty.setText("Humidity: " + weather.currentCondition.getHumidity() + "%");
                pressure.setText("Pressure: " + weather.currentCondition.getPressure() + "hpa");
                wind.setText("Wind: " + weather.wind.getSpeed() + "mps");
                sunrise.setText("Sunrise: " + sunriseDate);
                sunset.setText("Sunset: " + sunsetDate);
                updated.setText("Last Update: " + updateDate);
                description.setText("Condition: " + weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescription() + ")");
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_cityId) {
            Location = "Howrah,IND";
            renderWeatherData();
            return true;
        }

        if (id == R.id.refresh_id) {
            renderWeatherData();
            Message mssg = mHandler.obtainMessage(1, "Refreshing ... again");
            mssg.sendToTarget();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
