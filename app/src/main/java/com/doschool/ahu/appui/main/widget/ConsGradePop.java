package com.doschool.ahu.appui.main.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.ConsModel;
import com.doschool.ahu.appui.main.ui.bean.DepartBean;
import com.doschool.ahu.appui.main.ui.bean.MajorBean;
import com.doschool.ahu.appui.main.ui.bean.ReportDo;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import razerdp.basepopup.BasePopupWindow;

import static android.view.Gravity.BOTTOM;

/**
 * Created by X on 2019/2/13.
 *
 * 从底部弹出
 */
public class ConsGradePop extends BasePopupWindow {


    private TextView win_ht_cancle,win_ht_ok;
    private WheelPicker yc_pop_wheel;
    private int pickerPos;
    private String prtName,majName;
    private int prtId,majId;
    public ConsGradePop(Context context) {
        super(context);
        setPopupGravity(BOTTOM);
        byViewId();
    }

    //年级
    public ConsGradePop onGrade(List<String> gradeList){
        setGradeContent(gradeList);
        return this;
    }
    //星座
    public ConsGradePop onCons(List<ConsModel.ConsBean> consList){
        setConsContent(consList);
        return this;
    }

    //举报类型
    public ConsGradePop onReport(List<ReportDo> list){
        setReport(list);
        return this;
    }

    //院
    public ConsGradePop onDepartData(List<DepartBean.PartDta> departList){
        setDepart(departList);
        return this;
    }
    //系
    public ConsGradePop onMajorData(List<MajorBean> majorList){
        setMajor(majorList);
        return this;
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.year_cons_pop2);
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
        yc_pop_wheel=findViewById(R.id.yc_pop_wheel);

        win_ht_cancle.setOnClickListener(view -> dismiss());
    }

    @SuppressLint("CheckResult")
    private void setGradeContent(List<String> gradeList){
        yc_pop_wheel.setData(gradeList);
        pickerPos=yc_pop_wheel.getCurrentItemPosition();
        yc_pop_wheel.setOnItemSelectedListener((picker, data, position) -> pickerPos=position);
        RxView.clicks(win_ht_ok)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    if (onPickerListenerChange!=null){
                        onPickerListenerChange.onGrade(gradeList.get(pickerPos));
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void setConsContent(List<ConsModel.ConsBean> consList){
        yc_pop_wheel.setData(consList);
        pickerPos=yc_pop_wheel.getCurrentItemPosition();
        yc_pop_wheel.setOnItemSelectedListener((picker, data, position) -> pickerPos=position);
        RxView.clicks(win_ht_ok)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    if (onPickerListenerChange!=null){
                        onPickerListenerChange.onCons(consList.get(pickerPos).getName());
                    }
                });
    }
    private OnPickerListenerChange onPickerListenerChange;

    public void setOnPickerListenerChange(OnPickerListenerChange onPickerListenerChange) {
        this.onPickerListenerChange = onPickerListenerChange;
    }

    public interface OnPickerListenerChange{
        void onGrade(String grade);
        void onCons(String cons);
    }

    @SuppressLint("CheckResult")
    private void setReport(List<ReportDo> list){
        yc_pop_wheel.setData(list);
        pickerPos=yc_pop_wheel.getSelectedItemPosition();
        yc_pop_wheel.setOnItemSelectedListener((picker, data, position) -> pickerPos=position);
        RxView.clicks(win_ht_ok)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    if (onReportListener!=null){
                        onReportListener.onReport(list.get(pickerPos).getType(),list.get(pickerPos).getId());
                    }
                });
    }

    private OnReportListener onReportListener;

    public void setOnReportListener(OnReportListener onReportListener) {
        this.onReportListener = onReportListener;
    }

    public interface OnReportListener{
        void onReport(String type,int id);
    }

    @SuppressLint("CheckResult")
    private void setDepart(List<DepartBean.PartDta> departList){
        yc_pop_wheel.setData(departList);
        pickerPos=yc_pop_wheel.getCurrentItemPosition();
        yc_pop_wheel.setOnItemSelectedListener((picker, data, position) -> pickerPos=position);
        RxView.clicks(win_ht_ok)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    if (onDepartMajorListener!=null){
                        prtName=departList.get(pickerPos).getDepartAbbr();
                        prtId=departList.get(pickerPos).getId();
                        onDepartMajorListener.onDepart(prtName,prtId,pickerPos);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void setMajor(List<MajorBean> majorList){
        yc_pop_wheel.setData(majorList);
        pickerPos=yc_pop_wheel.getCurrentItemPosition();
        yc_pop_wheel.setOnItemSelectedListener((picker, data, position) -> pickerPos=position);
        RxView.clicks(win_ht_ok)
                .throttleFirst(1,TimeUnit.SECONDS)
                .subscribe(o -> {
                    dismiss();
                    if (onDepartMajorListener!=null){
                        prtName=majorList.get(pickerPos).getMajorName();
                        prtId=majorList.get(pickerPos).getId();
                        onDepartMajorListener.onMajor(prtName,prtId);
                    }
                });
    }

    private OnDepartMajorListener onDepartMajorListener;

    public void setOnDepartMajorListener(OnDepartMajorListener onDepartMajorListener) {
        this.onDepartMajorListener = onDepartMajorListener;
    }

    public interface OnDepartMajorListener{
        void onDepart(String ptName,int ptId,int position);
        void onMajor(String mjName,int mjId);
    }

}
