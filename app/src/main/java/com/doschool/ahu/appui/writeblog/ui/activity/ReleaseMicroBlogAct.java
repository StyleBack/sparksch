package com.doschool.ahu.appui.writeblog.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.model.PutObjectRequest;
import com.blankj.utilcode.util.KeyboardUtils;
import com.doschool.ahu.BuildConfig;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.writeblog.event.OnItemListener;
import com.doschool.ahu.appui.writeblog.ui.adapter.ChangePhAdapter;
import com.doschool.ahu.appui.writeblog.ui.bean.PhotoBean;
import com.doschool.ahu.appui.writeblog.ui.bean.VoteBean;
import com.doschool.ahu.appui.writeblog.ui.utils.AtUserPatternUtil;
import com.doschool.ahu.appui.writeblog.widget.BlogKeyborad;
import com.doschool.ahu.appui.writeblog.widget.WtpAddView;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.AppConfig;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.factory.BosFactory;
import com.doschool.ahu.factory.OnBosCallback;
import com.doschool.ahu.utils.AssetJson;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.MediaFileUtil;
import com.doschool.ahu.utils.PatternUtils;
import com.doschool.ahu.utils.RandomUtils;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.LoadingDialog;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.matisseutil.GifFilter;
import com.matisseutil.Glide4Engine;
import com.sunhapper.spedittool.view.SpEditText;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.doschool.ahu.configfile.CodeConfig.ADDBLOG_IV;
import static com.doschool.ahu.configfile.CodeConfig.ADDBLOG_VD;
import static com.doschool.ahu.configfile.CodeConfig.AT_CODE;
import static com.doschool.ahu.configfile.CodeConfig.COMMTIC_CODE;
import static com.doschool.ahu.configfile.CodeConfig.LAND_CODE;
import static com.doschool.ahu.configfile.CodeConfig.REQUEST_CODE_CHOOSE;

/**
 * Created by X on 2018/11/2
 *
 * 发布微博改版界面
 */
public class ReleaseMicroBlogAct extends BaseActivity {

