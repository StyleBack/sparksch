package com.doschool.ahu.appui.home.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.db.LoginDao;
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
public class SlideFunPop extends BasePopupWindow {


    private Context context;
    private TextView slide_one_actv;
    private TextView slide_cancle_tv;
    private LoginDao loginDao;

    public SlideFunPop(Context context) {
        super(context);
        this.context=context;
        setPopupGravity(BOTTOM);
        byViewId();
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.slide_bottom_pop_layout);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f,0,300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0,1f,300);
    }

    public SlideFunPop onData(int userId){
        setContent(userId);
        return this;
    }

    private void byViewId(){
        loginDao=new LoginDao(context);
        slide_one_actv=findViewById(R.id.slide_one_actv);
        slide_cancle_tv=findViewById(R.id.slide_cancle_tv);

        slide_cancle_tv.setOnClickListener(view -> dismiss());
    }

    @SuppressLint("CheckResult")
    private void setContent(int userId){
        if (loginDao!=null){
            if (userId==loginDao.getObject().getUserDO().getId()){
                slide_one_actv.setText("删除");
            }else {
                slide_one_actv.setText("举报");
            }
        }
        RxView.clicks(slide_one_actv)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    if (onFunClick!=null){
                        if (userId==loginDao.getObject().getUserDO().getId()){
                            onFunClick.onFun();
                        }else {
                            Observable.timer(300,TimeUnit.MILLISECONDS)
                                    .subscribe(aLong -> onFunClick.onFun());
                        }
                    }
                });
    }

    private OnFunClick onFunClick;

    public void setOnFunClick(OnFunClick onFunClick) {
        this.onFunClick = onFunClick;
    }

    public interface OnFunClick{
        void onFun();
    }
}
