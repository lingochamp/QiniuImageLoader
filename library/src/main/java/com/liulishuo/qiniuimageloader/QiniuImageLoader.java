/**
 * Copyright (c) 2015 LingoChamp Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liulishuo.qiniuimageloader;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Jacksgong on 15/8/3.
 *
 * @Api: http://developer.qiniu.com/docs/v6/api/reference/fop/image/imagemogr2.html
 */
public abstract class QiniuImageLoader<T extends QiniuImageLoader> {

    private final static String TAG = "QiniuImageLoader";

    private int mode = MODE_FIT_XY;
    private int w = 0;
    private int h = 0;
    private String oriUrl;
    private ImageView imageView;

    /**
     * 限定缩略图的宽最少为<Width>，高最少为<Height>，进行等比缩放，居中裁剪。
     * <p/>
     * 转后的缩略图通常恰好是 <Width>x<Height> 的大小（有一个边缩放的时候会因为超出矩形框而被裁剪掉多余部分）。
     * <p/>
     * 如果只指定 w 参数或只指定 h 参数，代表限定为长宽相等的正方图。
     * <p/>
     * 强制使用给定w与h
     */
    private final static int MODE_CENTER_CROP = 1;

    /**
     * 限定缩略图的宽最多为<Width>，高最多为<Height>，进行等比缩放，不裁剪。
     * <p/>
     * 如果只指定 w 参数则表示限定宽（长自适应），只指定 h 参数则表示限定长（宽自适应）。
     * <p/>
     * 如果给定的w或h大于原图，则采用原图
     */
    private final static int MODE_FIT_XY = 2;

    /**
     * 强制需要原图，通常是图片需要动态缩放才需要
     */
    private final static int MODE_FORCE_ORIGIN = -1;


    public QiniuImageLoader(final Context context, final String oriUrl) {
        this.context = context;
        this.oriUrl = oriUrl;
    }

    public QiniuImageLoader(final ImageView imageView, final String oriUrl) {
        this.imageView = imageView;
        this.oriUrl = oriUrl;
    }

    /**
     * 指定最大宽度 为w
     *
     * @param w
     * @return
     */
    public T w(final int w) {
        this.w = w;
        return (T) this;
    }

    /**
     * 指定最大宽度
     *
     * @param wResource DimenRes
     * @return
     */
    public T wR(final int wResource) {
        if (getContext() == null) {
            return (T) this;
        }
        this.w = getContext().getResources().getDimensionPixelSize(wResource);
        return (T) this;

    }

    /**
     * 指定最大宽高
     *
     * @param size
     * @return
     */
    public T size(final int size) {
        w(size);
        h(size);
        return (T) this;
    }

    /**
     * 指定最大宽高
     *
     * @param sizeResource DimenRes
     * @return
     */
    public T sizeR(final int sizeResource) {
        if (getContext() == null) {
            return (T) this;
        }

        final int size = getContext().getResources().getDimensionPixelSize(sizeResource);
        size(size);

        return (T) this;
    }

    /**
     * 指定最大高度
     *
     * @param h
     * @return
     */
    public T h(final int h) {
        this.h = h;
        return (T) this;
    }

    /**
     * 指定最大高度
     *
     * @param hResource DimenRes
     * @return
     */
    public T hR(final int hResource) {
        if (getContext() == null) {
            return (T) this;
        }

        this.h = getContext().getResources().getDimensionPixelSize(hResource);
        return (T) this;
    }

    /**
     * 指定模式为mode
     *
     * @param mode
     * @return
     */
    public T mode(final int mode) {
        this.mode = mode;
        return (T) this;
    }

    /**
     * {@link #mode} to {@link #MODE_FIT_XY}
     * <p/>
     * 指定模式为FitXY
     *
     * @return
     * @see <url></url>https://github.com/lingochamp/QiniuImageLoader</url>
     */
    public T fitXY() {
        return mode(MODE_FIT_XY);
    }

    /**
     * 指定模式为CenterCrop
     *
     * @return
     * @see <url>https://github.com/lingochamp/QiniuImageLoader</url>
     */
    public T centerCrop() {
        return mode(MODE_CENTER_CROP);
    }

