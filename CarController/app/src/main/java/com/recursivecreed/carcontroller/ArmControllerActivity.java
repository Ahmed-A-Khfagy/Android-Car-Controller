  
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
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

public class ArmControllerActivity extends AppCompatActivity {

    public static BluetoothSocket btSocket;

    int progressBase = 90, progressGripper = 90;

    SeekBar baseSeekBar, gripperSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arm_controller);
        baseSeekBar = findViewById(R.id.base_SeekBar);
        gripperSeekBar = findViewById(R.id.gripper_seekBar);
        baseSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int value = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressBase = value;
                msg("Base value : " + value);
                if(btSocket != null){
                    try {
                        String toSend = "s1" + progressBase;
                        btSocket.getOutputStream().write(toSend.getBytes());
                    }
                    catch (IOException e) {
                        msg("Error");
                    }
                }
                else {
                    msg("Device is disconnected");
                }
            }
        });

        gripperSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int value = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                value = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressGripper = value;
                msg("Gripper value : " + value);
                if(btSocket != null){
                    try {
                        String toSend = "s2" + progressGripper;
                        btSocket.getOutputStream().write(toSend.getBytes());
                    }
                    catch (IOException e) {
                        msg("Error");
                    }
                }
                else {
                    msg("Device is disconnected");
                }
            }
        });
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
            Intent intent = new Intent(ArmControllerActivity.this, MainActivity.class);
            MainActivity.btSocket = btSocket;
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
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
