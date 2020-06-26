package com.doschool.ahu.appui.writeblog.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.Smile;
import com.doschool.ahu.factory.JsonEmoj;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.EmojLayout2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by X on 2018/9/26
 *
 * 评论键盘
 */
public class CommentBord extends FrameLayout implements View.OnClickListener{

    public ImageView cb_iv_smil;
    public EditText cb_ex;
    public TextView cb_send;
    public EmojLayout2 cb_emlay;
    private boolean firstSm=true;
    private int blodId,tarUid,tarCommId,userId;
    private String name;
    private List<Smile.SourceExpressionsBean> smileList;

    public CommentBord(@NonNull Context context) {
        this(context,null);
    }

    public CommentBord(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CommentBord(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI(){
        smileList = new ArrayList<>();
        smileList= JsonEmoj.getJson(getContext()).getSourceExpressions();
        LayoutInflater.from(getContext()).inflate(R.layout.comment_bord_lay,this);
        cb_iv_smil=findViewById(R.id.cb_iv_smil);
        cb_ex=findViewById(R.id.cb_ex);
        cb_send=findViewById(R.id.cb_send);
        cb_emlay=findViewById(R.id.cb_emlay);

        cb_iv_smil.setOnClickListener(this);
        cb_send.setOnClickListener(this);
        cb_ex.setOnClickListener(this);

        cb_emlay.setEditText(cb_ex);
    }

    public void setInit(int blogId,int tarUid,int tarCommId,String name){
        this.blodId=blogId;
        this.tarUid=tarUid;
        this.tarCommId=tarCommId;
        this.name=name;

        cb_ex.setFocusable(true);
        cb_ex.setFocusableInTouchMode(true);

        cb_ex.setHint("回复"+name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cb_iv_smil://召唤emj
                if (KeyboardUtils.isSoftInputVisible((Activity) getContext())){
//                    KeyboardUtils.hideSoftInput((Activity) getContext());
                    Activity activity= (Activity) getContext();
                    InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    if (imm == null) return;
                    imm.hideSoftInputFromWindow(cb_ex.getWindowToken(), 0);
                    new Handler().postDelayed(() -> {
                        if (cb_emlay.getVisibility()==GONE){
                            if (firstSm){
                                cb_emlay.showEmojLayout();
                                firstSm=false;
                            }
                            cb_emlay.setVisibility(VISIBLE);
                        }
                    },100);
                }else {
                    if (cb_emlay.getVisibility()==GONE){
                        if (firstSm){
                            cb_emlay.showEmojLayout();
                            firstSm=false;
                        }
                        cb_emlay.setVisibility(VISIBLE);
                    }else {
                        cb_emlay.setVisibility(GONE);
                        showKeyboard();
                    }
                }
                break;
            case R.id.cb_send://评论
                if (blodId==0){
                    XLToast.showToast("请选择目标用户");
                    break;
                }
                KeyboardUtils.hideSoftInput((Activity) getContext());
                cb_emlay.setVisibility(GONE);
                if (!TextUtils.isEmpty(cb_ex.getText().toString())){
                    if (onSendListener!=null){
                        onSendListener.onSend(blodId,tarUid,tarCommId,name,emTostring(),cb_ex);
                    }
                }else {
                    XLToast.showToast("评论不能为空！");
                }
                break;
            case R.id.cb_ex://zheli
                cb_emlay.setVisibility(GONE);
                showKeyboard();
                break;
        }
    }

    private String emTostring(){
        Editable s=cb_ex.getText();
        ImageSpan[] spans=s.getSpans(0, s.length(), ImageSpan.class);
        List<ImageSpan> listSpans = sortByIndex(s, spans);
        for (ImageSpan span:listSpans) {
            int startIndex = s.getSpanStart(span);
            int endIndex = s.getSpanEnd(span);
            for (int i=0;i<smileList.size();i++){
                if (TextUtils.equals(s.subSequence(startIndex,endIndex).toString(),smileList.get(i).getExpressionImageName())){
                    s.replace(startIndex,endIndex,smileList.get(i).getExpressionString());
                }
            }
        }
        return s.toString();
    }
    private List<ImageSpan> sortByIndex(final Editable editInput, ImageSpan[]array){
        ArrayList<ImageSpan> sortList = new ArrayList<>();
        for (ImageSpan span : array){
            sortList.add(span);
        }
        Collections.sort(sortList, new Comparator<ImageSpan>() {
            @Override
            public int compare(ImageSpan lhs, ImageSpan rhs) {
                return editInput.getSpanStart(lhs) - editInput.getSpanStart(rhs);
            }
        });

        return sortList;
    }

    private OnSendListener onSendListener;

    public void setOnSendListener(OnSendListener onSendListener){
        this.onSendListener=onSendListener;
    }

    public interface OnSendListener{
        void onSend(int blogId,int tarUid,int tarCommId,String name,String content,EditText editText);
    }

    private void hideKey(Activity activity){
        if (activity.getCurrentFocus() != null) {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showKeyboard() {
//        cb_ex.setFocusable(true);
//        cb_ex.setFocusableInTouchMode(true);
//        cb_ex.requestFocus();
//        InputMethodManager inputManager = (InputMethodManager) cb_ex.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.showSoftInput(cb_ex, 0);
        KeyboardUtils.showSoftInput((Activity) getContext());
    }
}