    /**
     * 请求图片最大宽高为GL10.GL_MAX_TEXTURE_SIZE
     *
     * @return
     * @see #MODE_FORCE_ORIGIN
     */
    public T forceOrigin() {
        return mode(MODE_FORCE_ORIGIN);
    }

    /**
     * 根据七牛提供的API生成目标URL
     *
     * @return 目标URL
     * @see <url>http://developer.qiniu.com/docs/v6/api/reference/fop/image/imageview2.html</url>
     */
    public String createQiniuUrl() {
        String u = this.oriUrl;

        if (!isUrl(u)) {
            return u;
        }


        int width = this.w;
        int height = this.h;
        final int maxWidth = getScreenWith();
        final int maxHeight = getMaxHeight();

        if (this.mode == MODE_FORCE_ORIGIN) {
            width = GL10.GL_MAX_TEXTURE_SIZE;
            height = GL10.GL_MAX_TEXTURE_SIZE;
        } else {
            // 其中一个有效
            width = (height <= 0 && width > 0 && width > maxWidth) ? maxWidth : width;
            height = (width <= 0 && height > 0 && height > maxHeight) ? maxHeight : height;


            // 两个都无效
            width = (width <= 0 && height <= 0) ? maxWidth : width;

            //两个都有效
            if (width > 0 && height > 0) {
                if (width > maxWidth) {
                    height = (int) (height * ((float) maxWidth / width));
                    width = maxWidth;
                }

                if (height > maxHeight) {
                    width = (int) (width * ((float) maxHeight / height));
                    height = maxHeight;
                }
            }
        }


        // size /thumbnail/<width>x<height>[>最大宽高/<最小宽高]
        // /thumbnail/[!]<width>x<height>[r] 限定短边，生成不小于<width>x<height>的 默认不指定!与r，为限定长边
        // %3E = URLEncoder.encoder(">", "utf-8")
        final String maxHeightUtf8 = "%3E";

        String resizeParams = "";
        if (width > 0 || height > 0) {
            if (width <= 0) {
                // h > 0
                resizeParams = this.mode == MODE_FORCE_ORIGIN || this.mode == MODE_FIT_XY ?
                        // fit xy
                        String.format("/thumbnail/x%d%s", height, maxHeightUtf8) :
                        // center crop
                        String.format("/thumbnail/!%dx%dr/gravity/Center/crop/%dx%d", height, height, height, height);

            } else if (height <= 0) {
                // w > 0
                resizeParams = this.mode == MODE_FORCE_ORIGIN || this.mode == MODE_FIT_XY ?
                        // fit xy
                        String.format("/thumbnail/%dx%s", width, maxHeightUtf8) :
                        // center crop
                        String.format("/thumbnail/!%dx%dr/gravity/Center/crop/%dx%d", width, width, width, width);
            } else {
                // h > 0 && w > 0
                resizeParams = this.mode == MODE_FORCE_ORIGIN || this.mode == MODE_FIT_XY ?
                        // fit xy
                        String.format("/thumbnail/%dx%d%s", width, height, maxHeightUtf8) :
                        // center crop
                        String.format("/thumbnail/!%dx%dr/gravity/Center/crop/%dx%d", width, height, width, height);
            }
        }

        //op
        String opParams = "";
        for (Op op : opList) {
            opParams += op.getOpUrlParam();
        }

        // format
        String formatParams = "";

        switch (this.format) {
            case webp:
            case jpg:
            case gif:
            case png:
                formatParams = String.format("/format/%s", this.format.toString());
                break;
            case origin:
                break;
        }

        if (!TextUtils.isEmpty(resizeParams) || !TextUtils.isEmpty(formatParams)) {
            if (oriUrl.contains("?ImageView") || oriUrl.contains("?imageMogr2") || oriUrl.contains("?imageView2")) {
                Log.e(TAG, String.format("oriUrl should create 7Niu url by self, %s", oriUrl));

                if (!oriUrl.contains("/format")) {
                    u = String.format("%s%s", oriUrl, formatParams);
                }
            } else {
                u = String.format("%s?imageMogr2/auto-orient%s%s%s", oriUrl, resizeParams, opParams, formatParams);
            }

        }


        Log.d(TAG, String.format("【oriUrl】: %s 【url】: %s , (w: %d, h: %d)", oriUrl, u, w, h));
        return u;
    }

