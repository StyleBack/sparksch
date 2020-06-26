package com.doschool.ahu.appui.main.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidubce.services.bos.BosClient;
import com.doschool.ahu.BuildConfig;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.adapter.BoxAdapter;
import com.doschool.ahu.appui.main.ui.bean.DepartBean;
import com.doschool.ahu.appui.main.ui.bean.MajorBean;
import com.doschool.ahu.appui.main.widget.ConsGradePop;
import com.doschool.ahu.appui.main.widget.SexPop;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.appui.reglogin.ui.LoginAcademicActivity;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.factory.BosFactory;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.RandomUtils;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.widget.LoadingDialog;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
import com.jakewharton.rxbinding2.view.RxView;
import com.matisseutil.GifFilter;
import com.matisseutil.Glide4Engine;
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
import java.util.List;
import java.util.concurrent.TimeUnit;


import static com.doschool.ahu.configfile.ApiConfig.API_ADD_USERVER;
import static com.doschool.ahu.configfile.ApiConfig.API_SCHOOL_DEPART;

/**
 * Created by X on 2018/10/23
 *
 * 手机用户验证界面
 */
public class UpLoadValidationActivity extends BaseActivity {

    public static final int RES_CODE=12;

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;

    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;

    @ViewInject(R.id.tool_right_tv)
    private TextView tool_right_tv;

    @ViewInject(R.id.old_child_tv)
    private TextView old_child_tv;

    @ViewInject(R.id.load_cn_tx)
    private TextView load_cn_tx;

    @ViewInject(R.id.load_cnbain)
    private TextView load_cnbain;

    @ViewInject(R.id.load_ex_numsch)
    private EditText load_ex_numsch;

    @ViewInject(R.id.load_ex_yx)
    private TextView load_ex_yx;

    @ViewInject(R.id.load_ex_zy)
    private TextView load_ex_zy;

    @ViewInject(R.id.load_ex_name)
    private EditText load_ex_name;

    @ViewInject(R.id.load_tsex)
    private TextView load_tsex;

