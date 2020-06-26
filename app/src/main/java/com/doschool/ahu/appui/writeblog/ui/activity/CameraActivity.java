package com.doschool.ahu.appui.writeblog.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.doschool.ahu.R;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.utils.MediaFileUtil;
import com.doschool.ahu.utils.XLToast;

import org.xutils.view.annotation.ViewInject;


import static com.doschool.ahu.configfile.AppConfig.SDK_VIDEO;
import static com.doschool.ahu.configfile.CodeConfig.REQUEST_CODE_SHOOT;

/**
 * Created by X on 2018/9/26
 *
 * 拍摄视频
 */
public class CameraActivity extends BaseActivity {


    @ViewInject(R.id.jcameraview)
    private JCameraView jCameraView;

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_camera_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {

        init();
    }

    private void init(){
        //设置视频保存路径
        jCameraView.setSaveVideoPath(SDK_VIDEO);
        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH);
        //设置视频质量
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_HIGH);
        jCameraView.setTip("轻触拍照，按住摄像");

        //JCameraView监听
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //打开Camera失败回调
                XLToast.showToast("相机打开失败");
            }

            @Override
            public void AudioPermissionError() {
                //没有录取权限回调
                XLToast.showToast("请打开录音权限");
            }
        });

        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                String path= MediaFileUtil.saveBitmap(SDK_VIDEO,bitmap);
                Intent intent=new Intent();
                intent.putExtra("path",path);
                setResult(REQUEST_CODE_SHOOT,intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
//                String path= MediaFileUtil.saveBitmap(SDK_VIDEO,firstFrame);
                Intent intent=new Intent();
//                intent.putExtra("path",path);
                intent.putExtra("url",url);
                setResult(REQUEST_CODE_SHOOT,intent);
                finish();
            }
        });

        //左边按钮点击事件
        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        //右边按钮点击事件
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        jCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        jCameraView.onPause();
    }

    @Override
    protected void DetoryViewAndThing() {

    }
}
