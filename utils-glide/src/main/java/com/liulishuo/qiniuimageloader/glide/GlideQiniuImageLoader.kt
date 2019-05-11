package com.liulishuo.qiniuimageloader.glide

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.liulishuo.qiniuimageloader.QiniuImageLoader

/**
 * Create by StefanJi on 2019-05-10
 */

class GlideQiniuImageLoader : QiniuImageLoader<GlideQiniuImageLoader> {

    private var transformation: BitmapTransformation? = null
    private var requestListener: RequestListener<Drawable>? = null
    private var target: Target<Drawable>? = null

    internal constructor(context: Context, oriUrl: String) : super(context, oriUrl)

    internal constructor(imageView: ImageView, oriUrl: String) : super(imageView, oriUrl)

    companion object {
        var DEFAULT_TARGET: Target<Drawable>? = null
    }

    override fun attachWithNoClear(url: String) {
        GlideLoader.display(imageView, url, defaultDrawable, transformation, requestListener)
    }

    override fun fetch() {
        if (target == null) {
            target = DEFAULT_TARGET
        }
        GlideLoader.fetch(context, createQiniuUrl(), target)
    }

    fun transformation(transformation: BitmapTransformation): GlideQiniuImageLoader {
        this.transformation = transformation
        return this
    }

    fun attachCallback(listener: RequestListener<Drawable>): GlideQiniuImageLoader {
        this.requestListener = listener
        return this
    }

    fun target(target: Target<Drawable>): GlideQiniuImageLoader {
        this.target = target
        return this
    }
}