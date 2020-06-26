package com.doschool.ahu.factory;

import android.content.Context;
import android.os.Bundle;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;


import com.blankj.utilcode.util.SPUtils;
import com.doschool.ahu.appui.discover.ui.activity.HeadLineActivity;
import com.doschool.ahu.appui.discover.ui.activity.HotBlogActivity;
import com.doschool.ahu.appui.discover.ui.activity.HotTopicListActivity;
import com.doschool.ahu.appui.home.ui.activity.BlogDetailActivity;
import com.doschool.ahu.appui.infors.ui.activity.PraiseMeActivity;
import com.doschool.ahu.appui.main.ui.activity.WebActivity;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.base.model.DoUrlModel;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.configfile.DoUrlConfig;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.utils.AlertUtils;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.XLGson;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;


import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;

import java.util.List;
import java.util.regex.Pattern;

import static com.doschool.ahu.configfile.ApiConfig.API_CLICK_SERVICE;
import static com.doschool.ahu.configfile.ApiConfig.API_PUSHOPEN;

/**
 * Created by X on 2018/9/27
 * dourl处理
 */
public class AppDoUrlFactory {

    //传来的dourl转object
    public static void gotoAway(Context context, String url, String id, String name) {
        DoUrlModel model = XLGson.fromJosn(url, DoUrlModel.class);
        gotoAway(context, model, id, name);
    }

    public static void jumpActivity(Context context, String json, String id, String name) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String action = jsonObject.getString("action");
            String uniqueKey="";
            if (jsonObject.has("uniqueKey")){
                uniqueKey=jsonObject.getString("uniqueKey");
            }
            JSONArray jsonArray = jsonObject.getJSONArray("paramList");
            DoUrlModel doUrlModel = new DoUrlModel();
            doUrlModel.setAction(action);
            List<String> list = new ArrayList<>();
            if (jsonArray!=null && jsonArray.length() != 0) {
                String titleName = "";
                String topicId = "";
                topicId = jsonArray.getString(0);
                list.add(topicId);
                if (jsonArray.length() == 2 && !jsonArray.isNull(1)) {
                    titleName = jsonArray.getString(1);
                    list.add(titleName);
                }
                doUrlModel.setParamList(list);
                gotoAway(context, doUrlModel, topicId, titleName);
            } else {
                gotoAway(context, doUrlModel, "", "");
            }
            if (!TextUtils.isEmpty(uniqueKey)){
                clickUM(context,uniqueKey);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void gotoAway(Context context, DoUrlModel doUrl, String id, String name) {

        if (TextUtils.equals(doUrl.getAction(), DoUrlConfig.ACTION_BLOG)) {//微博详情
            if (!TextUtils.isEmpty(id)){
                Bundle bundle = new Bundle();
                bundle.putInt("blogId", Integer.parseInt(id));
                bundle.putString("blogTag","unRecom");
                IntentUtil.toActivity(context, bundle, BlogDetailActivity.class);
            }else {
                if (doUrl.getParamList().size() > 0 && doUrl.getParamList() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("blogId", Integer.parseInt(doUrl.getParamList().get(0)));
                    bundle.putString("blogTag","unRecom");
                    IntentUtil.toActivity(context, bundle, BlogDetailActivity.class);
                }
            }
        } else if (TextUtils.equals(doUrl.getAction(), DoUrlConfig.ACTION_BLOG_ADD)) {//发布微博

        } else if (TextUtils.equals(doUrl.getAction(), DoUrlConfig.ACTION_TOPIC)) {//话题
            if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(name)){
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("name", name);
                IntentUtil.toActivity(context, bundle, HotTopicListActivity.class);
            }else {
                if (doUrl.getParamList().size() > 0 && doUrl.getParamList() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", doUrl.getParamList().get(0));
                    bundle.putString("name", doUrl.getParamList().get(1));
                    IntentUtil.toActivity(context, bundle, HotTopicListActivity.class);
                }
            }
        } else if (TextUtils.equals(doUrl.getAction(), DoUrlConfig.ACTION_HOT_BLOG)) {//热门微博
            IntentUtil.toActivity(context, null, HotBlogActivity.class);
        } else if (TextUtils.equals(doUrl.getAction(), DoUrlConfig.ACTION_WEB)) {//外部网页
            if (noneMember(context)) {
                AlertUtils.alertToVerify(context, new LoginDao(context).getObject().getHandleStatus());
            } else {
                if (!TextUtils.isEmpty(id) && isNumeric(id)){
                    clickService(context,Integer.parseInt(id));
                }
                Bundle bundle = new Bundle();
                if (doUrl.getParamList() != null && doUrl.getParamList().size() > 0) {
                    bundle.putString("URL", doUrl.getParamList().get(0));
                }
                IntentUtil.toActivity(context, bundle, WebActivity.class);
            }
        } else if (TextUtils.equals(doUrl.getAction(), DoUrlConfig.ACTION_HEADLINE)) {//头条
            if (noneMember(context)) {
                AlertUtils.alertToVerify(context, new LoginDao(context).getObject().getHandleStatus());
            } else {
                IntentUtil.toActivity(context, null, HeadLineActivity.class);
            }
        } else if (TextUtils.equals(doUrl.getAction(), DoUrlConfig.ACTION_COMMENT)) {//评论我的
            if (noneMember(context)) {
                AlertUtils.alertToVerify(context, new LoginDao(context).getObject().getHandleStatus());
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("type", CodeConfig.MSG_TYPE_COMMENT);
                IntentUtil.toActivity(context, bundle, PraiseMeActivity.class);
            }
        } else if (TextUtils.equals(doUrl.getAction(), DoUrlConfig.ACTION_LIKE)) {//赞我的
            if (noneMember(context)) {
                AlertUtils.alertToVerify(context, new LoginDao(context).getObject().getHandleStatus());
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("type", CodeConfig.MSG_TYPE_PRAISE);
                IntentUtil.toActivity(context, bundle, PraiseMeActivity.class);
            }
        }
    }

    //未验证用户
    public static boolean noneMember(Context context) {
        int type = SPUtils.getInstance().getInt("phtype");
        LoginDao loginDao = new LoginDao(context);
        if (type == -1 && loginDao.getObject() != null && loginDao.getObject().getUserDO().getStatus() == 0) {
            return true;
        }
        return false;
    }

    public static void clickService(Context context, int id) {
        ArrayMap<String, String> map = XLNetHttps.getBaseMap(context);
        map.put("toolId", String.valueOf(id));
        XLNetHttps.request(API_CLICK_SERVICE, map,  BaseModel.class, new XLCallBack() {
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

    public static void clickUM(Context context,String key){
        ArrayMap<String,String> keyMap=XLNetHttps.getBaseMap(context);
        keyMap.put("uniqueKey",key);
        XLNetHttps.request(API_PUSHOPEN, keyMap,BaseModel.class, new XLCallBack() {
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


    public static boolean isNumeric(String str){

        Pattern pattern = Pattern.compile("[0-9]*");

        return pattern.matcher(str).matches();

    }
}