    public void clear() {
        this.context = null;
        this.imageView = null;
        this.mode = MODE_FIT_XY;
        this.w = 0;
        this.h = 0;
        this.opList.clear();
    }


    /**
     * 宽度等于屏幕的宽度
     *
     * @return
     */
    public T screenW() {
        if (getContext() == null) {
            return (T) this;
        }
        this.w = getScreenWith();
        return (T) this;
    }

    /**
     * 宽度为屏幕的宽度的一半
     *
     * @return
     */
    public T halfScreenW() {
        if (getContext() == null) {
            return (T) this;
        }
        this.w = getScreenWith() / 2;
        return (T) this;
    }

    /**
     * @param n n倍
     * @return 高度会等于 宽度的n倍
     */
    public T wTimesN2H(final float n) {
        this.h = (int) (this.w * n);
        return (T) this;
    }


    private static boolean isUrl(final String url) {
        return !TextUtils.isEmpty(url) && url.startsWith("http");
    }

    protected Context getContext() {
        Context context = this.context;

        if (context == null) {
            context = this.imageView == null ? null : this.imageView.getContext();
        }

        return context;
    }

    // for format
    private enum Format {
        origin, jpg, gif, png, webp
    }

    private enum OpName {
        none, blur, rotate
    }

    private static class Op {
        OpName name = OpName.none;
        int val1;
        int val2;

        /**
         * @param radius [1, 50]
         * @param sigma  [0, -]
         * @return
         */
        public Op blur(final int radius, final int sigma) {
            this.name = OpName.blur;
            this.val1 = radius;
            this.val2 = sigma;
            return this;
        }

        /**
         * @param rotateDegree [1, 360]
         * @return
         */
        public Op rotate(final int rotateDegree) {
            this.name = OpName.rotate;
            this.val1 = rotateDegree;
            return this;
        }

        public String getOpUrlParam() {
            switch (name) {
                case none:
                    return "";
                case blur:
                    return String.format("/blur/%dx%d", this.val1, this.val2);
                case rotate:
                    return String.format("/rotate/%d", this.val1);
            }

            return "";
        }


    }

    private final static Format COMMEND_FORMAT = Format.webp;
    private Format format = COMMEND_FORMAT;

    private List<Op> opList = new ArrayList<>();

    /**
     * 请求图片进行高斯模糊处理
     *
     * @param radius [1, 50]
     * @param sigma  [0, -]
     * @return
     */
    public T addOpBlur(final int radius, final int sigma) {
        opList.add(new Op().blur(radius, sigma));
        return (T) this;
    }

    /**
     * 请求图片进行旋转处理
     *
     * @param rotateDegree [1, 360]
     * @return
     */
    public T addOpRotate(final int rotateDegree) {
        opList.add(new Op().rotate(rotateDegree));
        return (T) this;
    }

    /**
     * 请求图片jpg格式
     *
     * @return
     */
    public T formatJpg() {
        this.format = Format.jpg;
        return (T) this;
    }

    /**
     * 请求图片原格式
     *
     * @return
     */
    public T formatOrigin() {
        this.format = Format.origin;
        return (T) this;
    }

    /**
     * 请求图片png格式
     *
     * @return
     */
    public T formatPng() {
        this.format = Format.png;
        return (T) this;
    }

    /**
     * 请求图片webp格式(默认格式)
     *
     * @return
     */
    public T formatWebp() {
        this.format = Format.webp;
        return (T) this;
    }

    private Context context;

    private int getScreenWith() {
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    private int getMaxHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 加载图片到目标ImageView上并清理所有变量
     *
     * @see #attachWithNoClear()
     */
    public void attach() {
        attachWithNoClear();
        clear();
    }

    /**
     * for download & attach image 2 imageView
     */
    public abstract void attachWithNoClear();

    /**
     * for just download image
     */
    public abstract void fetch();

    protected ImageView getImageView() {
        return imageView;
    }

    protected String getOriUrl() {
        return oriUrl;
    }

    protected int getW() {
        return w;
    }

    protected int getH() {
        return h;
    }

    protected int getMode() {
        return mode;
    }

    protected Format getFormat() {
        return format;
    }

    protected List<Op> getOpList() {
        return opList;
    }
}
