package com.doschool.ahu.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.doschool.ahu.R;

/**
 * Created by X on 2018/10/8
 */
public class ReLoginDialog extends Dialog implements View.OnClickListener {

    private String okName;
    private String reLoginName;
    private TextView res_tvk,res_tvrel;

    public ReLoginDialog(@NonNull Context context,AtentListener atentListener) {
        this(context, R.style.ComDialog,atentListener);
    }

    public ReLoginDialog(@NonNull Context context, int themeResId,AtentListener atentListener) {
        super(context, themeResId);
        this.atentListener=atentListener;
    }

    public ReLoginDialog setOKName(String name){
        this.okName=name;
        return this;
    }

    public ReLoginDialog setReLoginName(String name){
        this.reLoginName=name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_resert);
        init();
    }

    private void init(){
        res_tvk=findViewById(R.id.res_tvk);
        res_tvrel=findViewById(R.id.res_tvrel);

        res_tvk.setOnClickListener(this);
        res_tvrel.setOnClickListener(this);

        if(!TextUtils.isEmpty(reLoginName)){
            res_tvrel.setText(reLoginName);
        }

        if(!TextUtils.isEmpty(okName)){
            res_tvk.setText(okName);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.res_tvk:
            case R.id.res_tvrel:
                atentListener.toAct(this);
                break;
        }
    }

    private AtentListener atentListener;
    public interface  AtentListener{
        void  toAct(Dialog dialog);
    }
}
