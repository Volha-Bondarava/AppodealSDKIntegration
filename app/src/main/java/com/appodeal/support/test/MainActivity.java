package com.appodeal.support.test;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;

public class MainActivity extends AppCompatActivity {

    private TextView counter;
    private Button mButton;
    private boolean consentValue;

    CountDownTimer staticInterstitialTimer;
    CountDownTimer bannerTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String appKey = "fee50c333ff3825fd6ad6d38cff78154de3025546d47a84f";
        Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL | Appodeal.BANNER, consentValue);

        counter = findViewById(R.id.counter);
        mButton = findViewById(R.id.button);

        staticInterstitialTimer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // обновление счётчика
                counter.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                showInterstitial();
                // рестарт таймера
                staticInterstitialTimer.start();

            }
        };

        bannerTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                // закрытие баннера
                hideBanner();
            }
        };

        staticInterstitialTimer.start();
        bannerTimer.start();

        showBanner();

    }

    public void disableAd(View view) {
        staticInterstitialTimer.cancel();
    }

    public void showInterstitial() {
        staticInterstitialTimer.cancel();
        Appodeal.show(this, Appodeal.INTERSTITIAL);
    }

    public void showBanner() {
        Appodeal.show(this, Appodeal.BANNER);
    }

    public void hideBanner() {
        Appodeal.hide(this, Appodeal.BANNER);
    }
}
