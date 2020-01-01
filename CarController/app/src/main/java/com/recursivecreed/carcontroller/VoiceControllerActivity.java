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
import android.speech.RecognizerIntent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class VoiceControllerActivity extends AppCompatActivity {

    public static BluetoothSocket btSocket;
    ImageButton take_voice_input;
    TextView display_said;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_controller);
        take_voice_input = findViewById(R.id.voice_controll_btn);
        display_said = findViewById(R.id.display_what_said);
        take_voice_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                if(intent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(intent, 10);
                else
                    msg("Your device doesn\'t support taking speech input..");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String command = result.get(0);
                if (command.equals("forward")) {
                    moveForward();
                } else if (command.equals("backward")) {
                    moveBackward();
                } else if (command.equals("left")) {
                    moveLeft();
                } else if (command.equals("right")) {
                    moveRight();
                } else if (command.equals("stop")) {
                    stopCar();
                } else if (command.equals("act")) {
                    stopCarWithStyle();
                } else if (command.equals("disconnect")) {
                    Disconnect();
                } else if (command.equals("trace")) {
                    traceRay();
                } else {
                    command = "unknown";
                }
                display_said.setText(command);
            }
        }
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
            Intent intent = new Intent(VoiceControllerActivity.this, MainActivity.class);
            MainActivity.btSocket = btSocket;
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void traceRay(){
        if(btSocket != null){
            try {
                btSocket.getOutputStream().write('7');
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
