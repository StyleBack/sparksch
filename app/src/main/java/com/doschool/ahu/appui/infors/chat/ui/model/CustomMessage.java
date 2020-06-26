package com.doschool.ahu.appui.infors.chat.ui.model;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.infors.chat.ui.adapter.holder.ViewHolder;
import com.doschool.ahu.appui.infors.chat.ui.presenter.ChatPresenter;
import com.doschool.ahu.appui.infors.chat.ui.viewfeatures.ChatView;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.utils.XLGlideLoader;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.ext.message.TIMMessageDraft;
import com.tencent.imsdk.ext.message.TIMMessageLocator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


/**
 * 自定义消息
 */
public class CustomMessage extends Message implements ChatView{


    private String TAG = getClass().getSimpleName();

    private final int TYPE_TYPING = 14;

    private Type type;
    private String desc;
    private String data;
    private String identify;
    private ChatPresenter presenter;
    private JSONObject jsonObj;
    private String str;
    private String contents;
    private String imageUrl;
    private String title;
    private int extType;
    private String paramList;

    public CustomMessage(TIMMessage message){
        this.message = message;
        TIMCustomElem elem = (TIMCustomElem) message.getElement(0);
        parse(elem.getData());
        identify = message.getConversation().getPeer();
        presenter = new ChatPresenter(this, identify, TIMConversationType.C2C);
    }

    public CustomMessage(Type type,String info){
        message = new TIMMessage();
        String data = "";
        JSONObject dataJson = new JSONObject();
        TIMCustomElem elem = new TIMCustomElem();
        try{
            switch (type){
                case TYPING:
                    dataJson.put("userAction",TYPE_TYPING);
                    dataJson.put("actionParam","EIMAMSG_InputStatus_Ing");
                    data = dataJson.toString();
                case BANGBANGTANG:
                    elem.setDesc(CodeConfig.IM_BBT_INFO);
                    data = info;
                    break;
            }
        }catch (JSONException e){
            Log.e(TAG, "generate json error");
        }
        elem.setData(data.getBytes());
        message.addElement(elem);
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    private void parse(byte[] data){
//        type = Type.INVALID;
        type = Type.BANGBANGTANG;
        try{
             str = new String(data, "UTF-8");
             jsonObj = new JSONObject(str);
            int action = jsonObj.getInt("userAction");
            switch (action){
                case TYPE_TYPING:
                    type = Type.TYPING;
                    this.data = jsonObj.getString("actionParam");
                    if (this.data.equals("EIMAMSG_InputStatus_End")){
                        type = Type.INVALID;
                    }
                    break;
            }

        }catch (IOException | JSONException e){
            Log.e(TAG, "parse json error");

        }
    }

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ViewHolder viewHolder, Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.show_bbt,null);
        ImageView iv = (ImageView) inflate.findViewById(R.id.iv_bbt);
        TextView tvSlo = (TextView) inflate.findViewById(R.id.tv_sender);
        TextView tvDj = (TextView) inflate.findViewById(R.id.tv_dianji);
        try {
            JSONObject jsonObject = new JSONObject(str);
            imageUrl = jsonObject.optString("imageUrl");
            title=jsonObject.optString("title");
            extType=jsonObject.optInt("extType");
            if (imageUrl.equals("")){
                iv.setVisibility(View.GONE);
            }else {
                XLGlideLoader.loadCircleImage(iv,imageUrl);
            }
            if (extType==2){
//                if (isSelf()){
                    tvSlo.setText(title);
//                }else {
//                    tvSlo.setText("Ta已关注你。");
//                }
            }else {
                tvSlo.setText(title);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        tvDj.setOnClickListener(view -> {
            if (imageUrl.equals("")){
//                Bundle bundle=new Bundle();
//                bundle.putInt("userid", Integer.parseInt(identify));
//                IntentUtil.toActivity(getContext(),bundle,PersionalActivity.class);
            }else {
                new AlertDialog.Builder(context).setMessage("确定要送ta一个棒棒糖吗")
                        .setPositiveButton("是的", (dialog, id) -> sendBbtTextt())
                        .setNegativeButton("不送", (dialog, id) -> dialog.cancel()).create().show();
            }
        });
        clearView(viewHolder);
        getBubbleView(viewHolder).addView(inflate);
        showStatus(viewHolder);
    }

    private void sendBbtTextt(){

    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        String str = getRevokeSummary();
        if (str != null) return "["+str+"]";
        return "[有一条新的消息]";
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    @Override
    public void showMessage(TIMMessage message) {

    }

    @Override
    public void showMessage(List<TIMMessage> messages) {

    }

    @Override
    public void showRevokeMessage(TIMMessageLocator timMessageLocator) {

    }

    @Override
    public void clearAllMessage() {

    }

    @Override
    public void onSendMessageSuccess(TIMMessage message) {

    }

    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {

    }

    @Override
    public void sendImage() {

    }

    @Override
    public void sendPhoto() {

    }

    @Override
    public void sendText() {

    }

    @Override
    public void sendBbtText() {

    }

    @Override
    public void sendFile() {

    }

    @Override
    public void startSendVoice() {

    }

    @Override
    public void endSendVoice() {

    }

    @Override
    public void sendVideo(String fileName) {

    }

    @Override
    public void cancelSendVoice() {

    }

    @Override
    public void cancleMoveVoice() {

    }

    @Override
    public void sending() {

    }

    @Override
    public void showDraft(TIMMessageDraft draft) {

    }

    @Override
    public void videoAction() {

    }

    @Override
    public void showToast(String msg) {

    }

    public enum Type{
        TYPING,
        INVALID,
        BANGBANGTANG
    }
}
