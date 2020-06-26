package com.doschool.ahu.appui.home.ui.holderlogic;

import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.activity.BlogDetailActivity;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.StringUtil;
import com.doschool.ahu.utils.Utils;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.widget.MediaController;
import com.doschool.ahu.widget.component.MagicText;
import com.doschool.ahu.widget.component.SuperText;
import com.doschool.ahu.widget.nine.GridLayoutHelper;
import com.doschool.ahu.widget.nine.ImageData;
import com.doschool.ahu.widget.nine.NineImageView;
import com.orhanobut.logger.Logger;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by X on 2018/9/12
 */
public class PersonalHolder extends BaseRvHolder {

    public TextView pp_tvday;
    public TextView pp_tvym;
    public TextView pp_tvcontent;
    public RelativeLayout pp_rl;
    public NineImageView pp_nine_iv;
    public TextView pp_tvtime;
    public RelativeLayout pp_rl_content;

    public FrameLayout pp_fl;
    public PLVideoTextureView pp_video;
    public MediaController pp_controller;
    public ImageButton pp_controllerstop_play;
    public TextView pp_current_time;
    public SeekBar pp_progress_bar;
    public TextView pp_end_time;
    public ImageButton pp_screen_image;
    public ImageView pp_cover_image;
    public ImageButton pp_cover_stop;
    public LinearLayout pp_pp_loading_view;

    private int margin;
    private int maxImgWidth;
    private int maxImgHeight;
    private int cellWidth;
    private int cellHeight;
    private int minImgWidth;
    private int minImgHeight;
    private List<ImageData> images;
    private ImageData imageData;

    public PersonalHolder(View itemView) {
        super(itemView);
        margin = ConvertUtils.dp2px( 3);
        maxImgHeight = maxImgWidth = (ScreenUtils.getScreenWidth() - ConvertUtils.dp2px(116));
        cellHeight = cellWidth = (maxImgWidth - margin * 3) / 3;
        minImgHeight = minImgWidth = cellWidth;

        pp_tvday=findViewById(R.id.pp_tvday);
        pp_tvym=findViewById(R.id.pp_tvym);
        pp_tvcontent=findViewById(R.id.pp_tvcontent);
        pp_rl=findViewById(R.id.pp_rl);
        pp_nine_iv=findViewById(R.id.pp_nine_iv);
        pp_tvtime=findViewById(R.id.pp_tvtime);
        pp_rl_content=findViewById(R.id.pp_rl_content);

        pp_fl=findViewById(R.id.pp_fl);
        pp_video=findViewById(R.id.pp_video);
        pp_controller=findViewById(R.id.pp_controller);
        pp_controllerstop_play=findViewById(R.id.pp_controllerstop_play);
        pp_current_time=findViewById(R.id.pp_current_time);
        pp_progress_bar=findViewById(R.id.pp_progress_bar);
        pp_end_time=findViewById(R.id.pp_end_time);
        pp_screen_image=findViewById(R.id.pp_screen_image);
        pp_cover_image=findViewById(R.id.pp_cover_image);
        pp_cover_stop=findViewById(R.id.pp_cover_stop);
        pp_pp_loading_view=findViewById(R.id.pp_pp_loading_view);

    }

    public static PersonalHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new PersonalHolder(view);
    }

    public void setPPData(Context context,MicroblogVO.DataBean data){
        SpannableString spannableString= StringUtil.stringToSpannableString(data.getMicroblogMainDO().getContent(), context, 16);
        pp_tvcontent.setText(spannableString);
        SuperText.txtlink(context,pp_tvcontent);
        pp_tvtime.setText(data.getMicroblogMainDO().getGmtCreate());

        String date=data.getMicroblogMainDO().getGmtCreate();
        String day=date.substring(8,10);
        pp_tvday.setText(day);
        String year=date.substring(0,4);
        String month=date.substring(5,7);
        pp_tvym.setText(year+"."+month);

        if (data.getThumbnailList()!=null && data.getThumbnailList().size()>0 || !TextUtils.isEmpty(data.getVideoName())){
            pp_rl.setVisibility(View.VISIBLE);
            if (data.getThumbnailList()!=null && data.getThumbnailList().size()>0){
                nine(data);
                pp_nine_iv.setVisibility(View.VISIBLE);
            }else {
                pp_nine_iv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(data.getVideoName())){
                pp_fl.setVisibility(View.VISIBLE);
                XLGlideLoader.loadImageByUrl(pp_cover_image,data.getVideoThumbnail());
            }else {
                pp_fl.setVisibility(View.GONE);
            }
        }else {
            pp_rl.setVisibility(View.GONE);
        }

        //微博详情
        pp_rl_content.setOnClickListener(s-> {
            Bundle bundle=new Bundle();
            bundle.putInt("blogId",data.getMicroblogMainDO().getId());
            bundle.putString("blogTag","unRecom");
            IntentUtil.toActivity(context,bundle, BlogDetailActivity.class);});
    }

    private void nine(final MicroblogVO.DataBean data) {

        //设置事件 才能触发itemclick
//        pp_nine_iv.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });
//        pp_nine_iv.setOnItemClickListener(new NineImageView.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//            }
//        });
        images = new ArrayList<>();
        for (int i = 0; i < data.getThumbnailList().size(); i++) {
            imageData = new ImageData(data.getThumbnailList().get(i));
            imageData.realHeight = ConvertUtils.dp2px(120);
            imageData.realWidth = ConvertUtils.dp2px(120);
            images.add(imageData);

            pp_nine_iv.loadGif(false)
                    .enableRoundCorner(true)
                    .setRoundCornerRadius(5)
                    .setData(images, getLayoutHelper(images));
        }
    }

    private GridLayoutHelper getLayoutHelper(List<ImageData> list) {
        int spanCount = Utils.getSize(list);
        if (spanCount == 1) {
            int width = list.get(0).realWidth;
            int height = list.get(0).realHeight;
            if (width > 0 && height > 0) {
                float whRatio = width * 1f / height;
                if (width > height) {
                    width = Math.max(minImgWidth, Math.min(width, maxImgWidth));
                    height = Math.max(minImgHeight, (int) (width / whRatio));
                } else {
                    height = Math.max(minImgHeight, Math.min(height, maxImgHeight));
                    width = Math.max(minImgWidth, (int) (height * whRatio));
                }
            } else {
                width = cellWidth;
                height = cellHeight;
            }
            return new GridLayoutHelper(spanCount, width, height, margin);
        }

        if (spanCount > 3) {
            spanCount = (int) Math.ceil(Math.sqrt(spanCount));
        }

        if (spanCount > 3) {
            spanCount = 3;
        }
        return new GridLayoutHelper(spanCount, cellWidth, cellHeight, margin);
    }
}
