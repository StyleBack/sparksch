package com.doschool.ahu.appui.main.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.Smile;
import com.doschool.ahu.utils.AssetsUtils;

import java.util.List;

/**
 * Created by X on 2018/8/31
 */
public class EmotionAdapter extends BaseAdapter {


    private List<Smile.SourceExpressionsBean> ebList;
    Context context;

    public EmotionAdapter(Context context, List<Smile.SourceExpressionsBean> ebList) {
        this.ebList = ebList;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ImageView ivEmotion = new ImageView(context);
        ivEmotion.setImageBitmap(AssetsUtils.getBitmap(context,ebList.get(position).getExpressionImageName()));
        LinearLayout layout = new LinearLayout(context);
        layout.setGravity(Gravity.CENTER);
        layout.setTag(ebList.get(position));
        layout.addView(ivEmotion, ConvertUtils.dp2px(28), ConvertUtils.dp2px(48));
        return layout;

    }

    public int getCount() {
        return ebList.size();
    }

    @Override
    public Object getItem(int position) {
        return ebList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
