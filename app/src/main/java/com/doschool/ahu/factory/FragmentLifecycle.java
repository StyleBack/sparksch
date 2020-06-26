package com.doschool.ahu.factory;

/**
 * Created by X on 2018/9/10
 */
public interface FragmentLifecycle {
    void onFragmentPause();
    void onFragmentResume();
    void onBackPressed();
    void onActivityPause();
    void onActivityResume();
    void onActivityDestroy();
}
