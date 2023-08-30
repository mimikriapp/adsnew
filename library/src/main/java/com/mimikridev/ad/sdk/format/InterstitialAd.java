package com.mimikridev.ad.sdk.format;


import static com.mimikridev.ad.sdk.util.Constant.AD_STATUS_ON;
import static com.mimikridev.ad.sdk.util.Constant.APPLOVIN_DISCOVERY;
import static com.mimikridev.ad.sdk.util.Constant.APPLOVIN_MAX;
import static com.mimikridev.ad.sdk.util.Constant.FACEBOOK;
import static com.mimikridev.ad.sdk.util.Constant.IRONSOURCE;
import static com.mimikridev.ad.sdk.util.Constant.NONE;
import static com.mimikridev.ad.sdk.util.Constant.PANGLE;
import static com.mimikridev.ad.sdk.util.Constant.STARTAPP;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdk;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAd;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialAdLoadListener;
import com.bytedance.sdk.openadsdk.api.interstitial.PAGInterstitialRequest;
import com.facebook.ads.InterstitialAdListener;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener;
import com.mimikridev.ad.sdk.util.OnInterstitialAdDismissedListener;
import com.mimikridev.ad.sdk.util.OnInterstitialAdShowedListener;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;

import java.util.concurrent.TimeUnit;

public class InterstitialAd {

    @SuppressWarnings("deprecation")
    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;
        private com.facebook.ads.InterstitialAd fanInterstitialAd;
        private StartAppAd startAppAd;
        private MaxInterstitialAd maxInterstitialAd;
        public AppLovinInterstitialAdDialog appLovinInterstitialAdDialog;
        public AppLovinAd appLovinAd;

        private PAGInterstitialAd interstitialAd;
        private int retryAttempt;
        private int counter = 1;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobInterstitialId = "";
        private String googleAdManagerInterstitialId = "";
        private String fanInterstitialId = "";
        private String appLovinInterstitialId = "";
        private String appLovinInterstitialZoneId = "";
        private String ironSourceInterstitialId = "";
        private String pangleInterstitialId = "";
        private int placementStatus = 1;
        private int interval = 3;
        private boolean legacyGDPR = false;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder build() {
            loadInterstitialAd();
            return this;
        }

        public Builder build(OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
            loadInterstitialAd(onInterstitialAdDismissedListener);
            return this;
        }

        public void show() {
            showInterstitialAd();
        }

        public void show(OnInterstitialAdShowedListener onInterstitialAdShowedListener, OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
            showInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Builder setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }


        public Builder setFanInterstitialId(String fanInterstitialId) {
            this.fanInterstitialId = fanInterstitialId;
            return this;
        }

        public Builder setAppLovinInterstitialId(String appLovinInterstitialId) {
            this.appLovinInterstitialId = appLovinInterstitialId;
            return this;
        }

