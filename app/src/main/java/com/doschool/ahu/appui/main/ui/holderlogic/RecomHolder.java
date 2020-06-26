package com.doschool.ahu.appui.main.ui.holderlogic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.activity.HotTopicListActivity;
import com.doschool.ahu.appui.home.ui.activity.BlogDetailActivity;
import com.doschool.ahu.appui.home.ui.activity.OtherSingleActivity;
import com.doschool.ahu.appui.main.event.UserComm;
import com.doschool.ahu.appui.main.ui.activity.BrowseImageActivity;
import com.doschool.ahu.appui.main.ui.adapter.BlogCommentAdpter;
import com.doschool.ahu.appui.main.ui.adapter.VoteAdapter;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVoteOptionsDto;
import com.doschool.ahu.appui.main.ui.bean.RecomList;
import com.doschool.ahu.base.BaseApplication;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.StringUtil;
import com.doschool.ahu.utils.TimeUtil;
import com.doschool.ahu.utils.Utils;
import com.doschool.ahu.utils.VideoUtils;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.MediaController;
import com.doschool.ahu.widget.component.SuperText;
import com.doschool.ahu.widget.nine.GridLayoutHelper;
import com.doschool.ahu.widget.nine.ImageData;
import com.doschool.ahu.widget.nine.NineImageView;
import com.jakewharton.rxbinding2.view.RxView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by X on 2019/1/21.
 */
public class RecomHolder extends BaseRvHolder {

    public LinearLayout blog_item_parent;
    public RelativeLayout blog_rl_item;
    public RelativeLayout top_rl;//top
    public LinearLayout blog_ll_it;
    public ImageView top_ivhead;//top头像
    public TextView top_tvname;//top昵称
    public TextView top_tvmsg;//top m
    public TextView top_tvxdj;//top新动静

    public ImageView blog_ivhead;//header 头像
    public TextView blog_tvname;//header 昵称
    public ImageView blog_ividentify;//header 老师或组织标签
    public ImageView blog_ivsex;//header 学生男女
    public TextView blog_tvyx;//header 时间  院系
    public ImageView blog_ivfun;//header 功能键

    public TextView content_tv_topic;//content 话题标签
    public TextView content_tv_cont;//content 内容

    public ImageView operat_ivlove;//operat 赞
    public TextView operat_tvlove_count;//operat 赞数量
    public TextView operat_tvcomm_count;//operat 评论数量
    public TextView operat_tvcomm_ll;//浏览数
    public TextView operat_kac;//投票的分享

    public ImageView blog_ivhot;//hot 标签

    public RecyclerView comment_rv;//comment 评论列表
    public LinearLayout child_llcomm;
    public BlogCommentAdpter blogCommentAdpter;
    public LinearLayoutManager linearLayoutManager;

    public NineImageView nine_iv;//9宫格图片
    public RelativeLayout nine_ll;

    public FrameLayout video_fl;//视频
    public PLVideoTextureView plvideo;
    public MediaController media_controller;
    public ImageButton controller_stop_play;
    public TextView controller_current_time;
    public SeekBar controller_progress_bar;
    public TextView controller_end_time;
    public ImageButton full_screen_image;
    public ImageView cover_image;
    public ImageButton cover_stop_play;
    public LinearLayout loading_view;

    private int margin;
    private int maxImgWidth;
    private int maxImgHeight;
    private int cellWidth;
    private int cellHeight;
    private int minImgWidth;
    private int minImgHeight;
    private List<ImageData> images;
    private ImageData imageData;

    public String videoPath;

    //地标
    public LinearLayout location_ll;
    public TextView location_tx;

    //投票
    public LinearLayout item_vote_ll;
    public RecyclerView vote_item_rv;
    public VoteAdapter voteAdapter;
    public LinearLayout operat_lka;

