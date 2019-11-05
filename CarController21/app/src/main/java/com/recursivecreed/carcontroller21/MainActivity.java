package com.recursivecreed.carcontroller21;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Menu get_devices;
    Set<BluetoothDevice> mBTdevice;
    BluetoothAdapter bluetoothAdapter;
    ProgressDialog progress;
    public static BluetoothSocket btSocket = null;
    boolean isBtConnected = false;
    String address;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ImageButton go_to_arrow_controller_button, go_to_gyro_controller_button, go_to_voice_button, go_to_arm_control, go_to_settings
            , go_to_trace, go_to_avoid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        go_to_arrow_controller_button = findViewById(R.id.go_to_arrow_controller_button);
        go_to_gyro_controller_button = findViewById(R.id.gyro_controller_button);
        go_to_voice_button = findViewById(R.id.voice_controller_button);
        go_to_arm_control = findViewById(R.id.arm_button);
        go_to_settings = findViewById(R.id.settings_button);
        go_to_trace = findViewById(R.id.trace_btn);
        go_to_avoid = findViewById(R.id.avoid_btn);
        if(bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();
        }
        else  {
            if (bluetoothAdapter.isEnabled())  { }
            else  {
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon,1);
            }
        }

        go_to_arrow_controller_button.setOnClickListener(this);
        go_to_gyro_controller_button.setOnClickListener(this);
        go_to_voice_button.setOnClickListener(this);
        go_to_arm_control.setOnClickListener(this);
        go_to_settings.setOnClickListener(this);
        go_to_trace.setOnClickListener(this);
        go_to_avoid.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        get_devices = menu;

        getMenuInflater().inflate(R.menu.devices_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item){
        if(btSocket == null) {
            if (item.getItemId() == R.id.refresh_button) {

                mBTdevice = bluetoothAdapter.getBondedDevices();

                if (mBTdevice.size() > 0) {
                    SubMenu mSubmenu = get_devices.addSubMenu(0, 0, 0, "Devices");
                    int ctr = 1;
                    for (BluetoothDevice bt : mBTdevice) {
                        mSubmenu.add(0, ctr, ctr, bt.getName() + "\n" + bt.getAddress());
                        ++ctr;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found", Toast.LENGTH_LONG).show();
                }
            } else {
                int ctr = 1;
                for (BluetoothDevice bt : mBTdevice) {
                    if (ctr == item.getItemId()) {
                        Toast.makeText(getApplicationContext(), "you pressed on a list item " + ctr + " " + bt.getAddress(), Toast.LENGTH_LONG).show();
                        address = bt.getAddress();
                        new ConnectBT().execute();
                        break;
                    }
                    ++ctr;
                }
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "You\'re already connected!!\nPlease disconnect first", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.go_to_arrow_controller_button){
//            if(btSocket != null) {
                Intent intent = new Intent(MainActivity.this, ArrowControlActivity.class);
                ArrowControlActivity.btSocket = btSocket;
                startActivity(intent);
                finish();
//            }
//            else {
//                Toast.makeText(MainActivity.this, "Please connect to a device first..!", Toast.LENGTH_LONG).show();
//            }
        }
        else if(v.getId() == R.id.gyro_controller_button){
//            if(btSocket != null) {
                Intent intent = new Intent(MainActivity.this, GyroControllerActivity.class);
                GyroControllerActivity.btSocket = btSocket;
                startActivity(intent);
                finish();
//            }
//            else {
//                Toast.makeText(MainActivity.this, "Please connect to a device first..!", Toast.LENGTH_LONG).show();
//            }
        }
        else if(v.getId() == R.id.voice_controller_button){
//            if(btSocket != null) {
                Intent intent = new Intent(MainActivity.this, VoiceControllerActivity.class);
                VoiceControllerActivity.btSocket = btSocket;
                startActivity(intent);
                finish();
//            }
//            else {
//                Toast.makeText(MainActivity.this, "Please connect to a device first..!", Toast.LENGTH_LONG).show();
//            }
        }
        else if(v.getId() == R.id.arm_button){
//            if(btSocket != null) {
                Intent intent = new Intent(MainActivity.this, ArmControllerActivity.class);
                ArmControllerActivity.btSocket = btSocket;
                startActivity(intent);
                finish();
//            }
//            else {
//                Toast.makeText(MainActivity.this, "Please connect to a device first..!", Toast.LENGTH_LONG).show();
//            }
        }
        else if(v.getId() == R.id.trace_btn){
            TraceActivity.btSocket = btSocket;
            Intent intent = new Intent(MainActivity.this, TraceActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v.getId() == R.id.avoid_btn){
            AvoidActivity.btSocket = btSocket;
            Intent intent = new Intent(MainActivity.this, AvoidActivity.class);
            startActivity(intent);
            finish();
        }
        else if(v.getId() == R.id.settings_button){
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess = true;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(MainActivity.this, "Connecting...", "Please wait!");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (btSocket == null || !isBtConnected) {
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                    btSocket = device.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            }
            catch (IOException e) {
                ConnectSuccess = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
            }
            else {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
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
