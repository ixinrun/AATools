package com.ixinrun.lib_aatools.tools.float_view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ixinrun.lib_aatools.R;
import com.ixinrun.lib_aatools.base.AAToolsActivity;
import com.ixinrun.lib_aatools.base.BaseFloatingView;

/**
 * 功能描述: 开发工具悬浮球
 * </p>
 *
 * @author xinrun
 * @date 2022/12/6
 */
public class DeveloperBall extends BaseFloatingView {

    public DeveloperBall(Context context) {
        super(context);
        ImageView iv = new ImageView(context);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(120, 120);
        iv.setLayoutParams(lp);
        iv.setBackgroundResource(R.drawable.developer_ic);
        addView(iv);

        setStartPosition(50, 250);
        isAttachEdge(true);
        setShallow(true, 0.6f);
        setOnFloatViewClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = v.getContext();
                c.startActivity(new Intent(c, AAToolsActivity.class));
            }
        });
    }
}
