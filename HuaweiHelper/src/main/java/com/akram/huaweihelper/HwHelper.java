package com.akram.huaweihelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.AudioFocusType;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.ads.nativead.MediaView;
import com.huawei.hms.ads.nativead.NativeAd;
import com.huawei.hms.ads.nativead.NativeAdLoader;
import com.huawei.hms.ads.nativead.NativeView;
import com.huawei.hms.ads.splash.SplashView;

import retrofit2.Call;
import retrofit2.Callback;


public class HwHelper {

    private static final int AD_TIMEOUT = 10000;
    private static final int MSG_AD_TIMEOUT = 1001;
    private static boolean hasPaused = false;


    private static Activity act;
    private static CountDownTimer timer;
    private static Class<?> toActivity;

    public static void install(Activity activity, Class<?> target, FrameLayout splashViewHolder, String baseUrl, String path, int bg) {
        act = activity;
        toActivity = target;
        Pref pref = new Pref(activity);

        AdsService instance = AdsApiInstance.getInstance(baseUrl);
        instance.getAdsConfig(path).enqueue(new Callback<CustomAdConfig>() {
            @Override
            public void onResponse(@NonNull Call<CustomAdConfig> call, @NonNull retrofit2.Response<CustomAdConfig> response) {
                if (response.isSuccessful()) {

                    if (response.body() == null)
                        return;

                    CustomAdConfig config = response.body();


                    if (config.getHwAppId().equals("")) {
                        HwAds.init(activity);
                    } else {
                        HwAds.init(activity, config.getHwAppId());
                    }

                    pref.saveSplash(config.getHwSplashAdId());
                    pref.saveInter(config.getHwInterId());
                    pref.saveNative(config.getHwNativeId());
                    pref.saveReward(config.getHwRewardedAdId());


                    Pref.InterCount = config.getHwInterCount();

                    SplashView splashView = new SplashView(activity);
                    splashViewHolder.removeAllViews();
                    splashViewHolder.addView(splashView);

                    loadAd(splashView, config.getHwSplashAdId(), bg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CustomAdConfig> call, @NonNull Throwable t) {
                Log.d("TAG", "onFailure: " + t.getLocalizedMessage());
                jump();
            }
        });
    }

    private static void loadAd(SplashView splashView, String slotId, int bg) {

        timer = new CountDownTimer(6500, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                hasPaused = false;
                jump();
            }
        };
        timer.start();

        AdParam adParam = new AdParam.Builder().build();
        SplashView.SplashAdLoadListener splashAdLoadListener = new SplashView.SplashAdLoadListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                jump();
            }

            @Override
            public void onAdDismissed() {
                jump();
            }
        };

        int orientation = getScreenOrientation();

        splashView.setSloganResId(bg);

        splashView.setAudioFocusType(AudioFocusType.NOT_GAIN_AUDIO_FOCUS_WHEN_MUTE);

        splashView.load(slotId, orientation, adParam, splashAdLoadListener);

