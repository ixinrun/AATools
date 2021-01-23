package com.toperc.lib_aatools.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author ixinrun
 * @data 2020/9/25
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getName();
    protected Activity mContext = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        initView();
        initListener();
        loadData(savedInstanceState);
    }

    protected abstract int getContentView();

    protected abstract void initView();

    protected void initListener() {
    }

    protected abstract void loadData(Bundle savedInstanceState);
}
