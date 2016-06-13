package com.cctvjiatao.customviewgroup.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jiatao on 2016/6/13.
 *
 * 一、ViewGroup是是什么？作用呢？
 *  1、它相当于放置View的容器。xml布局文件中，凡是以“layout”开头的属性都是和ViewGroup（即容器）相关的，比如高度（layout_height）、宽度（layout_width）、对齐方式（layout_gravity）等；
 *  2、它给childView计算出建议的宽、高和测量模式，决定childView的位置；
 *     为什么是“建议的宽、高”而不是直接确定呢？因为当childView的宽、高设置为wrap_content时，只有childView自己才能计算出自己的宽和高。
 *
 * 二、View的作用是什么？
 *  1、它根据测量模式和ViewGroup给出的建议的宽、高，计算出自己的宽、高；
 *  2、在ViewGroup为其指定的区域内绘制自己的形态；
 *
 * 三、View的三种测量模式
 *  1、EXACTLY：表示设置了精确的值，一般当childView设置其宽高为精确值、match_parent时，ViewGroup会将其设置为EXACTLY；
 *  2、AT_MOST：表示子布局被限制在一个最大值内，一般当childView设置其宽、高为wrap_content时，ViewGroup会将其设置为AT_MOST；
 *  3、UNSPECIFIED：表示子布局想要多大就多大，一般出现在AadapterView的item的heightMode中、ScrollView的childView的heightMode中；此种模式比较少见。
 *
 * 四、ViewGroup 和 LayoutParams的关系
 *  当在LinearLayout中写childView的时候，可以写layout_gravity，layout_weight属性；
 *  而在RelativeLayout中的childView有layout_centerInParent属性，却没有layout_gravity，layout_weight，这是为什么呢？
 *  这是因为每个ViewGroup需要指定一个LayoutParams，用于确定支持childView支持哪些属性，比如LinearLayout指定LinearLayout.LayoutParams等
 *  如果去看LinearLayout的源码，会发现其内部定义了LinearLayout.LayoutParams，在此类中，你可以发现weight和gravity的身影。
 *
 * 五、从API角度分析ViewGroup和View的作用
 *  View 根据 ViewGroup 传入的测量值和测量模式，确定自己的宽、高（在onMeasure中完成），然后在onDraw中完成对自己的绘制；
 *  ViewGroup需要给View传入View的测量值和测量模式（在onMeasure中完成），而且对于此ViewGroup的父布局，ViewGroup也要在onMeasure中完成对自己宽、高的确定；
 *  ViewGroup需要再onLayout中完成对其childView的位置的指定。
 */
public class CustomViewGroup extends ViewGroup {

    public CustomViewGroup(Context context) {
        super(context);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 一、重写generateLayoutParams，确定该ViewGroup的LayoutParams
     * 返回MarginLayoutParams的实例，这样就为我们的ViewGroup指定了其LayoutParams为MarginLayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /**
     * 二、计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //1、获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        // 2、计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        // 3、如果ViewGroup布局是wrap_content时，根据childView的尺寸，计算容器的宽和高
        int width = 0;//ViewGroup的宽度
        int height = 0;//ViewGroup的高度
        int cCount = getChildCount();//childView的数量
        int cWidth = 0;//childView的总宽度
        int cHeight = 0;//childView的总高度
        MarginLayoutParams cParams = null;//View的测量模式
        int lHeight = 0;// 用于计算左边两个childView的高度
        int rHeight = 0;// 用于计算右边两个childView的高度，最终高度取二者之间大值
        int tWidth = 0;// 用于计算上边两个childView的宽度
        int bWidth = 0;// 用于计算下面两个childiew的宽度，最终宽度取二者之间大值
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();
            if (i == 0 || i == 1) {// 上面两个childView
                tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            if (i == 2 || i == 3) {// 下面两个childView
                bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            if (i == 0 || i == 2) {// 左面两个childView
                lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }
            if (i == 1 || i == 3) {// 右面两个childView
                rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }
        }
        width = Math.max(tWidth, bWidth);//取最大宽度
        height = Math.max(lHeight, rHeight);//去最大高度
        //4、如果是wrap_content设置为我们计算的值;否则直接设置为父容器计算的值
        setMeasuredDimension(
                (widthMode == MeasureSpec.EXACTLY) ? sizeWidth : width,
                (heightMode == MeasureSpec.EXACTLY) ? sizeHeight : height
        );
    }

    /**
     * 三、重写onLayout，对其所有childView进行定位（设置childView的绘制区域）
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;
        //遍历所有childView根据其宽和高，以及margin进行布局
        for (int i = 0; i < cCount; i++) {
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();
            int cl = 0, ct = 0, cr = 0, cb = 0;
            switch (i) {
                case 0:
                    cl = cParams.leftMargin;
                    ct = cParams.topMargin;
                    break;
                case 1:
                    cl = getWidth() - cWidth - cParams.leftMargin - cParams.rightMargin;
                    ct = cParams.topMargin;
                    break;
                case 2:
                    cl = cParams.leftMargin;
                    ct = getHeight() - cHeight - cParams.bottomMargin;
                    break;
                case 3:
                    cl = getWidth() - cWidth - cParams.leftMargin - cParams.rightMargin;
                    ct = getHeight() - cHeight - cParams.bottomMargin;
                    break;
            }
            cr = cl + cWidth;
            cb = cHeight + ct;
            childView.layout(cl, ct, cr, cb);
        }
    }
}
