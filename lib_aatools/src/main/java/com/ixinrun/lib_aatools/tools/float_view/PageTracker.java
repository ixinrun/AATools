package com.ixinrun.lib_aatools.tools.float_view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ixinrun.lib_aatools.R;
import com.ixinrun.lib_aatools.base.ActivityLifecycleUtil;
import com.ixinrun.lib_aatools.base.BaseFloatingView;

/**
 * 功能描述: 页面定位器
 * </p>
 *
 * @author xinrun
 * @date 2022/12/6
 */
public class PageTracker extends BaseFloatingView {
    private TextView mPackageNameTv;
    private TextView mClassNameTv;
    private ImageView mCloseIv;

    /**
     * 是否关闭
     */
    private boolean isClose;

    public PageTracker(Context context) {
        super(context);
        inflate(context, R.layout.tracker_floating_layout, this);
        mPackageNameTv = findViewById(R.id.tv_package_name);
        mClassNameTv = findViewById(R.id.tv_class_name);
        mCloseIv = findViewById(R.id.iv_close);
        mCloseIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isClose = true;
                dismiss();
            }
        });

        // 前台activity监听
        ActivityLifecycleUtil.getInstance().addForegroundListener(new ActivityLifecycleUtil.OnForegroundListener() {

            @Override
            public void onForeground(Activity act) {
                if (isClose) {
                    return;
                }
                showAt(act);
            }

            @Override
            public void onDismiss() {
                dismiss();
            }
        });
    }

    /**
     * 显示在某个页面
     */
    public void showAt(Activity a) {
        CharSequence packageName = a.getPackageName();
        CharSequence className = a.getClass().getName();
        if (!TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(className)) {
            String pn = packageName.toString();
            String cn = className.toString();
            if (cn.startsWith(pn)) {
                cn = cn.substring(pn.length());
            }
            mPackageNameTv.setText(pn);
            mClassNameTv.setText(cn);
        }
        show();
        isClose = false;
    }
}
