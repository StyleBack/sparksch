package com.doschool.ahu.appui.main.event;

import android.widget.ImageView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.bean.SingleUserVO;
import com.doschool.ahu.appui.main.ui.bean.UserVO;
import com.doschool.ahu.utils.XLGlideLoader;

/**
 * Created by X on 2018/9/6
 */
public class UserComm {

    public static void updateIdentify(ImageView imageView, UserVO userVO){
        if (userVO.isOR()){
            XLGlideLoader.loadImageById(imageView, R.mipmap.icon_identify_org_or);
        }else if (userVO.isTeacher()){
            XLGlideLoader.loadImageById(imageView, R.mipmap.icon_identify_teacher);
        }else if (userVO.isST()){
            if (userVO.isBoy()){
                XLGlideLoader.loadImageById(imageView, R.mipmap.icon_identity_boy);
            }else if (userVO.isGirl()){
                XLGlideLoader.loadImageById(imageView, R.mipmap.icon_identity_girl);
            }
        }
    }

    public static void updateIdentify(ImageView imageView, SingleUserVO userVO){
        if (userVO.isOR()){
            XLGlideLoader.loadImageById(imageView, R.mipmap.icon_identify_org_or);
        }else if (userVO.isTeacher()){
            XLGlideLoader.loadImageById(imageView, R.mipmap.icon_identify_teacher);
        }else if (userVO.isST()){
            if (userVO.isBoy()){
                XLGlideLoader.loadImageById(imageView, R.mipmap.icon_identity_boy);
            }else if (userVO.isGirl()){
                XLGlideLoader.loadImageById(imageView, R.mipmap.icon_identity_girl);
            }
        }
    }
}
