package com.doschool.ahu.appui.main.ui.holderlogic;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.activity.SearchBlogActivity;
import com.doschool.ahu.appui.main.ui.activity.SearchTopicActivity;
import com.doschool.ahu.appui.main.ui.activity.SearchUserActivity;
import com.doschool.ahu.appui.main.ui.bean.SearchWrap;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.utils.IntentUtil;

/**
 * Created by X on 2018/9/27
 */
public class SearchWrapHolder extends BaseRvHolder {

    public LinearLayout wrap_ll;
    public TextView wrap_tx;

    public SearchWrapHolder(View itemView) {
        super(itemView);
        wrap_ll=findViewById(R.id.wrap_ll);
        wrap_tx=findViewById(R.id.wrap_tx);
    }

    public static SearchWrapHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new SearchWrapHolder(view);
    }

    public void serachHolder(Context context, SearchWrap wrap){
            if (!TextUtils.isEmpty(wrap.getKeyword())){
                String word="";
                if (wrap.getSearchType()==1){//用户
                    word="搜索"+wrap.getKeyword()+"相关的用户";
                }else if (wrap.getSearchType()==2){//微博
                    word="搜索"+wrap.getKeyword()+"相关的动态";
                }else if (wrap.getSearchType()==3){//话题
                    word="搜索"+wrap.getKeyword()+"相关的话题";
                }
                SpannableString spannableString=new SpannableString(word);
                spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.new_color)),
                        2,wrap.getKeyword().length()+2, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                wrap_tx.setText(spannableString);

                //跳转相应结果页
                wrap_ll.setOnClickListener(v -> {
                    Bundle bundle=new Bundle();
                    bundle.putInt("type",wrap.getSearchType());
                    bundle.putString("keywords",wrap.getKeyword());
                    if (wrap.getSearchType()==1){
                        IntentUtil.toActivity(context,bundle, SearchUserActivity.class);
                    }else if (wrap.getSearchType()==2){
                        IntentUtil.toActivity(context,bundle, SearchBlogActivity.class);
                    }else if (wrap.getSearchType()==3){
                        IntentUtil.toActivity(context,bundle, SearchTopicActivity.class);
                    }
                });
            }
    }
}
