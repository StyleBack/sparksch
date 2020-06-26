package com.doschool.ahu.appui.main.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.Gravity;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.activity.PlayVideoActivity;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.appui.main.ui.bean.ShareDO;
import com.doschool.ahu.appui.main.ui.holderlogic.MicroBlogHolder;
import com.doschool.ahu.base.adapter.BaseRvAdapter;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.factory.BlogFunctionListener;
import com.doschool.ahu.utils.AlertUtils;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.ParticleAnim;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.widget.MediaController;
import com.doschool.ahu.widget.ShareDialog;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jakewharton.rxbinding2.view.RxView;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import org.xutils.common.Callback;


import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import razerdp.util.SimpleAnimationUtils;

import static com.doschool.ahu.configfile.ApiConfig.API_KACA;

/**
 * Created by X on 2018/9/5
 *
 * 微博列表
 */
public class MicroBlogAdapter extends BaseRvAdapter<MicroblogVO.DataBean,MicroBlogHolder> {



    private String mic;
    public MicroBlogAdapter(Context context, String mic ) {
        super(context);
        this.mic=mic;
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.blog_item_layout;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void bindData(final MicroBlogHolder holder, final int position, final MicroblogVO.DataBean data) {
        holder.setUpdata(context,data,mic,position);
        //视频播放
        holder.cover_image.setOnClickListener(v -> {
            Bundle bundle=new Bundle();
            bundle.putString("video",data.getVideoName());
            bundle.putString("videoThu",data.getVideoThumbnail());
            bundle.putInt("mediaCodec", AVOptions.MEDIA_CODEC_AUTO);
            IntentUtil.toActivity(context,bundle, PlayVideoActivity.class);
        });
        //全屏播放
        holder.full_screen_image.setOnClickListener(v -> {
//                if (mOnFullScreenListener != null) {
//                    mOnFullScreenListener.onFullScreen(holder.plvideo, holder.media_controller);
//                }
        });

        //点赞  取消
        holder.operat_ivlove.setOnClickListener(v -> {
            if (noneMember()){
                AlertUtils.alertToVerify(context,new LoginDao(context).getObject().getHandleStatus());
            }else {
                updateLike(context,data,data.getMicroblogMainDO().getId(),data.getIsLike(),holder);
                if (data.getIsLike()!=1){
                    ParticleAnim.getPartic(context,v,R.mipmap.icon_love);
                }
            }
        });

        //功能键
        holder.blog_ivfun.setOnClickListener(v -> {
            if (noneMember()){
                AlertUtils.alertToVerify(context,new LoginDao(context).getObject().getHandleStatus());
            }else {
                if (blogFunctionListener!=null){
                    blogFunctionListener.btnFunction(position,data.getIsCollect(),data.getMicroblogMainDO().getId(),data.getUserVO().getUserId());
                }
            }
        });

        //生成图片
        RxView.clicks(holder.operat_lka)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (noneMember()){
                        AlertUtils.alertToVerify(context,new LoginDao(context).getObject().getHandleStatus());
                    }else {
                        shareView(context,data.getMicroblogMainDO().getId());
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

    @Override
    protected MicroBlogHolder onCreateHolder(ViewGroup parent, int viewType) {
        return MicroBlogHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    private boolean noneMember(){
        int type= SPUtils.getInstance().getInt("phtype");
        LoginDao loginDao=new LoginDao(context);
        if (type==-1 && loginDao.getObject()!=null && loginDao.getObject().getUserDO().getStatus()==0){
            return true;
        }
        return false;
    }


    private OnFullScreenListener mOnFullScreenListener;
    public void setOnFullScreenListener(OnFullScreenListener listener) {
        mOnFullScreenListener = listener;
    }
    public interface OnFullScreenListener {
        void onFullScreen(PLVideoTextureView videoView, MediaController mediaController);
    }

    private void updateLike(Context context, final MicroblogVO.DataBean data, int id, final int isLike,MicroBlogHolder holder){
        ArrayMap<String,String> map= XLNetHttps.getBaseMap(context);
        map.put("blogId",String.valueOf(id));
        if (isLike==1){
            map.put("type", CodeConfig.BLOG_UNLIKE);
        }else {
            map.put("type", CodeConfig.BLOG_LIKE);
        }

        XLNetHttps.request(ApiConfig.API_ISLIKE, map, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel= XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    if (isLike==1){
                        data.setIsLike(0);
                        data.setLikeCount(data.getLikeCount()-1);
                        XLGlideLoader.loadImageById(holder.operat_ivlove,R.mipmap.icon_love_un);
                    }else {
                        data.setIsLike(1);
                        data.setLikeCount(data.getLikeCount()+1);
                        XLGlideLoader.loadImageById(holder.operat_ivlove,R.mipmap.icon_love);
                    }

                    if (data.getLikeCount()==0){
                        holder.operat_tvlove_count.setText("0");
                    }else {
                        if (data.getLikeCount()>=1000){
                            String count=new DecimalFormat("0.00").format(Double.valueOf(String.valueOf(data.getLikeCount()))/1000);
                            holder.operat_tvlove_count.setText(count+"k");
                        }else {
                            holder.operat_tvlove_count.setText(String.valueOf(data.getLikeCount()));
                        }
                    }
//                    MicroBlogAdapter.this.notifyDataSetChanged();
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

    private BlogFunctionListener blogFunctionListener;

    public void setBlogFunctionListener(BlogFunctionListener blogFunctionListener){
        this.blogFunctionListener=blogFunctionListener;
    }


}
