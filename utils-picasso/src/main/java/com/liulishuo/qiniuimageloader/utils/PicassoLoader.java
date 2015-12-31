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
 */
package com.liulishuo.qiniuimageloader.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

/**
 * Created by Jacksgong on 12/13/15.
 */
public class PicassoLoader {

    private final static String TAG = "PicassoLoader";

    /**
     * 设置全局默认的占位图
     *
     * @param defaultPlaceHolder
     * @param defaultAvatarPlaceHolder
     */
    public static void setGlobalPlaceHolder(final int defaultPlaceHolder, final int defaultAvatarPlaceHolder) {
        PicassoQiniuImageLoader.DEFAULT_PLACE_HOLDER = defaultPlaceHolder;
        PicassoQiniuImageLoader.DEFAULT_AVATAR_PLACE_HOLDER = defaultAvatarPlaceHolder;
    }

    /**
     * 设置全局默认的Target提供者
     *
     * @param provider Nullable
     * @see PicassoQiniuImageLoader#DEFAULT_TARGET_PROVIDER
     */
    public static void setGlobalTargetProvider(final TargetProvider provider) {
        PicassoQiniuImageLoader.DEFAULT_TARGET_PROVIDER = provider;
    }

    public static PicassoQiniuImageLoader createLoader(final ImageView imageView, final String oriUrl) {
        return new PicassoQiniuImageLoader(imageView, oriUrl);
    }

    static void display(ImageView imageView, String url, Drawable drawable, final Transformation transformation, Target target, Callback callback) {
        if (imageView == null) {
            return;
        }


        //最终都是在这里触发
        attachToImage(imageView, url, drawable, transformation, target, callback);
    }

    static void fetch(final Context context, String url, Target callback) {
        RequestCreator creator = attach(context, url);

        if (creator == null) {
            Log.e(TAG, String.format("creator == null : url[%s], context[%s]", url, context));
            return;
        }

        if (callback == null) {
            creator.fetch();
        } else {
            creator.into(callback);
        }

    }


    static void attachToImage(final ImageView imageView, final String url, final Drawable placeHolder, final Transformation transformation, final Target target, final Callback callback) {
        if (imageView == null) {
            return;
        }

        final RequestCreator creator = attach(imageView.getContext(), url, placeHolder);
        if (creator == null) {
            return;
        }

        if (transformation != null) {
            creator.transform(transformation);
        }

        if (target != null) {
            creator.tag(target);
        }

        try {
            creator.into(imageView, callback);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    static RequestCreator attach(final Context context, String url) {
        return attach(context, url, null);
    }

    static RequestCreator attach(final Context context, String url, final Drawable placeHolder) {
        if (context == null) {
            return null;
        }

        try {
            url = url == null ? null :
                    (url.trim().length() <= 0 ? null : url);


            if (url != null && !url.startsWith("http")) {
                url = "file:" + url;
            }

            RequestCreator creator = Picasso.with(context)
                    .load(url);

            if (placeHolder != null) {
                creator.placeholder(placeHolder);
            }

            return creator;

        } catch (Throwable e) {
            e.printStackTrace();
        }


        return null;
    }

    public interface TargetProvider {
        Target get(final String originUrl, final Context context);
    }

}

