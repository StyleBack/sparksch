package com.doschool.ahu.appui.main.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.event.WebJsInterface;
import com.doschool.ahu.appui.qrcode.CaptureActivity;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.db.AppConfigDao;
import com.doschool.ahu.utils.MediaFileUtil;
import com.doschool.ahu.utils.UrlUtil;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.AgentWebConfig;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.just.agentweb.download.DownloadListenerAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by X on 2018/9/28
 *
 * web浏览器
 */
public class WebActivity extends BaseActivity {

    public static final int WEBGO=1;
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";

    protected AgentWeb mAgentWeb;

    @ViewInject(R.id.web_back_iv)
    private ImageView web_back_iv;

    @ViewInject(R.id.iv_finish)
    private ImageView iv_finish;

    @ViewInject(R.id.toolbar_title)
    private TextView toolbar_title;

    @ViewInject(R.id.iv_more)
    private ImageView iv_more;

    @ViewInject(R.id.web_llparent)
    private LinearLayout web_llparent;

    private AlertDialog mAlertDialog;

    private int web_code;
    private String webURL;
    private String rootUrl;
    private PopupMenu mPopupMenu;
    private AppConfigDao appConfigDao;
    private ArrayMap<String,String> map=new ArrayMap<>();

    private String menuContent;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_web_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        appConfigDao=new AppConfigDao(this);
        if (appConfigDao.getAppCinfigDO()!=null){
            rootUrl=appConfigDao.getAppCinfigDO().getWebviewRootUrl();
        }
        map= XLNetHttps.getBaseMap(this);
        if (getIntent().getExtras()!=null){
            webURL=getIntent().getExtras().getString("URL");
            web_code=getIntent().getExtras().getInt("code");
        }
        init();
    }

    @SuppressLint("CheckResult")
    private void init(){
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(web_llparent, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(-1,3)
                .setAgentWebWebSettings(getSettings())
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .go(webURL);
        if (web_code!=WEBGO){
            map.put("url", webURL + "");
            String postData = UrlUtil.paramMap2String("post", map);
            mAgentWeb.getUrlLoader().postUrl(rootUrl,postData.getBytes());
        }
        mAgentWeb.getJsAccessEntrace().quickCallJs("finishWindow");
        mAgentWeb.getJsInterfaceHolder().addJavaObject("closeWin",new WebJsInterface(this));

        //二维码扫描
        mAgentWeb.getJsAccessEntrace().quickCallJs("jsbridge_qrscan");
        WebJsInterface webJsInterface=new WebJsInterface(this);
        webJsInterface.setOnQRCode(menu ->{
            menuContent=menu;
            RxPermissions permissions=new RxPermissions(this);
            permissions.requestEachCombined(Manifest.permission.CAMERA)
                    .subscribe(permission -> {
                        if (permission.granted){//同意后调用
                            Intent intent = new Intent(WebActivity.this, CaptureActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_SCAN);
                        }else if (permission.shouldShowRequestPermissionRationale){//禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                        }else {//禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                            if (!permissions.isGranted(Manifest.permission.CAMERA)){
                                XLToast.showToast("您的相机权限未打开！");
                            }
                        }
                    });
                });
        mAgentWeb.getJsInterfaceHolder().addJavaObject("doschool",webJsInterface);
    }

    /**
     * @return IAgentWebSettings
     */
    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;

            @Override
            public IAgentWebSettings toSetting(WebView webView) {
                super.toSetting(webView);
                getWebSettings().setJavaScriptEnabled(true);
                getWebSettings().setSupportZoom(true);
                getWebSettings().setBuiltInZoomControls(true);
                getWebSettings().setMinimumFontSize(8);
                return this;
            }

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView  webView
             * @param downloadListener  downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, android.webkit.DownloadListener downloadListener) {
                return super.setDownloader(webView, DefaultDownloadImpl.create((Activity) webView.getContext(),
                        webView, mDownloadListenerAdapter, mDownloadListenerAdapter,
                        this.mAgentWeb.getPermissionInterceptor()));
            }
        };
    }


    /**
     * 更新于 AgentWeb  4.0.0
     */
    protected DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {
        /**
         * @param url                下载链接
         * @param userAgent          UserAgent
         * @param contentDisposition ContentDisposition
         * @param mimetype           资源的媒体类型
         * @param contentLength      文件长度
         * @param extra              下载配置 ， 用户可以通过 Extra 修改下载icon ， 关闭进度条 ， 是否强制下载。
         * @return true 表示用户处理了该下载事件 ， false 交给 AgentWeb 下载
         */
        @Override
        public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
            extra.setIcon(R.mipmap.app_launcher);
            return false;
        }

        @Override
        public void onProgress(String url, long downloaded, long length, long usedTime) {
            super.onProgress(url, downloaded, length, usedTime);
            if (downloaded==length){
                XLToast.showToast("下载成功！");
            }
        }
    };

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i("Info", "BaseWebActivity onPageStarted");
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (handler != null) {
                handler.proceed();//忽略证书的错误继续加载页面内容，不会变成空白页面
            }
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //do you work
            Log.i("Info","onProgress:"+newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title)) {
                toolbar_title.setText(title);
            }else {
                toolbar_title.setText("");
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_SCAN: // 扫描二维码/条码回传
                    if (data != null) {
                        //返回的文本内容
                        String content = data.getStringExtra(DECODED_CONTENT_KEY);
                        mAgentWeb.getUrlLoader().loadUrl("javascript:doschool_qrscan('"+menuContent+"','"+content+"')");
                    }
                    break;
            }
        }
    }

    @Event({R.id.web_back_iv,R.id.iv_finish,R.id.iv_more})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.web_back_iv://返回上一个web  直至finish页面
                if (!mAgentWeb.back()){
                    finish();
                }
                break;
            case R.id.iv_finish:
                showDialog();
                break;
            case R.id.iv_more://更多
                showPoPup(view);
                break;
        }
    }

    private void showDialog() {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .setMessage("您确定要关闭该页面吗?")
                    .setNegativeButton("再，See一眼", (dialog, which) -> {
                        if (mAlertDialog != null) {
                            mAlertDialog.dismiss();
                        }
                    })//
                    .setPositiveButton("确定", (dialog, which) -> {

                        if (mAlertDialog != null) {
                            mAlertDialog.dismiss();
                        }
                        WebActivity.this.finish();
                    }).create();
        }
        mAlertDialog.show();
        mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.title_color));
        mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.now_txt_color));
    }

    /**
     * 显示更多菜单
     *
     * @param view 菜单依附在该View下面
     */
    private void showPoPup(View view) {
        if (mPopupMenu == null) {
            mPopupMenu = new PopupMenu(this, view);
            mPopupMenu.inflate(R.menu.toolbar_menu);
            mPopupMenu.setOnMenuItemClickListener(mOnMenuItemClickListener);
        }
        mPopupMenu.show();
    }

    /**
     * 菜单事件
     */
    private PopupMenu.OnMenuItemClickListener mOnMenuItemClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {

                case R.id.refresh:
                    if (mAgentWeb != null) {
                        mAgentWeb.getUrlLoader().reload(); // 刷新
                    }
                    return true;

                case R.id.copy:
                    if (mAgentWeb != null) {
                        toCopy(WebActivity.this, mAgentWeb.getWebCreator().getWebView().getUrl());
                    }
                    return true;
                case R.id.default_browser:
                    if (mAgentWeb != null) {
                        openBrowser(mAgentWeb.getWebCreator().getWebView().getUrl());
                    }
                    return true;
                case R.id.default_clean:
                    toCleanWebCache();
                    return true;
                case R.id.error_website:
                    loadErrorWebSite();
                    return true;
                default:
                    return false;
            }

        }
    };


    /**
     * 测试错误页的显示
     */
    private void loadErrorWebSite() {
        if (mAgentWeb != null) {
            mAgentWeb.getUrlLoader().loadUrl("");
        }
    }

    /**
     * 清除 WebView 缓存
     */
    private void toCleanWebCache() {
        if (this.mAgentWeb != null) {
            //清理所有跟WebView相关的缓存 ，数据库， 历史记录 等。
            this.mAgentWeb.clearWebCache();
            //清空所有 AgentWeb 硬盘缓存，包括 WebView 的缓存 , AgentWeb 下载的图片 ，视频 ，apk 等文件。
            AgentWebConfig.clearDiskCache(this);
            AgentWebConfig.removeAllCookies();
            AgentWebConfig.removeSessionCookies();
            //清理h5的存储
            mAgentWeb.getUrlLoader().loadUrl("javascript:localStorage.clear()");
            XLToast.showToast("已清理缓存");
        }
    }

    /**
     * 复制字符串
     *
     * @param context
     * @param text
     */
    private void toCopy(Context context, String text) {
        ClipboardManager mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));
    }

    /**
     * 打开浏览器
     *
     * @param targetUrl 外部浏览器打开的地址
     */
    private void openBrowser(String targetUrl) {
        if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
            XLToast.showToast(targetUrl + " 该链接无法使用浏览器打开！");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri mUri = Uri.parse(targetUrl);
        intent.setData(mUri);
        startActivity(intent);


    }

    @Override
    protected void DetoryViewAndThing() {
        mAgentWeb.getWebLifeCycle().onDestroy();
    }
}
