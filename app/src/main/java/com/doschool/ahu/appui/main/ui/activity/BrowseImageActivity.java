package com.doschool.ahu.appui.main.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.adapter.BrowseAdapter;
import com.doschool.ahu.appui.main.widget.FixedViewPager;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.configfile.AppConfig;
import com.doschool.ahu.utils.RandomUtils;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.progress.CircleProgressView;
import com.doschool.ahu.xlhttps.XLDownLoad;
import com.jaeger.library.StatusBarUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by X on 2018/9/29
 *
 *
 * 预览大图
 */
public class BrowseImageActivity extends BaseActivity {

    @ViewInject(R.id.fix_vp)
    private FixedViewPager fix_vp;

    @ViewInject(R.id.tv_tip)
    private TextView tv_tip;

    @ViewInject(R.id.rootView)
    private RelativeLayout rootView;

    @ViewInject(R.id.downProgress)
    private CircleProgressView downProgress;


    private List<String> images=new ArrayList<>();
    private List<String> thumbs=new ArrayList<>();

    private BrowseAdapter browseAdapter;
    private int index;

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this,0,null);
        StatusBarUtil.setDarkMode(this);
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_photo_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {

        if (getIntent().getExtras()!=null){
            index=getIntent().getExtras().getInt("index");
            images= (List<String>) getIntent().getExtras().getSerializable("images");
            thumbs= (List<String>) getIntent().getExtras().getSerializable("thumbs");
        }

        tv_tip.setText(String.format(getString(R.string.image_index), (index + 1), images.size()));
        browseAdapter=new BrowseAdapter(this,images,thumbs);
        fix_vp.setAdapter(browseAdapter);
        fix_vp.setCurrentItem(index);
        fix_vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                index = position;
                tv_tip.setText(String.format(getString(R.string.image_index), (index + 1), images.size()));
            }
        });
    }

    @Event({R.id.ph_ivdown})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.ph_ivdown://下载
                RxPermissions permissions=new RxPermissions(this);
                permissions.requestEachCombined(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(permission -> {
                            if (permission.granted){//同意后调用
                                if (images!=null && images.size()>0){
                                    download();
                                }
                            }else if (permission.shouldShowRequestPermissionRationale){//禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                            }else {//禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                                if (!permissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                                    XLToast.showToast("您的存储权限未打开！");
                                }
                                if (!permissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
                                    XLToast.showToast("您的存储权限未打开！");
                                }
                            }
                        });
                break;
        }
    }

    private void download(){
        String path=AppConfig.DOWNLOAD_FILE+ RandomUtils.getRandName(5)+".jpg";
        XLDownLoad.dowanLoadFile(images.get(index), path, XLDownLoad.GETS, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
                downProgress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                downProgress.setProgress((int) current);
            }

            @Override
            public void onSuccess(File result) {
                XLToast.showToast("成功下载！");
                //通知图库更新
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//如果是4.4及以上版本
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(new File(path)); //out is your output file
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);
                } else {
                    sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://" + Environment.getExternalStorageDirectory())));
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                XLToast.showToast("下载失败！请稍后重试！");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                downProgress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void DetoryViewAndThing() {

    }
}
