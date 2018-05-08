package com.example.ncgamelands.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ncgamelands.R;
import com.example.ncgamelands.model.CustomInfo;
import com.example.ncgamelands.util.AppUtil;
import com.example.ncgamelands.util.RuntimeData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegulationActivity extends BaseActivity {

    @BindView(R.id.ivback)
    ImageView ivback;
    @BindView(R.id.tvtitle)
    TextView tvtitle;
    @BindView(R.id.ivicon)
    ImageView ivicon;

    @BindView(R.id.llbiggame)
    LinearLayout llbiggame;
    @BindView(R.id.llsmallgame)
    LinearLayout llsmallgame;

    @BindView(R.id.tvbiggame)
    TextView tvbiggame;
    @BindView(R.id.tvsmallgame)
    TextView tvsmallgame;

    @BindView(R.id.biggame)
    LinearLayout biggame;
    @BindView(R.id.smallgame)
    LinearLayout smallgame;

    @BindView(R.id.wvbig)
    WebView wvbig;
    @BindView(R.id.wvsmall)
    WebView wvsmall;

    CustomInfo item;

    private static String bigURL;
    private static String smallURL;
    int landId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regulation);
        ButterKnife.bind(this);

        ivicon.setVisibility(View.VISIBLE);
        ivicon.setImageResource(R.drawable.ic_file_download_white);

        item = RuntimeData.getInstance().getItem();
        if (item != null) {
            tvtitle.setText(item.getLabel());
            landId = item.getId();
        }

        llbiggame.setSelected(true);
        invalidateOptions();

        loadBigWV();
        loadSmallWV();
    }

    private void loadBigWV() {
        bigURL = "file:///android_asset/small_game_regulations.htm";

        switch (landId) {
            case 1:
                bigURL = "file:///android_asset/south_land.htm";
                break;
            case 2:
                bigURL = "file:///android_asset/alcoa_land.htm";
                break;
            case 3:
                bigURL = "file:///android_asset/second_creek_land.htm";
                break;
            default:
                break;
        }

        WebSettings webSetting = wvbig.getSettings();
//        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);

        wvbig.setWebViewClient(new WebViewClient());
        wvbig.loadUrl(bigURL);
    }

    private void loadSmallWV() {
        smallURL = "file:///android_asset/small_game_regulations.htm";

        WebSettings webSetting = wvsmall.getSettings();
//        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);

        wvsmall.setWebViewClient(new WebViewClient());
        wvsmall.loadUrl(smallURL);
    }

    private void invalidateOptions() {
        tvbiggame.setSelected(llbiggame.isSelected());

        llsmallgame.setSelected(!llbiggame.isSelected());
        tvsmallgame.setSelected(llsmallgame.isSelected());

        if (llbiggame.isSelected()) {
            biggame.setVisibility(View.VISIBLE);
            smallgame.setVisibility(View.GONE);
        } else {
            biggame.setVisibility(View.GONE);
            smallgame.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.ivicon)
    public void onDownload() {
        ArrayList<String> fileList;

        switch (landId) {
            case 1:
                fileList = new ArrayList<>();
                fileList.add("south_land.pdf");
                fileList.add("small_game_regulations.pdf");

                AppUtil.copyFromAssetsToDevice(this, fileList);
                break;
            case 2:
                fileList = new ArrayList<>();
                fileList.add("alcoa_land.pdf");
                fileList.add("small_game_regulations.pdf");

                AppUtil.copyFromAssetsToDevice(this, fileList);
                break;
            case 3:
                fileList = new ArrayList<>();
                fileList.add("second_creek_land.pdf");
                fileList.add("small_game_regulations.pdf");

                AppUtil.copyFromAssetsToDevice(this, fileList);
                break;
            default:
                break;
        }

        WebSettings webSetting = wvbig.getSettings();
//        webSetting.setBuiltInZoomControls(true);
        webSetting.setJavaScriptEnabled(true);

        wvbig.setWebViewClient(new WebViewClient());
        wvbig.loadUrl(bigURL);
    }

    @OnClick(R.id.ivback)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.llbiggame)
    public void onBiggame() {
        llbiggame.setSelected(true);
        invalidateOptions();
    }

    @OnClick(R.id.llsmallgame)
    public void onSmallgame() {
        llbiggame.setSelected(false);
        invalidateOptions();
    }

    public class WebViewClient extends android.webkit.WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            try {
                view.loadUrl(url);
            } catch (Exception e) {
                Log.e("TAG", e.getMessage());
            }

            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            progressBar.setVisibility(View.GONE);

            try {
//                RegulationActivity.smallURL = url;
            } catch (Exception e) {
                Log.e("TAG", e.getMessage());
            }
        }

    }

}