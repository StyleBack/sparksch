package com.doschool.ahu.appui.home.ui.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.discover.ui.activity.HotTopicListActivity;
import com.doschool.ahu.appui.home.ui.adapter.BlogDtAdapter;
import com.doschool.ahu.appui.home.ui.bean.BlogComt;
import com.doschool.ahu.appui.home.ui.bean.BlogDt;
import com.doschool.ahu.appui.home.widget.SlideFunPop;
import com.doschool.ahu.appui.infors.chat.ui.model.CustomMessage;
import com.doschool.ahu.appui.infors.chat.ui.model.CustomModel;
import com.doschool.ahu.appui.infors.chat.ui.model.Message;
import com.doschool.ahu.appui.infors.chat.ui.presenter.ChatPresenter;
import com.doschool.ahu.appui.infors.chat.ui.viewfeatures.ChatView;
import com.doschool.ahu.appui.main.event.UserComm;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.main.ui.activity.BrowseImageActivity;
import com.doschool.ahu.appui.main.ui.activity.PlayVideoActivity;
import com.doschool.ahu.appui.main.ui.activity.ReportActivity;
import com.doschool.ahu.appui.main.ui.adapter.VoteAdapter;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVoteOptionsDto;
import com.doschool.ahu.appui.main.ui.bean.ShareDO;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.appui.writeblog.widget.CommentBord;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.base.model.DoUrlModel;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.AlertUtils;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.ParticleAnim;
import com.doschool.ahu.utils.StringUtil;
import com.doschool.ahu.utils.TimeUtil;
import com.doschool.ahu.utils.Utils;
import com.doschool.ahu.utils.VideoUtils;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.widget.MediaController;
import com.doschool.ahu.widget.ShareDialog;
import com.doschool.ahu.widget.component.SuperText;
import com.doschool.ahu.widget.nine.GridLayoutHelper;
import com.doschool.ahu.widget.nine.ImageData;
import com.doschool.ahu.widget.nine.NineImageView;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.message.TIMMessageDraft;
import com.tencent.imsdk.ext.message.TIMMessageLocator;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import razerdp.util.SimpleAnimationUtils;

import static android.graphics.Color.TRANSPARENT;
import static com.doschool.ahu.configfile.ApiConfig.API_ADDFOLLOW;
import static com.doschool.ahu.configfile.ApiConfig.API_DELETE_COMM;
import static com.doschool.ahu.configfile.ApiConfig.API_KACA;
import static com.doschool.ahu.configfile.CodeConfig.FOLLOWSTATE_FOLLOW;
import static com.doschool.ahu.configfile.CodeConfig.FOLLOWSTATE_FOLLOWED;
import static com.doschool.ahu.configfile.CodeConfig.FOLLOWSTATE_NULL;
import static com.doschool.ahu.configfile.CodeConfig.IM_BBT_INFO;
import static com.doschool.ahu.configfile.CodeConfig.IM_SEND_MSG;
import static com.doschool.ahu.configfile.CodeConfig.REPORT_BLOG;
import static com.doschool.ahu.configfile.DoUrlConfig.ACTION_GUNAZHU;

/**
 * Created by X on 2018/9/13
 *
 * 微博详情
 */
public class BlogDetailActivity extends BaseActivity implements ChatView {


    public static final int VOTE_CLICK=-2023;

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;
    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;
    @ViewInject(R.id.tool_right_tv)
    private TextView tool_right_tv;
    @ViewInject(R.id.toolbar_rl)
    private RelativeLayout toolbar_rl;

    @ViewInject(R.id.com_bod)
    private CommentBord com_bod;

    private ImageView blog_dt_ivh,blog_dt_ivfy,blog_dt_ivsex,operat_ivlove,operat_ivcomm,blog_dt_ivhot,blog_dt_fun;
    private TextView blog_dt_tvname,blog_dt_tvyx,blog_dt_topic,blog_dt_tvcon,operat_tvlove_count,operat_tvcomm_count;
    private RelativeLayout nine_ll,blog_dt_rl;
    private NineImageView nine_iv;
    private FrameLayout video_fl;
    private PLVideoTextureView plvideo;
    private MediaController media_controller;
    private ImageButton controller_stop_play,full_screen_image,cover_stop_play;
    private TextView controller_current_time;
    private SeekBar controller_progress_bar;
    private TextView controller_end_time;
    private ImageView cover_image;
    private LinearLayout loading_view;
    private LinearLayout operat_llcomm;
    private TextView operat_tvcomm_ll;//浏览数
    //地标
    public LinearLayout location_ll;
    public TextView location_tx;

