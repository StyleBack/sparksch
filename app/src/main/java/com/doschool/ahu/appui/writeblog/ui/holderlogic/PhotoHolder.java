package com.doschool.ahu.appui.writeblog.ui.holderlogic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.writeblog.ui.bean.PhotoBean;
import com.doschool.ahu.base.adapter.BaseRvHolder;
import com.doschool.ahu.utils.MediaFileUtil;
import com.doschool.ahu.utils.XLGlideLoader;

/**
 * Created by X on 2018/9/19
 */
public class PhotoHolder extends BaseRvHolder {

    public ImageView pil_iv,pil_delete,pil_play;

    public PhotoHolder(View itemView) {
        super(itemView);
        pil_iv=findViewById(R.id.pil_iv);
        pil_delete=findViewById(R.id.pil_delete);
        pil_play=findViewById(R.id.pil_play);
    }

    public static PhotoHolder newInstance(ViewGroup parent,int layout){
        View view= LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        return new PhotoHolder(view);
    }

    public void setPho(Context context, PhotoBean photoBean){
        XLGlideLoader.loadImageByUrl(pil_iv,photoBean.getPath());
        if (MediaFileUtil.isVideoFileType(photoBean.getPath())){
            pil_play.setVisibility(View.VISIBLE);
            pil_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }else {
            pil_play.setVisibility(View.GONE);
        }
    }
}
