package com.doschool.ahu.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by X on 2018/8/23
 *
 * 实现BaseRvAdapter
 */
public abstract class BaseRvAdapter<D,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Context context;
    //数据源
    protected List<D> list;
    public BaseRvAdapter(Context context) {
        this.context = context;
        this.list= Collections.emptyList();
    }

    public List<D> getList() {
        return this.list;
    }

    public void setDatas(Collection<D> collections){
        this.validateCollection(collections);
        this.list= (List<D>) collections;
        this.notifyDataSetChanged();
    }

    public void addDatas(Collection<D> collections){
        this.validateCollection(collections);
        if (this.list!=null && this.list.size()!=0){
            this.list.addAll(collections);
        }else {
            this.list= (List<D>) collections;
        }
        this.notifyDataSetChanged();
    }

    //删除数据
    public void removeData(int position){
        list.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    //清空数据
    public void clearAdapter(){
        list.clear();
        this.notifyDataSetChanged();
    }

    //更新数据
    public void upData(List<D> list){
        this.list=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (getItemLayoutID(viewType)!=0){
//            View view= LayoutInflater.from(context).inflate(getItemLayoutID(viewType),parent,false);
//            return (VH) new BaseRvHolder(view);
            return onCreateHolder(parent,viewType);
        }else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        bindData(holder,position,list.get(position));
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    protected void validateCollection(Collection<D> collections){
        if (collections==null){
            throw new IllegalArgumentException("The list cannot be null");
        }
    }

    /**
     * 根据viewType返回布局资源
     * @param viewType 可以写多样式布局
     * @return
     */
    protected abstract int getItemLayoutID(int viewType);

    /**
     * 创建视图管理者
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract VH onCreateHolder(ViewGroup parent, int viewType);

    /**
     * 绑定数据
     * @param holder 视图管理者
     * @param position
     * @param data 数据源
     */
    protected abstract void bindData(VH holder, int position,D data);

}