    public RecomHolder(View itemView) {
        super(itemView);

        margin = ConvertUtils.dp2px( 3);
        maxImgHeight = maxImgWidth = (ScreenUtils.getScreenWidth() - ConvertUtils.dp2px(12) * 2);
        cellHeight = cellWidth = (maxImgWidth - margin * 3) / 3;
        minImgHeight = minImgWidth = cellWidth;

        blog_item_parent=findViewById(R.id.blog_item_parent);
        blog_rl_item=findViewById(R.id.blog_rl_item);
        blog_ll_it=findViewById(R.id.blog_ll_it);
        top_rl=findViewById(R.id.top_rl);
        top_ivhead=findViewById(R.id.top_ivhead);
        top_tvname=findViewById(R.id.top_tvname);
        top_tvmsg=findViewById(R.id.top_tvmsg);
        top_tvxdj=findViewById(R.id.top_tvxdj);

        blog_ivhead=findViewById(R.id.blog_ivhead);
        blog_tvname=findViewById(R.id.blog_tvname);
        blog_ividentify=findViewById(R.id.blog_ividentify);
        blog_ivsex=findViewById(R.id.blog_ivsex);
        blog_tvyx=findViewById(R.id.blog_tvyx);
        blog_ivfun=findViewById(R.id.blog_ivfun);

        content_tv_topic=findViewById(R.id.content_tv_topic);
        content_tv_cont=findViewById(R.id.content_tv_cont);

        blog_ivhot=findViewById(R.id.blog_ivhot);

        operat_ivlove=findViewById(R.id.operat_ivlove);
        operat_tvlove_count=findViewById(R.id.operat_tvlove_count);
        operat_tvcomm_count=findViewById(R.id.operat_tvcomm_count);
        operat_tvcomm_ll=findViewById(R.id.operat_tvcomm_ll);
        operat_kac=findViewById(R.id.operat_kac);
        operat_lka=findViewById(R.id.operat_lka);

        comment_rv=findViewById(R.id.comment_rv);
        child_llcomm=findViewById(R.id.child_llcomm);

        nine_iv=findViewById(R.id.nine_iv);
        nine_ll=findViewById(R.id.nine_ll);

        video_fl=findViewById(R.id.video_fl);
        plvideo=findViewById(R.id.plvideo);
        media_controller=findViewById(R.id.media_controller);
        controller_stop_play=findViewById(R.id.controller_stop_play);
        controller_current_time=findViewById(R.id.controller_current_time);
        controller_progress_bar=findViewById(R.id.controller_progress_bar);
        controller_end_time=findViewById(R.id.controller_end_time);
        full_screen_image=findViewById(R.id.full_screen_image);
        cover_image=findViewById(R.id.cover_image);
        cover_stop_play=findViewById(R.id.cover_stop_play);
        loading_view=findViewById(R.id.loading_view);

        location_ll=findViewById(R.id.location_ll);
        location_tx=findViewById(R.id.location_tx);

        item_vote_ll=findViewById(R.id.item_vote_ll);
        vote_item_rv=findViewById(R.id.vote_item_rv);
    }

    public static RecomHolder newInstance(ViewGroup parent,int layout){
        View view=LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new RecomHolder(view);
    }

    @SuppressLint("CheckResult")
    public void setRecomHolder(Context context, RecomList rcom, String mic ){
        if (rcom==null){
            blog_item_parent.setVisibility(View.GONE);
            return;
        }else {
            blog_item_parent.setVisibility(View.VISIBLE);
        }

        //item点击事件
        RxView.clicks(blog_rl_item)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(o -> {
                    //判断是否联网
                    if (!NetworkUtils.isConnected()){
                        XLToast.showToast(BaseApplication.getContext().getResources().getString(R.string.unnetwork));
                    } else {
                        Bundle bundle=new Bundle();
                        bundle.putInt("blogId",rcom.getMicroblogMainDO().getId());
                        bundle.putString("blogTag","Recom");
                        IntentUtil.toActivity(context,bundle, BlogDetailActivity.class);
                    }
                });

        //top新动静
        if (rcom.getUpUser()!=null){
            blog_ivhot.setVisibility(View.GONE);
            top_rl.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(rcom.getUpText())){
                top_tvxdj.setVisibility(View.VISIBLE);
            }else {
                top_tvxdj.setVisibility(View.GONE);
            }
            XLGlideLoader.loadCircleImage(top_ivhead,rcom.getUpUser().getHeadImage());
            top_tvname.setText(rcom.getUpUser().getNickName());
            top_tvmsg.setText(rcom.getUpText());
            //去新动静个人主页
            top_rl.setOnClickListener(v -> {
                if (new LoginDao(context).getObject().getUserDO().getId()!=rcom.getUpUser().getUserId()){
                    Bundle bundle=new Bundle();
                    bundle.putInt("userid",rcom.getUpUser().getUserId());
                    IntentUtil.toActivity(context,bundle,OtherSingleActivity.class);
                }
            });
        }else {
            top_rl.setVisibility(View.GONE);
            if (rcom.getHotRank()>0 && rcom.getHotRank()<=3){
                blog_ivhot.setVisibility(View.VISIBLE);
                if (rcom.getHotRank()==1){
                    blog_ivhot.setImageResource(R.mipmap.icon_top1);
                }else if (rcom.getHotRank()==2){
                    blog_ivhot.setImageResource(R.mipmap.icon_top2);
                }else if (rcom.getHotRank()==3){
                    blog_ivhot.setImageResource(R.mipmap.icon_top3);
                }
            }else {
                blog_ivhot.setVisibility(View.GONE);
            }
        }

