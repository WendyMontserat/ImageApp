package com.bring.cloverpetal;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Potlibate extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        atrLep();
    }

    private void atrLep() {
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                JSONObject json = new JSONObject(conversionData);
                write_key1(json.toString());
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                JSONObject json = new JSONObject();
                try {
                    json.put("error", errorMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                write_key1(json.toString());
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> attributionData) {
                for (String attrName : attributionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + attributionData.get(attrName));
                }
            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }
        };

        AppsFlyerLib.getInstance().init("QPa8HEEssbzdKfbBmpHjyN", conversionListener, this); //todo вставить ключ апса
        AppsFlyerLib.getInstance().start(this);
    }

    public void write_key1(String s) {
        SharedPreferences myPrefs = getSharedPreferences("file", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("key1", s);
        editor.apply();
    }
}

