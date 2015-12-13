package com.liulishuo.image7niuloader.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
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
public class ImageLoader {

    private final static String TAG = "ImageLoader";

    public static DemoImage7NiuLoader display7NiuAvatar(final ImageView imageView, final String oriUrl) {
        DemoImage7NiuLoader image7NiuLoader = new DemoImage7NiuLoader(imageView, oriUrl);
        return image7NiuLoader.avatar();
    }

    public static DemoImage7NiuLoader display7Niu(final ImageView imageView, final String oriUrl) {
        return new DemoImage7NiuLoader(imageView, oriUrl);
    }

    public static DemoImage7NiuLoader display7Niu(final ImageView imageView, final String oriUrl, @DrawableRes final int defaultDrawable) {
        DemoImage7NiuLoader image7NiuLoader = new DemoImage7NiuLoader(imageView, oriUrl);
        image7NiuLoader.defaultD(defaultDrawable);
        return image7NiuLoader;
    }

    static void display(ImageView imageView, String url, Drawable drawable, final Transformation transformation, Callback callback) {
        if (imageView == null) {
            return;
        }


        //最终都是在这里触发
        attachToImage(imageView, url, drawable, transformation, null, callback);
    }

    static void fetch(final Context context, String url, FetchCallBack callback) {
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

    public static class FetchCallBack implements Target {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}

