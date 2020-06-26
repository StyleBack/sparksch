package com.doschool.ahu.appui.home.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.doschool.ahu.R;
import com.doschool.ahu.appui.home.ui.activity.BlogDetailActivity;
import com.doschool.ahu.appui.home.ui.activity.OtherSingleActivity;
import com.doschool.ahu.appui.main.ui.bean.AppBannerDo;
import com.doschool.ahu.appui.main.ui.bean.MicroblogVO;
import com.doschool.ahu.base.model.BaseModel;
import com.doschool.ahu.db.LoginDao;
import com.doschool.ahu.factory.AppDoUrlFactory;
import com.doschool.ahu.utils.IntentUtil;
import com.doschool.ahu.utils.StringUtil;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.widget.GlideApp;
import com.doschool.ahu.widget.XImageViewRoundOval;
import com.doschool.ahu.widget.component.SuperText;
import com.doschool.ahu.xlhttps.XLCallBack;
import com.doschool.ahu.xlhttps.XLNetHttps;

import org.xutils.common.Callback;

import static com.doschool.ahu.configfile.ApiConfig.API_CLICK_BANNER;


/**
 * Created by X on 2018/10/17
 */
public class BannerItem extends FrameLayout {

    private XImageViewRoundOval ivBackground;
    private RelativeLayout hotLayout;
    private ImageView ivHead;
    private TextView tvName;
    private TextView tvContent;
    private RelativeLayout fastbarLayout;
    private EditText fastEdit;
    private Button fastButton;
    private FakeEditTextWithIcon topicbarLayout;


    public BannerItem(Context context) {
        this(context,null);
    }

    public BannerItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.banner_item_lay, this);

        ivBackground = findViewById(R.id.ivBackground);

        hotLayout = (RelativeLayout) findViewById(R.id.hotLayout);
        ivHead = (ImageView) findViewById(R.id.ivHead);
        tvName = (TextView) findViewById(R.id.tvName);
        tvContent = (TextView) findViewById(R.id.tvContent);

        fastbarLayout = (RelativeLayout) findViewById(R.id.fastbarLayout);
        fastEdit = (EditText) findViewById(R.id.fastEdit);
        fastButton = (Button) findViewById(R.id.fastButton);

        topicbarLayout = (FakeEditTextWithIcon) findViewById(R.id.topicbarLayout);

    }


    public void update(final AppBannerDo.BannerData banner) {
        try {
            //设置背景图片
            ivBackground.setType(XImageViewRoundOval.TYPE_ROUND);
            ivBackground.setRoundRadius(20);
            GlideApp.with(getContext())
                    .load(banner.getImage())
                    .error(R.mipmap.default_gray)
                    .into(ivBackground);
            if (banner.getType() == AppBannerDo.BannerData.TYPE_TOPIC) {
//                topicbarLayout.setVisibility(VISIBLE);
//                topicbarLayout.updateUI(banner);
//                if (banner.getTopicName().length() > 0){
//                    topicbarLayout.setHint(banner.getTopicName());
//                }

            } else if (banner.getType() == AppBannerDo.BannerData.TYPE_HOT) {
                hotLayout.setVisibility(VISIBLE);
                MicroblogVO.DataBean blog = banner.getData();
                //头像
                XLGlideLoader.loadCircleImage(ivHead,blog.getUserVO().getHeadImage());
                ivHead.setOnClickListener(v -> {
                    if (new LoginDao(getContext()).getObject().getUserDO().getId()!=blog.getUserVO().getUserId()){
                        Bundle bundle=new Bundle();
                        bundle.putInt("userid",blog.getUserVO().getUserId());
                        IntentUtil.toActivity(getContext(),bundle,OtherSingleActivity.class);
                    }
                });

                //姓名
                tvName.setText(blog.getUserVO().getNickName());
                tvName.setOnClickListener(v -> {
                    if (new LoginDao(getContext()).getObject().getUserDO().getId()!=blog.getUserVO().getUserId()){
                        Bundle bundle=new Bundle();
                        bundle.putInt("userid",blog.getUserVO().getUserId());
                        IntentUtil.toActivity(getContext(),bundle,OtherSingleActivity.class);
                    }
                });


                // 内容
                SpannableString spannableString= StringUtil.stringToSpannableString(blog.getMicroblogMainDO().getContent(), getContext(), 16);
                tvContent.setText(spannableString);
                SuperText.txtlink(getContext(),tvContent);
                tvContent.setOnClickListener(v -> {
                    Bundle bundle=new Bundle();
                    bundle.putInt("blogId",blog.getMicroblogMainDO().getId());
                    bundle.putString("blogTag","unRecom");
                    IntentUtil.toActivity(getContext(),bundle, BlogDetailActivity.class);
                });

            } else {
//                if (!TextUtils.isEmpty(banner.getFastbarDourl())){
//                    DoUrlModel model= XLGson.fromJosn(banner.getFastbarDourl(),DoUrlModel.class);
//                    if (!TextUtils.isEmpty(model.getAction())){
//                        //设置跳转
//                        fastbarLayout.setVisibility(VISIBLE);
//
//                        //设置假输入框
//                        if (banner.getTopicName().length() > 0) {
//                            fastEdit.setHint(banner.getTopicName());
//                        }
//
//                    }
//                }

            }

            ivBackground.setOnClickListener(v -> {
                clickBanner(banner.getId());
                AppDoUrlFactory.gotoAway(getContext(),
                        banner.getImageDourl(),
                        String.valueOf(banner.getTopicId()),
                        banner.getBannerName());
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //banner点击记录
    private void clickBanner(int id){
        ArrayMap<String,String> ban=XLNetHttps.getBaseMap(getContext());
        ban.put("bannerId",String.valueOf(id));
        XLNetHttps.request(API_CLICK_BANNER, ban, BaseModel.class, new XLCallBack() {
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
}
