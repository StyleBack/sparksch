package com.doschool.ahu.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.activity.UpLoadValidationActivity;
import com.doschool.ahu.appui.main.ui.activity.VerifingActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static android.graphics.Color.TRANSPARENT;

/**
 * Created by X on 2018/10/25
 */
public class AlertUtils {

    public static void alertToVerify(Context context, int status){
        View view= LayoutInflater.from(context).inflate(R.layout.alert_phlogin_lay,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);
        RelativeLayout ph_build_rl=view.findViewById(R.id.ph_build_rl);
        ImageView ph_ivx=view.findViewById(R.id.ph_ivx);
        AlertDialog dialog=builder.create();
        dialog.setCancelable(true);
        //背景透明 避免圆角边框的同时会出现白色直角部分
        Window window=dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        dialog.show();

        ph_ivx.setOnClickListener(v -> dialog.dismiss());
        ph_build_rl.setOnClickListener(v ->{
            dialog.dismiss();
            if (status==1){
                IntentUtil.toActivity(context,null, VerifingActivity.class);
            }else {
                IntentUtil.toActivity(context,null, UpLoadValidationActivity.class);
            }
        });
    }

    //加载
    public static Disposable interval(TextView view){
        return Observable.interval(100,300,TimeUnit.MILLISECONDS)
                .take(600000)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    long load=aLong%3;
                    if (load==0){
                        view.setText("下载中.");
                    }else if (load==1){
                        view.setText("下载中..");
                    }else {
                        view.setText("下载中...");
                    }
                });
    }

}
