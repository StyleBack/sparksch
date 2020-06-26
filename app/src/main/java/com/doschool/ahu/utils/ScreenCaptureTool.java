package com.doschool.ahu.utils;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;

import java.io.File;
import java.io.FileOutputStream;

import static com.doschool.ahu.configfile.AppConfig.SAVE_SCREEN_IMG;

/**
 * Created by X on 2019/1/23.
 *
 * 截屏工具类
 */
public class ScreenCaptureTool {

    private ScreenCaptureTool() {}


    public static Bitmap screenCapture(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        final Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.destroyDrawingCache(); //销毁下次重新
        return bitmap;
    }

    public static Bitmap cutScreenCapture(View view, int cut) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        final Bitmap cacheBitmap = view.getDrawingCache();
        final int width = cacheBitmap.getWidth();
        final int height =  cacheBitmap.getHeight();
        final Bitmap bitmap = Bitmap.createBitmap(cacheBitmap, 0, 0, width, height - cut);
        view.destroyDrawingCache(); //销毁下次重新
        return bitmap;
    }

    public static Bitmap cutScreenCapture(View view) {
        final int cut = ConvertUtils.dp2px(50);
        return cutScreenCapture(view, cut);
    }

    public static void saveBitmap(final Activity context, final Bitmap bitmap) {
        if (bitmap != null) {
            new Thread(() -> {
                try {
                    // 图片文件路径
                    String filePath = MediaFileUtil.saveImg(SAVE_SCREEN_IMG );
                    String path=filePath+ RandomUtils.getRandName(5)+".png";
                    File file = new File(path);
                    FileOutputStream os = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                    os.flush();
                    os.close();
                    galleryAddPic(context, file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void galleryAddPic(final Activity context, File file) {

        //通知图库更新
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//如果是4.4及以上版本
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file); //out is your output file
            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);
        } else {
            context.sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        }
        XLToast.showToast("图片已保存");
    }

    //scroll
    public static Bitmap getScrollViewBitmap(NestedScrollView view) {
        int height = 0;
        for (int i = 0; i < view.getChildCount(); i++) {
            height += view.getChildAt(i).getHeight();
        }
        // 创建对应大小的bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    //bitmap换背景色
    public static Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap) {
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(orginBitmap.getWidth(),
                orginBitmap.getHeight(), orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, orginBitmap.getWidth(), orginBitmap.getHeight(), paint);
        canvas.drawBitmap(orginBitmap, 0, 0, paint);
        return bitmap;
    }
}
