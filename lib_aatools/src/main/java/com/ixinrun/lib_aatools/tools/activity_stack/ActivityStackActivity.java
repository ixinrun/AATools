package com.ixinrun.lib_aatools.tools.activity_stack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ixinrun.lib_aatools.R;
import com.ixinrun.lib_aatools.base.ActivityLifecycleUtil;
import com.ixinrun.lib_aatools.base.BaseActivity;

/**
 * 功能描述:
 * </p>
 *
 * @author xinrun
 * @date 2022/12/9
 */
public class ActivityStackActivity extends BaseActivity {
    private Toolbar mToolbar;
    private RecyclerView mRv;
    private ActivityStackAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.common_list_activity;
    }

    @Override
    protected void initView() {
        mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle("Activity栈");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRv = findViewById(R.id.rv);
        mRv.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new ActivityStackAdapter();
        mRv.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
        mAdapter.setNewData(ActivityLifecycleUtil.getInstance().getActivityLogs());
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ActivityStackActivity.class);
        context.startActivity(intent);
    }
}
