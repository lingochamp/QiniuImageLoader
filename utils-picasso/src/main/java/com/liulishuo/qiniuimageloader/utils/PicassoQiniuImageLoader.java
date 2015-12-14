package com.liulishuo.qiniuimageloader.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
 *      http://www.apache.org/licenses/LICENSE-2.0
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

    static PicassoLoader.TargetProvider DEFAULT_TARGET_PROVIDER = null;

    private boolean isAvatar = false;

    public PicassoQiniuImageLoader(Context context, String oriUrl) {
        super(context, oriUrl);
    }

    public PicassoQiniuImageLoader(ImageView imageView, String oriUrl) {
        super(imageView, oriUrl);
    }


    public PicassoQiniuImageLoader avatar() {
        this.isAvatar = true;
        return this;
    }


    @Override
    public void attachWithNoClear() {
        if (getImageView() == null) {
            throw new InvalidParameterException(String.format("imageView must not be null! %s", getOriUrl()));
        }

        String u = create7NiuUrl();

        Drawable d = getDefaultDrawable();

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

    private Target target;

    public PicassoQiniuImageLoader target(final Target target) {
        this.target = target;
        return this;
    }

    private Transformation transformation;

    public PicassoQiniuImageLoader transform(final Transformation transformation) {
        this.transformation = transformation;
        return this;
    }

    private PicassoLoader.FetchCallBack fetchCallback;

    public PicassoQiniuImageLoader fetchCallback(final PicassoLoader.FetchCallBack fetchCallback) {
        this.fetchCallback = fetchCallback;
        return this;
    }

    private Callback attachCallback;

    public PicassoQiniuImageLoader attachCallback(final Callback attachCallback) {
        this.attachCallback = attachCallback;
        return this;
    }

    @Override
    public void fetch() {
        if (getContext() == null) {
            throw new InvalidParameterException(String.format("can't get context ?? url[%s]", getOriUrl()));
        }

        PicassoLoader.fetch(getContext(), create7NiuUrl(), fetchCallback);
    }

    @Override
    public void clear() {
        super.clear();
        this.attachCallback = null;
        this.fetchCallback = null;
        this.transformation = null;
        this.target = null;
    }
}
