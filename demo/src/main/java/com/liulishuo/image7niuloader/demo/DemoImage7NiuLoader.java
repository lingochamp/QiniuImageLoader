package com.liulishuo.image7niuloader.demo;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.liulishuo.image7niuload.Image7NiuLoader;
import com.squareup.picasso.Callback;

import java.security.InvalidParameterException;

/**
 * Created by Jacksgong on 12/13/15.
 */
public class DemoImage7NiuLoader extends Image7NiuLoader {

    private boolean isAvatar = false;

    public DemoImage7NiuLoader(Context context, String oriUrl) {
        super(context, oriUrl);
    }

    public DemoImage7NiuLoader(ImageView imageView, String oriUrl) {
        super(imageView, oriUrl);
    }


    public DemoImage7NiuLoader avatar() {
        this.isAvatar = true;
        return this;
    }


    @Override
    public void attachWithNoClear() {
        if (getImageView() == null) {
            throw new InvalidParameterException(String.format("imageview must not be null! %s", getOriUrl()));
        }

        String u = create7NiuUrl();

        Drawable d = getDefaultDrawable();

        if (d == null) {
            if (isAvatar) {
                // TODO default avatar resource
                d = getDrawable(getImageView(), R.mipmap.ic_launcher);
            } else {
                // TODO default image resource
                d = getDrawable(getImageView(), R.mipmap.ic_launcher);
            }
        }

        ImageLoader.display(getImageView(), u, d, null, attachCallback);
    }


    private ImageLoader.FetchCallBack fetchCallback;

    public Image7NiuLoader fetchCallback(final ImageLoader.FetchCallBack fetchCallback) {
        this.fetchCallback = fetchCallback;
        return this;
    }

    private Callback attachCallback;

    public Image7NiuLoader attachCallback(final Callback attachCallback) {
        this.attachCallback = attachCallback;
        return this;
    }

    @Override
    public void fetch() {
        if (getContext() == null) {
            throw new InvalidParameterException(String.format("can't get context ?? url[%s]", getOriUrl()));
        }

        ImageLoader.fetch(getContext(), create7NiuUrl(), fetchCallback);
    }

    @Override
    public void clear() {
        super.clear();
        this.attachCallback = null;
        this.fetchCallback = null;
    }
}
