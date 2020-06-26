package com.doschool.ahu.appui.main.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidubce.services.bos.BosClient;
import com.doschool.ahu.BuildConfig;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.event.XMessageEvent;
import com.doschool.ahu.appui.main.ui.bean.CityBean;
import com.doschool.ahu.appui.main.ui.bean.ConsModel;
import com.doschool.ahu.appui.main.ui.bean.DepartBean;
import com.doschool.ahu.appui.main.widget.ConsGradePop;
import com.doschool.ahu.appui.main.widget.MajorHomePop;
import com.doschool.ahu.appui.main.widget.PersonIVPop;
import com.doschool.ahu.appui.reglogin.bean.LoginVO;
import com.doschool.ahu.base.BaseActivity;
import com.doschool.ahu.configfile.ApiConfig;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.factory.BosFactory;
import com.doschool.ahu.utils.AssetJson;
import com.doschool.ahu.utils.EventUtils;
import com.doschool.ahu.utils.RandomUtils;
import com.doschool.ahu.utils.TimeUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.utils.XLToast;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;
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

import static com.doschool.ahu.appui.main.ui.activity.ChangePersonalActivity.CHANGE_CODE;
import static com.doschool.ahu.configfile.ApiConfig.API_SCHOOL_DEPART;
import static com.doschool.ahu.configfile.CodeConfig.ME_BGIV;
import static com.doschool.ahu.configfile.CodeConfig.ME_HDIV;

/**
 * Created by X on 2018/10/11
 *
 * 个人资料修改
 */
public class PersonalDataActivity extends BaseActivity {


    private static final int REQUEST_COE=11;

    @ViewInject(R.id.tool_back_iv)
    private ImageView tool_back_iv;
    @ViewInject(R.id.tool_title_tv)
    private TextView tool_title_tv;
    private LoginVO.LoginData personalDta=new LoginVO.LoginData();

    @ViewInject(R.id.p_ivhead)
    private ImageView p_ivhead;

    @ViewInject(R.id.p_tvnick)
    private TextView p_tvnick;

    @ViewInject(R.id.p_tvzhuanye)
    private TextView p_tvzhuanye;

    @ViewInject(R.id.p_tvsex)
    private TextView p_tvsex;

    @ViewInject(R.id.p_tvjia)
    private TextView p_tvjia;

    @ViewInject(R.id.p_tvxing)
    private TextView p_tvxing;

    @ViewInject(R.id.p_tvqing)
    private TextView p_tvqing;

    @ViewInject(R.id.p_tvnian)
    private TextView p_tvnian;

    @ViewInject(R.id.p_tvxq)
    private TextView p_tvxq;

    @ViewInject(R.id.p_tvqm)
    private TextView p_tvqm;

    @ViewInject(R.id.i1_t1)
    private TextView i1_t1;

    @ViewInject(R.id.i2_i1)
    private TextView i2_i1;

    @ViewInject(R.id.i2_i2)
    private TextView i2_i2;

    @ViewInject(R.id.i2_i3)
    private TextView i2_i3;

    @ViewInject(R.id.i3_i1)
    private TextView i3_i1;

    @ViewInject(R.id.i3_i2)
    private TextView i3_i2;

    private BosClient client;
    private ArrayMap<String,String> dataMap=new ArrayMap<>();
    private LoginDao loginDao;
    private ArrayMap<String,String> departMap=new ArrayMap<>();
    private List<DepartBean.PartDta> departList=new ArrayList<>();

    @Override
    protected int getContentLayoutID() {
        return R.layout.act_persondata_lay;
    }

    @Override
    protected void initViewAndEvents(Bundle savedInstanceState) {
        tool_back_iv.setVisibility(View.VISIBLE);
        tool_title_tv.setVisibility(View.VISIBLE);
        tool_title_tv.setText("个人信息");
        dataMap= XLNetHttps.getBaseMap(this);
        client= BosFactory.getClient();
        loginDao=new LoginDao(this);
        departMap= XLNetHttps.getBaseMap(this);
        if (getIntent().getExtras()!=null){
            personalDta= (LoginVO.LoginData) getIntent().getExtras().getSerializable("gtag");
        }
        initData(personalDta);
        goWork();
        intiDepart();
        //解析省市
        citys= AssetJson.getJosnCitys(this).getCitys();
        consList=AssetJson.getJsonCons(this).getCons();

        gradeList= TimeUtil.getYears();
    }

