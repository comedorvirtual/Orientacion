package com.example.orientacion;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magneticField;
    private TextView anglex;
    private TextView angley;
    private TextView anglez;
    private final float[] mLastAccelerometer = new float[3];
    private final float[] mLastMagnetometer = new float[3];

    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientation = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anglex = findViewById(R.id.tv_angle_x);
        angley = findViewById(R.id.tv_angle_y);
        anglez = findViewById(R.id.tv_angle_z);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null)
            magneticField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
            accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(accelerometer != null){
            mSensorManager.registerListener(this,accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }

        if(magneticField != null){
            mSensorManager.registerListener(this,magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager = null;
        accelerometer = null;
        magneticField = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer,
                    0, event.values.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer,
                    0, event.values.length);
        }
        updateOrientationAngles();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(mRotationMatrix, null,
                mLastAccelerometer, mLastMagnetometer);

        // "mRotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(mRotationMatrix, mOrientation);

        // "mOrientationAngles" now has up-to-date information.
        mOrientation[0] = (float) Math.toDegrees(mOrientation[0]);
        mOrientation[1] = (float) Math.toDegrees(mOrientation[1]);
        mOrientation[2] = (float) Math.toDegrees(mOrientation[2]);
        String one = mOrientation[0]+"";
        String two = mOrientation[1]+"";
        String three = mOrientation[2]+"";
        anglex.setText(one);
        angley.setText(two);
        anglez.setText(three);
        //Log.e("mensajes","Toast por defecto"+mOrientation[0] );
        //Log.e("mensajes","Toast por defecto"+mOrientation[1] );
        //Log.e("mensajes","Toast por defecto"+mOrientation[2] );

    }
}