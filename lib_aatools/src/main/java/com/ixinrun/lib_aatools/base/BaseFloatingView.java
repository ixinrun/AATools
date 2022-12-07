package com.ixinrun.lib_aatools.base;

import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.FloatRange;

/**
 * 功能描述: 基类悬浮窗，包含贴边功能，子类可以继承实现不同的视图
 * </p>
 *
 * @author xinrun
 * @date 2022/12/6
 */
public class BaseFloatingView extends FrameLayout {
    private static final int TOUCH_TIME_THRESHOLD = 150;

    private int mScreenWidth;
    private int mScreenHeight;
    private int mStatusBarHeight;
    private WindowManager mWm;
    private WindowManager.LayoutParams mLp;

    private boolean mIsShowing;
    private float mOriginalX;
    private float mOriginalY;
    private long mLastTouchDownTime;
    private Handler mHandler;
    private MoveAnimator mMoveAnimator;

    private boolean isFix;
    private boolean isAttachEdge;
    private int mAttachEdgeMargin;
    private boolean isShallow;
    private float mAlpha;
    private OnClickListener mOnClickListener;

    public BaseFloatingView(Context context) {
        super(context);
        init();
    }

    private void init() {
        initScreenInfo();
        initWindowManager();
        mHandler = new Handler(Looper.getMainLooper());
        mMoveAnimator = new MoveAnimator();
        isFix = false;
        isAttachEdge = false;
        mAttachEdgeMargin = 10;
        isShallow = false;
        mAlpha = 0;
    }

    private void initScreenInfo() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
        mStatusBarHeight = 20;
    }

    private void initWindowManager() {
        mWm = (WindowManager) getContext().getSystemService(Service.WINDOW_SERVICE);
        mLp = new WindowManager.LayoutParams();
        mLp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mLp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mLp.gravity = Gravity.START | Gravity.TOP;
        mLp.type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE;
        mLp.format = PixelFormat.RGBA_8888;
        mLp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isShallow) {
            setAlpha(mAlpha);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setAlpha(1f);
                changeOriginalTouchParams(event);
                mMoveAnimator.stop();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isFix) {
                    updateViewPosition(event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isFix && isAttachEdge) {
                    moveToEdge();
                }
                if (isOnClickEvent() && mOnClickListener != null) {
                    mOnClickListener.onClick(this);
                }
                // 5秒后自动隐藏
                if (isShallow) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setAlpha(mAlpha);
                        }
                    }, 5000);
                }
                break;
            default:
                break;
        }
        return false;
    }

    private void changeOriginalTouchParams(MotionEvent event) {
        mOriginalX = event.getX();
        mOriginalY = event.getY();
        mLastTouchDownTime = System.currentTimeMillis();
    }

    private void updateViewPosition(MotionEvent event) {
        float desX = event.getRawX() - mOriginalX;
        if (desX < 0) {
            desX = 0;
        }
        if (desX > mScreenWidth - getWidth()) {
            desX = mScreenWidth - getWidth();
        }

        float desY = event.getRawY() - mOriginalY;
        if (desY < 0) {
            desY = 0;
        }
        if (desY > mScreenHeight - getHeight()) {
            desY = mScreenHeight - getHeight();
        }
        updatePosition(desX, desY);
    }

    private void moveToEdge() {
        float moveX = isNearestLeft(mLp) ? mAttachEdgeMargin : mScreenWidth - getWidth() - mAttachEdgeMargin;
        if (mLp.y < mAttachEdgeMargin) {
            mMoveAnimator.start(mLp, moveX, mAttachEdgeMargin);
        } else if (mScreenHeight - (mLp.y + getHeight() + mStatusBarHeight) < mAttachEdgeMargin) {
            mMoveAnimator.start(mLp, moveX, mScreenHeight - mStatusBarHeight - getHeight() - mAttachEdgeMargin);
        } else {
            mMoveAnimator.start(mLp, moveX, mLp.y);
        }
    }

    private boolean isNearestLeft(WindowManager.LayoutParams windowParams) {
        int middle = mScreenWidth / 2;
        return windowParams.x < middle;
    }

    private boolean isOnClickEvent() {
        return System.currentTimeMillis() - mLastTouchDownTime < TOUCH_TIME_THRESHOLD;
    }

    class MoveAnimator implements Runnable {
        private float destinationX;
        private float destinationY;
        private long startingTime;
        private WindowManager.LayoutParams layoutParams;

        void start(WindowManager.LayoutParams layoutParams, float x, float y) {
            this.destinationX = x;
            this.destinationY = y;
            this.startingTime = System.currentTimeMillis();
            this.layoutParams = layoutParams;
            mHandler.post(this);
        }

        @Override
        public void run() {
            if (getRootView() == null || getRootView().getParent() == null) {
                return;
            }
            float progress = Math.min(1, (System.currentTimeMillis() - startingTime) / 400f);
            float deltaX = (destinationX - layoutParams.x) * progress;
            float deltaY = (destinationY - layoutParams.y) * progress;

            updatePosition(layoutParams.x + deltaX, layoutParams.y + deltaY);

            if (progress < 1) {
                mHandler.post(this);
            }
        }

        private void stop() {
            mHandler.removeCallbacks(this);
        }
    }

    private void updatePosition(float x, float y) {
        mLp.x = (int) x;
        mLp.y = (int) y;
        if (getWindowToken() != null) {
            mWm.updateViewLayout(this, mLp);
        }
    }

    /**
     * 初始位置
     */
    public BaseFloatingView setStartPosition(int x, int y) {
        updatePosition(x, y);
        return this;
    }

    /**
     * 是否固定
     */
    public BaseFloatingView isFix(boolean isFix) {
        this.isFix = isFix;
        return this;
    }

    /**
     * 是否自动贴边
     */
    public BaseFloatingView isAttachEdge(boolean isAttach) {
        this.isAttachEdge = isAttach;
        return this;
    }

    /**
     * 自动贴边边界距离
     */
    public BaseFloatingView setAttachEdgeMargin(int margin) {
        this.mAttachEdgeMargin = margin;
        return this;
    }

    /**
     * 颜色变浅
     *
     * @param isShallow 是否变浅
     * @param alpha     透明度
     */
    public BaseFloatingView setShallow(boolean isShallow, @FloatRange(from = 0.0, to = 1.0) float alpha) {
        this.isShallow = isShallow;
        if (alpha < 0) {
            this.mAlpha = 0;
        } else if (alpha > 1f) {
            this.mAlpha = 1f;
        } else {
            this.mAlpha = alpha;
        }
        return this;
    }

    /**
     * view点击
     */
    public BaseFloatingView setOnFloatViewClickListener(OnClickListener l) {
        this.mOnClickListener = l;
        return this;
    }

    /**
     * 显示
     */
    public void show() {
        if (!mIsShowing) {
            mIsShowing = true;
            mWm.addView(this, mLp);
        }
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        if (mIsShowing) {
            mIsShowing = false;
            mWm.removeView(this);
        }
    }
}
