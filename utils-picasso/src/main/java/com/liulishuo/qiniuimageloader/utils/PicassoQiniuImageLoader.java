package com.liulishuo.qiniuimageloader.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import com.liulishuo.qiniuimageloader.QiniuImageLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.security.InvalidParameterException;

/**
 * Copyright (c) 2015 LingoChamp Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by Jacksgong on 12/13/15.
 */
public class PicassoQiniuImageLoader extends QiniuImageLoader<PicassoQiniuImageLoader> {

    static int DEFAULT_PLACE_HOLDER = 0;
    static int DEFAULT_AVATAR_PLACE_HOLDER = 0;

    /**
     * 建议用于绑定Activity生命周期，回收网络资源所用
     * (如Activity#onResume时，根据target区分出其他Activity，并将他们全部暂停)
     */
    static PicassoLoader.TargetProvider DEFAULT_TARGET_PROVIDER = null;

    private boolean isAvatar = false;

    public PicassoQiniuImageLoader(Context context, String oriUrl) {
        super(context, oriUrl);
    }

    public PicassoQiniuImageLoader(ImageView imageView, String oriUrl) {
        super(imageView, oriUrl);
    }


    /**
     * 占位图采用默认头像的占位图
     *
     * @return
     * @see #DEFAULT_AVATAR_PLACE_HOLDER
     */
    public PicassoQiniuImageLoader avatar() {
        this.isAvatar = true;
        return this;
    }


    /**
     * 使用Picasso加载图片到目标ImageView上，并且不清理存储的各类属性参数
     */
    @Override
    public void attachWithNoClear() {
        if (getImageView() == null) {
            throw new InvalidParameterException(String.format("imageView must not be null! %s", getOriUrl()));
        }

        String u = createQiniuUrl();

        Drawable d = this.defaultDrawable;

        if (d == null) {
            if (isAvatar) {
                d = getDrawable(getImageView(), DEFAULT_AVATAR_PLACE_HOLDER);
            } else {
                d = getDrawable(getImageView(), DEFAULT_PLACE_HOLDER);
            }
        }

        PicassoLoader.display(getImageView(), u, d, transformation, findTarget(), attachCallback);
    }

    private Target findTarget() {
        if (this.target != null) {
            return this.target;
        }

        if (DEFAULT_TARGET_PROVIDER != null) {
            return DEFAULT_TARGET_PROVIDER.get(getOriUrl(), getContext());
        }

        return null;
    }

    private Drawable defaultDrawable;

    /**
     * 指定占位图
     *
     * @param defaultDrawable DrawableRes
     * @return
     */
    public PicassoQiniuImageLoader defaultD(final int defaultDrawable) {
        this.defaultDrawable = getDrawable(getImageView(), defaultDrawable);
        return this;
    }

    /**
     * 指定占位图
     *
     * @param defaultDrawable
     * @return
     */
    public PicassoQiniuImageLoader defaultD(final Drawable defaultDrawable) {
        this.defaultDrawable = defaultDrawable;
        return this;
    }

    /**
     * @param imageView
     * @param resourceId DrawableRes
     * @return
     */
    protected Drawable getDrawable(final ImageView imageView, final int resourceId) {
        if (resourceId == 0) {
            return null;
        }

        Drawable drawable = null;
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawable = imageView.getResources().getDrawable(resourceId);
            } else {
                drawable = imageView.getContext().getDrawable(resourceId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return drawable;
    }

    private Target target;

    /**
     * 指定picasso的Target
     *
     * @param target
     * @return
     */
    public PicassoQiniuImageLoader target(final Target target) {
        this.target = target;
        return this;
    }

    private Transformation transformation;

    /**
     * 指定Transformation
     *
     * @param transformation
     * @return
     */
    public PicassoQiniuImageLoader transformation(final Transformation transformation) {
        this.transformation = transformation;
        return this;
    }

    private Callback attachCallback;

    /**
     * 指定picasso的Callback
     *
     * @param attachCallback
     * @return
     */
    public PicassoQiniuImageLoader attachCallback(final Callback attachCallback) {
        this.attachCallback = attachCallback;
        return this;
    }

    /**
     * 下载图片到本地
     */
    @Override
    public void fetch() {
        if (getContext() == null) {
            throw new InvalidParameterException(String.format("can't get context ?? url[%s]", getOriUrl()));
        }

        PicassoLoader.fetch(getContext(), createQiniuUrl(), findTarget());
    }

    @Override
    public void clear() {
        super.clear();
        this.attachCallback = null;
        this.transformation = null;
        this.target = null;
    }
}