        public Builder setAppLovinInterstitialZoneId(String appLovinInterstitialZoneId) {
            this.appLovinInterstitialZoneId = appLovinInterstitialZoneId;
            return this;
        }
        public Builder setPangleInterstitialId(String pangleInterstitialId) {
            this.pangleInterstitialId = pangleInterstitialId;
            return this;
        }
        public Builder setIronSourceInterstitialId(String ironSourceInterstitialId) {
            this.ironSourceInterstitialId = ironSourceInterstitialId;
            return this;
        }

        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setInterval(int interval) {
            this.interval = interval;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public void loadInterstitialAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (adNetwork) {
                    case FACEBOOK:
                        fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, fanInterstitialId);
                        com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                fanInterstitialAd.loadAd();
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                                loadBackupInterstitialAd();
                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                Log.d(TAG, "FAN Interstitial is loaded");
                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                        fanInterstitialAd.loadAd(loadAdConfig);
                        break;

                    case STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad ad) {
                                Log.d(TAG, "Startapp Interstitial Ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable Ad ad) {
                                Log.d(TAG, "Failed to load Startapp Interstitial Ad");
                                loadBackupInterstitialAd();
                            }
                        });
                        break;
                    case APPLOVIN_MAX:
                        maxInterstitialAd = new MaxInterstitialAd(appLovinInterstitialId, activity);
                        maxInterstitialAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                retryAttempt = 0;
                                Log.d(TAG, "AppLovin Interstitial Ad loaded...");
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                maxInterstitialAd.loadAd();
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                retryAttempt++;
                                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                                new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                                loadBackupInterstitialAd();
                                Log.d(TAG, "failed to load AppLovin Interstitial");
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                maxInterstitialAd.loadAd();
                            }
                        });

                        // Load the first ad
                        maxInterstitialAd.loadAd();
                        break;

                    case APPLOVIN_DISCOVERY:
                        //AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle interstitialExtras = new Bundle();
                        interstitialExtras.putString("zone_id", appLovinInterstitialZoneId);
                        //builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                        AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinAd = ad;
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                loadBackupInterstitialAd();
                            }
                        });
                        appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                        break;

                    case PANGLE:
                        Log.d(TAG, "PANGLE interstitial ad choosed");
                        PAGInterstitialRequest request = new PAGInterstitialRequest();
                        PAGInterstitialAd.loadAd(pangleInterstitialId,
                                request,
                                new PAGInterstitialAdLoadListener() {
                                    @Override
                                    public void onError(int code, String message) {
                                        Log.d(TAG, "PANGLE interstitial ad error "+ code + " " + message);
                                        loadBackupInterstitialAd();
                                    }

                                    @Override
                                    public void onAdLoaded(PAGInterstitialAd interstitialAd) {
                                        Log.d(TAG, "PANGLE interstitial ad loaded");
                                    }
                                });


