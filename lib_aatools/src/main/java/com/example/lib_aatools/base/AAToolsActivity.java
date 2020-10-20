package com.example.lib_aatools.base;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lib_aatools.R;
import com.example.lib_aatools.tools.DataCleanHelper;
import com.example.lib_aatools.tools.crash_log.CrashLogActivity;
import com.example.lib_aatools.tools.file.FileViewActivity;
import com.example.lib_aatools.tools.tracker.TrackerUtil;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

/**
 * 功能描述: 开发工具展示页面
 * </p>
 *
 * @author toperc
 * @data 2020/8/25
 */
public class AAToolsActivity extends BaseActivity {

    private TextView mMoreBtn;
    private TextView mExitBtn;
    private FlexboxLayout mCommonlyToolsFl;
    private FlexboxLayout mCustomToolsFl;
    private LinearLayout mCustomToolsLl;

    @Override
    protected int getContentView() {
        return R.layout.aatools_activity;
    }

    @Override
    protected void initView() {
        mMoreBtn = findViewById(R.id.more_btn);
        mExitBtn = findViewById(R.id.exit_btn);
        mCommonlyToolsFl = findViewById(R.id.commonly_tools_fl);
        mCustomToolsLl = findViewById(R.id.custom_tools_ll);
        mCustomToolsFl = findViewById(R.id.custom_tools_fl);

        //创建常用item
        creatCommonlyTools();
        //创建自定义item
        creatCustomTools();
    }

    @Override
    protected void initListener() {
        super.initListener();
        //更多
        mMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点右边退出", Toast.LENGTH_SHORT).show();
            }
        });

        //退出
        mExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void loadData(Bundle savedInstanceState) {
    }

    /**
     * 创建常用的工具类
     */
    private void creatCommonlyTools() {
//        creatItem(mCommonlyToolsFl, new ItemBean(R.drawable.item_server_log_ic, "日志重连", new ItemBean.OnItemClickListener() {
//            @Override
//            public boolean onClick(Context context) {
//                // serverlog重连
//                Toast.makeText(mContext, "正在连接日志服务器...", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        }));

        creatItem(mCommonlyToolsFl, new ItemBean(R.drawable.item_crash_log_ic, "崩溃日志", new ItemBean.OnItemClickListener() {
            @Override
            public boolean onClick(Context context) {
                CrashLogActivity.startActivity(context);
                return false;
            }
        }));

        creatItem(mCommonlyToolsFl, new ItemBean(R.drawable.item_tracker_ic, "页面追踪", new ItemBean.OnItemClickListener() {
            @Override
            public boolean onClick(Context context) {
                return TrackerUtil.openTracker(context);
            }
        }));

        creatItem(mCommonlyToolsFl, new ItemBean(R.drawable.item_files_view_ic, "沙盒浏览", new ItemBean.OnItemClickListener() {
            @Override
            public boolean onClick(Context context) {
                FileViewActivity.startActivity(context);
                return false;
            }
        }));

        creatItem(mCommonlyToolsFl, new ItemBean(R.drawable.item_data_clean_ic, "缓存清除", new ItemBean.OnItemClickListener() {
            @Override
            public boolean onClick(Context context) {
                new AlertDialog.Builder(context)
                        .setTitle("注意")
                        .setMessage("使用缓存清除功能，将会清除当前应用保存的所有数据，需谨慎操作，清除完成后应用也将会自动重启，确定这样做吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                DataCleanHelper.cleanApplicationData(Util.sApp, Util.sFilePaths);
                                appRestart();
                            }
                        }).create().show();

                return false;
            }
        }));

        creatItem(mCommonlyToolsFl, new ItemBean(R.drawable.item_app_restart_ic, "应用重启", new ItemBean.OnItemClickListener() {
            @Override
            public boolean onClick(Context context) {
                appRestart();
                return true;
            }
        }));

        creatItem(mCommonlyToolsFl, new ItemBean(R.drawable.item_strict_mode_ic, "严苛模式", new ItemBean.OnItemClickListener() {
            @Override
            public boolean onClick(Context context) {
                new AlertDialog.Builder(context)
                        .setTitle("注意")
                        .setMessage("严苛模式用于帮助开发者调试程序的顺滑性，使用过程中配合控制台过滤StrictMode日志。" +
                                "由于企业级开发以业务为主，程序臃肿，需开发者逐步优化。此模式开启后可能不利于QA正常测试，确定开启吗？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                                        .detectCustomSlowCalls() //API等级11，使用StrictMode.noteSlowCode
                                        .detectDiskReads()
                                        .detectDiskWrites()
                                        .detectNetwork()   // or .detectAll() for all detectable problems
                                        .penaltyDialog() //弹出违规提示对话框
                                        .penaltyLog() //在Logcat 中打印违规异常信息
                                        .penaltyFlashScreen() //API等级11
                                        .build());

                                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                                        .detectLeakedSqlLiteObjects()
                                        .detectLeakedClosableObjects() //API等级11
                                        .penaltyLog()
                                        .penaltyDeath()
                                        .build());
                            }
                        }).create().show();

                return false;
            }
        }));
    }

    /**
     * 创建私有的工具类
     */
    private void creatCustomTools() {
        List<ItemBean> list = Util.sCustomItems;
        if (list == null || list.isEmpty()) {
            return;
        }
        mCustomToolsLl.setVisibility(View.VISIBLE);
        for (ItemBean bean : list) {
            creatItem(mCustomToolsFl, bean);
        }
    }

    /**
     * creat item
     *
     * @param root
     * @param bean
     */
    private void creatItem(ViewGroup root, final ItemBean bean) {
        if (bean == null) {
            return;
        }
        View item = LayoutInflater.from(mContext).inflate(R.layout.aatools_item, root, false);
        ImageView iv = item.findViewById(R.id.item_iv);
        TextView tv = item.findViewById(R.id.item_tv);

        iv.setBackgroundResource(bean.getSrc() > 0 ? bean.getSrc() : R.drawable.item_def_ic);
        tv.setText(bean.getText());

        //设置等间距
        FlexboxLayout.LayoutParams rootLp = (FlexboxLayout.LayoutParams) item.getLayoutParams();
        rootLp.setFlexBasisPercent(0.333f);

        //点击处理
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getmListener() != null) {
                    boolean finish = bean.getmListener().onClick(mContext);
                    if (finish) {
                        finish();
                    }
                }
            }
        });

        //添加到布局
        root.addView(item);
    }

    /**
     * 应用重启
     */
    private void appRestart() {
        final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        //杀掉以前进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
