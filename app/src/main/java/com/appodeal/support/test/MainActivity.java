package com.appodeal.support.test;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeCallbacks;
import com.appodeal.ads.native_ad.views.NativeAdViewNewsFeed;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String APP_KEY = "625d2c10e9c5a41641778c062cf499fac2e7c5b56e3cdf19";
    // todo
    public static final int interstitialTime = 15000;
    public static final int bannerTime = 5000;

    private Button mButton;
    private ListView mListView;
    private TextView counter;

    private boolean isFirstThirtySeconds = true;
    private List<NativeAd> mNativeAdList;
    private int interstitialTimerMoment;
    private int bannerTimerMoment;
    private boolean isInterstitial = false;
    private boolean bannerShowed = false;

    CountDownTimer staticInterstitialTimer;
    CountDownTimer bannerTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button);
        counter = findViewById(R.id.counter);
        mListView = findViewById(R.id.list_view);
        mListView.setVisibility(View.INVISIBLE);

        initializeSDK();

        // set up timers
        setUpInterstitialTimer();
        setUpBannerTimer();

        // start the interstitial timer
        staticInterstitialTimer.start();

        // show banner
        showBanner();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // stop timers
        staticInterstitialTimer.cancel();
        bannerTimer.cancel();
        // get interstitial interval
        if (!counter.getText().equals("")) {
            interstitialTimerMoment = Integer.parseInt(counter.getText().toString());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (!isInterstitial) {
            staticInterstitialTimer = new CountDownTimer(interstitialTimerMoment * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    counter.setText(String.valueOf(l / 1000));
                }

                @Override
                public void onFinish() {
                    showInterstitial();
                    if (isFirstThirtySeconds) {
                        isFirstThirtySeconds = false;
                    }
                    setUpInterstitialTimer();
                }
            };
        }

        if (!bannerShowed) {
            bannerTimer = new CountDownTimer(bannerTimerMoment * 1000, 1000) {

                @Override
                public void onTick(long l) {
                    bannerTimerMoment = (int) l / 1000;
                }

                @Override
                public void onFinish() {
                    hideBanner();
                }
            };
        }
        // start timers from new time
        staticInterstitialTimer.start();
        bannerTimer.start();
    }

    public void showInterstitial() {
        isInterstitial = true;
        Appodeal.show(this, Appodeal.INTERSTITIAL);
    }

    public void showBanner() {
        Appodeal.show(this, Appodeal.BANNER_TOP);
    }

    public void hideBanner() {
        Appodeal.hide(this, Appodeal.BANNER_TOP);
        bannerShowed = true;
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
        staticInterstitialTimer = new CountDownTimer(interstitialTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // update the counter label
                counter.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                showInterstitial();
                if (isFirstThirtySeconds) {
                    isFirstThirtySeconds = false;
                }
            }
        };
    }

    public void setUpBannerTimer() {
        bannerTimer = new CountDownTimer(bannerTime, 1000) {
            @Override
            public void onTick(long l) {
                bannerTimerMoment = (int) l / 1000;
                System.out.println(bannerTimerMoment);
            }

            @Override
            public void onFinish() {
                // hide the top banner
                hideBanner();
            }
        };
    }

    public void initializeSDK() {
        Appodeal.setAutoCache(Appodeal.NATIVE, false);
        Appodeal.setTesting(true);
        Appodeal.initialize(this, APP_KEY, Appodeal.INTERSTITIAL | Appodeal.BANNER_TOP | Appodeal.NATIVE, true);
        Appodeal.cache(this, Appodeal.NATIVE, 5);

        // set up Ad callbacks
        setUpNativeCallbacks();
        setUpBannerCallbacks();
        setUpInterstitialCallbacks();
    }

    public void buttonAction(View view) {
        if (isFirstThirtySeconds) {
            staticInterstitialTimer.cancel();
            counter.setText("");
        }
        mListView.setVisibility(View.VISIBLE);
    }

    public void setUpNativeCallbacks() {
        Appodeal.setNativeCallbacks(new NativeCallbacks() {
            @Override
            public void onNativeLoaded() {
                mNativeAdList = Appodeal.getNativeAds(5);
                mListView.setAdapter(new ListAdapter());
            }

            @Override
            public void onNativeFailedToLoad() {

            }

            @Override
            public void onNativeShown(NativeAd nativeAd) {

            }

            @Override
            public void onNativeClicked(NativeAd nativeAd) {

            }

            @Override
            public void onNativeExpired() {

            }
        });
    }

    private class ListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mNativeAdList.size();
        }

        @Override
        public Object getItem(int i) {
            return mNativeAdList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return mNativeAdList.get(i).hashCode();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.list_item, viewGroup, false);
            }
            NativeAdViewNewsFeed nativeAdViewNewsFeed = (NativeAdViewNewsFeed) view;
            nativeAdViewNewsFeed.setNativeAd((NativeAd) getItem(i));
            return nativeAdViewNewsFeed;
        }
    }
}
