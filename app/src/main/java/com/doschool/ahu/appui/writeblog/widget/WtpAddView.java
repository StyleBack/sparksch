package com.doschool.ahu.appui.writeblog.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doschool.ahu.R;

/**
 * Created by X on 2019/1/8.
 *
 */
public class WtpAddView extends LinearLayout {

    private LinearLayout parentLl;
    private EditText vote_ext;
    private ImageView vote_iv;

    public WtpAddView(Context context) {
        super(context);
    }

    public WtpAddView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void createUI(LinearLayout parentLl, String hintCont,TextView addTv){
        this.parentLl=parentLl;
        View view=LayoutInflater.from(parentLl.getContext()).inflate(R.layout.vote_item_layout,null,true);
        vote_ext=view.findViewById(R.id.vote_ext);
        vote_iv=view.findViewById(R.id.vote_iv);
        if (!TextUtils.isEmpty(hintCont)){
            vote_ext.setHint(parentLl.getContext().getResources().getString(R.string.blog_hint_rtp)+"\t\t例："+hintCont);
        }else {
            vote_ext.setHint(parentLl.getContext().getResources().getString(R.string.blog_hint_rtp));
        }
        vote_iv.setOnClickListener(v -> {
            RelativeLayout relativeLayout= (RelativeLayout) v.getParent();
            LinearLayout layout= (LinearLayout) relativeLayout.getParent();
            delView(layout,addTv);
        });
        parentLl.addView(view);

        if(parentLl.getChildCount() >2){
            showVisible();
        }
    }

    private void delView(LinearLayout l,TextView addTv) {
        parentLl.removeView(l);
        if (addTv.getVisibility()==GONE){
            addTv.setVisibility(VISIBLE);
        }
        if (parentLl.getChildCount()<3){
            for (int i = 0; i < parentLl.getChildCount(); i++) {
                LinearLayout child = (LinearLayout) parentLl.getChildAt(i);
                vote_iv = child.findViewById(R.id.vote_iv);
                vote_iv.setVisibility(View.GONE);
            }
        }
    }

    private void showVisible() {
        for (int i = 0; i < parentLl.getChildCount(); i++) {
            LinearLayout child = (LinearLayout) parentLl.getChildAt(i);
            vote_iv = child.findViewById(R.id.vote_iv);
            vote_iv.setVisibility(View.VISIBLE);
        }
    }

}
