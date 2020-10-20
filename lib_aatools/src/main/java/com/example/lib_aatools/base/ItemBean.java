package com.example.lib_aatools.base;

import android.content.Context;
import android.support.annotation.DrawableRes;

/**
 * 功能描述: 小工具item
 * </p>
 *
 * @author toperc
 * @data 2020/9/24
 */
public class ItemBean {
    private int src;
    private String text;
    private OnItemClickListener mListener;

    public ItemBean(@DrawableRes int src, String text, OnItemClickListener l) {
        this.src = src;
        this.text = text;
        this.mListener = l;
    }

    public int getSrc() {
        return src;
    }

    public String getText() {
        return text;
    }

    public OnItemClickListener getmListener() {
        return mListener;
    }

    public interface OnItemClickListener {
        /**
         * item 点击
         *
         * @param context
         * @return 是否关闭工具界面
         */
        boolean onClick(Context context);
    }
}