        timeoutHandler.removeMessages(MSG_AD_TIMEOUT);
        timeoutHandler.sendEmptyMessageDelayed(MSG_AD_TIMEOUT, AD_TIMEOUT);
    }

    private static int getScreenOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    private static Handler timeoutHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (act.hasWindowFocus()) {
                jump();
            }
            return false;
        }
    });

    private static void jump() {
        if (!hasPaused) {
            hasPaused = true;

            if (timer != null)
                timer.cancel();

            Intent intent = new Intent(act, toActivity);
            act.startActivity(intent);
            act.finish();
        }
    }

    public static void onStop() {
        timeoutHandler.removeMessages(MSG_AD_TIMEOUT);
        hasPaused = true;
    }

    public static void onRestart() {
        hasPaused = false;
        jump();
    }

    public static void showInter(Activity activity) {
        Pref pref = new Pref(activity);
        if (Pref.InterHolder >= Pref.InterCount) {
            InterstitialAd interstitialAd = new InterstitialAd(activity);
            interstitialAd.setAdId(pref.getInterId());
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show(activity);
                    } else {
                        Toast.makeText(activity, "Ad did not load", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onAdFailed(int i) {
                    super.onAdFailed(i);
                    Log.d("TAG", "onAdFailed: " + i);
                }
            });

            AdParam adParam = new AdParam.Builder().build();
            interstitialAd.loadAd(adParam);
            Pref.InterHolder = 0;
        } else
            Pref.InterHolder++;
    }

    public static void showInterOnDemand(Activity activity) {
        Pref pref = new Pref(activity);
        InterstitialAd interstitialAd = new InterstitialAd(activity);
        interstitialAd.setAdId(pref.getInterId());
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show(activity);
                } else {
                    Toast.makeText(activity, "Ad did not load", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAdFailed(int i) {
                super.onAdFailed(i);
                Log.d("TAG", "onAdFailed: " + i);
            }
        });

        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.loadAd(adParam);
    }

    public static void loadNative(Context context, FrameLayout frameLayout) {
        Pref pref = new Pref(context);
        NativeAdLoader.Builder builder = new NativeAdLoader.Builder(context, pref.getNativeId());
        NativeAdLoader nativeAdLoader = builder.setNativeAdLoadedListener(new NativeAd.NativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                NativeView nativeView = (NativeView) LayoutInflater.from(context).inflate(R.layout.layout_native, null);

                initNativeAdView(nativeAd, nativeView);

                frameLayout.removeAllViews();
                frameLayout.addView(nativeView);
            }
        }).setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailed(int errorCode) {

            }
        }).build();
        nativeAdLoader.loadAds(new AdParam.Builder().build(), 5);
    }

    public static void loadNativeBig(Context context, FrameLayout frameLayout) {
        Pref pref = new Pref(context);
        NativeAdLoader.Builder builder = new NativeAdLoader.Builder(context, pref.getNativeId());
        NativeAdLoader nativeAdLoader = builder.setNativeAdLoadedListener(new NativeAd.NativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                NativeView nativeView = (NativeView) LayoutInflater.from(context).inflate(R.layout.layout_native_big, null);

                initNativeAdView(nativeAd, nativeView);

                frameLayout.removeAllViews();
                frameLayout.addView(nativeView);
            }
        }).setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailed(int errorCode) {

            }
        }).build();
        nativeAdLoader.loadAds(new AdParam.Builder().build(), 5);
    }

    private static void initNativeAdView(NativeAd nativeAd, NativeView nativeView) {
        // Register and populate the title view.
        nativeView.setTitleView(nativeView.findViewById(R.id.ad_title));
        ((TextView) nativeView.getTitleView()).setText(nativeAd.getTitle());
        // Register and populate the multimedia view.
        nativeView.setMediaView((MediaView) nativeView.findViewById(R.id.ad_media));
        nativeView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        // Register and populate other asset views.
        nativeView.setAdSourceView(nativeView.findViewById(R.id.ad_source));
        nativeView.setCallToActionView(nativeView.findViewById(R.id.ad_call_to_action));
        if (null != nativeAd.getAdSource()) {
            ((TextView) nativeView.getAdSourceView()).setText(nativeAd.getAdSource());
        }
        nativeView.getAdSourceView()
                .setVisibility(null != nativeAd.getAdSource() ? View.VISIBLE : View.INVISIBLE);
        if (null != nativeAd.getCallToAction()) {
            ((Button) nativeView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }
        nativeView.getCallToActionView()
                .setVisibility(null != nativeAd.getCallToAction() ? View.VISIBLE : View.INVISIBLE);


        nativeView.setNativeAd(nativeAd);
    }

    public static void onExit(Context context, OnCloseListener onCloseListener) {
        Pref pref = new Pref(context);
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();


        Button no = dialog.findViewById(R.id.no);
        Button yes = dialog.findViewById(R.id.yes);
        FrameLayout adHolder = dialog.findViewById(R.id.ad_holder);

        NativeAdLoader.Builder builder = new NativeAdLoader.Builder(context, pref.getNativeId());
        NativeAdLoader nativeAdLoader = builder.setNativeAdLoadedListener(new NativeAd.NativeAdLoadedListener() {
            @Override
            public void onNativeAdLoaded(NativeAd nativeAd) {
                NativeView nativeView = (NativeView) LayoutInflater.from(context).inflate(R.layout.layout_native_big, null);

                initNativeAdView(nativeAd, nativeView);

                if (adHolder != null) {
                    adHolder.removeAllViews();
                    adHolder.addView(nativeView);
                }
            }
        }).setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailed(int errorCode) {

            }
        }).build();
        nativeAdLoader.loadAds(new AdParam.Builder().build(), 5);


        if (no != null) {
            no.setOnClickListener(view -> dialog.dismiss());
        }

        if (yes != null) {
            yes.setOnClickListener(view -> {
                dialog.dismiss();
                onCloseListener.close();
            });
        }


    }

    public interface OnCloseListener {
        void close();
    }

}
