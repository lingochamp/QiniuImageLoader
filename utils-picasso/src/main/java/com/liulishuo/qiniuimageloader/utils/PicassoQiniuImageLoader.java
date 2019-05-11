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
package com.liulishuo.qiniuimageloader.utils;

import android.content.Context;
import android.widget.ImageView;

import com.liulishuo.qiniuimageloader.QiniuImageLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.security.InvalidParameterException;

/**
 * Created by Jacksgong on 12/13/15.
 */
public class PicassoQiniuImageLoader extends QiniuImageLoader<PicassoQiniuImageLoader> {

    /**
     * 建议用于绑定Activity生命周期，回收网络资源所用
     * (如Activity#onResume时，根据target区分出其他Activity，并将他们全部暂停)
     */
    static PicassoLoader.TargetProvider DEFAULT_TARGET_PROVIDER = null;

    public PicassoQiniuImageLoader(Context context, String oriUrl) {
        super(context, oriUrl);
    }

    public PicassoQiniuImageLoader(ImageView imageView, String oriUrl) {
        super(imageView, oriUrl);
    }

    @Override
    protected void attachWithNoClear(String url) {
        PicassoLoader.display(
                getImageView(),
                url,
                this.defaultDrawable,
                transformation,
                findTarget(),
                attachCallback);
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

    private Target target;

    /**
     * 指定picasso的Target
     *
     * @param target com.squareup.picasso.Target
     * @return PicassoQiniuImageLoader
     */
    public PicassoQiniuImageLoader target(final Target target) {
        this.target = target;
        return this;
    }

    private Transformation transformation;

    /**
     * 指定Transformation
     *
     * @param transformation Image transformation
     * @return PicassoQiniuImageLoader
     */
    public PicassoQiniuImageLoader transformation(final Transformation transformation) {
        this.transformation = transformation;
        return this;
    }

    private Callback attachCallback;

    /**
     * 指定picasso的Callback
     *
     * @param attachCallback Image load callback
     * @return PicassoQiniuImageLoader
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
    }
}