        //header发布者信息
        if (rcom.getUserVO()!=null){
            XLGlideLoader.loadCircleImage(blog_ivhead,rcom.getUserVO().getHeadImage());
            //备注
            if (!TextUtils.isEmpty(rcom.getUserVO().getRemarkName())){
                blog_tvname.setText(rcom.getUserVO().getRemarkName());
            }else {
                blog_tvname.setText(rcom.getUserVO().getNickName());
            }
            if (rcom.getUserVO().isOR() || rcom.getUserVO().isTeacher()){
                blog_ivsex.setVisibility(View.GONE);
                blog_ividentify.setVisibility(View.VISIBLE);
                UserComm.updateIdentify(blog_ividentify,rcom.getUserVO());
            }else {
                if (rcom.getUserVO().getSex()==0){
                    blog_ivsex.setVisibility(View.GONE);
                }else {
                    blog_ivsex.setVisibility(View.VISIBLE);
                }
                blog_ividentify.setVisibility(View.GONE);
                UserComm.updateIdentify(blog_ivsex,rcom.getUserVO());
            }
            Long time=TimeUtils.string2Millis(
                    rcom.getMicroblogMainDO().getGmtCreate(),
                    new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
            if (!TextUtils.isEmpty(rcom.getUserVO().getDepartAbbr())){
                blog_tvyx.setText(TimeUtil.date2USDiy(time)+"\t"+rcom.getUserVO().getDepartAbbr());
            }else {
                blog_tvyx.setText(TimeUtil.date2USDiy(time));
            }

            blog_ivhead.setOnClickListener(v -> {
                if (new LoginDao(context).getObject().getUserDO().getId()!=rcom.getUserVO().getUserId()){
                    Bundle bundle=new Bundle();
                    bundle.putInt("userid",rcom.getUserVO().getUserId());
                    IntentUtil.toActivity(context,bundle,OtherSingleActivity.class);
                }
            });
        }

        //content内容
        if (!TextUtils.isEmpty(rcom.getTopicName())){
            content_tv_topic.setText("#"+rcom.getTopicName()+"#");
            content_tv_topic.setVisibility(View.VISIBLE);
            //去对应的相关话题列表
            RxView.clicks(content_tv_topic)
                    .throttleFirst(2,TimeUnit.SECONDS)
                    .subscribe(o -> {
                        if (!TextUtils.isEmpty(mic)){
                            Bundle bundle=new Bundle();
                            bundle.putString("id",String.valueOf(rcom.getMicroblogMainDO().getTopicId()));
                            bundle.putString("name",rcom.getTopicName());
                            IntentUtil.toActivity(context,bundle, HotTopicListActivity.class);
                        }
                    });
        }else {
            content_tv_topic.setVisibility(View.GONE);
        }

        SpannableString spannableString= StringUtil.stringToSpannableString(rcom.getMicroblogMainDO().getContent(), context, 16);
        content_tv_cont.setText(spannableString);
        SuperText.txtlink(context,content_tv_cont);

        //地标
        if (rcom.getMicroblogPlaceDO()!=null){
            if (!TextUtils.isEmpty(rcom.getMicroblogPlaceDO().getPlaceName())){
                location_tx.setText(rcom.getMicroblogPlaceDO().getPlaceName());
                location_ll.setVisibility(View.VISIBLE);
            }else {
                location_ll.setVisibility(View.GONE);
            }
        }else {
            location_ll.setVisibility(View.GONE);
        }

        //投票
        if (rcom.getMicroblogVoteOptionsDtoList()!=null && rcom.getMicroblogVoteOptionsDtoList().size()>0){
            LinearLayoutManager layoutManager=new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            vote_item_rv.setLayoutManager(layoutManager);
            voteAdapter=new VoteAdapter(context,rcom.isVote());
            vote_item_rv.setAdapter(voteAdapter);
            voteAdapter.setDatas(rcom.getMicroblogVoteOptionsDtoList());

            voteAdapter.setOnVoteListener(new VoteAdapter.OnVoteListener() {
                @Override
                public void onVote(boolean isvote) {
                    if (isvote){
                        rcom.setVote(isvote);
                        operat_lka.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onList(List<MicroblogVoteOptionsDto> dtoList) {
                    rcom.getMicroblogVoteOptionsDtoList().clear();
                    rcom.getMicroblogVoteOptionsDtoList().addAll(dtoList);
                    voteAdapter.setDatas(rcom.getMicroblogVoteOptionsDtoList());
                    voteAdapter.setAnimAction(true);
                }
            });
            item_vote_ll.setVisibility(View.VISIBLE);
        }else {
            item_vote_ll.setVisibility(View.GONE);
        }
        if (rcom.isVote()){
            operat_lka.setVisibility(View.VISIBLE);
        }else {
            operat_lka.setVisibility(View.GONE);
        }

        //operat操作按钮
        if (rcom.getIsLike()==1){
            XLGlideLoader.loadImageById(operat_ivlove,R.mipmap.icon_love);
        }else {
            XLGlideLoader.loadImageById(operat_ivlove,R.mipmap.icon_love_un);
        }

        if (rcom.getLikeCount()==0){
            operat_tvlove_count.setText("0");
        }else {
            if (rcom.getLikeCount()>=1000){
                String count=new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(rcom.getLikeCount()))/1000);
                operat_tvlove_count.setText(count+"k");
            }else {
                operat_tvlove_count.setText(String.valueOf(rcom.getLikeCount()));
            }
        }

        if (rcom.getCommentCount()==0){
            operat_tvcomm_count.setText("0");
        }else {
            if (rcom.getCommentCount()>=1000){
                String count=new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(rcom.getCommentCount()))/1000);
                operat_tvcomm_count.setText(count+"k");
            }else {
                operat_tvcomm_count.setText(String.valueOf(rcom.getCommentCount()));
            }
        }

        if (rcom.getMicroblogMainDO().getBrowseNum()==0){
            operat_tvcomm_ll.setText("0");
        }else {
            if (rcom.getMicroblogMainDO().getBrowseNum()>=1000000){
                String count=new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(rcom.getMicroblogMainDO().getBrowseNum()))/1000000);
                operat_tvcomm_ll.setText(count+"M");
            }else if (rcom.getMicroblogMainDO().getBrowseNum()>=1000){
                String count=new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(rcom.getMicroblogMainDO().getBrowseNum()))/1000);
                operat_tvcomm_ll.setText(count+"K");
            }else {
                operat_tvcomm_ll.setText(String.valueOf(rcom.getMicroblogMainDO().getBrowseNum()));
            }
        }

        //commentlist 一级评论列表
        if (rcom.getMicroblogCommentVOList()!=null && rcom.getMicroblogCommentVOList().size()>0){
            linearLayoutManager=new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            comment_rv.setLayoutManager(linearLayoutManager);
            blogCommentAdpter=new BlogCommentAdpter(context);
            comment_rv.setAdapter(blogCommentAdpter);
            blogCommentAdpter.setDatas(rcom.getMicroblogCommentVOList());
            child_llcomm.setVisibility(View.VISIBLE);
        }else {
            child_llcomm.setVisibility(View.GONE);
        }

        //9宫格图片
        if (rcom.getThumbnailList()!=null && rcom.getThumbnailList().size()>0){
            initNine(rcom);

            //设置事件 才能触发itemclick
            nine_iv.setOnLongClickListener(v -> false);
            nine_iv.setOnItemClickListener(position -> {
                Bundle bundle=new Bundle();
                bundle.putInt("index",position);
                bundle.putSerializable("images", (Serializable) rcom.getPictureList());
                bundle.putSerializable("thumbs", (Serializable) rcom.getThumbnailList());
                IntentUtil.toActivity(context,bundle, BrowseImageActivity.class);
            });
            nine_ll.setVisibility(View.VISIBLE);
        }else {
            nine_ll.setVisibility(View.GONE);
        }

        //视频
        if (!TextUtils.isEmpty(rcom.getVideoName())){
            video_fl.setVisibility(View.VISIBLE);
            videoPath=rcom.getVideoName();
            XLGlideLoader.loadImageByUrl(cover_image,rcom.getVideoThumbnail());
            plvideo.setAVOptions(VideoUtils.createAVOptions());
            plvideo.setBufferingIndicator(loading_view);
            plvideo.setMediaController(media_controller);
            plvideo.setLooping(true);
            plvideo.setOnInfoListener((i, i1) -> {
                if (i == PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START) {
                    cover_image.setVisibility(View.GONE);
                    cover_stop_play.setVisibility(View.GONE);
                    media_controller.hide();
                }
            });
        }else {
            video_fl.setVisibility(View.GONE);
        }
    }

    private void initNine(final RecomList data) {
        images = new ArrayList<>();
        for (int i = 0; i < data.getThumbnailList().size(); i++) {
            imageData = new ImageData(data.getThumbnailList().get(i));
            imageData.realHeight = ConvertUtils.dp2px(180);
            imageData.realWidth = ConvertUtils.dp2px(180);
            images.add(imageData);
            nine_iv.loadGif(false)
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
