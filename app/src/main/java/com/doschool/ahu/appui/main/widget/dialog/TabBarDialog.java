package com.doschool.ahu.appui.main.widget.dialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.writeblog.ui.activity.CameraActivity;
import com.doschool.ahu.utils.XLToast;
import com.jakewharton.rxbinding2.view.RxView;
import com.matisseutil.GifFilter;
import com.matisseutil.Glide4Engine;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

import static com.doschool.ahu.configfile.CodeConfig.REQUEST_CODE_PH;
import static com.doschool.ahu.configfile.CodeConfig.REQUEST_CODE_SHOOT;

/**
 * Created by X on 2018/8/14
 */
public class TabBarDialog extends Dialog {

    private Context context;
    private Handler handler;
    private RelativeLayout rlMain;
    private LinearLayout llBtnArticle,llBtnMiniCanera, llBtnPhoto, llBtnMenu;
    private ImageView ivMenu,iv_memory,iv_camera,iv_photo;

    public TabBarDialog(@NonNull Context context) {
        this(context, R.style.Main_dialog_style);
    }

    public TabBarDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //全屏
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
        /**
         * 初始化
         */
    @SuppressLint("CheckResult")
    private void init() {
        handler = new Handler();
        //填充视图
        setContentView(R.layout.main_dialog);
        rlMain = (RelativeLayout) findViewById(R.id.mainPublish_dialog_rlMain);

        llBtnArticle = (LinearLayout) findViewById(R.id.mainPublish_dialog_llBtnArticle);
        llBtnMiniCanera = (LinearLayout) findViewById(R.id.mainPublish_dialog_llBtnMiniCamera);
        llBtnPhoto = (LinearLayout) findViewById(R.id.mainPublish_dialog_llBtnPhoto);
        llBtnMenu = (LinearLayout) findViewById(R.id.mainPublish_dialog_llBtnMenu);
        ivMenu = (ImageView) findViewById(R.id.mainPublish_dialog_ivMenu);
        iv_memory=(ImageView)findViewById(R.id.iv_memory);
        iv_camera=(ImageView)findViewById(R.id.iv_camera);
        iv_photo=(ImageView)findViewById(R.id.iv_photo);

        RxView.clicks(ivMenu)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        outputDialog();
                    }
                });

        RxView.clicks(rlMain)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        outputDialog();
                    }
                });
    }

    /**
     * 取消对话框（带动画）
     */
    private void outputDialog() {
        //退出动画
        rlMain.startAnimation(AnimationUtils.loadAnimation(context, R.anim.mainactivity_fade_out));
        ivMenu.startAnimation(AnimationUtils.loadAnimation(context, R.anim.mainactivity_rotate_left));
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                dismiss();
            }
        }, 400);
        llBtnArticle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.mainactivity_push_bottom_out));
        llBtnArticle.setVisibility(View.INVISIBLE);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                llBtnMiniCanera.startAnimation(AnimationUtils.loadAnimation(context, R.anim.mainactivity_push_bottom_out));
                llBtnMiniCanera.setVisibility(View.INVISIBLE);
            }
        }, 50);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                llBtnPhoto.startAnimation(AnimationUtils.loadAnimation(context, R.anim.mainactivity_push_bottom_out));
                llBtnPhoto.setVisibility(View.INVISIBLE);
            }
        }, 100);

    }

    /**
     * 进入对话框（带动画）
     */
    private void inputDialog() {
        llBtnArticle.setVisibility(View.INVISIBLE);
        llBtnMiniCanera.setVisibility(View.INVISIBLE);
        llBtnPhoto.setVisibility(View.INVISIBLE);
        //背景动画
        rlMain.startAnimation(AnimationUtils.loadAnimation(context, R.anim.mainactivity_fade_in));
        //菜单按钮动画
        ivMenu.startAnimation(AnimationUtils.loadAnimation(context, R.anim.mainactivity_rotate_right));
        //选项动画
        llBtnArticle.setVisibility(View.VISIBLE);
        llBtnArticle.startAnimation(AnimationUtils.loadAnimation(context, R.anim.mainactivity_push_bottom_in));
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                llBtnMiniCanera.setVisibility(View.VISIBLE);
                llBtnMiniCanera.startAnimation(AnimationUtils.loadAnimation(context, R.anim.mainactivity_push_bottom_in));
            }
        }, 100);
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                llBtnPhoto.setVisibility(View.VISIBLE);
                llBtnPhoto.startAnimation(AnimationUtils.loadAnimation(context, R.anim.mainactivity_push_bottom_in));
            }
        }, 200);
    }

    @Override
    public void show() {
        super.show();
        inputDialog();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isShowing()) {
            outputDialog();
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @SuppressLint("CheckResult")
    public TabBarDialog setArticleBtnClickListener(final Class<?> clazz, final Bundle bundle) {
        RxView.clicks(iv_memory)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o ->{
                    intentGo(clazz,bundle);
                } );
        return this;
    }

    @SuppressLint("CheckResult")
    public TabBarDialog setMiniCaneraBtnClickListener(final Class<?> clazz, final Bundle bundle) {
        RxView.clicks(iv_camera)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        intentGo(clazz,bundle);
                    }
                });
        return this;
    }

    @SuppressLint("CheckResult")
    public TabBarDialog setPhotoBtnClickListener(final Class<?> clazz, final Bundle bundle) {
        RxView.clicks(iv_photo)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        intentGo(clazz,bundle);
                    }
                });
        return this;
    }

    private void intentGo( final Class<?> clazz, final Bundle bundle){
        outputDialog();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(context,clazz);
                if (bundle!=null){
                    intent.putExtras(bundle);
                }
                context.startActivity(intent);
            }
        },400);
    }

    //录制视频
    @SuppressLint("CheckResult")
    public TabBarDialog setMiniCaneraBtnClickListener(final Activity activity) {
        RxView.clicks(iv_camera)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> getPerssions(activity,1));
        return this;
    }
    private void handVideo(final Activity activity){
        outputDialog();
        handler.postDelayed(() -> {
            Intent intent=new Intent(activity, CameraActivity.class);
            activity.startActivityForResult(intent,REQUEST_CODE_SHOOT);
        },400);
    }


    //从相册选择视频
    @SuppressLint("CheckResult")
    public TabBarDialog setPhotoBtnClickListener(Activity activity) {
        RxView.clicks(iv_photo)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> getPerssions(activity,2));
        return this;
    }
    //从相册选择视频
    private void intentVideo(){
        outputDialog();
        handler.postDelayed(() -> miatiss(),400);
    }
    private void miatiss(){
        Matisse.from((Activity) context)
                .choose(MimeType.ofVideo())
                .showSingleMediaType(true)
                .countable(true)
                .maxSelectable(1)
                .addFilter(new GifFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(context.getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(REQUEST_CODE_PH);
    }

    //获取权限
    @SuppressLint("CheckResult")
    private void getPerssions(Activity activity, int zone){
        RxPermissions permissions=new RxPermissions((FragmentActivity) activity);
        permissions.requestEachCombined(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted){//同意后调用
                        if (zone==1){
                            handVideo(activity);
                        }else {
                            intentVideo();
                        }
                    }else if (permission.shouldShowRequestPermissionRationale){//禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                    }else {//禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                        if (!permissions.isGranted(Manifest.permission.CAMERA)){
                            XLToast.showToast("您的相机权限未打开！");
                        }
                        if (!permissions.isGranted(Manifest.permission.RECORD_AUDIO)){
                            XLToast.showToast("您的麦克风权限未打开！");
                        }
                        if (!permissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            XLToast.showToast("您的存储权限未打开！");
                        }
                        if (!permissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
                            XLToast.showToast("您的存储权限未打开！");
                        }
                    }
                });
    }
}