/*
                        PAGInterstitialAd.loadAd(pangleInterstitialId,
                                new PAGInterstitialRequest(),
                                new PAGInterstitialAdLoadListener() {
                                    @Override
                                    public void onError(int code, String message) {
                                        Log.d(TAG, "PANGLE interstitial ad error" + code + "msg"+message);
                                        loadBackupInterstitialAd();
                                    }

                                    @Override
                                    public void onAdLoaded(PAGInterstitialAd pagInterstitialAd) {
                                        interstitialAd = pagInterstitialAd;
                                        Log.d(TAG, "Pangle Interstitial Ad loaded...");
                                    }
                                });

 */

                        break;


                    case IRONSOURCE:
                        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
                            @Override
                            public void onAdReady(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdReady");
                            }

                            @Override
                            public void onAdLoadFailed(IronSourceError ironSourceError) {
                                Log.d(TAG, "onInterstitialAdLoadFailed" + " " + ironSourceError);
                                loadBackupInterstitialAd();
                            }

                            @Override
                            public void onAdOpened(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdOpened");
                            }

                            @Override
                            public void onAdShowSucceeded(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowSucceeded");
                            }

                            @Override
                            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowFailed" + " " + ironSourceError);
                                loadBackupInterstitialAd();
                            }

                            @Override
                            public void onAdClicked(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClicked");
                            }

                            @Override
                            public void onAdClosed(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClosed");
                                loadInterstitialAd();
                            }
                        });
                        IronSource.loadInterstitial();
                        break;


                }
            }
        }

        public void loadBackupInterstitialAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAdNetwork) {
                    case FACEBOOK:
                        fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, fanInterstitialId);
                        com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                fanInterstitialAd.loadAd();
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {

                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                Log.d(TAG, "FAN Interstitial is loaded");
                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                        fanInterstitialAd.loadAd(loadAdConfig);
                        break;

                    case STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad ad) {
                                Log.d(TAG, "Startapp Interstitial Ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable Ad ad) {
                                Log.d(TAG, "Failed to load Startapp Interstitial Ad");
                            }
                        });
                        Log.d(TAG, "load StartApp as backup Ad");
                        break;
                    case APPLOVIN_MAX:
                        maxInterstitialAd = new MaxInterstitialAd(appLovinInterstitialId, activity);
                        maxInterstitialAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                retryAttempt = 0;
                                Log.d(TAG, "AppLovin Interstitial Ad loaded...");
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                maxInterstitialAd.loadAd();
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                retryAttempt++;
                                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                                new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                                Log.d(TAG, "failed to load AppLovin Interstitial");
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                maxInterstitialAd.loadAd();
                            }
                        });

                        // Load the first ad
                        maxInterstitialAd.loadAd();
                        break;

                    case APPLOVIN_DISCOVERY:
                        //AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle interstitialExtras = new Bundle();
                        interstitialExtras.putString("zone_id", appLovinInterstitialZoneId);
                       // builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                        AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinAd = ad;
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                            }
                        });
                        appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                        break;

                    case IRONSOURCE:
                        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
                            @Override
                            public void onAdReady(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdReady");
                            }

                            @Override
                            public void onAdLoadFailed(IronSourceError ironSourceError) {
                                Log.d(TAG, "onInterstitialAdLoadFailed" + " " + ironSourceError);
                            }

                            @Override
                            public void onAdOpened(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdOpened");
                            }

                            @Override
                            public void onAdShowSucceeded(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowSucceeded");
                            }

                            @Override
                            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowFailed" + " " + ironSourceError);
                            }

                            @Override
                            public void onAdClicked(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClicked");
                            }

                            @Override
                            public void onAdClosed(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClosed");
                                loadInterstitialAd();
                            }
                        });
                        IronSource.loadInterstitial();
                        break;

                    case PANGLE:
                        PAGInterstitialRequest request = new PAGInterstitialRequest();
                        PAGInterstitialAd.loadAd(pangleInterstitialId,
                                request,
                                new PAGInterstitialAdLoadListener() {
                                    @Override
                                    public void onError(int code, String message) {
                                        Log.d(TAG, "PANGLE interstitial ad error" + code + "msg"+message);
                                    }

                                    @Override
                                    public void onAdLoaded(PAGInterstitialAd interstitialAd) {
                                        Log.d(TAG, "PANGLE interstitial ad loaded");
                                    }
                                });
                        break;

                    case NONE:
                        //do nothing
                        break;
                }
            }
        }

        public void showInterstitialAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                if (counter == interval) {
                    switch (adNetwork) {
                        case FACEBOOK:
                            if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                                fanInterstitialAd.show();
                                Log.d(TAG, "fan interstitial not null");
                            } else {
                                showBackupInterstitialAd();
                                Log.d(TAG, "fan interstitial null");
                            }
                            break;

                        case STARTAPP:
                            if (startAppAd != null) {
                                startAppAd.showAd();
                                Log.d(TAG, "startapp interstitial not null [counter] : " + counter);
                            } else {
                                showBackupInterstitialAd();
                                Log.d(TAG, "startapp interstitial null");
                            }
                            break;

                        case APPLOVIN_MAX:
                            if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                                Log.d(TAG, "ready : " + counter);
                                maxInterstitialAd.showAd();
                                Log.d(TAG, "show ad");
                            } else {
                                showBackupInterstitialAd();
                            }
                            break;

                        case APPLOVIN_DISCOVERY:
                            if (appLovinInterstitialAdDialog != null) {
                                appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                            }
                            break;

                        case PANGLE:
                            /*PAGInterstitialRequest request = new PAGInterstitialRequest();
                            PAGInterstitialAd.loadAd(pangleInterstitialId,
                                    request,
                                    new PAGInterstitialAdLoadListener() {
                                        @Override
                                        public void onError(int code, String message) {
                                            Log.d(TAG, "PANGLE interstitial ad error");

                                        }

                                        @Override
                                        public void onAdLoaded(PAGInterstitialAd interstitialAd) {
                                            Log.d(TAG, "PANGLE interstitial ad loaded");
                                        }
                                    });

                             */


                          //  interstitialAd.show(activity);
                            if (interstitialAd != null) {
                                interstitialAd.show(activity);
                            }
                            else {showBackupInterstitialAd();
                            }

                            break;

                        case IRONSOURCE:
                            if (IronSource.isInterstitialReady()) {
                                IronSource.showInterstitial(ironSourceInterstitialId);
                            } else {
                                showBackupInterstitialAd();
                            }
                            break;


                    }
                    counter = 1;
                } else {
                    counter++;
                }
                Log.d(TAG, "Current counter : " + counter);
            }
        }

        public void showBackupInterstitialAd() {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                Log.d(TAG, "Show Backup Interstitial Ad [" + backupAdNetwork.toUpperCase() + "]");
                switch (backupAdNetwork) {
                    case FACEBOOK:
                        if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                            fanInterstitialAd.show();
                        }
                        break;

                    case STARTAPP:
                        if (startAppAd != null) {
                            startAppAd.showAd();
                        }
                        break;

                    case APPLOVIN_MAX:
                        if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                            maxInterstitialAd.showAd();
                        }
                        break;

                    case APPLOVIN_DISCOVERY:
                        if (appLovinInterstitialAdDialog != null) {
                            appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                        }
                        break;

                    case IRONSOURCE:
                        if (IronSource.isInterstitialReady()) {
                            IronSource.showInterstitial(ironSourceInterstitialId);
                        }
                        break;

                    case PANGLE:

                        if (interstitialAd != null) {
                            interstitialAd.show(activity);

                        }
                        break;

                    case NONE:
                        //do nothing
                        break;
                }
            }
        }

        public void loadInterstitialAd(OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (adNetwork) {
                    case FACEBOOK:
                        fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, fanInterstitialId);
                        com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                fanInterstitialAd.loadAd();
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                Log.d(TAG, "FAN Interstitial is loaded");
                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                        fanInterstitialAd.loadAd(loadAdConfig);
                        break;

                    case STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad ad) {
                                Log.d(TAG, "Startapp Interstitial Ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable Ad ad) {
                                Log.d(TAG, "Failed to load Startapp Interstitial Ad");
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            }
                        });
                        break;

                    case APPLOVIN_MAX:
                        maxInterstitialAd = new MaxInterstitialAd(appLovinInterstitialId, activity);
                        maxInterstitialAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                retryAttempt = 0;
                                Log.d(TAG, "AppLovin Interstitial Ad loaded...");
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                maxInterstitialAd.loadAd();
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                retryAttempt++;
                                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                                new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                                Log.d(TAG, "failed to load AppLovin Interstitial");
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                maxInterstitialAd.loadAd();
                            }
                        });

                        // Load the first ad
                        maxInterstitialAd.loadAd();
                        break;

                    case APPLOVIN_DISCOVERY:
                        //AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle interstitialExtras = new Bundle();
                        interstitialExtras.putString("zone_id", appLovinInterstitialZoneId);
                        //builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                        AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinAd = ad;
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            }
                        });
                        appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                        break;

                    case PANGLE:
                        Log.d(TAG, "PANGLE interstitial ad choosed");
                     /*   PAGInterstitialRequest request = new PAGInterstitialRequest();
                        PAGInterstitialAd.loadAd(pangleInterstitialId,
                                request,
                                new PAGInterstitialAdLoadListener() {
                                    @Override
                                    public void onError(int code, String message) {
                                        Log.d(TAG, "PANGLE interstitial ad error "+ code + " " + message);
                                        loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                                    }

                                    @Override
                                    public void onAdLoaded(PAGInterstitialAd interstitialAd) {
                                        Log.d(TAG, "PANGLE interstitial ad loaded");
                                    }
                                });

                      */



                        PAGInterstitialAd.loadAd(pangleInterstitialId,
                                new PAGInterstitialRequest(),
                                new PAGInterstitialAdLoadListener() {
                                    @Override
                                    public void onError(int code, String message) {
                                        Log.d(TAG, "PANGLE interstitial ad error" + code + "msg"+message);
                                        loadBackupInterstitialAd();
                                    }

                                    @Override
                                    public void onAdLoaded(PAGInterstitialAd pagInterstitialAd) {
                                        interstitialAd = pagInterstitialAd;
                                        Log.d(TAG, "Pangle Interstitial Ad loaded...");
                                    }
                                });



                        break;

                    case IRONSOURCE:
                        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
                            @Override
                            public void onAdReady(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdReady");
                            }

                            @Override
                            public void onAdLoadFailed(IronSourceError ironSourceError) {
                                Log.d(TAG, "onInterstitialAdLoadFailed" + " " + ironSourceError);
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            }

                            @Override
                            public void onAdOpened(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdOpened");
                            }

                            @Override
                            public void onAdShowSucceeded(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowSucceeded");
                            }

                            @Override
                            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowFailed" + " " + ironSourceError);
                                loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            }

                            @Override
                            public void onAdClicked(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClicked");
                            }

                            @Override
                            public void onAdClosed(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClosed");
                                loadInterstitialAd(onInterstitialAdDismissedListener);
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }
                        });
                        IronSource.loadInterstitial();
                        break;


                }
            }
        }

        public void loadBackupInterstitialAd(OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                switch (backupAdNetwork) {
                    case FACEBOOK:
                        fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, fanInterstitialId);
                        com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                            @Override
                            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                                fanInterstitialAd.loadAd();
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {

                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                Log.d(TAG, "FAN Interstitial is loaded");
                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                        fanInterstitialAd.loadAd(loadAdConfig);
                        break;

                    case STARTAPP:
                        startAppAd = new StartAppAd(activity);
                        startAppAd.loadAd(new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad ad) {
                                Log.d(TAG, "Startapp Interstitial Ad loaded");
                            }

                            @Override
                            public void onFailedToReceiveAd(@Nullable Ad ad) {
                                Log.d(TAG, "Failed to load Startapp Interstitial Ad");
                            }
                        });
                        Log.d(TAG, "load StartApp as backup Ad");
                        break;


                    case APPLOVIN_MAX:
                        maxInterstitialAd = new MaxInterstitialAd(appLovinInterstitialId, activity);
                        maxInterstitialAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(MaxAd ad) {
                                retryAttempt = 0;
                                Log.d(TAG, "AppLovin Interstitial Ad loaded...");
                            }

                            @Override
                            public void onAdDisplayed(MaxAd ad) {
                            }

                            @Override
                            public void onAdHidden(MaxAd ad) {
                                maxInterstitialAd.loadAd();
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }

                            @Override
                            public void onAdClicked(MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(String adUnitId, MaxError error) {
                                retryAttempt++;
                                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                                new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                                Log.d(TAG, "failed to load AppLovin Interstitial");
                            }

                            @Override
                            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                                maxInterstitialAd.loadAd();
                            }
                        });

                        // Load the first ad
                        maxInterstitialAd.loadAd();
                        break;

                    case APPLOVIN_DISCOVERY:
                        //AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle interstitialExtras = new Bundle();
                        interstitialExtras.putString("zone_id", appLovinInterstitialZoneId);
                        //builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                        AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinAd = ad;
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                            }
                        });
                        appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                        break;

                    case PANGLE:
                        PAGInterstitialRequest request = new PAGInterstitialRequest();
                        PAGInterstitialAd.loadAd(pangleInterstitialId,
                                request,
                                new PAGInterstitialAdLoadListener() {
                                    @Override
                                    public void onError(int code, String message) {
                                        Log.d(TAG, "PANGLE interstitial ad error" + code + "msg"+message);
                                    }

                                    @Override
                                    public void onAdLoaded(PAGInterstitialAd interstitialAd) {
                                        Log.d(TAG, "PANGLE interstitial ad loaded");
                                    }
                                });
                        break;



                    case IRONSOURCE:
                        IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
                            @Override
                            public void onAdReady(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdReady");
                            }

                            @Override
                            public void onAdLoadFailed(IronSourceError ironSourceError) {
                                Log.d(TAG, "onInterstitialAdLoadFailed" + " " + ironSourceError);
                            }

                            @Override
                            public void onAdOpened(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdOpened");
                            }

                            @Override
                            public void onAdShowSucceeded(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowSucceeded");
                            }

                            @Override
                            public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdShowFailed" + " " + ironSourceError);
                            }

                            @Override
                            public void onAdClicked(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClicked");
                            }

                            @Override
                            public void onAdClosed(AdInfo adInfo) {
                                Log.d(TAG, "onInterstitialAdClosed");
                                loadInterstitialAd(onInterstitialAdDismissedListener);
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }
                        });
                        IronSource.loadInterstitial();
                        break;


                    case NONE:
                        //do nothing
                        break;
                }
            }
        }

        public void showInterstitialAd(OnInterstitialAdShowedListener onInterstitialAdShowedListener, OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                if (counter == interval) {
                    switch (adNetwork) {
                        case FACEBOOK:
                            if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                                fanInterstitialAd.show();
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                                Log.d(TAG, "fan interstitial not null");
                            } else {
                                showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                                Log.d(TAG, "fan interstitial null");
                            }
                            break;

                        case STARTAPP:
                            if (startAppAd != null) {
                                startAppAd.showAd(new AdDisplayListener() {
                                    @Override
                                    public void adHidden(Ad ad) {
                                        onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                    }

                                    @Override
                                    public void adDisplayed(Ad ad) {
                                        onInterstitialAdShowedListener.onInterstitialAdShowed();
                                    }

                                    @Override
                                    public void adClicked(Ad ad) {

                                    }

                                    @Override
                                    public void adNotDisplayed(Ad ad) {

                                    }
                                });
                                Log.d(TAG, "startapp interstitial not null [counter] : " + counter);
                            } else {
                                showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                                Log.d(TAG, "startapp interstitial null");
                            }
                            break;


                        case APPLOVIN_MAX:
                            if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                                Log.d(TAG, "ready : " + counter);
                                maxInterstitialAd.showAd();
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                                Log.d(TAG, "show ad");
                            } else {
                                showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                            }
                            break;

                        case APPLOVIN_DISCOVERY:
                            if (appLovinInterstitialAdDialog != null) {
                                appLovinInterstitialAdDialog.setAdDisplayListener(new AppLovinAdDisplayListener() {
                                    @Override
                                    public void adDisplayed(AppLovinAd appLovinAd) {
                                        onInterstitialAdShowedListener.onInterstitialAdShowed();
                                    }

                                    @Override
                                    public void adHidden(AppLovinAd appLovinAd) {
                                        onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                    }
                                });
                                appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                            }
                            break;

                        case PANGLE:

                            if (interstitialAd != null) {
                                interstitialAd.show(activity);
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                            } else {
                                showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                            }
                            break;

                        case IRONSOURCE:
                            if (IronSource.isInterstitialReady()) {
                                IronSource.showInterstitial(ironSourceInterstitialId);
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                            } else {
                                showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                            }
                            break;


                    }
                    counter = 1;
                } else {
                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                    counter++;
                }
                Log.d(TAG, "Current counter : " + counter);
            } else {
                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
            }
        }

        public void showBackupInterstitialAd(OnInterstitialAdShowedListener onInterstitialAdShowedListener, OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {
                Log.d(TAG, "Show Backup Interstitial Ad [" + backupAdNetwork.toUpperCase() + "]");
                switch (backupAdNetwork) {
                    case FACEBOOK:
                        if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                            fanInterstitialAd.show();
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                        }
                        break;

                    case STARTAPP:
                        if (startAppAd != null) {
                            startAppAd.showAd(new AdDisplayListener() {
                                @Override
                                public void adHidden(Ad ad) {
                                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                }

                                @Override
                                public void adDisplayed(Ad ad) {
                                    onInterstitialAdShowedListener.onInterstitialAdShowed();
                                }

                                @Override
                                public void adClicked(Ad ad) {

                                }

                                @Override
                                public void adNotDisplayed(Ad ad) {

                                }
                            });
                        }
                        break;

                    case APPLOVIN_MAX:
                        if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                            maxInterstitialAd.showAd();
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                        }
                        break;

                    case APPLOVIN_DISCOVERY:
                        if (appLovinInterstitialAdDialog != null) {
                            appLovinInterstitialAdDialog.setAdDisplayListener(new AppLovinAdDisplayListener() {
                                @Override
                                public void adDisplayed(AppLovinAd appLovinAd) {
                                    onInterstitialAdShowedListener.onInterstitialAdShowed();
                                }

                                @Override
                                public void adHidden(AppLovinAd appLovinAd) {
                                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                }
                            });
                            appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                        }
                        break;

                    case IRONSOURCE:
                        if (IronSource.isInterstitialReady()) {
                            IronSource.showInterstitial(ironSourceInterstitialId);
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                        }
                        break;

                    case PANGLE:

                        if (interstitialAd != null) {
                            interstitialAd.show(activity);
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                        }
                        break;

                    case NONE:
                        //do nothing
                        break;
                }
            } else {
                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
            }
        }

    }

}