    public static int CODE_ID=-10;
    public static final int BLOG_REFRESH=10101;

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.tool_right_bgtv)
    private TextView tool_right_bgtv;

    //输入框
    @ViewInject(R.id.spedit)
    private SpEditText spedit;

    //字数控制
    @ViewInject(R.id.micro_count_tx)
    private TextView micro_count_tx;

    //添加图片
    @ViewInject(R.id.rv_blog)
    private RecyclerView rv_blog;

    //选地标
    @ViewInject(R.id.ll_loca_sl)
    private LinearLayout ll_loca_sl;
    @ViewInject(R.id.iv_loca)
    private ImageView iv_loca;
    @ViewInject(R.id.tx_loca)
    private TextView tx_loca;
    //清空地标
    @ViewInject(R.id.iv_clearloca)
    private ImageView iv_clearloca;

    //常用话题
    @ViewInject(R.id.ll_topic_sl)
    private LinearLayout ll_topic_sl;
    @ViewInject(R.id.iv_topic)
    private ImageView iv_topic;
    @ViewInject(R.id.tx_topic)
    private TextView tx_topic;
    //清除话题
    @ViewInject(R.id.iv_cleartopic)
    private ImageView iv_cleartopic;


    //投票
    @ViewInject(R.id.ll_wrtp)
    private LinearLayout ll_wrtp;
    @ViewInject(R.id.iv_wrtp)
    private ImageView iv_wrtp;
    @ViewInject(R.id.tx_wrtp)
    private TextView tx_wrtp;
    //清除
    @ViewInject(R.id.iv_cltp)
    private ImageView iv_cltp;
    //选项
    @ViewInject(R.id.rtp_tbtn)
    private TextView rtp_tbtn;
    @ViewInject(R.id.addview_lltp)
    private LinearLayout addview_lltp;

    //微博键盘
    @ViewInject(R.id.keyboard_blog)
    private BlogKeyborad keyboard_blog;

    private int count=0;
    private LoadingDialog.Builder builder;
    private LoadingDialog loadingDialog;
    private int id=CODE_ID;
    private String landName;

    private List<PhotoBean> photoList=new ArrayList<>();
    private LinearLayoutManager adManager;
    private String topicName;
    private ChangePhAdapter changePhAdapter;
    private ArrayMap<String,String> addMap=new ArrayMap<>();
    private int Uuid;
    private BosClient client;
    private LoginDao loginDao;
    private String pictureList;
    private String imgName="";

    private List<VoteBean.IntroducerTxtBean> voteList=new ArrayList<>();
    private List<String> hintList=new ArrayList<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_micro_blog_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(VISIBLE);
        tool_title_tv.setVisibility(VISIBLE);
        tool_title_tv.setText("校园日记");
        tool_right_bgtv.setVisibility(VISIBLE);
        tool_right_bgtv.setText("发送");
        addMap= XLNetHttps.getBaseMap(this);
        loginDao=new LoginDao(this);
        voteList=AssetJson.getVoteJs(this).getIntroducerTxt();
        if (loginDao.getObject()!=null){
            Uuid=loginDao.getObject().getUserDO().getId();
        }
        if (getIntent()!=null && getIntent().getExtras()!=null){
            if (getIntent().getExtras().containsKey("list")){
                photoList= (List<PhotoBean>) getIntent().getExtras().getSerializable("list");
            }
            if (getIntent().getExtras().containsKey("topicName")){//接收从话题跳转
                topicName=getIntent().getExtras().getString("topicName");
                if (!TextUtils.isEmpty(topicName)){
                    XLGlideLoader.loadImageById(iv_topic,R.mipmap.comm_tic_icon);
                    tx_topic.setText(topicName);
                    tx_topic.setTextColor(getResources().getColor(R.color.now_txt_color));
                    iv_cleartopic.setVisibility(VISIBLE);
                }
            }
        }
        client= BosFactory.getClient();
        initBoard();
        initAdPH();
    }

    @SuppressLint("CheckResult")
    private void initBoard(){
        //字数计数
        RxTextView.textChanges(spedit).subscribe(charSequence -> micro_count_tx.setText(charSequence.toString().length()+""));
        keyboard_blog.setSimlePort(spedit);

        //地标
        RxView.clicks(ll_loca_sl)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o->{
                    if (keyboard_blog.getSimle_panel().getVisibility() == VISIBLE){
                        keyboard_blog.getSimle_panel().setVisibility(GONE);
                    }
                    Bundle bundle=new Bundle();
                    bundle.putInt("id",id);
                    Intent intent=new Intent(this,LandMarkActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,LAND_CODE);
                });

        //常用话题
        RxView.clicks(ll_topic_sl)
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (keyboard_blog.getSimle_panel().getVisibility() == VISIBLE){
                        keyboard_blog.getSimle_panel().setVisibility(GONE);
                    }
                    Intent intent=new Intent(this,AddCommonTopicAct.class);
                    startActivityForResult(intent,COMMTIC_CODE);
                });

        //添加话题事件
        keyboard_blog.setOnKeyBoardListener(content -> {
            if (!TextUtils.isEmpty(content)){
                topicName=content;
                XLGlideLoader.loadImageById(iv_topic,R.mipmap.comm_tic_icon);
                tx_topic.setText(content);
                tx_topic.setTextColor(getResources().getColor(R.color.now_txt_color));
                iv_cleartopic.setVisibility(VISIBLE);
            }
        });

        //投票
        RxView.clicks(ll_wrtp)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (KeyboardUtils.isSoftInputVisible(ReleaseMicroBlogAct.this)){
                        KeyboardUtils.hideSoftInput(ReleaseMicroBlogAct.this);
                    }
                    if (keyboard_blog.getSimle_panel().getVisibility() == VISIBLE){
                        keyboard_blog.getSimle_panel().setVisibility(GONE);
                    }

                    if (hintList!=null && hintList.size()==0){
                        //取随机展示
                        int hint=new Random().nextInt(voteList.size());
                        hintList.add(voteList.get(hint).getHintConf());
                        hintList.add(voteList.get(hint).getHintCons());
                        XLGlideLoader.loadImageById(iv_wrtp,R.mipmap.wrtp_icon);
                        tx_wrtp.setTextColor(getResources().getColor(R.color.now_txt_color));
                        iv_cltp.setVisibility(VISIBLE);
                        rtp_tbtn.setVisibility(VISIBLE);

                        for (int i=0;i<hintList.size();i++){
                            WtpAddView wtpAddView=new WtpAddView(this);
                            wtpAddView.createUI(addview_lltp,hintList.get(i),rtp_tbtn);
                        }
                    }
                });


        //发布
        RxView.clicks(tool_right_bgtv)
                .throttleFirst(5, TimeUnit.SECONDS)
                .subscribe(o -> {
                    count=0;
                    submit();
                });
    }

    private void initAdPH(){
        adManager=new LinearLayoutManager(this);
        adManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_blog.setLayoutManager(adManager);
        changePhAdapter=new ChangePhAdapter(this,photoList);
        rv_blog.setAdapter(changePhAdapter);
        if (photoList!=null && photoList.size()>0){
            changePhAdapter.notifyDataSetChanged();
        }
        changePhAdapter.setOnItemListener(new OnItemListener() {
            @Override
            public void onAdd() {
                getPermis();
            }

            @Override
            public void onDelete(int position) {
                photoList.remove(position);
                changePhAdapter.notifyItemRemoved(position);
                changePhAdapter.notifyDataSetChanged();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void getPermis(){
        RxPermissions permissions=new RxPermissions(this);
        permissions.requestEachCombined(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted){//同意后调用
                        rvAddImg();
                    }else if (permission.shouldShowRequestPermissionRationale){//禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                    }else {//禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                        if (!permissions.isGranted(Manifest.permission.CAMERA)){
                            XLToast.showToast("您的相机权限未打开！");
                        }
                        if (!permissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            XLToast.showToast("您的存储权限未打开！");
                        }
                        if (!permissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
                            XLToast.showToast("您的存储权限未打开！");
                        }
                    }
                });
    }
    private void rvAddImg(){
        Matisse.from(this)
                .choose(MimeType.ofAll())
                .countable(true)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, BuildConfig.APPLICATION_ID + ".fileprovider"))
                .maxSelectable(9)
                .addFilter(new GifFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(REQUEST_CODE_CHOOSE);
    }


    @SuppressLint("CheckResult")
    private void submit(){
        if (TextUtils.isEmpty(spedit.getText().toString().trim())){
            XLToast.showToast("发送动态不能为空哟~~~");
            return;
        }
        loading();
        //处理at
        Observable.just(spedit.getText().toString())
                .subscribeOn(Schedulers.io())
                .map(s -> {
                    Map<String,String> atuserMap=new HashMap<>();
                    SpEditText.SpData[] spDatas = spedit.getSpDatas();
                    for (SpEditText.SpData spData : spDatas) {
                        if (!String.valueOf(spData.getCustomData()).equals("1")) {
                            String customData = (String) spData.getCustomData();
                            String showContent = (String) spData.getShowContent();
                            atuserMap.put(customData, showContent);
                        }
                    }
                    for (String key : atuserMap.keySet()) {
                        s = s.replace(atuserMap.get(key), key);
                    }
                    return s;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> addBlogContent(s));
    }

    private String addVote="";
    private String vote="";
    //提交内容
    private void addBlogContent(String content){

        // 投票
        for (int i=0;i<addview_lltp.getChildCount();i++){
            LinearLayout child = (LinearLayout) addview_lltp.getChildAt(i);
            EditText vote_ext=child.findViewById(R.id.vote_ext);
            if (!TextUtils.isEmpty(vote_ext.getText().toString().trim())){
                addVote+=vote_ext.getText().toString().trim()+",";
            }
        }

        addMap.put("content",PatternUtils.replaceLineBlanks(content.trim()));
        if (id>0){
            addMap.put("placeId",String.valueOf(id));
        }
        if (!TextUtils.isEmpty(topicName)){
            addMap.put("topicName",topicName);
        }

        if (!TextUtils.isEmpty(addVote)){
            vote=addVote.substring(0,addVote.length()-1);
            addMap.put("voteList",vote);
        }

        if (photoList!=null && photoList.size()>0){
            if (MediaFileUtil.isVideoFileType(photoList.get(0).getPath())){
                upLoadVideo();
            }else if (MediaFileUtil.isImageFileType(photoList.get(0).getPath())){
                upLoadBosImg();
            }
        }else {
            http();
        }
    }
    //上传视频
    private void upLoadVideo(){
        final File files=new File(photoList.get(0).getPath());//上传文件的目录
        final String videoName= RandomUtils.getRandName(Uuid,5);
        new Thread(() -> BosFactory.getRequest(client, CodeConfig.BOS_VIDEO + videoName+".mp4", files, (request, currentSize, totalSize) -> {
            if (currentSize==totalSize){
                upVideoImg(videoName);
            }
        })).start();
    }
    //上传视频第一帧的图片
    private void upVideoImg(final String name){
        String pathName=MediaFileUtil.saveImg(AppConfig.SDK_IMG)+File.separator+name+".jpg";
        Bitmap bitmap=MediaFileUtil.getVideoThumb(photoList.get(0).getPath());
        MediaFileUtil.saveBitmapFile(bitmap,pathName);
        final File file=new File(pathName);
        new Thread(() -> BosFactory.getRequest(client, CodeConfig.BOS_VIDEO + name+".jpg", file, new OnBosCallback() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                if (currentSize==totalSize){
                    hintGOVideo(name);
                    MediaFileUtil.deleteFile(new File(MediaFileUtil.saveImg(AppConfig.SDK_IMG)));
                    MediaFileUtil.deleteFile(new File(MediaFileUtil.saveImg(AppConfig.SDK_VIDEO)));
                }
            }
        })).start();
    }
    private void hintGOVideo(String fileName){
        addMap.put("videoName", fileName+".mp4");
        addMap.put("videoThumbnail", fileName+".jpg");
        http();
    }

    //传图片
    private void upLoadBosImg(){
        for (int i=0;i<photoList.size();i++){
            final File files=new File(photoList.get(i).getPath());//上传文件的目录
            final String imgRand=RandomUtils.getRandName(Uuid,5)+".jpg";
            imgName+=imgRand+",";
            new Thread(() -> BosFactory.getRequest(client, CodeConfig.BOS_BLOG + imgRand, files, (request, currentSize, totalSize) -> {
                if (currentSize==totalSize){
                    count++;
                    if (count==photoList.size()){
                            hintGOImg();
                    }
                }
            })).start();
        }
    }
    private void hintGOImg(){
        pictureList = imgName.substring(0, imgName.length() - 1);
        addMap.put("pictureList", pictureList);
        http();
    }

    //最后提交
    private void http(){
        XLNetHttps.request(ApiConfig.API_ADDBLOG, addMap, BaseModel.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                BaseModel baseModel= XLGson.fromJosn(result,BaseModel.class);
                if (baseModel.getCode()==0){
                    XLToast.showToast(baseModel.getMessage());
                    EventUtils.onPost(new XMessageEvent(BLOG_REFRESH));
                    finish();
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
                if (loadingDialog.isShowing()){
                    loadingDialog.dismiss();
                }
            }
        });
    }

    @Override
    protected void DetoryViewAndThing() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case AT_CODE://at处理结果
                    if (data!=null){
                        String userid=data.getStringExtra("userId");
                        String atStr=data.getStringExtra("atUser");
                        String at=AtUserPatternUtil.patternToUser(atStr);
                        //将处理后的at插入到edit中
                        if (spedit.getText().length()+at.length()<=300){
                            spedit.insertSpecialStr(at, false, atStr,
                                    new ForegroundColorSpan(getResources().getColor(R.color.now_txt_color)));
                        }else {
                            XLToast.showToast("字数超过限制哟~~~");
                        }
                    }
                    break;
                case LAND_CODE://地标处理
                    if (data!=null){
                        id=data.getExtras().getInt("lid");
                        landName=data.getExtras().getString("name");
                        if (id==CODE_ID){
                            XLGlideLoader.loadImageById(iv_loca,R.mipmap.loca_un_icon);
                            tx_loca.setText(getResources().getString(R.string.blog_loca));
                            tx_loca.setTextColor(getResources().getColor(R.color.dove_color));
                            iv_clearloca.setVisibility(GONE);
                        }else {
                            XLGlideLoader.loadImageById(iv_loca,R.mipmap.loca_icon);
                            tx_loca.setText(landName);
                            tx_loca.setTextColor(getResources().getColor(R.color.now_txt_color));
                            iv_clearloca.setVisibility(VISIBLE);
                        }
                    }
                    break;
                case COMMTIC_CODE://常用话题
                    if (data!=null){
                        topicName=data.getExtras().getString("ticname");
                        XLGlideLoader.loadImageById(iv_topic,R.mipmap.comm_tic_icon);
                        tx_topic.setText(topicName);
                        tx_topic.setTextColor(getResources().getColor(R.color.now_txt_color));
                        iv_cleartopic.setVisibility(VISIBLE);
                    }
                    break;
                case REQUEST_CODE_CHOOSE://图片或视频选择
                    for (int i = 0; i< Matisse.obtainPathResult(data).size(); i++){
                        if (photoList!=null && photoList.size()>0){
                            if (MediaFileUtil.isImageFileType(photoList.get(0).getPath())){
                                if (!MediaFileUtil.isImageFileType(Matisse.obtainPathResult(data).get(0))){
                                    XLToast.showToast("图片和视频只能选择一种哟~~~");
                                    break;
                                }
                            }else {
                                XLToast.showToast("图片和视频只能选择一种哟~~~");
                                break;
                            }
                        }
                        PhotoBean photoBean=new PhotoBean();
                        photoBean.setPath(Matisse.obtainPathResult(data).get(i));
                        if (MediaFileUtil.isVideoFileType(Matisse.obtainPathResult(data).get(i))){
                            photoBean.setType(ADDBLOG_VD);
                            photoList.add(photoBean);
                            if (photoList.size()==1){
                                break;
                            }
                        }else {
                            photoBean.setType(ADDBLOG_IV);
                            photoList.add(photoBean);
                            if (photoList.size()==9){
                                break;
                            }
                        }
                    }
                    keyboard_blog.setLists(photoList);
                    changePhAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Event({R.id.tool_back_iv,R.id.iv_clearloca,R.id.iv_cleartopic,R.id.iv_cltp,R.id.rtp_tbtn})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.tool_back_iv:
                finish();
                break;
            case R.id.iv_clearloca://清空地标
                if (KeyboardUtils.isSoftInputVisible(ReleaseMicroBlogAct.this)){
                    KeyboardUtils.hideSoftInput(ReleaseMicroBlogAct.this);
                }
                if (keyboard_blog.getSimle_panel().getVisibility() == VISIBLE){
                    keyboard_blog.getSimle_panel().setVisibility(GONE);
                }
                id=CODE_ID;
                XLGlideLoader.loadImageById(iv_loca,R.mipmap.loca_un_icon);
                tx_loca.setText(getResources().getString(R.string.blog_loca));
                tx_loca.setTextColor(getResources().getColor(R.color.dove_color));
                iv_clearloca.setVisibility(GONE);
                break;
            case R.id.iv_cleartopic://清空话题
                if (KeyboardUtils.isSoftInputVisible(ReleaseMicroBlogAct.this)){
                    KeyboardUtils.hideSoftInput(ReleaseMicroBlogAct.this);
                }
                if (keyboard_blog.getSimle_panel().getVisibility() == VISIBLE){
                    keyboard_blog.getSimle_panel().setVisibility(GONE);
                }
                topicName="";
                XLGlideLoader.loadImageById(iv_topic,R.mipmap.rtopic_icon);
                tx_topic.setText(getResources().getString(R.string.blog_tic));
                tx_topic.setTextColor(getResources().getColor(R.color.dove_color));
                iv_cleartopic.setVisibility(GONE);
                break;
            case R.id.iv_cltp://清除投票
                if (KeyboardUtils.isSoftInputVisible(ReleaseMicroBlogAct.this)){
                    KeyboardUtils.hideSoftInput(ReleaseMicroBlogAct.this);
                }
                if (keyboard_blog.getSimle_panel().getVisibility() == VISIBLE){
                    keyboard_blog.getSimle_panel().setVisibility(GONE);
                }
                rtp_tbtn.setVisibility(GONE);
                XLGlideLoader.loadImageById(iv_wrtp, R.mipmap.wrtp_icon_un);
                tx_wrtp.setTextColor(getResources().getColor(R.color.dove_color));
                iv_cltp.setVisibility(GONE);
                addview_lltp.removeAllViews();
                hintList.clear();
                break;
            case R.id.rtp_tbtn://添加投票选项
                if (addview_lltp.getChildCount()>=3){
                    rtp_tbtn.setVisibility(GONE);
                }
                WtpAddView wtpAddView=new WtpAddView(this);
                wtpAddView.createUI(addview_lltp,"",rtp_tbtn);
                break;
        }
    }

    private void loading(){
        builder=new LoadingDialog.Builder(ReleaseMicroBlogAct.this)
                .setMessage("上传中...")
                .setCancelable(false)
                .setCancelOutside(false);
        loadingDialog=builder.create();
        loadingDialog.show();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && keyboard_blog.getSimle_panel().getVisibility() == VISIBLE) {
            keyboard_blog.getSimle_panel().setVisibility(GONE);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
