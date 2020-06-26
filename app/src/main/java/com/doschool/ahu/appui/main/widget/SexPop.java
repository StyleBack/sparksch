package com.doschool.ahu.appui.main.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import razerdp.basepopup.BasePopupWindow;

import static android.view.Gravity.BOTTOM;

/**
 * Created by X on 2019/2/13.
 *
 * 从底部弹出
 */
public class SexPop extends BasePopupWindow {


    private TextView sex_tvboy;
    private TextView sex_tvgirl;
    private TextView sex_cancle;

    public SexPop(Context context) {
        super(context);
        setPopupGravity(BOTTOM);
        byViewId();
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.sex_pop_layout);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f,0,300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0,1f,300);
    }

    @SuppressLint("CheckResult")
    private void byViewId(){
        sex_tvboy=findViewById(R.id.sex_tvboy);
        sex_tvgirl=findViewById(R.id.sex_tvgirl);
        sex_cancle=findViewById(R.id.sex_cancle);

        sex_cancle.setOnClickListener(view -> dismiss());
        //男
        RxView.clicks(sex_tvboy)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    if (onSexListener!=null){
                        onSexListener.onSex(sex_tvboy.getText().toString(),2);
                    }
                });

        //女
        RxView.clicks(sex_tvgirl)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    if (onSexListener!=null){
                        onSexListener.onSex(sex_tvgirl.getText().toString(),1);
                    }
                });
    }

    private OnSexListener onSexListener;

    public void setOnSexListener(OnSexListener onSexListener) {
        this.onSexListener = onSexListener;
    }

    public interface OnSexListener{
        void onSex(String sexOT,int type);
    }

}
