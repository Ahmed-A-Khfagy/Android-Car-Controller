  
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
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class ArrowControlActivity extends AppCompatActivity implements View.OnClickListener {


    public static BluetoothSocket btSocket;
    ImageButton forward, backward, left, right, brakes, brakes_flash, disconnect, park, radar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrow_control);
        park = findViewById(R.id.parking_btn);
        radar = findViewById(R.id.radar_btn);
        forward = findViewById(R.id.forward_button);
        backward = findViewById(R.id.backward_button);
        left = findViewById(R.id.left_button);
        right = findViewById(R.id.right_button);
        brakes = findViewById(R.id.brakes_button);
        brakes_flash = findViewById(R.id.brakes_flash_button);
        disconnect = findViewById(R.id.dissconnect_button);
        forward.setOnClickListener(this);
        backward.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        brakes.setOnClickListener(this);
        brakes_flash.setOnClickListener(this);
        disconnect.setOnClickListener(this);
        park.setOnClickListener(this);
        radar.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.go_back_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        if(item.getItemId() == R.id.go_back_to_menu){
            Intent intent = new Intent(ArrowControlActivity.this, MainActivity.class);
            MainActivity.btSocket = btSocket;
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.forward_button){
            moveForward();
        }
        else if(v.getId() == R.id.backward_button){
            moveBackward();
        }
        else if(v.getId() == R.id.left_button){
            moveLeft();
        }
        else if(v.getId() == R.id.right_button){
            moveRight();
        }
        else if(v.getId() == R.id.brakes_button){
            stopCar();
        }
        else if(v.getId() == R.id.brakes_flash_button){
            stopCarWithStyle();
        }
        else if(v.getId() == R.id.parking_btn){
            parking();
        }
        else if(v.getId() == R.id.radar_btn){
            radarFn();
        }
        else if(v.getId() == R.id.dissconnect_button){
            Disconnect();
        }
    }

    private void parking(){
        if(btSocket != null){
            try {
                btSocket.getOutputStream().write('9');
            }
            catch (IOException e) {
                msg("Error");
            }
        }
        else {
            msg("Device is disconnected");
        }
    }

    private void radarFn(){
        if(btSocket != null){
            try {
                btSocket.getOutputStream().write('0');
            }
            catch (IOException e) {
                msg("Error");
            }
        }
        else {
            msg("Device is disconnected");
        }
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