    //投票
    public LinearLayout item_vote_ll,operat_lka;
    public RecyclerView vote_item_rv;
    public TextView operat_kac;//投票的分享
    public VoteAdapter voteAdapter;
    private LinearLayoutManager layoutManager;


    @ViewInject(R.id.blog_rv)
    private XRecyclerView blog_rv;
    private LinearLayoutManager linearLayoutManager;

    private ArrayMap<String,String> map=new ArrayMap<>();
    private int blogId;

    private ArrayMap<String,String> comtMap=new ArrayMap<>();
    private int lastId=0;
    private boolean isRefrsh=false;
    private boolean isLoad=false;

    private BlogDtAdapter blogDtAdapter;
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

    private BlogDt.BlogDtBean funBlog=new BlogDt.BlogDtBean();
    private LoginDao loginDao;

    private ArrayMap<String,String> atmMap=new ArrayMap<>();

    private int state=-1;
    private ArrayMap<String,String> guanMap=new ArrayMap<>();
    private ArrayMap<String,String> addMap=new ArrayMap<>();
    private ChatPresenter presenter;

    private RelativeLayout empty_comm;

    private ArrayMap<String,String> browMap=new ArrayMap<>();
    private String blogTag;
    private ArrayMap<String,String> tuiMap=new ArrayMap<>();


    @Override
    protected int getContentLayoutID() {
        return R.layout.act_blogdetail_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        toolbar_rl.setBackgroundColor(getResources().getColor(R.color.grey_link));
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("动态详情");
        tool_right_tv.setTextColor(getResources().getColor(R.color.now_txt_color));


        loginDao=new LoginDao(this);
        map= XLNetHttps.getBaseMap(this);
        comtMap=XLNetHttps.getBaseMap(this);
        atmMap=XLNetHttps.getBaseMap(this);
        guanMap=XLNetHttps.getBaseMap(this);
        addMap=XLNetHttps.getBaseMap(this);
        browMap=XLNetHttps.getBaseMap(this);
        tuiMap=XLNetHttps.getBaseMap(this);
        if (getIntent().getExtras()!=null){
            if (getIntent().getExtras().containsKey("blogId")){
                blogId=getIntent().getExtras().getInt("blogId");
            }
            if (getIntent().getExtras().containsKey("blogTag")){
                blogTag=getIntent().getExtras().getString("blogTag");
            }
        }

        initRV();
        blogDTView();
        initData();
        getComtList();
        getBrowseRecord();

        //推荐的浏览记录
        if (blogTag.equals("Recom")){
            getTuiJian();
        }
    }

    private void initRV(){
        linearLayoutManager=new LinearLayoutManager(this);
        XRvUtils.initRv(blog_rv,linearLayoutManager,LinearLayoutManager.VERTICAL,true,true,true);
        blogDtAdapter=new BlogDtAdapter(this);
        blog_rv.setAdapter(blogDtAdapter);
        blog_rv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                isRefrsh=true;
                lastId=0;
                getComtList();
                initData();
            }

