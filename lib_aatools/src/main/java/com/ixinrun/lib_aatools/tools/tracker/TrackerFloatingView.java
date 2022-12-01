package com.ixinrun.lib_aatools.tools.tracker;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ixinrun.lib_aatools.R;


/**
 * 功能描述: 页面信息悬浮框
 * </p>
 *
 * @author ixinrun
 * @date 2020/9/24
 */
public class TrackerFloatingView extends LinearLayout {
    private final Context mContext;
    private final WindowManager mWindowManager;
    private TextView mTvPackageName;
    private TextView mTvClassName;
    private ImageView mIvClose;

    public TrackerFloatingView(Context context) {
        super(context);
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        initView();
    }

    private void initView() {
        inflate(mContext, R.layout.tracker_floating_layout, this);
        mTvPackageName = findViewById(R.id.tv_package_name);
        mTvClassName = findViewById(R.id.tv_class_name);
        mIvClose = findViewById(R.id.iv_close);

        mIvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                TrackerService.stop(mContext);
            }
        });
    }

    /**
     * 刷新悬浮框
     *
     * @param packageName
     * @param className
     */
    public void update(String packageName, String className) {
        mTvPackageName.setText(packageName);
        mTvClassName.setText(className);
    }

    Point preP, curP;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preP = new Point((int) event.getRawX(), (int) event.getRawY());
                break;

            case MotionEvent.ACTION_MOVE:
                curP = new Point((int) event.getRawX(), (int) event.getRawY());
                int dx = curP.x - preP.x,
                        dy = curP.y - preP.y;

                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.getLayoutParams();
                layoutParams.x += dx;
                layoutParams.y += dy;
                mWindowManager.updateViewLayout(this, layoutParams);

                preP = curP;
                break;
            default:
                break;
        }

        return false;
    }

}
