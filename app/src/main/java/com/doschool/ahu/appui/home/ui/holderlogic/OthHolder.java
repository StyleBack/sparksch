package com.doschool.ahu.appui.home.ui.holderlogic;

import android.annotation.SuppressLint;
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
import com.blankj.utilcode.util.TimeUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.activity.BlogDetailActivity;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.StringUtil;
import com.doschool.ahu.utils.TimeUtil;
import com.doschool.ahu.utils.Utils;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.widget.MediaController;
import com.doschool.ahu.widget.component.SuperText;
import com.doschool.ahu.widget.nine.GridLayoutHelper;
import com.doschool.ahu.widget.nine.ImageData;
import com.doschool.ahu.widget.nine.NineImageView;
import com.jakewharton.rxbinding2.view.RxView;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by X on 2019/1/10.
 */
public class OthHolder extends BaseRvHolder {

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

    public View oth_view;
    public TextView oth_item_cont;
    public RelativeLayout oth_item_rl;
    public NineImageView oth_item_niv;
    public TextView oth_item_time;
    public ImageView single_opr_ivlike;
    public TextView single_opr_tvlike;
    public TextView single_opr_tvcot;
    public TextView single_opr_tvlan;
    public LinearLayout oth_item_llparent;

    private int margin;
    private int maxImgWidth;
    private int maxImgHeight;
    private int cellWidth;
    private int cellHeight;
    private int minImgWidth;
    private int minImgHeight;
    private List<ImageData> images;
    private ImageData imageData;

    public OthHolder(View itemView) {
        super(itemView);
        margin = ConvertUtils.dp2px( 3);
        maxImgHeight = maxImgWidth = (ScreenUtils.getScreenWidth() - ConvertUtils.dp2px(116));
        cellHeight = cellWidth = (maxImgWidth - margin * 3) / 3;
        minImgHeight = minImgWidth = cellWidth;

        pp_fl=itemView.findViewById(R.id.pp_fl);
        pp_video=itemView.findViewById(R.id.pp_video);
        pp_controller=itemView.findViewById(R.id.pp_controller);
        pp_controllerstop_play=itemView.findViewById(R.id.pp_controllerstop_play);
        pp_current_time=itemView.findViewById(R.id.pp_current_time);
        pp_progress_bar=itemView.findViewById(R.id.pp_progress_bar);
        pp_end_time=itemView.findViewById(R.id.pp_end_time);
        pp_screen_image=itemView.findViewById(R.id.pp_screen_image);
        pp_cover_image=itemView.findViewById(R.id.pp_cover_image);
        pp_cover_stop=itemView.findViewById(R.id.pp_cover_stop);
        pp_pp_loading_view=itemView.findViewById(R.id.pp_pp_loading_view);

        oth_view=itemView.findViewById(R.id.oth_view);
        oth_item_llparent=itemView.findViewById(R.id.oth_item_llparent);
        oth_item_cont=itemView.findViewById(R.id.oth_item_cont);
        oth_item_rl=itemView.findViewById(R.id.oth_item_rl);
        oth_item_niv=itemView.findViewById(R.id.oth_item_niv);
        oth_item_time=itemView.findViewById(R.id.oth_item_time);
        single_opr_ivlike=itemView.findViewById(R.id.single_opr_ivlike);
        single_opr_tvlike=itemView.findViewById(R.id.single_opr_tvlike);
        single_opr_tvcot=itemView.findViewById(R.id.single_opr_tvcot);
        single_opr_tvlan=itemView.findViewById(R.id.single_opr_tvlan);
    }

    public static OthHolder newInstance(ViewGroup parent,int layout){
        View view=LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new OthHolder(view);
    }

    @SuppressLint("CheckResult")
    public void setOth(Context context, MicroblogVO.DataBean data){
        SpannableString spannableString= StringUtil.stringToSpannableString(data.getMicroblogMainDO().getContent(), context, 16);
        oth_item_cont.setText(spannableString);
        SuperText.txtlink(context,oth_item_cont);
        Long time=TimeUtils.string2Millis(
                data.getMicroblogMainDO().getGmtCreate(),
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
        oth_item_time.setText(TimeUtil.date2USDiy(time));
        //operat操作按钮
        if (data.getIsLike()==1){
            XLGlideLoader.loadImageById(single_opr_ivlike,R.mipmap.icon_love);
        }else {
            XLGlideLoader.loadImageById(single_opr_ivlike,R.mipmap.icon_love_un);
        }
        if (data.getLikeCount()==0){
            single_opr_tvlike.setText("0");
        }else {
            if (data.getLikeCount()>=1000){
                String count=new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(data.getLikeCount()))/1000);
                single_opr_tvlike.setText(count+"k");
            }else {
                single_opr_tvlike.setText(String.valueOf(data.getLikeCount()));
            }
        }

        if (data.getCommentCount()==0){
            single_opr_tvcot.setText("0");
        }else {
            if (data.getCommentCount()>=1000){
                String count=new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(data.getCommentCount()))/1000);
                single_opr_tvcot.setText(count+"k");
            }else {
                single_opr_tvcot.setText(String.valueOf(data.getCommentCount()));
            }
        }

        if (data.getMicroblogMainDO().getBrowseNum()==0){
            single_opr_tvlan.setText("0");
        }else {
            if (data.getMicroblogMainDO().getBrowseNum()>=1000000){
                String count=new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(data.getMicroblogMainDO().getBrowseNum()))/1000000);
                single_opr_tvlan.setText(count+"M");
            }else if (data.getMicroblogMainDO().getBrowseNum()>=1000){
                String count=new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(data.getMicroblogMainDO().getBrowseNum()))/1000);
                single_opr_tvlan.setText(count+"K");
            }else {
                single_opr_tvlan.setText(String.valueOf(data.getMicroblogMainDO().getBrowseNum()));
            }
        }

        //9宫格和视频
        if (data.getThumbnailList()!=null && data.getThumbnailList().size()>0 || !TextUtils.isEmpty(data.getVideoName())){
            oth_item_rl.setVisibility(View.VISIBLE);
            if (data.getThumbnailList()!=null && data.getThumbnailList().size()>0){
                nine(data);
                oth_item_niv.setVisibility(View.VISIBLE);
            }else {
                oth_item_niv.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(data.getVideoName())){
                pp_fl.setVisibility(View.VISIBLE);
                XLGlideLoader.loadImageByUrl(pp_cover_image,data.getVideoThumbnail());
            }else {
                pp_fl.setVisibility(View.GONE);
            }
        }else {
            oth_item_rl.setVisibility(View.GONE);
        }

        RxView.clicks(oth_item_llparent)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    Bundle bundle=new Bundle();
                    bundle.putInt("blogId",data.getMicroblogMainDO().getId());
                    bundle.putString("blogTag","unRecom");
                    IntentUtil.toActivity(context,bundle, BlogDetailActivity.class);
                });
    }

    private void nine(final MicroblogVO.DataBean data) {
        images = new ArrayList<>();
        for (int i = 0; i < data.getThumbnailList().size(); i++) {
            imageData = new ImageData(data.getThumbnailList().get(i));
            imageData.realHeight = ConvertUtils.dp2px(120);
            imageData.realWidth = ConvertUtils.dp2px(120);
            images.add(imageData);

            oth_item_niv.loadGif(false)
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