            @Override
            public void onLoadMore() {
                isLoad=true;
                getComtList();
            }
        });

        //评论
        blogDtAdapter.setOnCommentListener((blogId, tarUid, tarCommId, name) -> com_bod.setInit(blogId,tarUid,tarCommId,name));
        blogDtAdapter.setOnCommentLong((type, commId,targetId,position,tag) -> {
            if (KeyboardUtils.isSoftInputVisible(BlogDetailActivity.this)){
                KeyboardUtils.hideSoftInput(BlogDetailActivity.this);
            }
            if (noneMember()){
                AlertUtils.alertToVerify(BlogDetailActivity.this,loginDao.getObject().getHandleStatus());
            }else {
                comView(type,commId,targetId,position,tag);
            }
        });
    }

    private void comView(int type,int commId,int targetId,int position,int tag){

        SlideFunPop comPop=new SlideFunPop(BlogDetailActivity.this);
        comPop.onData(targetId)
                .showPopupWindow();
        comPop.setOnFunClick(() -> {
            if (loginDao!=null){
                if (targetId ==loginDao.getObject().getUserDO().getId()){
                    deleteComm(commId,position,tag);
                }else {
                    Bundle bundle=new Bundle();
                    bundle.putInt("type",type);
                    bundle.putInt("Id", commId);
                    IntentUtil.toActivity(BlogDetailActivity.this,bundle, ReportActivity.class);
                }
            }
        });
    }

    private void initData(){
        map.put("blogId",String.valueOf(blogId));
        XLNetHttps.request(ApiConfig.API_BLOG_DETAIL, map, BlogDt.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BlogDt blogDt=XLGson.fromJosn(result,BlogDt.class);
                if (blogDt.getCode()==0){
                    initView(blogDt.getData());
                }
            }

            @Override
            public void XLError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void XLCancle(Callback.CancelledException cex) {

            }

            @Override
            public void XLFinish() {

            }
        });
    }

    //推荐记录
    private void getTuiJian(){
        tuiMap.put("blogId",String.valueOf(blogId));
        XLNetHttps.request(ApiConfig.API_RECOM_MU, tuiMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {

            }

            @Override
            public void XLError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void XLCancle(Callback.CancelledException cex) {

            }

            @Override
            public void XLFinish() {

            }
        });
    }

    //添加浏览记录
    private void getBrowseRecord(){
        browMap.put("blogId",String.valueOf(blogId));
        XLNetHttps.request(ApiConfig.API_BROWSE_RECODE, browMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {

            }

            @Override
            public void XLError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void XLCancle(Callback.CancelledException cex) {

            }

            @Override
            public void XLFinish() {

            }
        });
    }

    @SuppressLint("CheckResult")
    private void initView(final BlogDt.BlogDtBean blog){
        funBlog=blog;
        //关注
        if (loginDao.getObject()!=null){
            if (blog.getMicroblogMainDO().getUserId()==loginDao.getObject().getUserDO().getId()){
                tool_right_tv.setVisibility(View.GONE);
            }else {
                presenter = new ChatPresenter(this, String.valueOf(blog.getMicroblogMainDO().getUserId()), TIMConversationType.C2C);
                getHas(blog.getMicroblogMainDO().getUserId());
            }
        }

        //用户信息
        blog_dt_ivh.setOnClickListener(v -> {
                if (blog.getUserVO().getUserId()!=loginDao.getObject().getUserDO().getId()){
                    Bundle bundle=new Bundle();
                    bundle.putInt("userid",blog.getUserVO().getUserId());
                    IntentUtil.toActivity(BlogDetailActivity.this,bundle,OtherSingleActivity.class);
                }
        });
        XLGlideLoader.loadCircleImage(blog_dt_ivh,blog.getUserVO().getHeadImage());
        //备注
        if (!TextUtils.isEmpty(blog.getUserVO().getRemarkName())){
            blog_dt_tvname.setText(blog.getUserVO().getRemarkName());
        }else {
            blog_dt_tvname.setText(blog.getUserVO().getNickName());
        }
        if (blog.getUserVO().isOR() || blog.getUserVO().isTeacher()){
            blog_dt_ivsex.setVisibility(View.GONE);
            blog_dt_ivfy.setVisibility(View.VISIBLE);
            UserComm.updateIdentify(blog_dt_ivfy,blog.getUserVO());
        }else {
            if (blog.getUserVO().getSex()==0){
                blog_dt_ivsex.setVisibility(View.GONE);
            }else {
                blog_dt_ivsex.setVisibility(View.VISIBLE);
            }
            blog_dt_ivfy.setVisibility(View.GONE);
            UserComm.updateIdentify(blog_dt_ivsex,blog.getUserVO());
        }
        Long time= TimeUtils.string2Millis(
                blog.getMicroblogMainDO().getGmtCreate(),
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
        if (!TextUtils.isEmpty(blog.getUserVO().getDepartAbbr())){
            blog_dt_tvyx.setText(TimeUtil.date2USDiy(time)+"\t"+blog.getUserVO().getDepartAbbr());
        }else {
            blog_dt_tvyx.setText(TimeUtil.date2USDiy(time));
        }
        if (blog.getHotRank()>0){
            blog_dt_rl.setVisibility(View.VISIBLE);
            if (blog.getHotRank()==1){
                blog_dt_ivhot.setImageResource(R.mipmap.icon_top1);
            }else if (blog.getHotRank()==2){
                blog_dt_ivhot.setImageResource(R.mipmap.icon_top2);
            }else {
                blog_dt_ivhot.setImageResource(R.mipmap.icon_top3);
            }
        }else {
            blog_dt_rl.setVisibility(View.GONE);
        }

        //内容
        if (!TextUtils.isEmpty(blog.getTopicName())){
            blog_dt_topic.setText("#"+blog.getTopicName()+"#");
            blog_dt_topic.setVisibility(View.VISIBLE);
            //去对应的相关话题列表
            RxView.clicks(blog_dt_topic)
                    .throttleFirst(2,TimeUnit.SECONDS)
                    .subscribe(o -> {
                        Bundle bundle=new Bundle();
                        bundle.putString("id",String.valueOf(blog.getMicroblogMainDO().getTopicId()));
                        bundle.putString("name",blog.getTopicName());
                        IntentUtil.toActivity(this,bundle, HotTopicListActivity.class);
                    });
        }else {
            blog_dt_topic.setVisibility(View.GONE);
        }
        SpannableString spannableString= StringUtil.stringToSpannableString(blog.getMicroblogMainDO().getContent(), this, 16);
        blog_dt_tvcon.setText(spannableString);
        SuperText.txtlink(this,blog_dt_tvcon);
        //长按复制内容
        blog_dt_tvcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                alertCopy(blog_dt_tvcon.getText().toString().trim());
                return false;
            }
        });

        //地标
        if (blog.getMicroblogPlaceDO()!=null){
            if (!TextUtils.isEmpty(blog.getMicroblogPlaceDO().getPlaceName())){
                location_tx.setText(blog.getMicroblogPlaceDO().getPlaceName());
                location_ll.setVisibility(View.VISIBLE);
            }else {
                location_ll.setVisibility(View.GONE);
            }
        }else {
            location_ll.setVisibility(View.GONE);
        }

        //投票
        if (blog.getMicroblogVoteOptionsDtoList()!=null && blog.getMicroblogVoteOptionsDtoList().size()>0){
            layoutManager=new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            vote_item_rv.setLayoutManager(layoutManager);
            voteAdapter=new VoteAdapter(this,blog.isVote());
            vote_item_rv.setAdapter(voteAdapter);
            voteAdapter.setDatas(blog.getMicroblogVoteOptionsDtoList());

            voteAdapter.setOnVoteListener(new VoteAdapter.OnVoteListener() {
                @Override
                public void onVote(boolean isvote) {
                    if (isvote){
                        operat_lka.setVisibility(View.VISIBLE);
                        EventUtils.onPost(new XMessageEvent(VOTE_CLICK));
                    }
                }

                @Override
                public void onList(List<MicroblogVoteOptionsDto> dtoList) {
                    voteAdapter.setDatas(dtoList);
                }
            });
            item_vote_ll.setVisibility(View.VISIBLE);
        }else {
            item_vote_ll.setVisibility(View.GONE);
        }
        if (blog.isVote()){
            operat_lka.setVisibility(View.VISIBLE);
        }else {
            operat_lka.setVisibility(View.GONE);
        }
        //生成图片
        RxView.clicks(operat_lka)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> shareView(this,blog.getMicroblogMainDO().getId()));


        //9宫格图片
        if (blog.getThumbnailList()!=null && blog.getThumbnailList().size()>0){
            initNine(blog);

            //设置事件 才能触发itemclick
            nine_iv.setOnLongClickListener(v -> false);
            nine_iv.setOnItemClickListener(position -> {
                Bundle bundle=new Bundle();
                bundle.putInt("index",position);
                bundle.putSerializable("images", (Serializable) blog.getPictureList());
                bundle.putSerializable("thumbs", (Serializable) blog.getThumbnailList());
                IntentUtil.toActivity(BlogDetailActivity.this,bundle, BrowseImageActivity.class);
            });
            nine_ll.setVisibility(View.VISIBLE);
        }else {
            nine_ll.setVisibility(View.GONE);
        }

        //视频
        if (!TextUtils.isEmpty(blog.getVideoName())){
            video_fl.setVisibility(View.VISIBLE);
            videoPath=blog.getVideoName();
            XLGlideLoader.loadImageByUrl(cover_image,blog.getVideoThumbnail());
            plvideo.setAVOptions(VideoUtils.createAVOptions());
            plvideo.setBufferingIndicator(loading_view);
            plvideo.setMediaController(media_controller);
            plvideo.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
            plvideo.setLooping(true);
            plvideo.setOnInfoListener((i, i1) -> {
                if (i == PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START) {
                    cover_image.setVisibility(View.GONE);
                    cover_stop_play.setVisibility(View.GONE);
                    media_controller.hide();
                }
            });
            cover_image.setOnClickListener(v -> {
                Bundle bundle=new Bundle();
                bundle.putString("video",blog.getVideoName());
                bundle.putString("videoThu",blog.getVideoThumbnail());
                bundle.putInt("mediaCodec", AVOptions.MEDIA_CODEC_AUTO);
                IntentUtil.toActivity(BlogDetailActivity.this,bundle, PlayVideoActivity.class);
            });
        }else {
            video_fl.setVisibility(View.GONE);
        }

        //功能键
        RxView.clicks(blog_dt_fun)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (noneMember()){
                        AlertUtils.alertToVerify(this,loginDao.getObject().getHandleStatus());
                    }else {
                        fun();
                    }
                });

        //操作按钮
        if (blog.getIsLike()==1){
            XLGlideLoader.loadImageById(operat_ivlove,R.mipmap.icon_love);
        }else {
            XLGlideLoader.loadImageById(operat_ivlove,R.mipmap.icon_love_un);
        }
        //点赞
        operat_ivlove.setOnClickListener(v -> {
            if (noneMember()){
                AlertUtils.alertToVerify(this,loginDao.getObject().getHandleStatus());
            }else {
                upDateLike(blog);
                if (blog.getIsLike()!=1){
                    ParticleAnim.getPartic(BlogDetailActivity.this,v,R.mipmap.icon_love);
                }
            }
        });

        if (blog.getLikeCount()==0){
            operat_tvlove_count.setText("0");
        }else {
            if (blog.getLikeCount()>=1000){
                String count=new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(blog.getLikeCount()))/1000);
                operat_tvlove_count.setText(count+"k");
            }else {
                operat_tvlove_count.setText(String.valueOf(blog.getLikeCount()));
            }
        }

        if (blog.getCommentCount()==0){
            operat_tvcomm_count.setText("0");
        }else {
            if (blog.getCommentCount()>=1000){
                String count=new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(blog.getCommentCount()))/1000);
                operat_tvcomm_count.setText(count+"k");
            }else {
                operat_tvcomm_count.setText(String.valueOf(blog.getCommentCount()));
            }
        }

        if (blog.getMicroblogMainDO().getBrowseNum()==0){
            operat_tvcomm_ll.setText("0");
        }else {
            if (blog.getMicroblogMainDO().getBrowseNum()>=1000000){
                String count=new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(blog.getMicroblogMainDO().getBrowseNum()))/1000000);
                operat_tvcomm_ll.setText(count+"M");
            }else if (blog.getMicroblogMainDO().getBrowseNum()>=1000){
                String count=new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(blog.getMicroblogMainDO().getBrowseNum()))/1000);
                operat_tvcomm_ll.setText(count+"k");
            }else {
                operat_tvcomm_ll.setText(String.valueOf(blog.getMicroblogMainDO().getBrowseNum()));
            }
        }


        com_bod.setInit(blog.getMicroblogMainDO().getId(),
                blog.getUserVO().getUserId(),0,blog.getUserVO().getNickName());
        operat_llcomm.setOnClickListener(v -> com_bod.setInit(blog.getMicroblogMainDO().getId(),
                blog.getUserVO().getUserId(),0,blog.getUserVO().getNickName()));

        //评论
        com_bod.setOnSendListener((blogId, tarUid, tarCommId, name, content, editText) -> {
            if (noneMember()){
                AlertUtils.alertToVerify(BlogDetailActivity.this,loginDao.getObject().getHandleStatus());
            }else {
                commSubmit(blogId,tarUid,tarCommId,name,content,editText);
            }
        });
    }

    private ShareDialog shareDialog;
    private void shareView(Context context,int blogId){
        ArrayMap<String,String> map=XLNetHttps.getBaseMap(context);
        map.put("blogId",String.valueOf(blogId));
        XLNetHttps.request(API_KACA, map, ShareDO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                ShareDO shareDO=XLGson.fromJosn(result,ShareDO.class);
                if (shareDO.getCode()==0){
                    int alpha = (int) (255 * (float) 70 / 100);
                    int color = Color.argb(alpha, Color.red(context.getResources().getColor(R.color.white)),
                            Color.green(context.getResources().getColor(R.color.white)),
                            Color.blue(context.getResources().getColor(R.color.white)));
                    shareDialog=new ShareDialog(context);
                    shareDialog.setUpdata(shareDO)
                            .setPopupGravity(Gravity.CENTER)
                            .setShowAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(true))
                            .setDismissAnimation(SimpleAnimationUtils.getDefaultScaleAnimation(false))
                            .setBackgroundColor(color)
                            .showPopupWindow();
                }
            }

            @Override
            public void XLError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void XLCancle(Callback.CancelledException cex) {

            }

            @Override
            public void XLFinish() {

            }
        });
    }

    //长按内容复制
    private TextView copy_tv_cont;
    private void alertCopy(String tv_copy){
        View view=LayoutInflater.from(this).inflate(R.layout.alert_copy_layout,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        copy_tv_cont=view.findViewById(R.id.copy_tv_cont);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        //背景透明 避免圆角边框的同时会出现白色直角部分
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        dialog.show();
        copy_tv_cont.setOnClickListener(view1 -> {
            //获取剪贴板管理器
            ClipboardManager cm= (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData clipData=ClipData.newPlainText("Lable",tv_copy);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(clipData);
            dialog.dismiss();
            XLToast.showToast("复制成功！");
        });
    }

    //获取目标者信息
    private void getHas(int targetIds){
        guanMap.put("objId",String.valueOf(targetIds));
        XLNetHttps.request(ApiConfig.API_MINE, guanMap, LoginVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginVO= XLGson.fromJosn(result,LoginVO.class);
                if (loginVO.getCode()==0){
                    state=loginVO.getData().getFollowState();
                    if (state==FOLLOWSTATE_NULL || state==FOLLOWSTATE_FOLLOWED ){
                        tool_right_tv.setText("关注");
                    }else if (state==FOLLOWSTATE_FOLLOW){
                        tool_right_tv.setText("已关注");
                    }else {
                        tool_right_tv.setText("相互关注");
                    }
                    tool_right_tv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void XLError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void XLCancle(Callback.CancelledException cex) {

            }

            @Override
            public void XLFinish() {

            }
        });
    }

    //未认证的用户
    private boolean noneMember(){
        int type= SPUtils.getInstance().getInt("phtype");
        if (type==-1 && loginDao.getObject()!=null &&  loginDao.getObject().getUserDO().getStatus()==0){
            return true;
        }
        return false;
    }

    //评论
    private void commSubmit(int blogId, int tarUid, int tarCommId, final String name, String content, final EditText editText){

        atmMap.put("comment",content);
        atmMap.put("hostBlogId",String.valueOf(blogId));
        atmMap.put("targetUserId",String.valueOf(tarUid));
        atmMap.put("targetCommentId",String.valueOf(tarCommId));
        XLNetHttps.request(ApiConfig.API_ADDCOMMENT, atmMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel model=XLGson.fromJosn(result,BaseModel.class);
                if (model.getCode()==0){
                    editText.setText("");
                    editText.setHint("回复"+name);
                    blog_rv.refresh();
                }
            }

            @Override
            public void XLError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void XLCancle(Callback.CancelledException cex) {

            }

            @Override
            public void XLFinish() {

            }
        });
    }

    //点赞
    private void upDateLike(final BlogDt.BlogDtBean blog){
        ArrayMap<String,String> map= XLNetHttps.getBaseMap(this);
        map.put("blogId",String.valueOf(blog.getMicroblogMainDO().getId()));
        if (blog.getIsLike()==1){
            map.put("type", CodeConfig.BLOG_UNLIKE);
        }else {
            map.put("type", CodeConfig.BLOG_LIKE);
        }
        XLNetHttps.request(ApiConfig.API_ISLIKE, map, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel= XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    if (blog.getIsLike()==1){
                        blog.setIsLike(0);
                        blog.setLikeCount(blog.getLikeCount()-1);
                    }else {
                        blog.setIsLike(1);
                        blog.setLikeCount(blog.getLikeCount()+1);
                    }
                    initView(blog);
                }
            }

            @Override
            public void XLError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void XLCancle(Callback.CancelledException cex) {

            }

            @Override
            public void XLFinish() {

            }
        });
    }

    private void initNine(final BlogDt.BlogDtBean  data) {
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

    private void blogDTView(){
        View view= LayoutInflater.from(this).inflate(R.layout.blog_detail_head,null);
        blog_rv.addHeaderView(view);
        blog_dt_ivh=view.findViewById(R.id.blog_dt_ivh);
        blog_dt_ivfy=view.findViewById(R.id.blog_dt_ivfy);
        blog_dt_ivsex=view.findViewById(R.id.blog_dt_ivsex);
        operat_ivlove=view.findViewById(R.id.operat_ivlove);
        operat_ivcomm=view.findViewById(R.id.operat_ivcomm);
        operat_llcomm=view.findViewById(R.id.operat_llcomm);
        blog_dt_ivhot=view.findViewById(R.id.blog_dt_ivhot);
        blog_dt_fun=view.findViewById(R.id.blog_dt_fun);
        blog_dt_tvname=view.findViewById(R.id.blog_dt_tvname);
        blog_dt_tvyx=view.findViewById(R.id.blog_dt_tvyx);
        blog_dt_topic=view.findViewById(R.id.blog_dt_topic);
        blog_dt_tvcon=view.findViewById(R.id.blog_dt_tvcon);
        operat_tvlove_count=view.findViewById(R.id.operat_tvlove_count);
        operat_tvcomm_count=view.findViewById(R.id.operat_tvcomm_count);
        operat_tvcomm_ll=view.findViewById(R.id.operat_tvcomm_ll);
        nine_ll=view.findViewById(R.id.nine_ll);
        blog_dt_rl=view.findViewById(R.id.blog_dt_rl);
        nine_iv=view.findViewById(R.id.nine_iv);
        video_fl=view.findViewById(R.id.video_fl);

        item_vote_ll=view.findViewById(R.id.item_vote_ll);
        vote_item_rv=view.findViewById(R.id.vote_item_rv);
        operat_kac=view.findViewById(R.id.operat_kac);
        operat_lka=view.findViewById(R.id.operat_lka);

        plvideo=view.findViewById(R.id.plvideo);
        media_controller=view.findViewById(R.id.media_controller);
        controller_stop_play=view.findViewById(R.id.controller_stop_play);
        full_screen_image=view.findViewById(R.id.full_screen_image);
        cover_stop_play=view.findViewById(R.id.cover_stop_play);
        controller_current_time=view.findViewById(R.id.controller_current_time);
        controller_progress_bar=view.findViewById(R.id.controller_progress_bar);
        controller_end_time=view.findViewById(R.id.controller_end_time);
        cover_image=view.findViewById(R.id.cover_image);
        loading_view=view.findViewById(R.id.loading_view);

        location_ll=view.findViewById(R.id.location_ll);
        location_tx=view.findViewById(R.id.location_tx);

        empty_comm= view.findViewById(R.id.empty_comm);

        margin = ConvertUtils.dp2px( 3);
        maxImgHeight = maxImgWidth = (ScreenUtils.getScreenWidth() - ConvertUtils.dp2px(12) * 2);
        cellHeight = cellWidth = (maxImgWidth - margin * 3) / 3;
        minImgHeight = minImgWidth = cellWidth;
    }

    private void getComtList(){
        comtMap.put("blogId",String.valueOf(blogId));
        comtMap.put("lastId",String.valueOf(lastId));
        comtMap.put("size","10");
        XLNetHttps.request(ApiConfig.API_BLOG_COMT_LIST, comtMap, BlogComt.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BlogComt blogComt= XLGson.fromJosn(result,BlogComt.class);
                if (blogComt.getCode()==0){
                    if (blogComt.getData()!=null && blogComt.getData().size()==0){
                        if (lastId==0){
                            empty_comm.setVisibility(View.VISIBLE);
                        }else {
                            empty_comm.setVisibility(View.GONE);
                        }
                    }else {
                        empty_comm.setVisibility(View.GONE);
                    }

                    if (lastId==0){
                        blogDtAdapter.getList().clear();
                        blogDtAdapter.notifyDataSetChanged();
                    }
                    if (blogComt.getData()!=null && blogComt.getData().size()>0){
                        blogDtAdapter.addDatas(blogComt.getData());
                    }
                    lastId=blogComt.getData().get(blogComt.getData().size()-1).getMicroblogCommentDO().getId();
                }
            }

            @Override
            public void XLError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void XLCancle(Callback.CancelledException cex) {

            }

            @Override
            public void XLFinish() {
                if (isRefrsh){
                    blog_rv.refreshComplete();
                    isRefrsh=false;
                }

                if (isLoad){
                    blog_rv.loadMoreComplete();
                    isLoad=false;
                }
            }
        });
    }

    @Event({R.id.tool_back_iv,R.id.tool_right_tv})
    private void onClicks(View view){
        switch (view.getId()){
            case R.id.tool_back_iv:
                finish();
                break;
            case R.id.tool_right_tv://关注
                if (noneMember()){
                    AlertUtils.alertToVerify(this,loginDao.getObject().getHandleStatus());
                }else {
                    if (state!=-1){
                        if (state==FOLLOWSTATE_NULL || state==FOLLOWSTATE_FOLLOWED ){//进行关注
                            addFollow(1);
                        }else {//取消
                            addFollow(0);
                        }
                    }
                }
                break;
        }
    }
    private void addFollow(int type){
        addMap.put("objId",String.valueOf(funBlog.getMicroblogMainDO().getUserId()));
        addMap.put("type",String.valueOf(type));
        XLNetHttps.request(API_ADDFOLLOW, addMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel=XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    getHas(funBlog.getMicroblogMainDO().getUserId());
                    if (type==1){
                        XLToast.showToast("关注成功！");
                        sendMSG();
                    }else {
                        XLToast.showToast("取消关注成功！");
                    }
                }
            }

            @Override
            public void XLError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void XLCancle(Callback.CancelledException cex) {

            }

            @Override
            public void XLFinish() {

            }
        });

    }
    private void sendMSG(){
        CustomModel customModel=new CustomModel();
        DoUrlModel doUrlModel=new DoUrlModel();
        doUrlModel.setAction(ACTION_GUNAZHU);
        customModel.setDoUrl(doUrlModel);
        customModel.setExtType(2);
        customModel.setImageUrl("");
        customModel.setTitle(IM_SEND_MSG);
        customModel.setVersion(String.valueOf(AppUtils.getAppVersionName()));

        Message message = new CustomMessage(CustomMessage.Type.BANGBANGTANG,XLGson.toJson(customModel));
        message.setDesc(IM_BBT_INFO);
        presenter.sendMessage(message.getMessage());
    }

    private void fun(){
        SlideFunPop slideFunPop=new SlideFunPop(BlogDetailActivity.this);
        slideFunPop.onData(funBlog.getUserVO().getUserId())
                .showPopupWindow();
        slideFunPop.setOnFunClick(() -> {
            if (loginDao!=null){
                if (funBlog.getUserVO().getUserId() ==loginDao.getObject().getUserDO().getId()){
                    deleteBlog();
                }else {
                    Bundle bundle=new Bundle();
                    bundle.putInt("type",REPORT_BLOG);
                    bundle.putInt("Id", funBlog.getMicroblogMainDO().getId());
                    IntentUtil.toActivity(BlogDetailActivity.this,bundle, ReportActivity.class);
                }
            }
        });
    }

    //删除
    private void deleteBlog(){
        ArrayMap<String,String> delMap=XLNetHttps.getBaseMap(this);
        delMap.put("blogId",String.valueOf(funBlog.getMicroblogMainDO().getId()));
        XLNetHttps.request(ApiConfig.API_DELETEBLOG, delMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel=XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    BlogDetailActivity.this.finish();
                }
            }

            @Override
            public void XLError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void XLCancle(Callback.CancelledException cex) {

            }

            @Override
            public void XLFinish() {

            }
        });
    }

    //删除评论
    private void deleteComm(int id,int postion,int tag){
        ArrayMap<String,String> deleteMap=XLNetHttps.getBaseMap(this);
        deleteMap.put("commentId",String.valueOf(id));
        XLNetHttps.request(API_DELETE_COMM, deleteMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel=XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
//                    initData();
//                    if (tag==-100){
//                       blogDtAdapter.removeData(postion);
//                    }else {
//
//                    }
                    blog_rv.refresh();
                    XLToast.showToast("评论删除成功！");
                }
            }

            @Override
            public void XLError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void XLCancle(Callback.CancelledException cex) {

            }

            @Override
            public void XLFinish() {

            }
        });
    }

    @Override
    protected void setStatusBar() {
        super.setStatusBar();
        StatusBarUtil.setColor(this,getResources().getColor(R.color.grey_link),0);
    }

    @Override
    protected void DetoryViewAndThing() {
        XRvUtils.destroyRv(blog_rv);
    }

    @Override
    public void showMessage(TIMMessage message) {

    }

    @Override
    public void showMessage(List<TIMMessage> messages) {

    }

    @Override
    public void showRevokeMessage(TIMMessageLocator timMessageLocator) {

    }

    @Override
    public void clearAllMessage() {

    }

    @Override
    public void onSendMessageSuccess(TIMMessage message) {

    }

    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {

    }

    @Override
    public void sendImage() {

    }

    @Override
    public void sendPhoto() {

    }

    @Override
    public void sendText() {

    }

    @Override
    public void sendBbtText() {

    }

    @Override
    public void sendFile() {

    }

    @Override
    public void startSendVoice() {

    }

    @Override
    public void endSendVoice() {

    }

    @Override
    public void sendVideo(String fileName) {

    }

    @Override
    public void cancelSendVoice() {

    }

    @Override
    public void cancleMoveVoice() {

    }

    @Override
    public void sending() {

    }

    @Override
    public void showDraft(TIMMessageDraft draft) {

    }

    @Override
    public void videoAction() {

    }

    @Override
    public void showToast(String msg) {

    }
}
