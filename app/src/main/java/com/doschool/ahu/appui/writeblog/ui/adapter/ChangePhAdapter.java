package com.doschool.ahu.appui.writeblog.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.writeblog.event.OnItemListener;
import com.doschool.ahu.appui.writeblog.ui.bean.PhotoBean;
import com.doschool.ahu.utils.XLGlideLoader;
import com.doschool.ahu.widget.XImageViewRoundOval;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.doschool.ahu.configfile.CodeConfig.ADDBLOG_IV;
import static com.doschool.ahu.configfile.CodeConfig.ADDBLOG_VD;

/**
 * Created by X on 2018/11/8
 */
public class ChangePhAdapter extends RecyclerView.Adapter<ChangePhAdapter.ChangeHolder> {

    private Context context;
    private List<PhotoBean> list;

    public ChangePhAdapter(Context context, List<PhotoBean> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChangeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_addph_layout,parent,false);
        return new ChangeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChangeHolder holder, int position) {

        if (list.size()==9){//此情况只有有图片
            holder.ai_virl.setVisibility(GONE);
            holder.aii_rlp.setVisibility(VISIBLE);
            holder.aii_idel.setVisibility(VISIBLE);
            //设置类型
            holder.aii_ivh.setType(XImageViewRoundOval.TYPE_ROUND);
            XLGlideLoader.loadImageByUrl(holder.aii_ivh,list.get(position).getPath());
        }else {
            if (list.size()==1){//此情况判断视频or图片
                if (list.get(0).getType()==ADDBLOG_IV) {//图片
                    holder.ai_virl.setVisibility(GONE);
                    holder.aii_rlp.setVisibility(VISIBLE);
                    //设置类型
                    holder.aii_ivh.setType(XImageViewRoundOval.TYPE_ROUND);
                    if (position==list.size()){
                        holder.aii_idel.setVisibility(GONE);
                        XLGlideLoader.loadImageById(holder.aii_ivh,R.mipmap.add_box_icon);

                        holder.aii_ivh.setOnClickListener(v -> {//添加图片
                            if (onItemListener!=null){
                                onItemListener.onAdd();
                            }
                        });
                    }else {
                        holder.aii_idel.setVisibility(VISIBLE);
                        XLGlideLoader.loadImageByUrl(holder.aii_ivh,list.get(position).getPath());
                    }
                }else if (list.get(0).getType()==ADDBLOG_VD){//视频
                    holder.aii_rlp.setVisibility(GONE);
                    holder.ai_virl.setVisibility(VISIBLE);

                    holder.ai_vihp.setType(XImageViewRoundOval.TYPE_ROUND);
                    XLGlideLoader.loadImageByUrl(holder.ai_vihp,list.get(position).getPath());

                    //删除视频
                    holder.ai_viel.setOnClickListener(v -> {
                        if (onItemListener!=null){
                            onItemListener.onDelete(position);
                        }
                    });
                }
            }else {//只有图片
                holder.ai_virl.setVisibility(GONE);
                holder.aii_rlp.setVisibility(VISIBLE);
                //设置类型
                holder.aii_ivh.setType(XImageViewRoundOval.TYPE_ROUND);
                if (position==list.size()){
                    holder.aii_idel.setVisibility(GONE);
                    XLGlideLoader.loadImageById(holder.aii_ivh,R.mipmap.add_box_icon);
                    holder.aii_ivh.setOnClickListener(v -> {//添加图片
                        if (onItemListener!=null){
                            onItemListener.onAdd();
                        }
                    });
                }else {
                    holder.aii_idel.setVisibility(VISIBLE);
                    XLGlideLoader.loadImageByUrl(holder.aii_ivh,list.get(position).getPath());
                }
            }
        }

            //删除图片
            holder.aii_idel.setOnClickListener(v -> {
                if (onItemListener!=null){
                    onItemListener.onDelete(position);
                }
            });

    }

    @Override
    public int getItemCount() {
        if (list!=null){
            if (list.size()>0){
                if (list.get(0).getType()==ADDBLOG_IV){
                    return list.size()==9?9:list.size()+1;
                }else if (list.get(0).getType()==ADDBLOG_VD){
                    return list.size();
                }
            }
            return 0;
        }
        return 0;
    }

    public static class ChangeHolder extends RecyclerView.ViewHolder{
         RelativeLayout aii_rlp,ai_virl;
         ImageView aii_idel,ai_viel;
         XImageViewRoundOval aii_ivh,ai_vihp;

        public ChangeHolder(View itemView) {
            super(itemView);
            aii_rlp=itemView.findViewById(R.id.aii_rlp);
            ai_virl=itemView.findViewById(R.id.ai_virl);
            aii_ivh=itemView.findViewById(R.id.aii_ivh);
            aii_idel=itemView.findViewById(R.id.aii_idel);
            ai_vihp=itemView.findViewById(R.id.ai_vihp);
            ai_viel=itemView.findViewById(R.id.ai_viel);
        }
    }

    private OnItemListener onItemListener;

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

}
