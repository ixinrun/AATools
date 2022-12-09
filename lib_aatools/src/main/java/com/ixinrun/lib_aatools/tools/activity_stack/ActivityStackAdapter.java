package com.ixinrun.lib_aatools.tools.activity_stack;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ixinrun.lib_aatools.R;
import com.ixinrun.lib_aatools.base.ActivityLifecycleUtil;

/**
 * 功能描述:
 * </p>
 *
 * @author xinrun
 * @date 2022/12/9
 */
public class ActivityStackAdapter extends BaseQuickAdapter<ActivityLifecycleUtil.ActivityLog, BaseViewHolder> {

    public ActivityStackAdapter() {
        super(R.layout.activity_stack_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActivityLifecycleUtil.ActivityLog item) {
        helper.setText(R.id.name_tv, item.name);
        helper.setText(R.id.tag_tv, item.tag);
        helper.setText(R.id.time_tv, item.time);
    }
}
