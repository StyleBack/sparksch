package com.doschool.ahu.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.doschool.ahu.R;
import com.doschool.ahu.appui.main.ui.adapter.EmojVPAdapter;
import com.doschool.ahu.appui.main.ui.bean.Smile;
import com.doschool.ahu.configfile.CodeConfig;
import com.doschool.ahu.factory.JsonEmoj;
import com.doschool.ahu.utils.AssetsUtils;
import com.doschool.ahu.utils.XLToast;
import com.sunhapper.spedittool.view.SpEditText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by X on 2018/8/30
 * emoj表情
 */
public class SimleBoxLayout extends RelativeLayout {


    private ViewPager emoj_vp;
    private DotGroup emoj_dot;
    private SpEditText editText;
    private int pageCount;
    private List<Smile.SourceExpressionsBean> smileList=new ArrayList<>();
    private EmojVPAdapter mPagerAdapter;
    private Disposable subscribe;
    private List<Smile.SourceExpressionsBean> mList=new ArrayList<>();


    public SimleBoxLayout(Context context) {
        this(context,null);
    }

    public SimleBoxLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SimleBoxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("CheckResult")
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.emoj_layout, this);
        emoj_vp=findViewById(R.id.emoj_vp);
        emoj_dot=findViewById(R.id.emoj_dot);

    }

    List<List<Smile.SourceExpressionsBean>> totalList = new ArrayList<>();

    public void showEmojLayout(){
        if(subscribe==null) {
            subscribe = Observable.create(new ObservableOnSubscribe<List<Smile.SourceExpressionsBean>>() {
                @Override
                public void subscribe(ObservableEmitter<List<Smile.SourceExpressionsBean>> e) throws Exception {
                    smileList = JsonEmoj.getJson(getContext()).getSourceExpressions();
                    e.onNext(smileList);
                    e.onComplete();
                }
            }).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Smile.SourceExpressionsBean>>() {
                        @Override
                        public void accept(List<Smile.SourceExpressionsBean> sourceExpressionsBeans) throws Exception {
                            pageCount = smileList.size() / (CodeConfig.EMOJ_ROW_COUNT * CodeConfig.EMOJ_COLUMN_COUNT) + 1;
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                        }
                    }, new Action() {
                        @Override
                        public void run() throws Exception {
                            mPagerAdapter = new EmojVPAdapter(getContext(), pageCount, smileList, onEmotionClickListener);
                            emoj_vp.setId("vp".hashCode());
                            emoj_vp.setAdapter(mPagerAdapter);
                            emoj_dot.init(pageCount);

                            totalList=mPagerAdapter.getTotalList();
                            mList = totalList.get(0);
                        }
                    });
        }



        emoj_vp.setOffscreenPageLimit(pageCount);
        emoj_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                emoj_dot.setCurrentItem(position);
                mList = totalList.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setEditText(SpEditText editText) {
        this.editText = editText;
    }

    public SpEditText getEditText() {
        return editText;
    }

    public ViewPager getViewPager() {
        return emoj_vp;
    }


    AdapterView.OnItemClickListener onEmotionClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String str=mList.get(position).getExpressionImageName();
            String name=mList.get(position).getExpressionString();
            if (str.equals("1001")) {
//                int keycodeDel = KeyEvent.KEYCODE_DEL;
//                KeyEvent keyEvent=new KeyEvent(KeyEvent.ACTION_DOWN,keycodeDel);
//                KeyEvent keEventUp=new KeyEvent(KeyEvent.ACTION_UP,keycodeDel);
//                getEditText().onKeyDown(keycodeDel,keyEvent);
//                getEditText().onKeyUp(keycodeDel,keEventUp);
                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0,
                        KeyEvent.KEYCODE_ENDCALL);
                getEditText().dispatchKeyEvent(event);
            } else {
                if (getEditText().length()+name.length()<=300){
                    getEditText().insertSpecialStr(name, false, 1,
                            AssetsUtils.getKeySpan(getContext(), Integer.parseInt(mList.get(position).getExpressionImageName())));
                }else {
                    XLToast.showToast("字数超过限制哟~~~");
                }
            }
        }
    };
}
