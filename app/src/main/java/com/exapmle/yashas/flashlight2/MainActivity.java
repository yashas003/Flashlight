package com.exapmle.yashas.flashlight2;

import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private InterstitialAd interstitial;
    ToggleButton button;
    AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-3422430838819888~7971856923");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest banner = new AdRequest.Builder().build();
        final AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(banner);

        button = findViewById(R.id.flash);

        boolean feature_camera_flash = getPackageManager().hasSystemFeature(getPackageManager().FEATURE_CAMERA_FLASH);
        boolean camera_light = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 60);
        final CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);

        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                try {
                    String cameraID = cameraManager.getCameraIdList()[0];

                    if (button.isChecked()) {
                        cameraManager.setTorchMode(cameraID, true);

                    } else {
                        cameraManager.setTorchMode(cameraID, false);

                        interstitial = new InterstitialAd(MainActivity.this);
                        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
                        interstitial.loadAd(adRequest);
                        interstitial.setAdListener(new AdListener() {
                            public void onAdLoaded() {
                                displayInterstitial();
                            }
                        });
                    }
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
