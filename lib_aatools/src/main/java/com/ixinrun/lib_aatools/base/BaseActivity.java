package com.ixinrun.lib_aatools.base;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author ixinrun
 * @date 2020/9/25
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
