package com.ecmp.edm.entity;

/**
 * *************************************************************************************************
 * <p/>
 * 实现功能：矩形类
 * <p>
 * ------------------------------------------------------------------------------------------------
 * 版本          变更时间             变更人                     变更原因
 * ------------------------------------------------------------------------------------------------
 * 1.0.00      2017-07-14 13:32      王锦光(wangj)                新建
 * <p/>
 * *************************************************************************************************
 */
public class ImageRectangle {
    /**
     * 宽度
     */
    private int width;
    /**
     * 高度
     */
    private int height;

    public ImageRectangle(int w, int h) {
        width = w;
        height = h;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
