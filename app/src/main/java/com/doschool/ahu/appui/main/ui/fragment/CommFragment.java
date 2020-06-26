package com.doschool.ahu.appui.main.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.infors.chat.ui.model.Conversation;
import com.doschool.ahu.appui.infors.chat.ui.model.MessageFactory;
import com.doschool.ahu.appui.infors.chat.ui.model.NomalConversation;
import com.doschool.ahu.appui.infors.chat.ui.presenter.ConversationPresenter;
import com.doschool.ahu.appui.infors.chat.ui.viewfeatures.ConversationView;
import com.doschool.ahu.appui.infors.chat.ui.viewfeatures.FriendshipMessageView;
import com.doschool.ahu.appui.infors.chat.util.PushUtil;
import com.doschool.ahu.appui.infors.ui.activity.NotifyingAssistantAct;
import com.doschool.ahu.appui.infors.ui.activity.PraiseMeActivity;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.main.ui.adapter.CvsAdapter;
import com.doschool.ahu.appui.main.ui.bean.CvsRedBean;
import com.doschool.ahu.appui.main.ui.bean.RedDotBean;
import com.doschool.ahu.appui.main.ui.holderlogic.CvsHolder;
import com.doschool.ahu.base.BaseFragment;
import com.doschool.ahu.appui.main.event.SwipeItemClickListener;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.db.ConversationDO;
import com.doschool.ahu.db.ConversationDao;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.AlertUtils;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XRvUtils;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jaeger.library.StatusBarUtil;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.sns.TIMFriendFutureItem;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.doschool.ahu.appui.infors.chat.ui.ChatActivity.UNRED_CODE_IM;
import static com.doschool.ahu.configfile.ApiConfig.API_DOT_RED;
import static com.tencent.imsdk.TIMConversationType.Group;

/**
 * Created by X on 2018/7/4
 *
 * 消息
 */
public class CommFragment extends BaseFragment implements ConversationView,FriendshipMessageView {


    //title
    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.com_xrv)
    private XRecyclerView com_xrv;
    private LinearLayoutManager linearLayoutManager;
    private CvsAdapter cvsAdapter;
    private List<Conversation> conversationList = new LinkedList<>();
    private ConversationPresenter presenter;
