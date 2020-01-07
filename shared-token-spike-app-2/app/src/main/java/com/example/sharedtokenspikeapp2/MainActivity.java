package com.example.sharedtokenspikeapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TARGET_PACKAGE_NAME = "com.example.sharedtokenspikeapp1";
    private static final String BEARER_TOKEN = "BEARER_TOKEN";

    Intent requestIntent;

    private BroadcastReceiver loginResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, intent.getStringExtra(BEARER_TOKEN), Toast.LENGTH_SHORT).show();

            // Clean up the requested service
            getApplicationContext().stopService(requestIntent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.registerReceiver(loginResponseReceiver, new IntentFilter("com.example.sharedtokenspikeapp2.TokenResponseReceiver"));

        Button serviceBtn = findViewById(R.id.button);
        serviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isPackageInstalled(TARGET_PACKAGE_NAME, getPackageManager())) {
                    requestIntent = new Intent();
                    requestIntent.setComponent(new ComponentName(TARGET_PACKAGE_NAME, TARGET_PACKAGE_NAME + ".TokenRequestService"));
                    // Provide ourselves as extra data so that the TokenRequestService can respond directly
                    requestIntent.putExtra("SOURCE_PACKAGE_NAME", getPackageName());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        getApplicationContext().startForegroundService(requestIntent);
                    } else {
                        getApplicationContext().startService(requestIntent);
                    }
                } else Toast.makeText(getApplicationContext(), "App not installed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            return packageManager.getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(loginResponseReceiver);
        super.onDestroy();
    }
}
