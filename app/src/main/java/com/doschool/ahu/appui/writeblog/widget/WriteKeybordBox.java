package com.doschool.ahu.appui.writeblog.widget;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.doschool.ahu.BuildConfig;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.writeblog.ui.bean.PhotoBean;
import com.doschool.ahu.utils.MediaFileUtil;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.EmojLayout2;
import com.matisseutil.GifFilter;
import com.matisseutil.Glide4Engine;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import static com.doschool.ahu.configfile.CodeConfig.REQUEST_CODE_CHOOSE;

/**
 * Created by X on 2018/9/18
 *
 * 微博键盘
 */
public class WriteKeybordBox extends FrameLayout implements View.OnClickListener {


    public WrCallBack callBack;

    public FrameLayout key_fl_topic,key_fl_img,key_fl_all,key_fl_emj,key_fl_at;
    public ImageView chatbox_keyboard;
    public RelativeLayout bottom_panel;
    public EmojLayout2 key_em_panel;
    public TopicPanel key_topic_panel;
    public EditText editText;
    public List<PhotoBean> pList=new ArrayList<>();

    public WriteKeybordBox(@NonNull Context context) {
        this(context,null);
    }

    public WriteKeybordBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WriteKeybordBox(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI(){
        LayoutInflater.from(getContext()).inflate(R.layout.wr_blog_keybord,this);
        key_fl_topic=findViewById(R.id.key_fl_topic);
        key_fl_img=findViewById(R.id.key_fl_img);
        key_fl_all=findViewById(R.id.key_fl_all);
        key_fl_emj=findViewById(R.id.key_fl_emj);
        key_fl_at=findViewById(R.id.key_fl_at);
        chatbox_keyboard=findViewById(R.id.chatbox_keyboard);
        bottom_panel=findViewById(R.id.bottom_panel);
        key_em_panel=findViewById(R.id.key_em_panel);
        key_topic_panel=findViewById(R.id.key_topic_panel);

        key_fl_topic.setOnClickListener(this);
        key_fl_img.setOnClickListener(this);
        key_fl_all.setOnClickListener(this);
        key_fl_emj.setOnClickListener(this);
        key_fl_at.setOnClickListener(this);

    }

    public void setEdit(EditText editText,RelativeLayout wr_rlm){
        this.editText=editText;
        key_em_panel.setEditText(editText);
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HideAll();
                showKeyboard();
            }
        });

        wr_rlm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (KeyboardUtils.isSoftInputVisible((Activity) getContext())){
                    if (callBack!=null){
                        callBack.hideKeyBoard();
                    }
                }
                HideAll();
            }
        });
    }

    private void HideAll() {
        key_em_panel.setVisibility(GONE);
        key_topic_panel.setVisibility(GONE);
    }

    public void showKeyboard() {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.key_fl_topic://添加话题
                if (KeyboardUtils.isSoftInputVisible((Activity) getContext())){
                    if (callBack!=null){
                        callBack.hideKeyBoard();
                    }
                }
                HideAll();
                new CompantDialog(getContext(), new CompantDialog.OnListener() {
                    @Override
                    public void click(Dialog dialog, String content) {
                        if (callBack!=null){
                            callBack.onTopicClick(content);
                        }
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.key_fl_img://选择图片
                if (KeyboardUtils.isSoftInputVisible((Activity) getContext())){
                    if (callBack!=null){
                        callBack.hideKeyBoard();
                    }
                }
                HideAll();
                RxPermissions permissions=new RxPermissions((FragmentActivity) getContext());
                permissions.requestEachCombined(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe(permission -> {
                            if (permission.granted){//同意后调用
                                if (pList!=null && pList.size()>0 && MediaFileUtil.isVideoFileType(pList.get(0).getPath())){
                                    XLToast.showToast("视频的数量已达上限");
                                }else {
                                    if (pList!=null && pList.size()<9){
                                        getPhoto();
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
            case R.id.key_fl_all://常用话题
                if (KeyboardUtils.isSoftInputVisible((Activity) getContext())){
                    if (callBack!=null){
                        callBack.hideKeyBoard();
                    }
                }
                if (key_topic_panel.getVisibility() == View.GONE) {
                    key_em_panel.setVisibility(GONE);
                    key_topic_panel.setVisibility(VISIBLE);
                } else {
                    key_topic_panel.setVisibility(GONE);
                    showKeyboard();
                }
                break;
            case R.id.key_fl_emj://emj
                if (KeyboardUtils.isSoftInputVisible((Activity) getContext())){
                    if (callBack!=null){
                        callBack.hideKeyBoard();
                    }
                }
                if (key_em_panel.getVisibility() == View.GONE) {
                    key_topic_panel.setVisibility(GONE);
                    key_em_panel.showEmojLayout();
                    key_em_panel.setVisibility(VISIBLE);
                } else {
                    key_em_panel.setVisibility(GONE);
                    showKeyboard();
                }
                break;
            case R.id.key_fl_at://at
//                WriteBlogActivity.countAtNum();
//                Intent intent=AtActivity.createIntent(getContext(),WriteBlogActivity.list);
//                ((Activity) getContext()).startActivityForResult(intent, CodeConfig.AT_CODE);
                break;
        }

    }

    private void getPhoto(){
        Matisse.from((Activity) getContext())
                .choose(MimeType.ofAll())
//                .showSingleMediaType(true)
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

    public void setLists(List<PhotoBean> pList){
        this.pList=pList;
    }

    public void setCallBack(WrCallBack callBack) {
        this.callBack = callBack;
        key_topic_panel.setCallBack(callBack);
    }

    public interface WrCallBack{
        void hideKeyBoard();

        void onTopicClick(String topic);
    }

}