//    private FriendshipManagerPresenter friendshipManagerPresenter;
//    private FriendshipConversation friendshipConversation;
    private List<String> groupList;
    private CvsRedBean cvsRedBean=new CvsRedBean();
    private ConversationDao conversationDao;
    private List<ConversationDO> mDate=new ArrayList<>();
    private Disposable subscribe;
    private LoginDao loginDao;
    private ArrayMap<String,String> dotMap=new ArrayMap<>();

    @ViewInject(R.id.dot_tpl)
    private View dot_tpl;
    @ViewInject(R.id.moment_red_tv)
    private TextView moment_red_tv;

    @ViewInject(R.id.dot_tzan)
    private View dot_tzan;
    @ViewInject(R.id.love_red_tv)
    private TextView love_red_tv;

    @ViewInject(R.id.dot_tzhi)
    private View dot_tzhi;

    @ViewInject(R.id.cvs_emptll)
    private LinearLayout cvs_emptll;
    private int numCount;

    private Handler mHandler=new Handler();

    public CommFragment() {
    }

    @Override
    protected int getContentLayoutID() {
        return R.layout.commfragment_layout;
    }

    @Override
    protected void initViewEvents(Bundle savedInstanceState) {
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("消息");
        EventUtils.register(this);
        loginDao=new LoginDao(getActivity());
        dotMap=XLNetHttps.getBaseMap(getActivity());
        initRv();
        initData();
        initDot();
    }

    private void initRv(){
        linearLayoutManager=new LinearLayoutManager(getActivity());
        XRvUtils.initRv(com_xrv,linearLayoutManager,LinearLayoutManager.VERTICAL,true,true,false);
    }

    private void initData(){

        //初始化会话列表
//        friendshipManagerPresenter = new FriendshipManagerPresenter(this);
        presenter = new ConversationPresenter(this);
        presenter.getConversation();

        conversationDao =new ConversationDao(getActivity());

        //显示
        cvsAdapter=new CvsAdapter(getActivity(), conversationDao);

        cvsAdapter.setSwipeItemClickListener(new SwipeItemClickListener() {
            @Override
            public void onTop(int position,CvsHolder holder) {
                if (holder.right_top.getText().equals("置顶")){
                    if (mDate!=null && mDate.size()>=10) {
                        conversationDao.deleteOrder(mDate.get(0).identify);
                    }
                    ConversationDO order=new ConversationDO();
                    order.identify=conversationList.get(position).getIdentify();
                    conversationDao.insertDate(order);
                    NomalConversation  conversation = (NomalConversation) conversationList.get(position);
                    conversationList.remove(position);
                    conversationList.add(0, conversation);
                    com_xrv.scrollToPosition(0);
                    upOrder();
                }else if (holder.right_top.getText().equals("取消置顶")){
                    conversationDao.deleteOrder(conversationList.get(position).getIdentify());
                    upOrder();
                }
            }

            @Override
            public void onDelete(int position) {
                conversationDao.deleteOrder(conversationList.get(position).getIdentify());
                NomalConversation conversation= (NomalConversation) conversationList.get(position);
                if (presenter.delConversation(conversation.getType(), conversation.getIdentify())){
                    conversationList.remove(conversation);
                }
                upOrder();
            }

            @Override
            public void onItemListener(int position) {
                conversationList.get(position).navToDetail(getActivity());
            }
        });

        com_xrv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mHandler.postDelayed(() -> {
                    upOrder();
                    initDot();
                    com_xrv.refreshComplete();
                },1000);
            }

            @Override
            public void onLoadMore() {
            }
        });
    }

    private void initDot(){
        XLNetHttps.request(API_DOT_RED, dotMap, RedDotBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                RedDotBean dotBean=XLGson.fromJosn(result,RedDotBean.class);
                if (dotBean.getCode()==0){
                    if (dotBean.getData().getCommentCount()>0){
                        if (dotBean.getData().getCommentCount()>99){
                            moment_red_tv.setText("99+");
                        }else {
                            moment_red_tv.setText(String.valueOf(dotBean.getData().getCommentCount()));
                        }
                        moment_red_tv.setVisibility(View.VISIBLE);
                    }else {
                        moment_red_tv.setVisibility(View.GONE);
                    }

                    if (dotBean.getData().getLikeCount()>0){
                        if (dotBean.getData().getLikeCount()>99){
                            love_red_tv.setText("99+");
                        }else {
                            love_red_tv.setText(String.valueOf(dotBean.getData().getLikeCount()));
                        }
                        love_red_tv.setVisibility(View.VISIBLE);
                    }else {
                        love_red_tv.setVisibility(View.GONE);
                    }
                    numCount=dotBean.getData().getCommentCount()+dotBean.getData().getLikeCount();
                    EventUtils.onPost(new XMessageEvent(CodeConfig.CVS_UNRED_CODE,getTotalUnreadNum()+numCount));
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
    public void onResume() {
        StatusBarUtil.setTranslucentForImageView(getActivity(),0,null);
        StatusBarUtil.setLightMode(getActivity());
        super.onResume();
        initDot();
        PushUtil.getInstance().reset();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        XRvUtils.destroyRv(com_xrv);
        EventUtils.unRegister(this);
        if (mHandler!=null){
            mHandler.removeCallbacksAndMessages(null);
            mHandler=null;
        }
    }

    //从聊天室接收通知更新未读
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void unRedCode(XMessageEvent event){
        if (event.getCode()==UNRED_CODE_IM){
            upOrder();
        }
    }

    /**
     * 初始化界面或刷新界面
     *
     * @param conversationList
     */

    @Override
    public void initView(List<TIMConversation> conversationList) {
        this.conversationList.clear();
//        groupList = new ArrayList<>();
        for (TIMConversation item:conversationList){
            switch (item.getType()){
                case C2C:
//                case Group:
                    this.conversationList.add(new NomalConversation(item));
//                    groupList.add(item.getPeer());
                    break;
            }
        }
//        friendshipManagerPresenter.getFriendshipLastMessage();
    }


    /**
     * 更新最新消息显示
     *
     * @param message 最后一条消息
     */
    @Override
    public void updateMessage(TIMMessage message) {
        if (message == null){
            cvsAdapter.notifyDataSetChanged();
            return;
        }
        if (message.getConversation().getType() == TIMConversationType.System || message.getConversation().getType() == Group){
            return;
        }

        NomalConversation conversation = new NomalConversation(message.getConversation());
        Iterator<Conversation> iterator =conversationList.iterator();
        while (iterator.hasNext()){
            Conversation c = iterator.next();
            if (conversation.equals(c)){
                conversation = (NomalConversation) c;
                iterator.remove();
                break;
            }
        }
        conversation.setLastMessage(MessageFactory.getMessage(message));
        conversationList.add(conversation);
        Collections.sort(conversationList);
//        refresh();
        upOrder();

    }
    /**
     * 获取好友关系链管理系统最后一条消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetFriendshipLastMessage(TIMFriendFutureItem message, long unreadCount) {
//        if (friendshipConversation == null) {
//            friendshipConversation = new FriendshipConversation(message);
//        } else {
//            friendshipConversation.setLastMessage(message);
//        }
//        friendshipConversation.setUnreadCount(unreadCount);
//
//        if (!conversationList.contains(friendshipConversation)) {
//            conversationList.add(friendshipConversation);
//        }
        Collections.sort(conversationList);
//        refresh();
        upOrder();
    }

    /**
     * 获取好友关系链管理最后一条系统消息的回调
     *
     * @param message 消息列表
     */
    @Override
    public void onGetFriendshipMessage(List<TIMFriendFutureItem> message) {
//        friendshipManagerPresenter.getFriendshipLastMessage();
    }



    /**
     * 更新好友关系链消息
     */
    @Override
    public void updateFriendshipMessage() {
//        friendshipManagerPresenter.getFriendshipLastMessage();
    }


    /**
     * 删除会话
     *
     * @param identify
     */
    @Override
    public void removeConversation(String identify) {
        Iterator<Conversation> iterator = conversationList.iterator();
        while(iterator.hasNext()){
            Conversation conversation = iterator.next();
            if (conversation.getIdentify()!=null&&conversation.getIdentify().equals(identify)){
                iterator.remove();
                cvsAdapter.notifyDataSetChanged();
                return;
            }
        }
    }


    /**
     * 更新群信息
     *
     * @param info
     */
    @Override
    public void updateGroupInfo(TIMGroupCacheInfo info) {

    }


    /**
     * 刷新
     */
    @Override
    public void refresh() {
        upOrder();
    }

    private void upOrder(){
        subscribe = Observable.create((ObservableOnSubscribe<List<Conversation>>) e -> {
            Collections.sort(conversationList);
            mDate= conversationDao.getAllDate();
            if (mDate!=null){
                for (int j=0;j<mDate.size();j++){
                    for (int k=0;k<conversationList.size();k++){
                        Conversation conversation = conversationList.get(k);
                        if (TextUtils.equals(conversationList.get(k).getIdentify(), mDate.get(j).identify) ) {
                            conversationList.remove(k);
                            conversationList.add(0, conversation);
                        }
                    }
                }
            }
            e.onNext(conversationList);
            e.onComplete();
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(orders -> {
                    if (orders.size()==0){
                        cvs_emptll.setVisibility(View.VISIBLE);
                    }else {
                        cvs_emptll.setVisibility(View.GONE);
                    }
                    com_xrv.setAdapter(cvsAdapter);
                    cvsAdapter.setDatas(orders);
                }, throwable -> {

                }, () -> {

                });

    }


    private int getTotalUnreadNum(){
        int num = 0;
        for (Conversation conversation : conversationList){
            NomalConversation nomalConversation= (NomalConversation) conversation;
            if (nomalConversation.getType()==Group){
                continue;
            }
            num += conversation.getUnreadNum();
        }
        return num;
    }

    @Event({R.id.msg_llright,R.id.msg_llleft,R.id.notice_llmsg})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.msg_llright://赞
                if (noneMember()){
                    AlertUtils.alertToVerify(getActivity(),loginDao.getObject().getHandleStatus());
                }else {
                    initDot();
                    Bundle bundle=new Bundle();
                    bundle.putString("type",CodeConfig.MSG_TYPE_PRAISE);
                    IntentUtil.toActivity(getActivity(),bundle, PraiseMeActivity.class);
                }

                break;
            case R.id.msg_llleft://评论
                if (noneMember()){
                    AlertUtils.alertToVerify(getActivity(),loginDao.getObject().getHandleStatus());
                }else {
                    initDot();
                    Bundle bundle=new Bundle();
                    bundle.putString("type",CodeConfig.MSG_TYPE_COMMENT);
                    IntentUtil.toActivity(getActivity(),bundle, PraiseMeActivity.class);
                }
                break;
            case R.id.notice_llmsg://通知助手
                IntentUtil.toActivity(getActivity(),null,NotifyingAssistantAct.class);
                break;
        }
    }

    //未验证用户
    private boolean noneMember(){
        int type= SPUtils.getInstance().getInt("phtype");
        if (type==-1 && loginDao.getObject()!=null &&  loginDao.getObject().getUserDO().getStatus()==0){
            return true;
        }
        return false;
    }
}
