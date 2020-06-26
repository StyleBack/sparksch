package com.doschool.ahu.appui.main.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.doschool.ahu.R;
import com.doschool.ahu.utils.XLGlideLoader;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by X on 2018/10/24
 */
public class BoxAdapter extends RecyclerView.Adapter<BoxAdapter.BoxHolder> {

    private Context context;
    private List<String> list;

    public BoxAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BoxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_rvbox,parent,false);
        return new BoxHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull BoxHolder holder, int position) {
        if (list.size()==2){
            XLGlideLoader.loadCornerImage(holder.box_iv,list.get(position),5);
            holder.box_delete.setVisibility(View.VISIBLE);
        }else {
            if (list.size()!=0 && position==list.size()-1){
                XLGlideLoader.loadCornerImage(holder.box_iv,list.get(position),5);
                holder.box_delete.setVisibility(View.VISIBLE);
            }else {
                XLGlideLoader.loadCornerImage(holder.box_iv,context.getResources().getDrawable(R.mipmap.add_box_icon),5);
                holder.box_delete.setVisibility(View.GONE);
            }
        }

        RxView.clicks(holder.box_iv)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> {
                    if (list.size()==0 || list.size()==1){
                        if (position==list.size()){
                            if (onBoxListener!=null){
                                onBoxListener.addImg();
                            }
                        }
                    }
                });

        holder.box_delete.setOnClickListener(v -> {
            if (onBoxListener!=null){
                onBoxListener.delet(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?1:(list.size()==2?2:list.size()+1);
    }

    public static class BoxHolder extends RecyclerView.ViewHolder{

        ImageView box_iv;
        ImageView box_delete;
        public BoxHolder(View itemView) {
            super(itemView);
            box_iv=itemView.findViewById(R.id.box_iv);
            box_delete=itemView.findViewById(R.id.box_delete);
        }
    }

    private OnBoxListener onBoxListener;
    public void setOnBoxListener(OnBoxListener onBoxListener) {
        this.onBoxListener = onBoxListener;
    }
    public interface OnBoxListener{
        void addImg();
        void delet(int position);
    }
}
