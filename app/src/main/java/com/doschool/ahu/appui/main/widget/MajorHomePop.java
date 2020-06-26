package com.doschool.ahu.appui.main.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.CityBean;
import com.doschool.ahu.appui.main.ui.bean.DepartBean;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import razerdp.basepopup.BasePopupWindow;

import static android.view.Gravity.BOTTOM;
import static com.doschool.ahu.configfile.CodeConfig.ME_HDIV;

/**
 * Created by X on 2019/2/13.
 *
 * 从底部弹出
 */
public class MajorHomePop extends BasePopupWindow {


    private TextView win_ht_cancle,win_ht_ok;
    private WheelPicker mh_picker_fr,mh_picker_sc;
    private int pickfrPos,picktoPos;
    private int departId,majorId;
    private String departName,majorName;
    private String proName,cityName;
    public MajorHomePop(Context context) {
        super(context);
        setPopupGravity(BOTTOM);
        byViewId();
    }

    //院系
    public MajorHomePop onMajorData(List<DepartBean.PartDta> departList){
        setMajor(departList);
        return this;
    }

    //城市
    public MajorHomePop onHomeData(List<CityBean.CitysBean> citys){
        setHomeContent(citys);
        return this;
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.major_home_pop);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f,0,300);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0,1f,300);
    }

    private void byViewId(){
        win_ht_cancle=findViewById(R.id.win_ht_cancle);
        win_ht_ok=findViewById(R.id.win_ht_ok);
        mh_picker_fr=findViewById(R.id.mh_picker_fr);
        mh_picker_sc=findViewById(R.id.mh_picker_sc);

        win_ht_cancle.setOnClickListener(view -> dismiss());
    }

    @SuppressLint("CheckResult")
    private void setMajor(List<DepartBean.PartDta> departList){
        //院
        mh_picker_fr.setData(departList);
        pickfrPos=mh_picker_fr.getSelectedItemPosition();
        //系
        mh_picker_sc.setData(departList.get(0).getMajorDOS());
        picktoPos=mh_picker_sc.getSelectedItemPosition();

        //院
        mh_picker_fr.setOnItemSelectedListener((picker, data, position) -> {
            pickfrPos=position;
            DepartBean.PartDta pdt=departList.get(position);
            mh_picker_sc.setData(pdt.getMajorDOS());
            mh_picker_sc.setSelectedItemPosition(0);
            picktoPos=mh_picker_sc.getSelectedItemPosition();
        });
        //系
        mh_picker_sc.setOnItemSelectedListener((picker, data, position) -> {
            picktoPos=position;
        });

        RxView.clicks(win_ht_ok)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    if (onMajorHomeListener!=null){
                        departId=departList.get(pickfrPos).getId();
                        departName=departList.get(pickfrPos).getDepartAbbr();

                        majorId=departList.get(pickfrPos).getMajorDOS().get(picktoPos).getId();
                        majorName=departList.get(pickfrPos).getMajorDOS().get(picktoPos).getMajorName();

                        onMajorHomeListener.onMajorSelect(departName,majorName,departId,majorId);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void setHomeContent(List<CityBean.CitysBean> citys){
        mh_picker_fr.setData(citys);
        pickfrPos=mh_picker_fr.getSelectedItemPosition();

        mh_picker_sc.setData(citys.get(0).getCityArr());
        picktoPos=mh_picker_sc.getSelectedItemPosition();

        mh_picker_fr.setOnItemSelectedListener((picker, data, position) -> {
            pickfrPos=position;
            CityBean.CitysBean citysBean=citys.get(position);
            mh_picker_sc.setData(citysBean.getCityArr());
            mh_picker_sc.setSelectedItemPosition(0);
            picktoPos=mh_picker_sc.getSelectedItemPosition();
        });

        mh_picker_sc.setOnItemSelectedListener((picker, data, position) -> {
            picktoPos=position;
        });

        RxView.clicks(win_ht_ok)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    if (onMajorHomeListener!=null){
                        proName=citys.get(pickfrPos).getName();
                        cityName=citys.get(pickfrPos).getCityArr().get(picktoPos).getName();
                        onMajorHomeListener.onHomeSelect(proName,cityName);
                    }
                });
    }

    private OnMajorHomeListener onMajorHomeListener;

    public void setOnMajorHomeListener(OnMajorHomeListener onMajorHomeListener) {
        this.onMajorHomeListener = onMajorHomeListener;
    }

    public interface OnMajorHomeListener{
        void onMajorSelect(String departName,String majorName,int departId,int majorId);
        void onHomeSelect(String proName,String cityName);
    }

}
