android阴影效果，参考这位大佬的[ShadowLayout](https://github.com/lijiankun24/ShadowLayout)
但是也是有问题和局限性，比如不支持圆角阴影，对layout做阴影偏移之后就会显示透明背景，影响显示效果，
所以我在他的基础上再做修改，改的地方后面会很多，所以就重新开了个项目，慢慢新增功能，打算做一个比较
全面的功能库，以后直接拿来直接使用。之后如果看到比较好的实现方式也会拿来使用的，会把这个库给做完善，
方便后续同学继续使用，自己以前做阴影效果也是花费了很多时间的。

## 目前已有的功能

* 1. 矩形阴影
* 2. 圆行阴影
* 3. 圆角阴影
* 4. 左右和上下阴影偏移
* 5. 子元素可以设置match_parent

## 不支持功能和局限性

* 1. v1可以设置单边设置阴影，v2不支持

## 使用

```
    //v1
    //矩形/圆角矩形/圆形
    <com.narkang.kshadow.v1.KShadowView
        android:layout_width="300dp"
        android:layout_height="110dp"
        android:layout_margin="20dp"
        app:shadowColor="#99000000"
        app:shadowRadius="10dp"
        app:shadowShape="rectangle"
        app:shadowSide="bottom"
        >
        //里面嵌套自己的布局
    </com.narkang.kshadow.v1.KShadowView>
    //v2
    //矩形/圆角矩形/圆行  不能设置单边的，因为默认对第一个子元素设置了居中处理
    <com.narkang.kshadow.v2.KShadowView
        android:layout_width="300dp"
        android:layout_height="60dp"
        android:layout_gravity="center_horizontal"
        android:layout_marginLeft="40dp"
        android:layout_marginRight="40dp"
        app:shadowColor="#99000000"
        app:shadowDx="0dp"
        app:shadowDy="0dp"
        app:shadowRadius="11dp"
        app:shadowShape="roundRectangle"
        app:shadowRoundRadius="10dp"
        app:shadowSide="all"
        >
        //里面嵌套自己的布局
    </com.narkang.kshadow.v2.KShadowView>
```

## 支持的自定义属性

* shadowColor         阴影颜色
* shadowRadius        阴影扩散范围
* shadowDx            阴影水平偏移方向，正值向右，负值向左
* shadowDy            阴影水平偏移方向，正值向右，负值向左
* shadowShape         阴影支持形状 rectangle|oval|roundRectangle
* shadowSide          阴影在哪个边上 all|left|top|right|bottom
* shadowRoundRadius   阴影半径

## 原理

阴影效果是使用Paint的setShadowLayer实现的，它有四个参数

```
/**
 *  参数一 radius:      阴影的扩散范围
 *  参数二 dx    :      阴影水平偏移
 *  参数三 dy    :      阴影竖直偏移
 *  参数四 shadowColor: 阴影颜色
 */
public void setShadowLayer(float radius, float dx, float dy, int shadowColor) {
}

```

上面就是主要原理，其它就是自定义的FrameLayout，在绘制子元素之后，然后调用这个api来绘制阴影

## 显示效果

<div align=center>
    <img src="screenshot/KShadowView.png" width="300" height="500"/>
</div>


