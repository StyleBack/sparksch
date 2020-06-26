package com.doschool.ahu.widget.nine;

import android.graphics.Point;

import java.io.Serializable;

/**
 * Created by X on 2018/9/5
 *
 * 图片源
 */
public class ImageData implements Serializable {
    private static final long serialVersionUID = -5242482266908222476L;

    public String url;
    public String text;

    public int realWidth;
    public int realHeight;

    public int startX;
    public int startY;
    public int width;
    public int height;

    public ImageData(String url) {
        this.url = url;
    }

    public ImageData from(ImageData imageData, LayoutHelper layoutHelper, int position) {
        if (imageData != null && layoutHelper != null) {
            Point coordinate = layoutHelper.getCoordinate(position);
            if (coordinate != null) {
                imageData.startX = coordinate.x;
                imageData.startY = coordinate.y;
            }

            Point size = layoutHelper.getSize(position);
            if (size != null) {
                imageData.width = size.x;
                imageData.height = size.y;
            }
        }
        return imageData;
    }

}
