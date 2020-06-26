package com.doschool.ahu.appui.main.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.doschool.ahu.appui.main.ui.bean.Smile;
import com.doschool.ahu.configfile.CodeConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by X on 2018/8/31
 */
public class EmojVPAdapter extends PagerAdapter {

    private Context context;
    private int pageCount;
    private List<Smile.SourceExpressionsBean> list;
    private AdapterView.OnItemClickListener onEmotionClickListener;


    public EmojVPAdapter(Context context, int pageCount, List<Smile.SourceExpressionsBean> list,AdapterView.OnItemClickListener onEmotionClickListener) {
        this.context = context;
        this.pageCount = pageCount;
        this.list = list;
        this.onEmotionClickListener=onEmotionClickListener;
    }

       List<List<Smile.SourceExpressionsBean>> totalList = new ArrayList<>();


    public List<List<Smile.SourceExpressionsBean>> getTotalList() {
        return totalList;
    }

    public void setTotalList(List<List<Smile.SourceExpressionsBean>> totalList) {
        this.totalList = totalList;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {

        // 填充resList,作为图片数据;
        List<Smile.SourceExpressionsBean> pageSmileList = new ArrayList<>();

        for (int i = CodeConfig.EMOJ_ROW_COUNT * CodeConfig.EMOJ_COLUMN_COUNT * position-position; i <= CodeConfig.EMOJ_ROW_COUNT * CodeConfig.EMOJ_COLUMN_COUNT * (position + 1) - (1+position); i++) {
            if (i >= list.size())
                break;
            if(CodeConfig.EMOJ_ROW_COUNT * CodeConfig.EMOJ_COLUMN_COUNT * (position + 1) - (1+position)==i){
                Smile.SourceExpressionsBean delBean=new Smile.SourceExpressionsBean();
                delBean.setExpressionImageName("1001");
                pageSmileList.add(delBean);
            }else {
                pageSmileList.add(list.get(i));
            }
            if(CodeConfig.EMOJ_ROW_COUNT * CodeConfig.EMOJ_COLUMN_COUNT * (position + 1) - (1+position)==i) {
                totalList.add(pageSmileList);
            }
        }


        GridView gridview = new GridView(context);
        gridview.setNumColumns(CodeConfig.EMOJ_COLUMN_COUNT);
        gridview.setAdapter(new EmotionAdapter(context, pageSmileList));
        gridview.setOnItemClickListener(onEmotionClickListener);
        container.addView(gridview, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        return gridview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return pageCount;
    }
}
