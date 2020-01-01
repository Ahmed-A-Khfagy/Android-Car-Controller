package com.recursivecreed.carcontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class TraceActivity extends AppCompatActivity {

    public static BluetoothSocket btSocket;

    ImageButton trace, stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);
        trace = findViewById(R.id.trace_btn);
        trace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                traceCar();
            }
        });

        stop = findViewById(R.id.btn_stop_trace);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopCar();
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
            Intent intent = new Intent(TraceActivity.this, MainActivity.class);
            MainActivity.btSocket = btSocket;
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void traceCar(){
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
