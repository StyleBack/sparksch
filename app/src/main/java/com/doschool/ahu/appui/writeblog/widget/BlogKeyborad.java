package com.doschool.ahu.appui.writeblog.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.doschool.ahu.BuildConfig;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.writeblog.event.OnKeyBoardListener;
import com.doschool.ahu.appui.writeblog.ui.activity.AtUserActivity;
import com.doschool.ahu.appui.writeblog.ui.bean.PhotoBean;
import com.doschool.ahu.utils.MediaFileUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.SimleBoxLayout;
import com.matisseutil.GifFilter;
import com.matisseutil.Glide4Engine;
import com.sunhapper.spedittool.view.SpEditText;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import static com.doschool.ahu.configfile.CodeConfig.AT_CODE;
import static com.doschool.ahu.configfile.CodeConfig.REQUEST_CODE_CHOOSE;

/**
 * Created by X on 2018/11/3
 *
 * 微博新键盘
 */
public class BlogKeyborad extends FrameLayout implements View.OnClickListener {

    private ImageView key_ivtopic,key_ivcam,key_ivsimle,key_ivat;
    private SimleBoxLayout simle_panel;
    private boolean firstSm=true;
    private SpEditText spEditText;
    public List<PhotoBean> pList=new ArrayList<>();

    public BlogKeyborad(@NonNull Context context) {
        this(context,null);
    }

    public BlogKeyborad(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BlogKeyborad(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI(){
        LayoutInflater.from(getContext()).inflate(R.layout.blog_keyboard_layout,this);
        key_ivtopic=findViewById(R.id.key_ivtopic);
        key_ivcam=findViewById(R.id.key_ivcam);
        key_ivsimle=findViewById(R.id.key_ivsimle);
        key_ivat=findViewById(R.id.key_ivat);
        simle_panel=findViewById(R.id.simle_panel);
        setSimle_panel(simle_panel);
        key_ivsimle.setOnClickListener(this);
        key_ivat.setOnClickListener(this);
        key_ivtopic.setOnClickListener(this);
        key_ivcam.setOnClickListener(this);
    }

    public void setSimlePort(SpEditText spEditText){
        this.spEditText=spEditText;
        simle_panel.setEditText(spEditText);
        spEditText.setOnClickListener(v -> {
            if (simle_panel.getVisibility()==VISIBLE){
                simle_panel.setVisibility(GONE);
                XLGlideLoader.loadImageById(key_ivsimle,R.mipmap.key_smile_icon);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.key_ivsimle://表情键盘
                if (KeyboardUtils.isSoftInputVisible((Activity) getContext())){
                    KeyboardUtils.hideSoftInput((Activity) getContext());
                    new Handler().postDelayed(() -> {
                        if (simle_panel.getVisibility()==GONE){
                            if (firstSm){
                                simle_panel.showEmojLayout();
                                firstSm=false;
                            }
                            simle_panel.setVisibility(VISIBLE);
                            XLGlideLoader.loadImageById(key_ivsimle,R.mipmap.key_smile_select);
                        }
                    },100);
                }else {
                    if (simle_panel.getVisibility()==GONE){
                        if (firstSm){
                            simle_panel.showEmojLayout();
                            firstSm=false;
                        }
                        simle_panel.setVisibility(VISIBLE);
                        XLGlideLoader.loadImageById(key_ivsimle,R.mipmap.key_smile_select);
                    }else {
                        simle_panel.setVisibility(GONE);
                        XLGlideLoader.loadImageById(key_ivsimle,R.mipmap.key_smile_icon);
                        showKey();
                    }
                }
                break;
            case R.id.key_ivat://@用户
                hideBoard();
                Intent intent= AtUserActivity.creatIntent(getContext());
                ((Activity)getContext()).startActivityForResult(intent,AT_CODE);
                break;
            case R.id.key_ivtopic://添加话题
                hideBoard();
                new CompantDialog(getContext(), (dialog, content) -> {
                    if (onKeyBoardListener!=null){
                        onKeyBoardListener.onTicName(content);
                    }
                    dialog.dismiss();
                }).show();
                break;
            case R.id.key_ivcam://拍摄
                hideBoard();
                RxPermissions permissions=new RxPermissions((FragmentActivity) getContext());
                permissions.requestEachCombined(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(permission -> {
                            if (permission.granted){//同意后调用
                                if (pList!=null && pList.size()>0 && MediaFileUtil.isVideoFileType(pList.get(0).getPath())){
                                    XLToast.showToast("视频的数量已达上限");
                                }else {
                                    if (pList!=null && pList.size()<9){
                                        intentPhoto();
                                    }else {
                                        XLToast.showToast("图片的数量已达上限");
                                    }
                                }
                            }else if (permission.shouldShowRequestPermissionRationale){//禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示
                            }else {//禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示
                                if (!permissions.isGranted(Manifest.permission.CAMERA)){
                                    XLToast.showToast("您的相机权限未打开！");
                                }
                                if (!permissions.isGranted(Manifest.permission.RECORD_AUDIO)){
                                    XLToast.showToast("您的麦克风权限未打开！");
                                }
                                if (!permissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                                    XLToast.showToast("您的存储权限未打开！");
                                }
                                if (!permissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
                                    XLToast.showToast("您的存储权限未打开！");
                                }
                            }
                        });
                break;
        }
    }

    //图片选择
    private void intentPhoto(){
        Matisse.from((Activity) getContext())
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

    //隐藏键盘
    private void hideBoard(){
        if (KeyboardUtils.isSoftInputVisible((Activity) getContext())){
            KeyboardUtils.hideSoftInput((Activity) getContext());
        }
        simle_panel.setVisibility(GONE);
        XLGlideLoader.loadImageById(key_ivsimle,R.mipmap.key_smile_icon);
    }

    //键盘显示
    private void showKey(){
        spEditText.setFocusable(true);
        spEditText.setFocusableInTouchMode(true);
        KeyboardUtils.showSoftInput((Activity) getContext());
    }

    private OnKeyBoardListener onKeyBoardListener;
    public void setOnKeyBoardListener(OnKeyBoardListener onKeyBoardListener){
        this.onKeyBoardListener=onKeyBoardListener;
    }

    public SimleBoxLayout getSimle_panel() {
        return simle_panel;
    }

    private void setSimle_panel(SimleBoxLayout simle_panel) {
        this.simle_panel = simle_panel;
    }

    public void setLists(List<PhotoBean> pList){
        this.pList=pList;
    }

}
