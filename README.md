# CustomViewGroup01
自定义一个ViewGroup，内部可以传入0到4个childView，分别依次显示在左上角，右上角，左下角，右下角
/** *
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