    private void goWork(){
        dataMap.put("objId",String.valueOf(personalDta.getUserDO().getId()));
        XLNetHttps.request(ApiConfig.API_MINE, dataMap, LoginVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginVO= XLGson.fromJosn(result,LoginVO.class);
                if (loginVO.getCode()==0){
                    initData(loginVO.getData());
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

    //获取院系
    private void intiDepart(){
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


    private void initData(LoginVO.LoginData loginData){
        XLGlideLoader.loadCircleImage(p_ivhead,loginData.getHeadThumbnail());
        p_tvnick.setText(loginData.getUserDO().getNickName());
        if (loginData.getUserDO().getMajorId()==-1){
            p_tvzhuanye.setText("暂无");
        }else {
            p_tvzhuanye.setText(loginData.getMajorName());
        }
        if (loginData.getUserDO().isGirl()){
            p_tvsex.setText("女");
        }
        if (loginData.getUserDO().isBoy()){
            p_tvsex.setText("男");
        }
        if (!TextUtils.isEmpty(loginData.getUserDO().getHometown())){
            p_tvjia.setText(loginData.getUserDO().getHometown());
        }else {
            p_tvjia.setHint("暂未公布");
        }
        if (!TextUtils.isEmpty(loginData.getUserDO().getConstel())){
            p_tvxing.setText(loginData.getUserDO().getConstel());
        }else {
            p_tvxing.setHint("神秘星座");
        }

        if (!TextUtils.isEmpty(loginData.getUserDO().getLoveStatus())){
            p_tvqing.setText(loginData.getUserDO().getLoveStatus());
        }else {
            p_tvqing.setHint("不告诉你");
        }

        if (!TextUtils.isEmpty(loginData.getUserDO().getEnrYear())){
            p_tvnian.setText(loginData.getUserDO().getEnrYear());
        }else {
            p_tvnian.setHint("暂无");
        }

        if (!TextUtils.isEmpty(loginData.getUserDO().getHobby())){
            p_tvxq.setText(loginData.getUserDO().getHobby());
        }else {
            p_tvxq.setHint("暂无");
        }

        if (!TextUtils.isEmpty(loginData.getUserDO().getSelfIntro())){
            p_tvqm.setText(loginData.getUserDO().getSelfIntro());
        }else {
            p_tvqm.setHint("暂无");
        }
    }

    @Override
    protected void DetoryViewAndThing() {

    }

    private void initPop(int code){

        PersonIVPop personIVPop=new PersonIVPop(this);
        personIVPop.onCode(code)
                .showPopupWindow();
        personIVPop.setOnIVListener(changeCode -> getPerssions(code));

    }


    //获取权限
    @SuppressLint("CheckResult")
    private void getPerssions(int code){
        RxPermissions permissions=new RxPermissions(PersonalDataActivity.this);
        permissions.requestEachCombined(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(permission -> {
                    if (permission.granted){//同意后调用
                        exBg(code);
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

    private void exBg(int requsetCode){
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .showSingleMediaType(true)
                .countable(true)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, BuildConfig.APPLICATION_ID + ".fileprovider"))
                .maxSelectable(1)
                .addFilter(new GifFilter(320, 320, 5 * Filter.K * Filter.K))
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .forResult(requsetCode);
    }

    @Event({R.id.tool_back_iv,R.id.lay1_rlxsex,R.id.lay1_rlxz,R.id.p_tvnian,R.id.lay1_rlhead,R.id.lay1_rlnick,R.id.lay2_rljia,
    R.id.lay2_rlxing,R.id.lay2_rlqg,R.id.lay2_rlnian,R.id.lay3_rlxq,R.id.lay3_rlqm,R.id.p_tvnian,R.id.lay1_rlbg})
    private void clicks(View view){
        switch (view.getId()){
            case R.id.tool_back_iv:
                finish();
                break;
            case R.id.lay1_rlxsex://性别
                XLToast.showToast("暂时无法修改性别！");
                break;
            case R.id.lay1_rlxz://专业
                showDepart();
                break;
            case R.id.p_tvnian:
            case R.id.lay2_rlnian://年级
                showGrade();
                break;
            case R.id.lay1_rlhead://换头像
                initPop(ME_HDIV);
                break;
            case R.id.lay1_rlbg://更换封面
                initPop(ME_BGIV);
                break;
            case R.id.lay1_rlnick://昵称
                Bundle bundle=new Bundle();
                bundle.putString("change",p_tvnick.getText().toString());
                bundle.putString("id",String.valueOf(personalDta.getUserDO().getId()));
                bundle.putInt("code",1);
                bundle.putString("title",i1_t1.getText().toString());
                Intent intent=new Intent(this,ChangePersonalActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
                break;
            case R.id.lay2_rljia://家乡
                showTown();
                break;
            case R.id.lay2_rlxing://星座
                showCons();
                break;
            case R.id.lay2_rlqg://情感
                bundle=new Bundle();
                bundle.putString("change",p_tvqing.getText().toString());
                bundle.putString("id",String.valueOf(personalDta.getUserDO().getId()));
                bundle.putInt("code",6);
                bundle.putString("title",i2_i3.getText().toString());
                intent=new Intent(this,ChangePersonalActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,6);
                break;
            case R.id.lay3_rlxq://兴趣
                bundle=new Bundle();
                bundle.putString("change",p_tvxq.getText().toString());
                bundle.putString("id",String.valueOf(personalDta.getUserDO().getId()));
                bundle.putInt("code",8);
                bundle.putString("title",i3_i1.getText().toString());
                intent=new Intent(this,ChangePersonalActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,8);
                break;
            case R.id.lay3_rlqm://签名
                bundle=new Bundle();
                bundle.putString("change",p_tvqm.getText().toString());
                bundle.putString("id",String.valueOf(personalDta.getUserDO().getId()));
                bundle.putInt("code",9);
                bundle.putString("title",i3_i2.getText().toString());
                intent=new Intent(this,ChangePersonalActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,9);
                break;
        }
    }

    //年级
    private List<String> gradeList=new ArrayList<>();
    private void showGrade(){
        ConsGradePop gradePop=new ConsGradePop(this);
        gradePop.onGrade(gradeList)
                .showPopupWindow();
        gradePop.setOnPickerListenerChange(new ConsGradePop.OnPickerListenerChange() {
            @Override
            public void onGrade(String grade) {
                p_tvnian.setText(grade);
                perLoad(0,"",0,0,"","",grade);
            }

            @Override
            public void onCons(String cons) {

            }
        });
    }

    //星座
    private List<ConsModel.ConsBean> consList=new ArrayList<>();
    private void showCons(){
        ConsGradePop consPop=new ConsGradePop(this);
        consPop.onCons(consList)
                .showPopupWindow();
        consPop.setOnPickerListenerChange(new ConsGradePop.OnPickerListenerChange() {
            @Override
            public void onGrade(String grade) {

            }

            @Override
            public void onCons(String cons) {
                p_tvxing.setText(cons);
                perLoad(0,"",0,0,"",cons,"");
            }
        });
    }

    //家乡选择
    private List<CityBean.CitysBean> citys=new ArrayList<>();
    private void showTown(){

        MajorHomePop homePop=new MajorHomePop(this);
        homePop.onHomeData(citys).showPopupWindow();
        homePop.setOnMajorHomeListener(new MajorHomePop.OnMajorHomeListener() {
            @Override
            public void onMajorSelect(String departName, String majorName, int departId, int majorId) {

            }

            @Override
            public void onHomeSelect(String proName, String cityName) {
                p_tvjia.setText(cityName);
                perLoad(0,"",0,0,cityName,"","");
            }
        });
    }

    //院系选择
    private void showDepart(){
        MajorHomePop majorPop=new MajorHomePop(this);
        majorPop.onMajorData(departList).showPopupWindow();
        majorPop.setOnMajorHomeListener(new MajorHomePop.OnMajorHomeListener() {
            @Override
            public void onMajorSelect(String departName, String majorName, int departId, int majorId) {
                p_tvzhuanye.setText(majorName);
                perLoad(0,"",departId,majorId,"","","");
            }

            @Override
            public void onHomeSelect(String proName, String cityName) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case ME_HDIV://头像
                    for (int i=0;i<Matisse.obtainPathResult(data).size();i++){
                        String path=Matisse.obtainPathResult(data).get(i);
                        XLGlideLoader.loadCircleImage(p_ivhead,path);
                        final File files=new File(path);//上传文件的目录
                        final String imgRand= RandomUtils.getRandName(personalDta.getUserDO().getId(),5)+".jpg";
                        new Thread(() -> BosFactory.getRequest(client, CodeConfig.BOS_HEAD + imgRand, files, (request, currentSize, totalSize) -> {
                            if (currentSize==totalSize){
                                perLoad(ME_HDIV,imgRand,0,0,"","","");
                            }
                        })).start();
                    }
                    break;
                case ME_BGIV://封面
                    for (int i=0;i<Matisse.obtainPathResult(data).size();i++){
                        String path=Matisse.obtainPathResult(data).get(i);
                        final File files=new File(path);//上传文件的目录
                        final String imgRand= RandomUtils.getRandName(personalDta.getUserDO().getId(),5)+".jpg";
                        new Thread(() -> BosFactory.getRequest(client, CodeConfig.BOS_BG + imgRand, files, (request, currentSize, totalSize) -> {
                            if (currentSize==totalSize){
                                perLoad(ME_BGIV,imgRand,0,0,"","","");
                            }
                        })).start();
                    }
                    break;
                case 1:
                    p_tvnick.setText(data.getExtras().getString("infos"));
                    break;
                case 6:
                    p_tvqing.setText(data.getExtras().getString("infos"));
                    break;
                case 8:
                    p_tvxq.setText(data.getExtras().getString("infos"));
                    break;
                case 9:
                    p_tvqm.setText(data.getExtras().getString("infos"));
                    break;
            }
        }
    }

    private void perLoad(int code,String file,int departid,int majorid,String home,String consN,String gradeSt){
        ArrayMap<String,String> arrayMap=XLNetHttps.getBaseMap(this);
        arrayMap.put("id",String.valueOf(personalDta.getUserDO().getId()));
        if (!TextUtils.isEmpty(file)){
            if (code==ME_BGIV){
                arrayMap.put("backgroundImage",file);
            }else {
                arrayMap.put("headImage",file);
            }
        }
        if (departid!=0 && majorid!=0){
            arrayMap.put("departId",String.valueOf(departid));
            arrayMap.put("majorId",String.valueOf(majorid));
        }
        if (!TextUtils.isEmpty(home)){
            arrayMap.put("hometown",home);
        }
        if (!TextUtils.isEmpty(consN)){
            arrayMap.put("constel",consN);
        }
        if (!TextUtils.isEmpty(gradeSt)){
            arrayMap.put("enrYear",gradeSt);
        }
        XLNetHttps.request(ApiConfig.API_UPDATE_USER, arrayMap, LoginVO.class, new XLCallBack() {
            @Override
            public void XLSucc(String result) {
                LoginVO loginV= XLGson.fromJosn(result,LoginVO.class);
                if (loginV.getCode()==0){
                    if (code==ME_BGIV){
                        XLToast.showToast("封面更换成功!");
                    }
                    EventUtils.onPost(new XMessageEvent(CHANGE_CODE));
                    loginDao.clearUserTable();
                    loginDao.saveObject(loginV.getData());
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
