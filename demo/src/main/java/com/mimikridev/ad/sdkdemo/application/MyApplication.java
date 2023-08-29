package com.mimikridev.ad.sdkdemo.application;

import static com.mimikridev.ad.sdk.util.Constant.AD_STATUS_ON;
import static com.mimikridev.ad.sdk.util.Constant.APPLOVIN_MAX;
import static com.mimikridev.ad.sdk.util.Constant.STARTAPP;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;

import com.mimikridev.ad.sdk.format.AppOpenAdAppLovin;
import com.mimikridev.ad.sdk.util.OnShowAdCompleteListener;
import com.mimikridev.ad.sdkdemo.data.Constant;

@SuppressWarnings("ConstantConditions")
public class MyApplication extends Application {

    private AppOpenAdAppLovin appOpenAdAppLovin;

    Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!Constant.FORCE_TO_SHOW_APP_OPEN_AD_ON_START) {
            registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
            ProcessLifecycleOwner.get().getLifecycle().addObserver(lifecycleObserver);

            appOpenAdAppLovin = new AppOpenAdAppLovin();

        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    LifecycleObserver lifecycleObserver = new DefaultLifecycleObserver() {
        @Override
        public void onStart(@NonNull LifecycleOwner owner) {
            DefaultLifecycleObserver.super.onStart(owner);
            if (Constant.isAppOpen) {
                if (Constant.OPEN_ADS_ON_RESUME) {
                    if (Constant.AD_STATUS.equals(AD_STATUS_ON)) {
                        switch (Constant.AD_NETWORK) {
                            case APPLOVIN_MAX:
                                if (!Constant.APPLOVIN_APP_OPEN_AP_ID.equals("0")) {
                                    if (!currentActivity.getIntent().hasExtra("unique_id")) {
                                        appOpenAdAppLovin.showAdIfAvailable(currentActivity, Constant.APPLOVIN_APP_OPEN_AP_ID);
                                    }
                                }
                                break;


                        }
                    }
                }
            }
        }
    };

    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            if (Constant.OPEN_ADS_ON_START) {
                if (Constant.AD_STATUS.equals(AD_STATUS_ON)) {
                    switch (Constant.AD_NETWORK) {

                        case APPLOVIN_MAX:
                            if (!Constant.APPLOVIN_APP_OPEN_AP_ID.equals("0")) {
                                if (!appOpenAdAppLovin.isShowingAd) {
                                    currentActivity = activity;
                                }
                            }
                            break;
                        case STARTAPP:

                    }
                }
            }
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
        }
    };

    public void showAdIfAvailable(@NonNull Activity activity, @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
        if (Constant.OPEN_ADS_ON_START) {
            if (Constant.AD_STATUS.equals(AD_STATUS_ON)) {
                switch (Constant.AD_NETWORK) {
                    case APPLOVIN_MAX:
                        if (!Constant.APPLOVIN_APP_OPEN_AP_ID.equals("0")) {
                            appOpenAdAppLovin.showAdIfAvailable(activity, Constant.APPLOVIN_APP_OPEN_AP_ID, onShowAdCompleteListener);
                            Constant.isAppOpen = true;
                        }
                        break;
                    case STARTAPP:

                }
            }
        }
    }

}
