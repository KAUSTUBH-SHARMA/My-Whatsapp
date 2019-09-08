package com.example.mywhatsapp;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button mLogout=findViewById(R.id.logout);
        Button mFindUser=findViewById(R.id.findUser);
        TextView mIp=findViewById(R.id.myIP);
        final EditText mUserName=findViewById(R.id.Username);
        final EditText mPassword=findViewById(R.id.Password);
        Button mlogin=findViewById(R.id.login);

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserName=mUserName.getText().toString();
                String Password=mPassword.getText().toString();
                //URL url=new URL("");
//                HttpURLConnection urlConnection =(HttpURLConnection) url.openConnection();
//                try{
//                    InputStream in =new BufferedInputStream(urlConnection.getInputStream());
//                    readStream(in);
//                }
//                finally {
//                    urlConnection.disconnect();
//                }
            }
        });

        mFindUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),FindUserActivity.class));
            }
        });

        //loging out .this is very easy with firebase
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut(); //The user is loged out
                //but we also want to the user to get out of this page as this page is reserved to logeed in user
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//clear every thing in activity
                startActivity(intent);
                finish();
                return;
            }
        });

        mIp.setText(getLocalIpAddress());

        getPermissions();
        if(isNetworkAvailabel()==false){
            Toast.makeText(this, "you are not connected to wifi", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Wow you are connected to wifi", Toast.LENGTH_LONG).show();
        }



    }

    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_CONTACTS,Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_WIFI_STATE},1);
        }
    }
    private boolean isNetworkAvailabel(){
        boolean haveConnecteWifi=false;
        ConnectivityManager cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] netInfo=cm.getAllNetworkInfo();
        for(NetworkInfo ni:netInfo){
            if(ni.getTypeName().equalsIgnoreCase("WIFI")){
                if(ni.isConnected())
                    haveConnecteWifi=true;
            }
        }
        return haveConnecteWifi;
    }
    public String getLocalIpAddress(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception ex) {


            Log.e("IP Address", ex.toString());
        }
        return null;
    }

}
