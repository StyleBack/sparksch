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
import io.reactivex.android.schedulers.AndroidSchedulers;
import razerdp.basepopup.BasePopupWindow;

import static android.view.Gravity.BOTTOM;
import static com.doschool.ahu.configfile.CodeConfig.ME_HDIV;

/**
 * Created by X on 2019/2/13.
 *
 * 从底部弹出
 */
public class PersonIVPop extends BasePopupWindow {


    private TextView tv_ivchange;
    private TextView i_cal;

    public PersonIVPop(Context context) {
        super(context);
        setPopupGravity(BOTTOM);
        byViewId();
    }

    public PersonIVPop onCode(int code){
        setContent(code);
        return this;
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.iv_pop_layout);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f,0,300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0,1f,300);
    }

    private void byViewId(){
        tv_ivchange=findViewById(R.id.tv_ivchange);
        i_cal=findViewById(R.id.i_cal);

        i_cal.setOnClickListener(view -> dismiss());
    }

    @SuppressLint("CheckResult")
    private void setContent(int code){

        if (code==ME_HDIV){
            tv_ivchange.setText("更换头像");
        }else {
            tv_ivchange.setText("更换封面");
        }

        RxView.clicks(tv_ivchange)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    Observable.timer(300,TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                if (onIVListener!=null){
                                    onIVListener.onIVChange(code);
                                }
                            });
                });
    }

    private OnIVListener onIVListener;

    public void setOnIVListener(OnIVListener onIVListener) {
        this.onIVListener = onIVListener;
    }

    public interface OnIVListener{
        void onIVChange(int changeCode);
    }

}
