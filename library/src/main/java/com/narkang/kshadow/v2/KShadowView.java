package com.narkang.kshadow.v2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.narkang.kshadow.R;


/**
 *  //目前支持的自定义属性
 *  <resources>
 *
 *     <!-- ShadowLayout 阴影 -->
 *     <declare-styleable name="ShadowLayout">
 *         <attr name="shadowColor" format="color"/>
 *         <attr name="shadowRadius" format="dimension"/>
 *         <attr name="shadowDx" format="dimension"/>
 *         <attr name="shadowDy" format="dimension"/>
 *         <attr name="shadowShape">
 *             <flag name="rectangle" value="0x0001"/>
 *             <flag name="oval" value="0x0010"/>
 *             <flag name="roundRectangle" value="0x0100"/>
 *         </attr>
 *         <attr name="shadowSide">
 *             <flag name="all" value="0x1111"/>
 *             <!--添加了圆角支持，目前不支持单边设置阴影-->
 *             <!--<flag name="left" value="0x0001"/>-->
 *             <!--<flag name="top" value="0x0010"/>-->
 *             <!--<flag name="right" value="0x0100"/>-->
 *             <!--<flag name="bottom" value="0x1000"/>-->
 *         </attr>
 *         <attr name="shadowRoundRadius" format="dimension"/>
 *     </declare-styleable>
 *
 * </resources>
 */
public class KShadowView extends FrameLayout {

    public static final int ALL = 0x1111;

    public static final int LEFT = 0x0001;

    public static final int TOP = 0x0010;

    public static final int RIGHT = 0x0100;

    public static final int BOTTOM = 0x1000;

    public static final int SHAPE_RECTANGLE = 0x0001;

    public static final int SHAPE_OVAL = 0x0010;

    public static final int SHAPE_ROUND_RECTANGLE = 0x0100;

    /**
     * 阴影的颜色
     */
    private int mShadowColor = Color.TRANSPARENT;

    /**
     * 阴影的大小范围
     */
    private float mShadowRadius = 0;

    /**
     *  圆角的大小
     */
    private float mShadowRoundRadius = 0;

    /**
     * 阴影 x 轴的偏移量
     */
    private float mShadowDx = 0;

    /**
     * 阴影 y 轴的偏移量
     */
    private float mShadowDy = 0;

    /**
     * 阴影显示的边界
     */
    private int mShadowSide = ALL;

    /**
     * 阴影的形状，圆形/矩形|圆角矩形
     */
    private int mShadowShape = SHAPE_RECTANGLE;

    private KShadowDrawable mShadowDrawable;

    /**
     *  测量之前的宽度
     */
    private float beforeMeasureWidth = 0;

    /**
     * 测量之前的高度
     */
    private float beforeMeasureHeight = 0;

    public KShadowView(@NonNull Context context) {
        this(context, null, 0);
    }

    public KShadowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KShadowView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(@Nullable AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.KShadowView);
        if (typedArray != null) {
            mShadowShape = typedArray.getInt(R.styleable.KShadowView_shadowShape, SHAPE_RECTANGLE);
            mShadowRadius = typedArray.getDimension(R.styleable.KShadowView_shadowRadius, 0);
            mShadowColor = typedArray.getColor(R.styleable.KShadowView_shadowColor,
                    getContext().getResources().getColor(android.R.color.black));
            mShadowDx = typedArray.getDimension(R.styleable.KShadowView_shadowDx, 0);
            mShadowDy = typedArray.getDimension(R.styleable.KShadowView_shadowDy, 0);
            mShadowSide = typedArray.getInt(R.styleable.KShadowView_shadowSide, ALL);
            mShadowRoundRadius = typedArray.getDimension(R.styleable.KShadowView_shadowRoundRadius, 0);

            typedArray.recycle();
        }

        mShadowDrawable = new KShadowDrawable(mShadowShape, mShadowColor,
                mShadowRadius, mShadowDx, mShadowDy, mShadowRoundRadius);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);       // 关闭硬件加速
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float effect = mShadowRadius;

        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        int leftMargin = params.leftMargin;
        int rightMargin = params.rightMargin;
        int topMargin = params.topMargin;
        int bottom = params.bottomMargin;

        float rectLeft = 0;
        float rectTop = 0;

        float rectRight = this.getMeasuredWidth();
        float rectBottom = this.getMeasuredHeight();
        beforeMeasureWidth = rectRight;
        beforeMeasureHeight = rectBottom;

        if ((mShadowSide & LEFT) == LEFT) {
            rectLeft = -effect;
        }
        if ((mShadowSide & TOP) == TOP) {
            rectTop = -effect;
        }
        if ((mShadowSide & RIGHT) == RIGHT) {
            rectRight = this.getMeasuredWidth() + effect;
        }
        if ((mShadowSide & BOTTOM) == BOTTOM) {
            rectBottom = this.getMeasuredHeight() + effect;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize((int) (rectRight - rectLeft)), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize((int) (rectBottom - rectTop)), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int count = getChildCount();
        if(count == 0) return;

        //第一个子元素需要居中显示
        FrameLayout.LayoutParams params = (LayoutParams) getChildAt(0).getLayoutParams();
        params.gravity = Gravity.CENTER;
        params.width = (int) beforeMeasureWidth;
        params.height = (int) beforeMeasureHeight;

        //如果设置了mShadowDy和mShadowDx，子元素需要调整位置
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int viewLeft = child.getLeft();
            int viewRight = child.getRight();
            int viewTop = child.getTop();
            int viewBottom = child.getBottom();

            switch (mShadowSide){
                case LEFT: break;
                case RIGHT: break;
                case TOP: break;
                case BOTTOM: break;
                case ALL:
                    if (mShadowDy != 0.0f) {
                        viewTop -= mShadowDy;
                        viewBottom -= mShadowDy;
                    }
                    if (mShadowDx != 0.0f) {
                        viewLeft -= mShadowDx;
                        viewRight -= mShadowDx;
                    }

                    break;
                default: break;
            }

            child.layout(viewLeft, viewTop, viewRight, viewBottom);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
//        Log.i("ShadowLayout", "ShadowLayout dispatchDraw");
        super.dispatchDraw(canvas);
        ViewCompat.setBackground(KShadowView.this, mShadowDrawable);
    }

//    @Override
//    public void setPadding(int left, int top, int right, int bottom) {
//        // NO OP
//    }
//
//    @Override
//    public void setPaddingRelative(int start, int top, int end, int bottom) {
//        // NO OP
//    }

    /**
     * dip2px dp 值转 px 值
     *
     * @param dpValue dp 值
     * @return px 值
     */
    private float dp2Px(float dpValue) {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        float scale = dm.density;
        return (dpValue * scale + 0.5F);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private int px2dip(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}

