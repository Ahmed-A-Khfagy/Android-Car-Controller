  
/**
  Copyright (c) Ahmed Alaa-Eldin Zakaria Ali Khfagy
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
       http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.recursivecreed.carcontroller;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class GyroControllerActivity extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    private SensorManager sensorManager;
    Sensor sensor;

    private float X, Y, Z;

    private float stillX, stillY, stillZ;

    public static BluetoothSocket btSocket;
    ImageButton btn_forward, btn_backward, btn_disconnect, btn_stop, btn_stop_with_style, btn_back, btn_reset;
    private boolean position_is_reseted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyro_controller);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener( GyroControllerActivity.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        btn_forward = findViewById(R.id.forward_gyro_button);
        btn_backward = findViewById(R.id.backward_gyro_button);
        btn_disconnect = findViewById(R.id.disconnect_bluetooth_gyro_button);
        btn_back = findViewById(R.id.back_to_main_gyro_button);
        btn_stop = findViewById(R.id.stop_gyro_button);
        btn_stop_with_style = findViewById(R.id.stop_with_style_gyro_button);
        btn_reset = findViewById(R.id.reset_position_button);
        btn_forward.setOnClickListener(this);
        btn_backward.setOnClickListener(this);
        btn_disconnect.setOnClickListener(this);
        btn_stop.setOnClickListener(this);
        btn_stop_with_style.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        X = event.values[0];
        Y = event.values[1];
        Z = event.values[2];

        if(position_is_reseted){
            if(Y - stillY < -3.5){
                moveLeft();
            }
            else if(Y - stillY > 3.5){
                moveRight();
            }
        }
//        else
//            msg("x : " + event.values[0] + "\ny : " + event.values[1] + "\nz : " + event.values[2]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        if(position_is_reseted) {
            if (v.getId() == R.id.forward_gyro_button) {
                moveForward();
            }
            else if (v.getId() == R.id.backward_gyro_button) {
                moveBackward();
            }
            else if (v.getId() == R.id.disconnect_bluetooth_gyro_button) {
                Disconnect();
            }
            else if (v.getId() == R.id.back_to_main_gyro_button) {
                Intent intent = new Intent(GyroControllerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if (v.getId() == R.id.stop_gyro_button) {
                stopCar();
            }
            else if (v.getId() == R.id.stop_with_style_gyro_button) {
                stopCarWithStyle();
            }
            else if (v.getId() == R.id.reset_position_button) {
                resetGyroPosition();
            }
        }
        else if (v.getId() == R.id.reset_position_button) {
            resetGyroPosition();
        }
        else if (v.getId() == R.id.disconnect_bluetooth_gyro_button) {
            Disconnect();
        }
        else if (v.getId() == R.id.back_to_main_gyro_button) {
            position_is_reseted = false;
            Intent intent = new Intent(GyroControllerActivity.this, MainActivity.class);
            MainActivity.btSocket = btSocket;
            startActivity(intent);
            finish();
        }
        else {
            msg("Please reset position first..");
        }
    }

    private void resetGyroPosition(){
        stillX = X;
        stillY = Y;
        stillZ = X;
        position_is_reseted = true;
    }

    private void moveForward(){
        if(btSocket != null){
            try {
                btSocket.getOutputStream().write('1');
            }
            catch (IOException e) {
                msg("Error");
            }
        }
        else {
            msg("Device is disconnected");
        }
    }

    private void moveBackward(){
        if(btSocket != null){
            try {
                btSocket.getOutputStream().write('2');
            }
            catch (IOException e) {
                msg("Error");
            }
        }
        else {
            msg("Device is disconnected");
        }
    }

    private void moveLeft(){
        if(btSocket != null){
            try {
                btSocket.getOutputStream().write('3');
            }
            catch (IOException e) {
                msg("Error");
            }
        }
        else {
            msg("Device is disconnected");
        }
    }

    private void moveRight(){
        if(btSocket != null){
            try {
                btSocket.getOutputStream().write('4');
            }
            catch (IOException e) {
                msg("Error");
            }
        }
        else {
            msg("Device is disconnected");
        }
    }

    private void stopCar(){
        if(btSocket != null){
            try {
                btSocket.getOutputStream().write('5');
            }
            catch (IOException e) {
                msg("Error");
            }
        }
        else {
            msg("Device is disconnected");
        }
    }

    private void stopCarWithStyle(){
        if(btSocket != null){
            try {
                btSocket.getOutputStream().write('6');
            }
            catch (IOException e) {
                msg("Error");
            }
        }
        else {
            msg("Device is disconnected");
        }
    }

    private void Disconnect() {
        if (btSocket != null) {
            try {
                btSocket.close();
            }
            catch (IOException e) { msg("Error");}
        }
        btSocket = null;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private boolean mDoubleBackButtonPressed = false;
    @Override
    public void onBackPressed(){
        if(mDoubleBackButtonPressed){
            super.onBackPressed();
            return;
        }
        mDoubleBackButtonPressed = true;
        msg("Press back again to exit");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDoubleBackButtonPressed = false;
            }
        }, 2000);
    }
}
