package com.doschool.ahu.appui.writeblog.event;

/**
 * Created by sunha on 2018/1/23 0023.
 */

public interface Measurable {

  int getWidth();

  int getHeight();

  boolean canMeasure();

  boolean needResize();
}
