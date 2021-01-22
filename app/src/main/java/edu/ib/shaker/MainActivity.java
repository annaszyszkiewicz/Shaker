package edu.ib.shaker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

/**
 * klasa obslugujaca aktywnosc aplikacji
 */
public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private static final String TAG = "EDUIB";
    private static float NSS = (float) 1. / 1000000000;
    private boolean color = false;
    private long lastUpdate;

    /**
     * przyslonieta metoda wykonujaca się przy uruchamianiu aktywnosci aplikacji
     * @param savedInstanceState zapisanie aktualnych informacji
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        TextView text = (TextView) findViewById(R.id.txt);
        text.setBackgroundColor(Color.BLUE);
    }

    /**
     * przyslonieta metoda wykonujaca sie przy zmianie ulozenia telefonu
     * zmienia kolor ekranu (obszaru tekstowego na calym ekranie)
     * przy potrzasnieciu urzadzeniem
     * @param event zdarzenie
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView text = (TextView) findViewById(R.id.txt);

        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];
        Log.d(TAG, String.valueOf(z));

        float root = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = (long) (event.timestamp * NSS);

        if (root >= 2) {
            if (actualTime - lastUpdate > 0.5) {

                lastUpdate = (long) (actualTime);

                if (color) {
                    text.setBackgroundColor(Color.DKGRAY);

                } else {
                    text.setBackgroundColor(Color.BLUE);
                }
                color = !color;
            }
        }
    }

    /**
     * przyslonieta metoda wykonujaca sie po wznowieniu dzialania
     */
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * przyslonieta metoda wykonujaca się po zatrzymaniu dzialania
     */
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    /**
     * metoda wykonujaca sie przy zmianie precyzji
     *
     * @param sensor   czujnik
     * @param accuracy precyzja
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}