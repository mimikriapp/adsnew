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
import android.util.Log;

import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;

import com.bytedance.sdk.openadsdk.api.init.PAGConfig;
import com.bytedance.sdk.openadsdk.api.init.PAGSdk;
import com.ironsource.mediationsdk.IronSource;
import com.mimikridev.ad.sdk.helper.AudienceNetworkInitializeHelper;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;


public class AdNetwork {

    public static class Initialize {

        private static final String TAG = "AdNetwork";
        Activity activity;
        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String startappAppId = "0";
        private String appLovinSdkKey = "";
        private String pangleAppId = "";
        private String ironSourceAppKey = "";
        private boolean debug = true;

        public Initialize(Activity activity) {
            this.activity = activity;
        }

        public Initialize build() {
            initAds();
            initBackupAds();
            return this;
        }

        public Initialize setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Initialize setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Initialize setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }


        public Initialize setStartappAppId(String startappAppId) {
            this.startappAppId = startappAppId;
            return this;
        }


        public Initialize setAppLovinSdkKey(String appLovinSdkKey) {
            this.appLovinSdkKey = appLovinSdkKey;
            return this;
        }


        public Initialize setIronSourceAppKey(String ironSourceAppKey) {
            this.ironSourceAppKey = ironSourceAppKey;
            return this;
        }
        public Initialize setPangleAppId(String pangleAppId) {
            this.pangleAppId = pangleAppId;
            return this;
        }

        public Initialize setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public void initAds() {
            if (adStatus.equals(AD_STATUS_ON)) {
                switch (adNetwork) {
                    case FACEBOOK:
                        AudienceNetworkInitializeHelper.initializeAd(activity, debug);
                        break;
                    case STARTAPP:
                        StartAppSDK.init(activity, startappAppId, false);
                        StartAppSDK.setTestAdsEnabled(debug);
                        StartAppAd.disableSplash();
                        StartAppSDK.setUserConsent(activity, "pas", System.currentTimeMillis(), true);
                        break;
                    case APPLOVIN_MAX:
                        AppLovinSdk.getInstance(activity).setMediationProvider(AppLovinMediationProvider.MAX);
                        AppLovinSdk.getInstance(activity).initializeSdk(config -> {
                        });
                        AudienceNetworkInitializeHelper.initialize(activity);
                        break;
                    case APPLOVIN_DISCOVERY:
                        AppLovinSdk.initializeSdk(activity);
                        break;

                    case PANGLE:
                        Log.d(TAG, PANGLE + "ADS  " + pangleAppId);
                      PAGConfig pAGInitConfig = new PAGConfig.Builder()
                                .appId(pangleAppId)
                                .debugLog(debug)
                                .supportMultiProcess(false)
                                .build();
                        PAGSdk.init(activity, pAGInitConfig, new PAGSdk.PAGInitCallback() {
                            @Override
                            public void success() {
                                Log.i(TAG, "PANGLE ADS is successfully initialized");
                            }
                            @Override
                            public void fail(int code, String msg) {
                                Log.i(TAG, "PANGLE ADS Failed to Initialize :  " + code);
                            }
                        });



                        break;



                    case IRONSOURCE:
                        String advertisingId = IronSource.getAdvertiserId(activity);
                        IronSource.setUserId(advertisingId);
                        IronSource.init(activity, ironSourceAppKey, () -> {
                            Log.d(TAG, "[" + adNetwork + "] initialize complete");
                        });
//                        IronSource.init(activity, ironSourceAppKey, IronSource.AD_UNIT.REWARDED_VIDEO);
//                        IronSource.init(activity, ironSourceAppKey, IronSource.AD_UNIT.INTERSTITIAL);
//                        IronSource.init(activity, ironSourceAppKey, IronSource.AD_UNIT.BANNER);
                        break;

                }
                Log.d(TAG, "[" + adNetwork + "] is selected as Primary Ads");
            }
        }

        public void initBackupAds() {
            if (adStatus.equals(AD_STATUS_ON)) {
                switch (backupAdNetwork) {
                    case FACEBOOK:
                        AudienceNetworkInitializeHelper.initializeAd(activity, debug);
                        break;
                    case STARTAPP:
                        StartAppSDK.init(activity, startappAppId, false);
                        StartAppSDK.setTestAdsEnabled(debug);
                        StartAppAd.disableSplash();
                        StartAppSDK.setUserConsent(activity, "pas", System.currentTimeMillis(), true);
                        break;
                    case APPLOVIN_MAX:
                        AppLovinSdk.getInstance(activity).setMediationProvider(AppLovinMediationProvider.MAX);
                        AppLovinSdk.getInstance(activity).initializeSdk(config -> {
                        });
                        AudienceNetworkInitializeHelper.initialize(activity);
                        break;

                    case APPLOVIN_DISCOVERY:
                        AppLovinSdk.initializeSdk(activity);
                        break;
                    case IRONSOURCE:
                        String advertisingId = IronSource.getAdvertiserId(activity);
                        IronSource.setUserId(advertisingId);
                        IronSource.init(activity, ironSourceAppKey, () -> {
                            Log.d(TAG, "[" + adNetwork + "] initialize complete");
                        });
//                        IronSource.init(activity, ironSourceAppKey, IronSource.AD_UNIT.REWARDED_VIDEO);
//                        IronSource.init(activity, ironSourceAppKey, IronSource.AD_UNIT.INTERSTITIAL);
//                        IronSource.init(activity, ironSourceAppKey, IronSource.AD_UNIT.BANNER);
                        break;

                    case PANGLE:
                        Log.d(TAG, PANGLE + "ADS  " + pangleAppId);
                        PAGConfig pAGInitConfig = new PAGConfig.Builder()
                                .appId(pangleAppId)
                                .debugLog(debug)
                                .supportMultiProcess(false)
                                .build();
                        PAGSdk.init(activity, pAGInitConfig, new PAGSdk.PAGInitCallback() {
                            @Override
                            public void success() {
                                Log.i(TAG, "PANGLE ADS is successfully initialized");
                            }
                            @Override
                            public void fail(int code, String msg) {
                                Log.i(TAG, "PANGLE ADS Failed to Initialize :  " + code);
                            }
                        });



                        break;
                    case NONE:
                        //do nothing
                        break;
                }
                Log.d(TAG, "[" + backupAdNetwork + "] is selected as Backup Ads");
            }
        }

    }

}
