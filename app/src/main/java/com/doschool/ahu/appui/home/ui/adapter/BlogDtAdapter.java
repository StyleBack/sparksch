package com.doschool.ahu.appui.home.ui.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.event.OnCommentListener;
import com.doschool.ahu.appui.home.event.OnCommentLong;
import com.doschool.ahu.appui.home.ui.holderlogic.BlogDtHolder;
import com.doschool.ahu.appui.main.ui.bean.MicroblogCommentVO;
import com.doschool.ahu.base.adapter.BaseRvAdapter;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.AlertUtils;
import com.doschool.ahu.utils.ParticleAnim;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;

import org.xutils.common.Callback;

import static com.doschool.ahu.configfile.CodeConfig.REPORT_COMMENT;

/**
 * Created by X on 2018/9/14
 */
public class BlogDtAdapter extends BaseRvAdapter<MicroblogCommentVO,BlogDtHolder> {

    private OnCommentListener onCommentListener;
    public void setOnCommentListener(OnCommentListener onCommentListener) {
        this.onCommentListener = onCommentListener;
    }

    private OnCommentLong onCommentLong;

    public void setOnCommentLong(OnCommentLong onCommentLong) {
        this.onCommentLong = onCommentLong;
    }

    public BlogDtAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.blog_dt_child_item;
    }

    @Override
    protected BlogDtHolder onCreateHolder(ViewGroup parent, int viewType) {
        return BlogDtHolder.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(final BlogDtHolder holder, int position, final MicroblogCommentVO data) {
        if (position==0){
            holder.dt_child_view.setVisibility(View.GONE);
        }else {
            holder.dt_child_view.setVisibility(View.VISIBLE);
        }
            holder.setHolder(context,position,data);

        //点赞
        holder.dt_child_lll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noneMember()){
                    AlertUtils.alertToVerify(context,new LoginDao(context).getObject().getHandleStatus());
                }else {
                    updateLike(context,data);
                    if (data.getIsLike()!=1){
                        ParticleAnim.getPartic(context,holder.dt_child_love,R.mipmap.icon_love);
                    }
                }
            }
        });

        //评论 一级
        holder.child_dtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommentListener!=null){
                    onCommentListener.onListener(data.getMicroblogCommentDO().getHostBlogId(),
                            data.getUserVO().getUserId(),data.getMicroblogCommentDO().getId(),
                            data.getUserVO().getNickName());
                }
            }
        });
        holder.child_dtrl.setOnLongClickListener(v -> {
            if (onCommentLong!=null){
                onCommentLong.onLongListener(REPORT_COMMENT,data.getMicroblogCommentDO().getId(),
                        data.getUserVO().getUserId(),position,-100);
            }
            return false;
        });

        //评论  二级
        if (holder.dtChildAdapter!=null){
            holder.dtChildAdapter.setOnCommentListenerChild((blogId, tarUid, tarCommId, name) -> {
                if (onCommentListener!=null){
                    onCommentListener.onListener(blogId,tarUid,tarCommId,name);
                }
            });
            holder.dtChildAdapter.setOnCommentLong((type, commId,targetId,positon,tag) -> {
                if (onCommentLong!=null){
                    onCommentLong.onLongListener(type,commId,targetId,position,tag);
                }
            });
        }
    }

    private boolean noneMember(){
        int type= SPUtils.getInstance().getInt("phtype");
        LoginDao loginDao=new LoginDao(context);
        if (type==-1 && loginDao.getObject()!=null && loginDao.getObject().getUserDO().getStatus()==0){
            return true;
        }
        return false;
    }


    private void updateLike(Context context, final MicroblogCommentVO data){
        ArrayMap<String,String> map= XLNetHttps.getBaseMap(context);
        map.put("commentId",String.valueOf(data.getMicroblogCommentDO().getId()));
        if (data.getIsLike() == 1) {
            map.put("type", CodeConfig.BLOG_UNLIKE);
        } else {
            map.put("type", CodeConfig.BLOG_LIKE);
        }
        XLNetHttps.request(ApiConfig.API_COMMENTLIKE, map, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel= XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    if (data.getIsLike()==1){
                        data.setIsLike(0);
                        data.setLikeCount(data.getLikeCount()-1);
                    }else {
                        data.setIsLike(1);
                        data.setLikeCount(data.getLikeCount()+1);
                    }
                    BlogDtAdapter.this.notifyDataSetChanged();
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
}
