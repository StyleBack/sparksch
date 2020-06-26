package com.doschool.ahu.appui.home.widget;

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
public class SlideRemarkPop extends BasePopupWindow {


    private TextView remark_tvname;
    private TextView spu_tv_report;
    private TextView spu_tv_cancle;

    public SlideRemarkPop(Context context) {
        super(context);
        setPopupGravity(BOTTOM);
        byViewId();
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.slide_bottom_pop_layout2);
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
        remark_tvname=findViewById(R.id.remark_tvname);
        spu_tv_report=findViewById(R.id.spu_tv_report);
        spu_tv_cancle=findViewById(R.id.spu_tv_cancle);

        spu_tv_cancle.setOnClickListener(view -> dismiss());
        //设置备注
        RxView.clicks(remark_tvname)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    if (onMarkpotListener!=null){
                        onMarkpotListener.onRemark();
                    }
                });

        //举报
        RxView.clicks(spu_tv_report)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    Observable.timer(300,TimeUnit.MILLISECONDS)
                            .subscribe(aLong -> {
                                if (onMarkpotListener!=null){
                                    onMarkpotListener.onReport();
                                }
                            });
                });
    }

    private OnMarkpotListener onMarkpotListener;

    public void setOnMarkpotListener(OnMarkpotListener onMarkpotListener) {
        this.onMarkpotListener = onMarkpotListener;
    }

    public interface OnMarkpotListener{
        void onRemark();
        void onReport();
    }

}
