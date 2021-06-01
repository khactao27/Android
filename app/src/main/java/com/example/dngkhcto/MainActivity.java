package com.example.dngkhcto;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText mssv;
    private EditText mac_address;
    private TextView time_local;
    private TextView time_server;
    private TextView token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mssv = (EditText)findViewById(R.id.mssv);
        mac_address = (EditText) findViewById(R.id.mac_address);
        time_local = findViewById(R.id.time_local);
        time_server = findViewById(R.id.time_server);
        token = findViewById(R.id.token);
        mac_address.setText(getMacAddress());
    }
    public String getMacAddress(){
        try{
            List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());
            String stringMac = "";
            for(NetworkInterface networkInterface : networkInterfaceList)
            {
                if(networkInterface.getName().equalsIgnoreCase("wlan0"))
                {
                    for(int i = 0 ;i <networkInterface.getHardwareAddress().length; i++){
                        String stringMacByte = Integer.toHexString(networkInterface.getHardwareAddress()[i]& 0xFF);
                        if(stringMacByte.length() == 1)
                        {
                            stringMacByte = "0" +stringMacByte;
                        }
                        stringMac = stringMac + stringMacByte.toUpperCase() + ":";
                    }
                    break;
                }
            }
            stringMac = stringMac.substring(0, stringMac.length() - 1);
            return stringMac;
        }catch (SocketException e)
        {
            e.printStackTrace();
        }
        return  "0";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getToken(View view) {
        LocalTime time = LocalTime.now();
        String mMssv = mssv.getText().toString();
        String mMac_Address = mac_address.getText().toString();
        String data = "{\"studentId\":\""+mMssv+"\", \"macAddress\":\"" +mMac_Address+"\"}";
        new FetchToken(time_local, time_server, token, time).execute(data);
    }
}