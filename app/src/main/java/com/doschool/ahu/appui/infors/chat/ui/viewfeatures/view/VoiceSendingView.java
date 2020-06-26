package com.doschool.ahu.appui.infors.chat.ui.viewfeatures.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.doschool.ahu.R;


/**
 * 发送语音提示控件
 */
public class VoiceSendingView extends RelativeLayout {


    private AnimationDrawable frameAnimation;
    private ImageView img;

    public VoiceSendingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.voice_sending, this);
        img = (ImageView)findViewById(R.id.microphone);
        setImage(img);
    }

    public void showRecording(){
        img.setBackgroundResource(R.drawable.animation_voice);
        frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();
    }

    public void showCancel(){
        if (frameAnimation.isRunning()){
            frameAnimation.stop();
        }
    }

    public void release(){
        frameAnimation.stop();
    }

    private void setImage(ImageView image){
        this.img=image;
    }

    public ImageView getImageView(){
        return img;
    }
}
