package com.doschool.ahu.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.blankj.utilcode.util.ConvertUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by X on 2018/8/31
 */
public class AssetsUtils {


    //转string
    public static SpannableString getSpanToString(Context context,int name){
        SpannableString str=null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open(String.format("defaultexpressionid/%d.png", name));
            Bitmap bitmap = BitmapFactory.decodeStream(is);

            Drawable drawable = new BitmapDrawable(context.getResources(),bitmap);
            drawable.setBounds(0, 0, ConvertUtils.sp2px((float) (14 * 1.1)), ConvertUtils.sp2px((float) (14 * 1.1)));
            ImageSpan imageSpan = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);

//            Matrix matrix = new Matrix();
//            int width = bitmap.getWidth();
//            int height = bitmap.getHeight();
//            matrix.postScale(1.2f, 1.2f);
//            final Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
//                    width/2, height/2, matrix, true);
            String content = String.valueOf(name);
            str = new SpannableString(String.valueOf(name));
//            ImageSpan span = new ImageSpan(context, resizedBitmap, ImageSpan.ALIGN_BASELINE);
            str.setSpan(imageSpan, 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    //读取assets的图片
    public static Bitmap getBitmap(Context context,String name){
        Bitmap bitmap = null;
        InputStream is=null;
        AssetManager am = context.getResources().getAssets();
        try {
            is = am.open(String.format("defaultexpressionid/%s.png",name));

            bitmap = BitmapFactory.decodeStream(is);
            Matrix matrix = new Matrix();
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            matrix.postScale(1.5f, 1.5f);
            final Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                    width, height, matrix, true);
            return  resizedBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //重定义simle
    public static ImageSpan getKeySpan(Context context,int name){
        ImageSpan imageSpan=null;
        try {
            AssetManager am = context.getAssets();
            InputStream is = am.open(String.format("defaultexpressionid/%d.png", name));
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            Drawable drawable = new BitmapDrawable(context.getResources(),bitmap);
            imageSpan= new EqualHeightSpan(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageSpan;
    }
}
