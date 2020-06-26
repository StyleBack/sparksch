package com.doschool.ahu.appui.writeblog.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.doschool.ahu.R;

/**
 * Created by X on 2018/9/18
 */
public class CompantDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private TextView title;
    private EditText content;
    private TextView cancel;
    private TextView submit;
    private String positiveName;
    private String negativeName;
    private String titles;
    private int txtLength;

    public CompantDialog setTitles(String titles){
        this.titles=titles;
        return this;
    }

    public CompantDialog setTxtLength(int txtLength){
        this.txtLength=txtLength;
        return this;
    }

    public CompantDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public CompantDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    public CompantDialog showCompant(){
        show();
        return this;
    }

    public CompantDialog(@NonNull Context context, OnListener listener) {
        this(context, R.style.ComDialog,listener);
    }

    public CompantDialog(@NonNull Context context, int themeResId, OnListener listener) {
        super(context, themeResId);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_topic_ex);
        setCanceledOnTouchOutside(true);
        init();
    }

    private void init(){
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        cancel=findViewById(R.id.cancel);
        submit=findViewById(R.id.submit);

        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);

        if (!TextUtils.isEmpty(titles)){
            title.setText(titles);
        }

        if (txtLength>0){
            InputFilter[] filters={new InputFilter.LengthFilter(txtLength)};
            content.setFilters(filters);
        }

        if(!TextUtils.isEmpty(positiveName)){
            submit.setText(positiveName);
        }

        if(!TextUtils.isEmpty(negativeName)){
            cancel.setText(negativeName);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                this.dismiss();
                break;
            case R.id.submit:
                if(listener != null){
                    listener.click(this, content.getText().toString().trim());
                }
                break;
        }
    }


    private OnListener listener;
    public interface OnListener{
        void click(Dialog dialog,String content);
    }
}
