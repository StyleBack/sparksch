package com.doschool.ahu.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.utils.XLGlideLoader;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * Created by X on 2018/11/29.
 */
public class SkipScreenDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout skip_type_one,skip_type_two,skip_type_thre;
    private ImageView one_iv_close,two_iv_close;
    private TextView thre_tv_cancle,thre_tv_ok;
    private XImageViewRoundOval one_iv_url,two_iv_url,thre_iv_url;

    private Context context;
    //默认外部区域不可点击
    private boolean defaultCancle=false;
    //确定按钮
    private String positiveName;
    //取消按钮
    private String negativeName;

    private int type;
    private String imgUrl;
    private String doUrl;
    private String leftUrl;
    private String rightUrl;

    public SkipScreenDialog(@NonNull Context context,OnSkipDoListener onSkipDoListener) {
        this(context, R.style.ComDialog,onSkipDoListener);
    }

    public SkipScreenDialog(@NonNull Context context, int themeResId,OnSkipDoListener onSkipDoListener) {
        super(context, themeResId);
        this.context=context;
        this.onSkipDoListener=onSkipDoListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialog();
    }

    @SuppressLint("CheckResult")
    private void initDialog(){
        setContentView(R.layout.skip_screen_layout);
        skip_type_one=findViewById(R.id.skip_type_one);
        skip_type_two=findViewById(R.id.skip_type_two);
        skip_type_thre=findViewById(R.id.skip_type_thre);
        one_iv_url=findViewById(R.id.one_iv_url);
        one_iv_close=findViewById(R.id.one_iv_close);
        two_iv_url=findViewById(R.id.two_iv_url);
        two_iv_close=findViewById(R.id.two_iv_close);
        thre_iv_url=findViewById(R.id.thre_iv_url);
        thre_tv_cancle=findViewById(R.id.thre_tv_cancle);
        thre_tv_ok=findViewById(R.id.thre_tv_ok);

        one_iv_close.setOnClickListener(this::onClick);
        two_iv_close.setOnClickListener(this::onClick);

        setCanceledOnTouchOutside(defaultCancle);
        if (!TextUtils.isEmpty(negativeName)){
            thre_tv_cancle.setText(negativeName);
        }
        if (!TextUtils.isEmpty(positiveName)){
            thre_tv_ok.setText(positiveName);
        }

        if (type==1){
            one_iv_url.setType(XImageViewRoundOval.TYPE_ROUND);
            one_iv_url.setRoundRadius(20);
            XLGlideLoader.loadImageByUrl(one_iv_url,imgUrl);
            skip_type_one.setVisibility(View.VISIBLE);
        }else if (type==2){
            two_iv_url.setType(XImageViewRoundOval.TYPE_ROUND);
            two_iv_url.setRoundRadius(20);
            XLGlideLoader.loadImageByUrl(two_iv_url,imgUrl);
            skip_type_two.setVisibility(View.VISIBLE);
        }else {
            thre_iv_url.setType(XImageViewRoundOval.TYPE_ROUND);
            thre_iv_url.setRoundRadius(20);
            XLGlideLoader.loadImageByUrl(thre_iv_url,imgUrl);
            skip_type_thre.setVisibility(View.VISIBLE);
        }

        //第一种
        RxView.clicks(one_iv_url)
                .throttleLast(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (onSkipDoListener!=null){
                        onSkipDoListener.onSkip(this,doUrl);
                    }
                });
        //第二种
        RxView.clicks(two_iv_url)
                .throttleLast(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (onSkipDoListener!=null){
                        onSkipDoListener.onSkip(this,doUrl);
                    }
                });
        //第三种
        RxView.clicks(thre_iv_url)
                .throttleLast(1,TimeUnit.SECONDS)
                .subscribe(o->{
                    if (onSkipDoListener!=null){
                        onSkipDoListener.onSkip(this,doUrl);
                    }
                });
        RxView.clicks(thre_tv_ok)
                .throttleLast(1,TimeUnit.SECONDS)
                .subscribe(o->{
                    if (onSkipDoListener!=null){
                        onSkipDoListener.onRightSkip(this,rightUrl);
                    }
                });
        RxView.clicks(thre_tv_cancle)
                .throttleLast(1,TimeUnit.SECONDS)
                .subscribe(o->{
                    if (onSkipDoListener!=null){
                        onSkipDoListener.onLeftSkip(this,leftUrl);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.one_iv_close:
            case R.id.two_iv_close:
                this.dismiss();
                break;
        }
    }

    //设置外部区域是否点击消失
    public SkipScreenDialog setCancleTouch(boolean defaultCancle){
        this.defaultCancle=defaultCancle;
        return this;
    }

    //确定按钮 内容展示
    public SkipScreenDialog setPositiveButton(String positiveName){
        this.positiveName = positiveName;
        return this;
    }
    //取消按钮 内容展示
    public SkipScreenDialog setNegativeButton(String negativeName){
        this.negativeName = negativeName;
        return this;
    }

    //显示类型
    public SkipScreenDialog setTypes(int type){
        this.type=type;
        return this;
    }

    //图片
    public SkipScreenDialog setImgUrl(String imgUrl){
        this.imgUrl=imgUrl;
        return this;
    }

    //图片dourl
    public SkipScreenDialog setDoUrl(String doUrl){
        this.doUrl=doUrl;
        return this;
    }
    //左按钮dourl
    public SkipScreenDialog setLeftDoUrl(String leftUrl){
        this.leftUrl=leftUrl;
        return this;
    }
    //右按钮dourl
    public SkipScreenDialog setRightDoUrl(String rightUrl){
        this.rightUrl=rightUrl;
        return this;
    }

    private OnSkipDoListener onSkipDoListener;
    public interface OnSkipDoListener{
        void onLeftSkip(Dialog dialog,String leftUrl);
        void onRightSkip(Dialog dialog,String rightUrl);
        void onSkip(Dialog dialog,String doUrl);
    }
}