    @ViewInject(R.id.rv_box)
    private RecyclerView rv_box;
    private GridLayoutManager gridLayoutManager;
    private BoxAdapter boxAdapter;
    private List<String> list=new ArrayList<>();
    private LoginDao loginDao;
    private ArrayMap<String,String> departMap=new ArrayMap<>();
    private List<DepartBean.PartDta> departList=new ArrayList<>();
    private List<MajorBean> majorList=new ArrayList<>();
    private BosClient client;
    private LoadingDialog.Builder builder;
    private LoadingDialog loadingDialog;
    private String imgName="";
    private int count=0;
    private ArrayMap<String,String> verMap=new ArrayMap<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_load_valida_layout;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("上传验证");
        tool_right_tv.setVisibility(View.VISIBLE);
        tool_right_tv.setTextColor(getResources().getColor(R.color.new_color));
        tool_right_tv.setText("提交");
        loginDao=new LoginDao(this);
        departMap= XLNetHttps.getBaseMap(this);
        verMap=XLNetHttps.getBaseMap(this);
        client= BosFactory.getClient();
        xbox();
        getDepart();
    }

    private void getDepart(){
        departMap.put("userType",String.valueOf(loginDao.getObject().getUserDO().getUserType()));
        XLNetHttps.requestNormal(API_SCHOOL_DEPART, departMap, DepartBean.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                DepartBean departBean= XLGson.fromJosn(result,DepartBean.class);
                if (departBean.getCode()==0){
                    departList=departBean.getData();
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

    @SuppressLint("CheckResult")
    private void xbox(){
        gridLayoutManager=new GridLayoutManager(this,2);
        rv_box.setLayoutManager(gridLayoutManager);

        boxAdapter=new BoxAdapter(this,list);
        rv_box.setAdapter(boxAdapter);

        boxAdapter.setOnBoxListener(new BoxAdapter.OnBoxListener() {
            @Override
            public void addImg() {
                getPermiss();
            }

            @Override
            public void delet(int position) {
                list.remove(position);
                boxAdapter.notifyItemRemoved(position);
                boxAdapter.notifyDataSetChanged();
            }
        });

        //老用户登录
        RxView.clicks(old_child_tv)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    Bundle bundle=new Bundle();
                    bundle.putString("old","old");
                    IntentUtil.toActivity(this,bundle, LoginAcademicActivity.class);
                });

        //提交
        RxView.clicks(tool_right_tv)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> submitVer());
    }

    @SuppressLint("CheckResult")
    private void getPermiss(){
        RxPermissions permissions=new RxPermissions(this);
        permissions.requestEachCombined(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted){//同意后调用
                        getUpPic();
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

    private void getUpPic(){
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .showSingleMediaType(true)
                .countable(true)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, BuildConfig.APPLICATION_ID + ".fileprovider"))
                .maxSelectable(2)
                .addFilter(new GifFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(RES_CODE);
    }

    private void submitVer(){
        if (!isVer()){
            return;
        }
        loading();
        for (int i=0;i<list.size();i++){
            final File files=new File(list.get(i));//上传文件的目录
            final String imgRand= RandomUtils.getRandName(loginDao.getObject().getUserDO().getId(),5)+".jpg";
            new Thread(() -> BosFactory.getRequest(client, CodeConfig.BOS_VERIFY + imgRand, files, (request, currentSize, totalSize) -> {
                if (currentSize==totalSize){
                    imgName+=imgRand+",";
                    count++;
                    if (count==list.size()){
                        okSucc();
                    }
                }
            })).start();
        }
    }
    private void okSucc(){
        String[] strArr=imgName.split(",");
        if (strArr.length==1){
            verMap.put("picture1",strArr[0]);
        }else if (strArr.length==2){
            verMap.put("picture1",strArr[0]);
            verMap.put("picture2",strArr[1]);
        }
        verMap.put("realName",load_ex_name.getText().toString());
        verMap.put("phone",loginDao.getObject().getUserDO().getPhone());
        verMap.put("departId", String.valueOf(prtId));
        verMap.put("sex", String.valueOf(sex));
        verMap.put("majorId", String.valueOf(majId));
        if (TextUtils.equals(load_cnbain.getText().toString(),getResources().getString(R.string.upload_xuh_c1))){
            verMap.put("funcId",load_ex_numsch.getText().toString());
        }else {
            verMap.put("noticeId",load_ex_numsch.getText().toString());
        }
        XLNetHttps.request(API_ADD_USERVER, verMap, LoginVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginVO=XLGson.fromJosn(result,LoginVO.class);
                if (loginVO.getCode()==0){
                    loginDao.clearUserTable();
                    loginDao.saveObject(loginVO.getData());
                    IntentUtil.toActivity(UpLoadValidationActivity.this,null,UpLoadFinishActivity.class);
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

    private void loading(){
        builder=new LoadingDialog.Builder(UpLoadValidationActivity.this)
                .setMessage("上传中...")
                .setCancelable(false)
                .setCancelOutside(false);
        loadingDialog=builder.create();
        loadingDialog.show();
    }

    private boolean isVer(){
        if (list==null || list.size()==0){
            XLToast.showToast("你还没有上传图片哟！");
            return false;
        }
        if (TextUtils.isEmpty(load_ex_name.getText().toString())){
            XLToast.showToast("请输入你的姓名！");
            return false;
        }
        if (TextUtils.isEmpty(load_ex_numsch.getText().toString())){
            XLToast.showToast("请输入你的学号或通知书编号！");
            return false;
        }
        if (sex==0){
            XLToast.showToast("请选择性别！");
            return false;
        }
        if (prtId<=0){
            XLToast.showToast("请选择院系！");
            return false;
        }
        if (majId<=0){
            XLToast.showToast("请选择专业！");
            return false;
        }
        return true;
    }

    @Event({R.id.tool_back_iv,R.id.load_cn_tx,R.id.load_rlprt,R.id.load_rlmaj,R.id.load_rlsex})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.tool_back_iv:
                finish();
                break;
            case R.id.load_cn_tx://切换有无学号
                if (TextUtils.equals(load_cn_tx.getText().toString(),getResources().getString(R.string.upload_num_unname))){
                    load_cnbain.setText(getResources().getString(R.string.upload_xuh_c2));
                    load_ex_numsch.setHint(getResources().getString(R.string.upload_hint_x2));
                    load_cn_tx.setText(getResources().getString(R.string.upload_num_name));
                }else {
                    load_cnbain.setText(getResources().getString(R.string.upload_xuh_c1));
                    load_ex_numsch.setHint(getResources().getString(R.string.upload_hint_x1));
                    load_cn_tx.setText(getResources().getString(R.string.upload_num_unname));
                }
                break;
            case R.id.load_rlprt://院系
                prtWheel();
                break;
            case R.id.load_rlmaj://专业
                majWheels();
                break;
            case R.id.load_rlsex://性别
                initSex();
                break;
        }
    }

    private int sex;
    private void initSex(){
        SexPop sexPop=new SexPop(this);
        sexPop.showPopupWindow();
        sexPop.setOnSexListener((sexOT, type) -> {
            sex=type;
            load_tsex.setText(sexOT);
        });
    }

    private int prtId;
    private void prtWheel(){
        ConsGradePop ptPop=new ConsGradePop(this);
        ptPop.onDepartData(departList).showPopupWindow();
        ptPop.setOnDepartMajorListener(new ConsGradePop.OnDepartMajorListener() {
            @Override
            public void onDepart(String ptName, int ptId,int position) {
                majId=-1;
                load_ex_zy.setText("");
                load_ex_yx.setText(ptName);
                prtId=ptId;
                majorList=departList.get(position).getMajorDOS();
            }

            @Override
            public void onMajor(String mjName, int mjId) {

            }
        });
    }

    private int majId;
    private void majWheels(){
        if (majorList!=null && majorList.size()!=0){
            ConsGradePop mjPop=new ConsGradePop(this);
            mjPop.onMajorData(majorList).showPopupWindow();
            mjPop.setOnDepartMajorListener(new ConsGradePop.OnDepartMajorListener() {
                @Override
                public void onDepart(String ptName, int ptId, int position) {

                }

                @Override
                public void onMajor(String mjName, int mjId) {
                    load_ex_zy.setText(mjName);
                    majId=mjId;
                }
            });
        }
    }

    @Override
    protected void DetoryViewAndThing() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case RES_CODE:
                    if (list.size()>=1 && Matisse.obtainPathResult(data).size()>1){
                        XLToast.showToast("最多选择2张图片上传");
                    }else {
                        for (int i=0;i<Matisse.obtainPathResult(data).size();i++){
                            list.add(Matisse.obtainPathResult(data).get(i));
                        }
                        boxAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }
}
