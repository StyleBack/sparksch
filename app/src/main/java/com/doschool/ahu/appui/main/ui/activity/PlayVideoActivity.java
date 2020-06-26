package com.doschool.ahu.appui.main.ui.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.doschool.ahu.R;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.configfile.AppConfig;
import com.doschool.ahu.widget.MediaController;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnBufferingUpdateListener;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnVideoSizeChangedListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by X on 2018/9/22
 */
public class PlayVideoActivity extends BaseActivity {


    @ViewInject(R.id.plview)
    private PLVideoTextureView plview;

    @ViewInject(R.id.cover_views)
    private ImageView cover_views;

    @ViewInject(R.id.loading_views)
    private LinearLayout loading_views;

    @ViewInject(R.id.top_view)
    private FrameLayout top_view;

    @ViewInject(R.id.play_image_btn)
    private ImageButton play_image_btn;

    @ViewInject(R.id.media_controller)
    private MediaController mediaController;

    private String path;
    private String videoThu;
    private int mediaCodec;
    private boolean mIsLiveStreaming;
    private boolean loop;
    private int mRotation = 0;
    private static final String TAG = PlayVideoActivity.class.getSimpleName();
    private int mDisplayAspectRatio = PLVideoTextureView.ASPECT_RATIO_FIT_PARENT; //default


