package com.doschool.ahu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.blankj.utilcode.util.ConvertUtils;
import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.Smile;
import com.doschool.ahu.base.BaseApplication;
import com.doschool.ahu.factory.JsonEmoj;


import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.doschool.ahu.base.BaseApplication.getContext;

/**
 * Created by X on 2018/9/14
 */
public class StringUtil {

    /**
     * 把String转成SpannableString
     */
    public static SpannableString stringToSpannableString(String str, final Context context, int textSizeSp) {
        List<Smile.SourceExpressionsBean> smileList = JsonEmoj.getJson(getContext()).getSourceExpressions();
        SpannableString spannableString = new SpannableString(str);
        String zhengze = "\\[[\\u4e00-\\u9fa5]{1,5}\\]";
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        Matcher matcher = sinaPatten.matcher(spannableString);

        while (matcher.find()) {
            String tid = null;
            String key = matcher.group();
            for (int i=0;i<smileList.size();i++){
                if (key.equals(smileList.get(i).getExpressionString())){
                    tid=smileList.get(i).getExpressionImageName();
                }
            }
            if (!TextUtils.isEmpty(tid)) {
                Bitmap bitmap=AssetsUtils.getBitmap(context,tid);
                Drawable drawable = new BitmapDrawable(context.getResources(),bitmap);
                drawable.setBounds(0, 0, ConvertUtils.sp2px((float) (textSizeSp * 1.2)), ConvertUtils.sp2px((float) (textSizeSp * 1.2)));

                ImageSpan imageSpan = new ImageSpan(drawable);                //通过图片资源id来得到bitmap，用一个ImageSpan来包装
                int end = matcher.start() + key.length();                    //计算该图片名字的长度，也就是要替换的字符串的长度
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);    //将该图片替换字符串中规定的位置中
            }

        }

        return spannableString;
    }

    /**
     * 从资源文件拿到文字
     */
    public static String getString(@StringRes int strId, Object... objs) {
        if (strId == 0) return null;
        return BaseApplication.getContext().getResources().getString(strId, objs);
    }
}
