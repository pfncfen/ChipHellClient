package com.fei_ke.chiphellclient;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieSyncManager;

import com.fei_ke.chiphellclient.utils.LogMessage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ContentLengthInputStream;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;
import com.umeng.update.UmengUpdateAgent;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

public class ChhApplication extends Application {
    private static ChhApplication instance;
    private String formHash;

    @Override
    public void onCreate() {
        super.onCreate();

        CookieSyncManager.createInstance(this);

        instance = this;

        LogMessage.setDebug(BuildConfig.DEBUG);

        initImageLoader();

        setupUpdate();
    }

    private void setupUpdate() {
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
    }


    public String getFormHash() {
        return formHash;
    }

    public void setFormHash(String formHash) {
        this.formHash = formHash;
    }

    // 初始化ImageLoader
    private void initImageLoader() {
        DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.logo)
                .showImageOnFail(R.drawable.logo)
                .showImageOnLoading(R.drawable.logo)
                // .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(10, TimeUnit.SECONDS);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .denyCacheImageMultipleSizesInMemory()
                .defaultDisplayImageOptions(defaultDisplayImageOptions)
                .imageDownloader(new OkHttpImageDownloader(this, client))
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }

    static class OkHttpImageDownloader extends BaseImageDownloader {


        private OkHttpClient client;


        public OkHttpImageDownloader(Context context, OkHttpClient client) {
            super(context);
            this.client = client;
        }


        @Override
        protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {
            Request request = new Request.Builder().url(imageUri).build();
            ResponseBody responseBody = client.newCall(request).execute().body();
            InputStream inputStream = responseBody.byteStream();
            int contentLength = (int) responseBody.contentLength();
            return new ContentLengthInputStream(inputStream, contentLength);
        }
    }

    public static ChhApplication getInstance() {
        return instance;
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return !TextUtils.isEmpty(formHash);
    }


}
