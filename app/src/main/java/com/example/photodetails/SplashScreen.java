package com.example.photodetails;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashScreen extends AppCompatActivity {
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermissionsMandAbove();
            }
        },1000);
    }


    //-----------------------------------------------------------------------------------------------------------------------------------------
    @TargetApi(23)
    public void checkPermissionsMandAbove() {
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= 23) {
            if (checkPermission()) {
                movetoLogin();
            } else {
                requestPermission();
            }
        } else {
            movetoLogin();
        }
    }

    //=------------------------------------------------------------------------------------------------------------------------------------------
    private void movetoLogin() {
        Intent in = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(in);
        finish();
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------
    private void requestPermission() {//6
        ActivityCompat.requestPermissions(SplashScreen.this, new String[]{
                READ_PHONE_STATE,
                WRITE_EXTERNAL_STORAGE,
                ACCESS_FINE_LOCATION,
                CAMERA
        }, RequestPermissionCode);
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------
    private boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
       return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED;
                 }

    //---------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestPermissionCode) {
            if (grantResults.length > 0) {
                boolean ReadPhoneStatePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean ReadStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean ReadLocationPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean ReadLogsPermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                if (ReadPhoneStatePermission && ReadStoragePermission && ReadLocationPermission && ReadLogsPermission) {
                    movetoLogin();
                } else {
                    Toast.makeText(SplashScreen.this, "Required All Permissions to granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }
}
