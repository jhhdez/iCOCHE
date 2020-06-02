package com.example.icoche;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Alarma extends AppCompatActivity implements SensorEventListener {

    private long last_update = 0, last_movement = 0;
    private float prevX = 0, prevY = 0, prevZ = 0;
    private float curX = 0, curY = 0, curZ = 0;
    Button btn;
    ImageButton btn_setting;
    private SensorManager sensorManager;
    MediaPlayer mp;
    private static final String TAG = "MyActivity";
    String texto;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String SWITCH1 = "switch1";
    private boolean switchSensibility;

    private final int PHONE_CALL_CODE = 10;
    int CurrentSDKVersion = Build.VERSION.SDK_INT;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarma);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        texto = sharedPreferences.getString(TEXT, null);
        switchSensibility = sharedPreferences.getBoolean(SWITCH1, false);


        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        mp = MediaPlayer.create(this, R.raw.audio);

        //leer estado de la configuracion

        btn = findViewById(R.id.textButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
                //ac = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                //sensorManager.registerListener(Alarma.this, ac,SensorManager.SENSOR_DELAY_NORMAL);
                sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
                if (sensors.size() > 0) {
                    sensorManager.registerListener(Alarma.this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
                }
            }
        });

        btn_setting = findViewById(R.id.btn_settings);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settings = new Intent(Alarma.this, setting_alarma.class);
                startActivity(settings);
            }
        });


    }


    /*@Override
    protected void onResume() {
        super.onResume();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0) {
            sensorManager.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }*/

    @Override
    protected void onStop() {
        mp.pause();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            long current_time = event.timestamp;

            curX = event.values[0];
            curY = event.values[1];
            curZ = event.values[2];

            if (prevX == 0 && prevY == 0 && prevZ == 0) {
                last_update = current_time;
                last_movement = current_time;
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
            }

            long time_difference = current_time - last_update;
            if (time_difference > 0) {
                float movement = Math.abs((curX + curY + curZ) - (prevX - prevY - prevZ)) / time_difference;
                int limit = 1500;
                float min_movement = 1E-6f;
                if (movement > min_movement) {
                    if (current_time - last_movement >= limit) {
                        Toast.makeText(getApplicationContext(), "Hay movimiento de " + movement, Toast.LENGTH_SHORT).show();
                        if (texto != null) {
                            Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + texto));
                            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    Activity#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for Activity#requestPermissions for more details.
                                return;
                            }
                            startActivity(intentCall);
                        }else{
                            mp.setLooping(true);
                            mp.start();
                        }

                    }
                    last_movement = current_time;
                }
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
                last_update = current_time;
            }


            /*((TextView) findViewById(R.id.txtAccX)).setText("Aceler—metro X: " + curX);
            ((TextView) findViewById(R.id.txtAccY)).setText("Aceler—metro Y: " + curY);
            ((TextView) findViewById(R.id.txtAccZ)).setText("Aceler—metro Z: " + curZ);*/
        }


    }

}