    @Override
    protected int getContentLayoutID() {
        return R.layout.act_playvideo_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {

        if (getIntent().getExtras() != null) {
            path = getIntent().getExtras().getString("video");
            videoThu = getIntent().getExtras().getString("videoThu");
            mediaCodec = getIntent().getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
            mIsLiveStreaming = getIntent().getIntExtra("liveStreaming", 1) == 1;
            loop = getIntent().getBooleanExtra("loop", false);
        }

        plview.setBufferingIndicator(loading_views);
        plview.setCoverView(cover_views);

        initV();
    }

    private void initV() {
        AVOptions options = new AVOptions();
        // the unit of timeout is ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, mIsLiveStreaming ? 1 : 0);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, mediaCodec);
        boolean disableLog = getIntent().getBooleanExtra("disable-log", false);
        options.setInteger(AVOptions.KEY_LOG_LEVEL, disableLog ? 5 : 0);
        boolean cache = getIntent().getBooleanExtra("cache", false);
        if (!mIsLiveStreaming && cache) {
            options.setString(AVOptions.KEY_CACHE_DIR, AppConfig.DEFAULT_CACHE_DIR_PATH);
        }
        plview.setAVOptions(options);

        plview.setOnInfoListener(mOnInfoListener);
        plview.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        plview.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
//        plview.setOnCompletionListener(mOnCompletionListener);
        plview.setOnErrorListener(mOnErrorListener);

        plview.setLooping(loop);
        plview.setMediaController(mediaController);
        plview.setVideoPath(path);
        plview.start();
    }

    @Event({R.id.play_image_btn})
    private void clicks(View view) {
        switch (view.getId()) {
            case R.id.play_image_btn:
                if (plview.isPlaying()) {
                    plview.pause();
                    play_image_btn.setVisibility(View.VISIBLE);
                } else {
                    plview.start();
                    play_image_btn.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        plview.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        plview.start();
    }

    @Override
    protected void DetoryViewAndThing() {
        plview.stopPlayback();
    }

    public void onClickRotate(View v) {
        mRotation = (mRotation + 90) % 360;
        plview.setDisplayOrientation(mRotation);
    }

    private int mVideoRotation;

    private PLOnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLOnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(int width, int height) {
            Log.i(TAG, "onVideoSizeChanged: width = " + width + ", height = " + height);
            if (width > height && mVideoRotation == 0) {
                //旋转方向
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                plview.setDisplayOrientation(270);
            }
            //如果视频角度是90度
            if (mVideoRotation == 90) {
                //旋转视频
                plview.setDisplayOrientation(270);
            }
        }
    };

    public void onClickSwitchScreen(View v) {
        mDisplayAspectRatio = (mDisplayAspectRatio + 1) % 5;
        plview.setDisplayAspectRatio(mDisplayAspectRatio);
        switch (plview.getDisplayAspectRatio()) {
            case PLVideoTextureView.ASPECT_RATIO_ORIGIN:
                showLogTips("Origin mode");
                break;
            case PLVideoTextureView.ASPECT_RATIO_FIT_PARENT:
                showLogTips("Fit parent !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT:
                showLogTips("Paved parent !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_16_9:
                showLogTips("16 : 9 !");
                break;
            case PLVideoTextureView.ASPECT_RATIO_4_3:
                showLogTips("4 : 3 !");
                break;
            default:
                break;
        }
    }

    private PLOnInfoListener mOnInfoListener = new PLOnInfoListener() {
        @Override
        public void onInfo(int what, int extra) {
            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_START:
                    break;
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_END:
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START:
                    mediaController.hide();
                    showLogTips("First video render time: " + extra + "ms");
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_RENDERING_START:
                    Log.i(TAG, "First audio render time: " + extra + "ms");
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FRAME_RENDERING:
                    Log.i(TAG, "video frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_FRAME_RENDERING:
                    Log.i(TAG, "audio frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_GOP_TIME:
                    Log.i(TAG, "Gop Time: " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_SWITCHING_SW_DECODE:
                    Log.i(TAG, "Hardware decoding failure, switching software decoding!");
                    break;
                case PLOnInfoListener.MEDIA_INFO_METADATA:
                    Log.i(TAG, plview.getMetadata().toString());
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_BITRATE:
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FPS:
                    updateStatInfo();
                    break;
                case PLOnInfoListener.MEDIA_INFO_CONNECTED:
                    Log.i(TAG, "Connected !");
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    Log.i(TAG, "Rotation changed: " + extra);
                    //保存视频角度
                    mVideoRotation = extra;

                    break;
                default:
                    break;
            }
        }
    };

    private PLOnErrorListener mOnErrorListener = new PLOnErrorListener() {
        @Override
        public boolean onError(int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLOnErrorListener.ERROR_CODE_IO_ERROR:
                    /**
                     * SDK will do reconnecting automatically
                     */
                    showLogTips("IO Error !");
                    return false;
                case PLOnErrorListener.ERROR_CODE_OPEN_FAILED:
                    showLogTips("failed to open player !");
                    break;
                case PLOnErrorListener.ERROR_CODE_SEEK_FAILED:
                    showLogTips("failed to seek !");
                    break;
                default:
                    showLogTips("unknown error !");
                    break;
            }
//            finish();
            return true;
        }
    };

//    private PLOnCompletionListener mOnCompletionListener = new PLOnCompletionListener() {
//        @Override
//        public void onCompletion() {
//            Log.i(TAG, "Play Completed !");
//            showLogTips("Play Completed !");
////            finish();
//        }
//    };

    private PLOnBufferingUpdateListener mOnBufferingUpdateListener = new PLOnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(int precent) {
            Log.i(TAG, "onBufferingUpdate: " + precent);
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            onPortraitChanged();
            Log.d("ez", "onConfigurationChanged: 111111111111111111");
        } else {
            Log.d("ez", "onConfigurationChanged:22222222222222222222 ");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            onLandscapeChanged();
        }
    }

    private void onLandscapeChanged() {
        if (plview == null) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) plview.getParent();
        viewGroup.removeAllViews();


        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        plview.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);



//        plview.setMediaController(mLandscapeMC);

    }

    private void onPortraitChanged() {
        if (plview == null) {
            return;
        }
        plview.setVisibility(View.GONE);


        plview.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);

    }

    private MediaController.OnClickSpeedAdjustListener mOnClickSpeedAdjustListener = new MediaController.OnClickSpeedAdjustListener() {
        @Override
        public void onClickNormal() {
            // 0x0001/0x0001 = 2
            plview.setPlaySpeed(0X00010001);
        }

        @Override
        public void onClickFaster() {
            // 0x0002/0x0001 = 2
            plview.setPlaySpeed(0X00020001);
        }

        @Override
        public void onClickSlower() {
            // 0x0001/0x0002 = 0.5
            plview.setPlaySpeed(0X00010002);
        }
    };

    private void showLogTips(final String tips) {
        Log.i(TAG, tips);
    }

    private void updateStatInfo() {
        long bitrate = plview.getVideoBitrate() / 1024;
        final String stat = bitrate + "kbps, " + plview.getVideoFps() + "fps";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mStatInfoTextView.setText(stat);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ez", "onDestroy: 销毁了。。。。");
    }
}
