package com.doschool.ahu.appui.home.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.db.LoginDao;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import razerdp.basepopup.BasePopupWindow;

import static android.view.Gravity.BOTTOM;
import static com.doschool.ahu.appui.home.ui.activity.PersionalActivity.BG_NUM;
import static com.doschool.ahu.appui.home.ui.activity.PersionalActivity.FUN_NUM;
import static com.doschool.ahu.appui.home.ui.activity.PersionalActivity.HD_NUM;

/**
 * Created by X on 2019/2/13.
 *
 * 从底部弹出
 */
public class PersonalPop extends BasePopupWindow {


    private LinearLayout pal_ll;
    private TextView pal_tvmark,pal_tviv,pal_cancle;
    private Context context;
    private LoginDao loginDao;

    public PersonalPop(Context context) {
        super(context);
        this.context=context;
        setPopupGravity(BOTTOM);
        byViewId();
    }

    public PersonalPop onPalData(int targetId,int code){
        setPal(targetId,code);
        return this;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.personal_pop);
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
        loginDao=new LoginDao(context);
        pal_ll=findViewById(R.id.pal_ll);
        pal_tvmark=findViewById(R.id.pal_tvmark);
        pal_tviv=findViewById(R.id.pal_tviv);
        pal_cancle=findViewById(R.id.pal_cancle);

        pal_cancle.setOnClickListener(view -> dismiss());

    }

    @SuppressLint("CheckResult")
    private void setPal(int targetId, int code){
        if (loginDao!=null){
            if (targetId==loginDao.getObject().getUserDO().getId()){//自己的不需要设置备注
                pal_ll.setVisibility(View.GONE);
            }else {
                pal_ll.setVisibility(View.VISIBLE);
            }
        }

        switch (code){
            case BG_NUM:
                pal_tviv.setText("更换封面");
                pal_tviv.setTextColor(context.getResources().getColor(R.color.title_color));
                break;
            case HD_NUM:
                pal_tviv.setText("更换头像");
                pal_tviv.setTextColor(context.getResources().getColor(R.color.title_color));
                break;
            case FUN_NUM:
                pal_tviv.setText("举报");
                pal_tviv.setTextColor(context.getResources().getColor(R.color.tab_count));
                break;
        }

        //设置备注
        RxView.clicks(pal_tvmark)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    if (onPersonalListener!=null){
                        onPersonalListener.onRemark();
                    }
                });

        RxView.clicks(pal_tviv)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    if (onPersonalListener!=null){
                        Observable.timer(300,TimeUnit.MILLISECONDS)
                                .subscribe(aLong -> onPersonalListener.onChange());
                    }
                });
    }

    private OnPersonalListener onPersonalListener;

    public void setOnPersonalListener(OnPersonalListener onPersonalListener) {
        this.onPersonalListener = onPersonalListener;
    }

    public interface OnPersonalListener{
        void onRemark();
        void onChange();
    }

}
