package com.appodeal.support.test;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;

public class MainActivity extends AppCompatActivity {

    public static final String APP_KEY = "625d2c10e9c5a41641778c062cf499fac2e7c5b56e3cdf19";

    private TextView counter;
    private Button mButton;

    private boolean isFirstThirtySeconds = true;

    CountDownTimer staticInterstitialTimer;
    CountDownTimer bannerTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeSDK();

        counter = findViewById(R.id.counter);
        mButton = findViewById(R.id.button);

        // set up timers
        setUpInterstitialTimer();
        setUpBannerTimer();

        // start the interstitial timer
        staticInterstitialTimer.start();

        // show banner
        showBanner();
    }

    public void showInterstitial() {
        Appodeal.show(this, Appodeal.INTERSTITIAL);
    }

    public void showBanner() {
        Appodeal.show(this, Appodeal.BANNER_TOP);
    }

    public void hideBanner() {
        Appodeal.hide(this, Appodeal.BANNER_TOP);
    }

    public void setUpInterstitialCallbacks() {

        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialClosed() {
                staticInterstitialTimer.start();
            }

            @Override
            public void onInterstitialLoaded(boolean b) {

            }

            @Override
            public void onInterstitialFailedToLoad() {

            }

            @Override
            public void onInterstitialShown() {

            }

            @Override
            public void onInterstitialClicked() {

            }

            @Override
            public void onInterstitialExpired() {

            }
        });
    }

    public void setUpBannerCallbacks() {
        Appodeal.setBannerCallbacks(new BannerCallbacks() {
            @Override
            public void onBannerLoaded(int i, boolean b) {

            }

            @Override
            public void onBannerFailedToLoad() {

            }

            @Override
            public void onBannerShown() {
                bannerTimer.start();
            }

            @Override
            public void onBannerClicked() {

            }

            @Override
            public void onBannerExpired() {

            }
        });
    }

    public void setUpInterstitialTimer() {
        setUpInterstitialCallbacks();
        staticInterstitialTimer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // update the counter label
                counter.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                staticInterstitialTimer.cancel();
                showInterstitial();
                if (isFirstThirtySeconds) {
                    isFirstThirtySeconds = false;
                }
            }
        };
    }

    public void setUpBannerTimer() {
        setUpBannerCallbacks();
        bannerTimer = new CountDownTimer(5000, 5000) {
            @Override
            public void onTick(long l) {
                // do nothing
            }

            @Override
            public void onFinish() {
                // hide the top banner
                hideBanner();
            }
        };
    }

    public void initializeSDK() {
        Appodeal.initialize(this, APP_KEY, Appodeal.INTERSTITIAL | Appodeal.BANNER_TOP, true);
        Appodeal.setTesting(true);
    }

    public void buttonAction(View view) {
        if (isFirstThirtySeconds) {
            staticInterstitialTimer.cancel();
        }
    }
}
