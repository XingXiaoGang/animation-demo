package com.example.xingxiaogang.animationdemo.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.RectF;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewStub;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xingxiaogang.animationdemo.R;
import com.example.xingxiaogang.animationdemo.SizeUtils;

/**
 * 新手引导View
 * Created by wangwei on 2016/8/5.
 */
public class BeginnerGuideView extends FrameLayout {

    private static final String TAG = "BeginnerGuideView";

    private static final String CENTER_X = "centerX";
    private static final String CENTER_Y = "centerY";
    private static final String SIZE_X = "width";
    private static final String SIZE_Y = "height";

    private View mFocusingView;
    private LinearLayout mCard;
    private TextView mTitle;
    private TextView mContent;
    private GuideTarget mGuideTarget;
    private TextView mGotItBtn;
    private AnimatorSet mAnimator;

    private static int layoutId = R.layout.beginners_guide_small_view;
    private static float widthPercent = 1.0f;   // View所占整个屏幕的比重
    private OnGotItClickListener listener;      // Got it 点击事件接口
    private boolean onGuiding = false;   // 引导情景的标志位


    public BeginnerGuideView(Context context) {
        this(context, null);
    }

    public BeginnerGuideView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setClipToPadding(false);
        setClipChildren(false);

        inflate(context, layoutId, this);

        mFocusingView = findViewById(R.id.focusing_view);
        mCard = (LinearLayout) findViewById(R.id.guide_card);

        LayoutParams layoutParams = new LayoutParams(mCard.getLayoutParams());
        layoutParams.width = (int) (SizeUtils.getScreenWidth() * widthPercent);
        mCard.setLayoutParams(layoutParams);

        mTitle = (TextView) findViewById(R.id.guide_card_title);
        mContent = (TextView) findViewById(R.id.guide_card_content);
        mGotItBtn = ((TextView) findViewById(R.id.guide_card_got_it));

        mCard.setTranslationX(-SizeUtils.getScreenWidth());

        mGotItBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                guideMiss();
                if (listener != null) {
                    listener.onClick(v);
                }
            }
        });
    }

    public void reset() {
        mFocusingView.setAlpha(1);
        setVisibility(VISIBLE);
    }

    public void guideMiss() {

        ViewCompat.animate(mCard)
                .translationX(getWidth())
                .setDuration(200)
                .withLayer()
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        setVisibility(View.GONE);
                    }
                })
                .start();

        ViewCompat.animate(mFocusingView)
                .alpha(0)
                .setDuration(200)
                .withLayer()
                .start();

        onGuiding = false;

    }

    public void setListener(OnGotItClickListener listener) {
        this.listener = listener;
    }

    public static BeginnerGuideView fromStub(ViewStub stub) {
        layoutId = R.layout.beginners_guide_small_view;
        widthPercent = 1.0f;
        return ((BeginnerGuideView) stub.inflate());
    }

    /**
     * 初始化引导页
     *
     * @param stub
     * @param layout_id 引导页的布局  （布局中包含的控件和控件的ID须和原先的一致）
     * @param width_p   布局占总屏幕的百分比
     * @return
     */
    public static BeginnerGuideView fromStub(ViewStub stub, int layout_id, float width_p) {
        layoutId = layout_id;
        widthPercent = width_p;
        return ((BeginnerGuideView) stub.inflate());
    }

    public BeginnerGuideView setGuideTarget(GuideTarget guideTarget) {
        mGuideTarget = guideTarget;
        return this;
    }

    public BeginnerGuideView setTitle(int res) {
        mTitle.setText(res);
        if (mTitle.getText().toString().trim().equals("")) {
            mTitle.setVisibility(GONE);
        }
        return this;
    }

    public BeginnerGuideView setGotItBtnName(int name) {
        mGotItBtn.setText(name);
        return this;
    }

    public BeginnerGuideView setContent(int res) {
        mContent.setText(res);
        return this;
    }

    public BeginnerGuideView setAccentColor(int color) {
        mCard.setBackgroundResource(color);
        return this;
    }

    public View show() {
        return show(0);
    }

    /**
     * 重写show方法，为了满足引导框不是全屏的需求
     *
     * @param translateX 引导框滑动的距离，不是满屏是滑动的距离是screenWidth - viewWidth
     * @return
     */
    public View show(float translateX) {
        final FocusingDrawable focusingDrawable = prepareFocusingView(mGuideTarget);
        correctCardPosition(mGuideTarget);

        mAnimator = prepareAnimator(mGuideTarget, focusingDrawable, translateX);
        mAnimator.start();
        onGuiding = true;
        return this;
    }

    public boolean isOnGuiding() {
        return onGuiding;
    }

    public void setOnGuiding(boolean onGuiding) {
        this.onGuiding = onGuiding;
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);

        if (screenState == SCREEN_STATE_OFF) {
            if (mAnimator != null && mAnimator.isRunning()) {
                mAnimator.end();
            }
        }
    }

    private float mDownY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        final int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        Log.d(TAG, "onTouchEvent() called with: " + "event = [" + event + "]");

        if (mAnimator != null && mAnimator.isRunning()) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }

        final int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {

            mDownY = event.getY();

            if (mGuideTarget != null) {
                final RectF rect = mGuideTarget.getTargetRegionOnWindow();

                if (!rect.contains(event.getX(), event.getY())) {
                    return true;
                } else {
                    Log.v(TAG, "press in target region!");
                    setVisibility(View.GONE);
                    if (mOnTargetRegionHitListener != null) {
                        mOnTargetRegionHitListener.onTargetRegionHit(rect);
                    }
                }
            }
        }

        if (action == MotionEvent.ACTION_MOVE) {
            final float curY = event.getY();

            if (Math.abs(curY - mDownY) >= touchSlop) {
                Log.d(TAG, "intercept touch slop: ");
                return true;
            }
        }

        return super.onTouchEvent(event);
    }

    private AnimatorSet prepareAnimator(GuideTarget guide, final FocusingDrawable focusingDrawable, float translateX) {

        final int screenHeight = SizeUtils.getScreenHeight();
        final int screenWidth = SizeUtils.getScreenWidth();

        focusingDrawable.setCenterPoint(screenWidth / 2, screenHeight / 2);

        final RectF target = guide.getTargetRegionOnWindow();

        final PropertyValuesHolder centerX = PropertyValuesHolder.ofFloat(CENTER_X, screenWidth / 2, target.centerX());
        final PropertyValuesHolder centerY = PropertyValuesHolder.ofFloat(CENTER_Y, screenHeight / 2, target.centerY());

        final float startSize = (float) Math.hypot(screenWidth, screenHeight);

        final PropertyValuesHolder sizeX = PropertyValuesHolder.ofFloat(SIZE_X, startSize, target.width());
        final PropertyValuesHolder sizeY = PropertyValuesHolder.ofFloat(SIZE_Y, startSize, target.height());

        final ValueAnimator focusingAnimator = ValueAnimator.ofPropertyValuesHolder(centerX, centerY, sizeX, sizeY)
                .setDuration(600);

        focusingAnimator.setInterpolator(new OvershootInterpolator(1F));

        focusingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final Float cx = (Float) animation.getAnimatedValue(CENTER_X);
                final Float cy = (Float) animation.getAnimatedValue(CENTER_Y);

                final Float w = (Float) animation.getAnimatedValue(SIZE_X);
                final Float h = (Float) animation.getAnimatedValue(SIZE_Y);

                focusingDrawable.setCenterPoint(cx, cy)
                        .setSize(w, h)
                        .invalidateSelf();
            }
        });

        final ObjectAnimator cardAnimator = ObjectAnimator.ofFloat(mCard, View.TRANSLATION_X, -screenWidth, translateX)
                .setDuration(400);

        final AnimatorSet set = new AnimatorSet();
        set.playSequentially(focusingAnimator, cardAnimator);
        return set;
    }

    private void correctCardPosition(GuideTarget target) {
        final RectF targetOnWindow = target.getTargetRegionOnWindow();
        final float centerY = targetOnWindow.centerY();

        final int screenHeight = SizeUtils.getScreenHeight();

        final LayoutParams params = (LayoutParams) mCard.getLayoutParams();

        final int OFFSET = SizeUtils.dp2px(20);

        if (centerY < screenHeight / 2) {
            params.bottomMargin = 0;
            params.topMargin = (int) (targetOnWindow.bottom + OFFSET);
            params.gravity = Gravity.TOP;
        } else {
            params.topMargin = 0;
            if (SizeUtils.hasNavBar(getResources())) {
                params.bottomMargin = getHeightSafely() - (int) (targetOnWindow.top - 3 * OFFSET);
            } else {
                params.bottomMargin = getHeightSafely() - (int) (targetOnWindow.top - OFFSET);
            }

            params.gravity = Gravity.BOTTOM;
        }
    }

    private int getHeightSafely() {
        return getMeasuredHeight() == 0 ? SizeUtils.getScreenHeight() : getMeasuredHeight();
    }

    private FocusingDrawable prepareFocusingView(GuideTarget target) {
        final FocusingDrawable focusingDrawable = target.getFocusingType();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mFocusingView.setBackground(focusingDrawable);
        } else {
            mFocusingView.setBackgroundDrawable(focusingDrawable);
        }

        return focusingDrawable;
    }

    public interface OnVisibilityChangeListener {

        void onVisibilityChanged(int oldVisibility, int newVisibility);
    }

    public interface OnTargetRegionHitListener {
        void onTargetRegionHit(RectF rect);
    }

    private OnVisibilityChangeListener mOnVisibilityChangeListener;
    private OnTargetRegionHitListener mOnTargetRegionHitListener;

    public BeginnerGuideView setOnVisibilityChangeListener(OnVisibilityChangeListener onVisibilityChangeListener) {
        mOnVisibilityChangeListener = onVisibilityChangeListener;
        return this;
    }

    public BeginnerGuideView setOnTargetRegionHitListener(OnTargetRegionHitListener listener) {
        mOnTargetRegionHitListener = listener;
        return this;
    }

    @Override
    public void setVisibility(int visibility) {
        final int old = getVisibility();
        super.setVisibility(visibility);
        if (mOnVisibilityChangeListener != null) {
            mOnVisibilityChangeListener.onVisibilityChanged(old, visibility);
        }
    }

    public static class GuideArguments {
        public int titleId;
        public int contentId;
        public int accentColorId;
        public int gotItBtnNameId;
    }


    /**
     * 把Got it的点击事件暴露出去
     */
    public interface OnGotItClickListener {
        void onClick(View v);
    }

}
