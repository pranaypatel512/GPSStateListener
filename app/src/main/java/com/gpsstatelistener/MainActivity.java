package com.gpsstatelistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!isGpsEnable(MainActivity.this)) {
            enableGPS(MainActivity.this);
        }
        registerReceiver(gpsReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
    }

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /* Every time you get GPS Enable dialog*/
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                if (!isGpsEnable(MainActivity.this)) {
                    enableGPS(MainActivity.this);
                }
                else{
                    Toast.makeText(MainActivity.this,"GPS enable",Toast.LENGTH_LONG).show();
                }
            }
        }
    };


    public static boolean isGpsEnable(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    public static void enableGPS(final Context context) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
        alertDialogBuilder
                .setMessage(context.getString(R.string.str_enable_gps_message))
                .setCancelable(false)
                .setPositiveButton(context.getString(R.string.str_enable_gps),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton(context.getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(gpsReceiver);
    }
}
