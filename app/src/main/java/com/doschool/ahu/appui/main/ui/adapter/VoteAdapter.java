package com.doschool.ahu.appui.main.ui.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.view.ViewGroup;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVoteOptionsDto;
import com.doschool.ahu.appui.main.ui.bean.VoteClick;
import com.doschool.ahu.appui.main.ui.holderlogic.VoteHoldet;
import com.doschool.ahu.base.adapter.BaseRvAdapter;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;

import org.xutils.common.Callback;

import java.util.List;

import static com.doschool.ahu.configfile.ApiConfig.API_VOTE_CLICK;

/**
 * Created by X on 2019/1/15.
 */
public class VoteAdapter extends BaseRvAdapter<MicroblogVoteOptionsDto,VoteHoldet> {

    private boolean isVote;

    public VoteAdapter(Context context,boolean isVote) {
        super(context);
        this.isVote=isVote;
    }

    private boolean isAnima;
    public void setAnimAction(boolean isAnima){
        this.isAnima=isAnima;
        notifyDataSetChanged();
    }

    @Override
    protected int getItemLayoutID(int viewType) {
        return R.layout.vote_child_item;
    }

    @Override
    protected VoteHoldet onCreateHolder(ViewGroup parent, int viewType) {
        return VoteHoldet.newInstance(parent,getItemLayoutID(viewType));
    }

    @Override
    protected void bindData(VoteHoldet holder, int position, MicroblogVoteOptionsDto data) {
        holder.setVoteHolder(context,data,isVote,isAnima);
        holder.child_rl.setOnClickListener(view -> {
            if (!isVote){
                clickVote(context,data.getMicroblogVoteOptionsDO().getId(),data.getMicroblogVoteOptionsDO().getBlogId());
            }else {
                XLToast.showToast("您已经投过票了");
            }
        });
    }

    private void clickVote(Context context,int id,int blogId){
        ArrayMap<String,String> map=XLNetHttps.getBaseMap(context);
        map.put("optionId",String.valueOf(id));
        map.put("blogId",String.valueOf(blogId));
        XLNetHttps.request(API_VOTE_CLICK, map, VoteClick.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                VoteClick voteClick=XLGson.fromJosn(result,VoteClick.class);
                if (voteClick.getCode()==0){
                    XLToast.showToast(voteClick.getMessage());
                    isVote=true;
                    if (onVoteListener!=null){
                        onVoteListener.onVote(true);
                        onVoteListener.onList(voteClick.getData());
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

    private OnVoteListener onVoteListener;

    public void setOnVoteListener(OnVoteListener onVoteListener) {
        this.onVoteListener = onVoteListener;
    }

    public interface OnVoteListener{
        void onVote(boolean isvote);
        void onList(List<MicroblogVoteOptionsDto> dtoList);
    }
}
