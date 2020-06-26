package com.doschool.ahu.appui.main.ui.bean;

import java.util.ArrayList;
import java.util.List;

import static com.doschool.ahu.configfile.CodeConfig.REPORT_GGSR;
import static com.doschool.ahu.configfile.CodeConfig.REPORT_QZPQ;
import static com.doschool.ahu.configfile.CodeConfig.REPORT_SQDS;
import static com.doschool.ahu.configfile.CodeConfig.REPORT_WF;
import static com.doschool.ahu.configfile.CodeConfig.REPORT_YYCB;
import static com.doschool.ahu.configfile.CodeConfig.REPORT_ZZMG;

/**
 * Created by X on 2018/10/15
 */
public class ReportDo {

    private int id;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public List<ReportDo> getReportList(){
        List<ReportDo> list=new ArrayList<>();
        ReportDo rd1=new ReportDo();
        rd1.setId(REPORT_SQDS);
        rd1.setType("色情低俗");
        list.add(rd1);

        ReportDo rd2=new ReportDo();
        rd2.setId(REPORT_GGSR);
        rd2.setType("广告骚扰");
        list.add(rd2);

        ReportDo rd3=new ReportDo();
        rd3.setId(REPORT_ZZMG);
        rd3.setType("政治敏感");
        list.add(rd3);

        ReportDo rd4=new ReportDo();
        rd4.setId(REPORT_YYCB);
        rd4.setType("谣言传播");
        list.add(rd4);

        ReportDo rd5=new ReportDo();
        rd5.setId(REPORT_QZPQ);
        rd5.setType("欺诈骗钱");
        list.add(rd5);

        ReportDo rd6=new ReportDo();
        rd6.setId(REPORT_WF);
        rd6.setType("违法（暴力恐怖、违禁品等）");
        list.add(rd6);

        if (list!=null){
            return list;
        }else {
            return null;
        }
    }
}
