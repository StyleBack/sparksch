package com.doschool.ahu.appui.main.ui.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by X on 2018/8/30
 */
public class CvsRedBean implements Parcelable {

    private int unRed;

    public int getUnRed() {
        return unRed;
    }

    public void setUnRed(int unRed) {
        this.unRed = unRed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.unRed);
    }

    public CvsRedBean() {
    }

    protected CvsRedBean(Parcel in) {
        this.unRed = in.readInt();
    }

    public static final Creator<CvsRedBean> CREATOR = new Creator<CvsRedBean>() {
        @Override
        public CvsRedBean createFromParcel(Parcel source) {
            return new CvsRedBean(source);
        }

        @Override
        public CvsRedBean[] newArray(int size) {
            return new CvsRedBean[size];
        }
    };
}
