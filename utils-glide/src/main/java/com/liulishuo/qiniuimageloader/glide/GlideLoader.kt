package com.liulishuo.qiniuimageloader.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.liulishuo.qiniuimageloader.QiniuImageLoader

/**
 * Create by StefanJi on 2019-05-10
 */

class GlideLoader {

    companion object {

        @JvmStatic
        fun setGlobalPlaceHolder(@DrawableRes defaultPlaceHolder: Int, @DrawableRes defaultAvatarPlaceHolder: Int) {
            QiniuImageLoader.DEFAULT_PLACE_HOLDER = defaultPlaceHolder
            QiniuImageLoader.DEFAULT_AVATAR_PLACE_HOLDER = defaultAvatarPlaceHolder
        }

        @JvmStatic
        fun setGlobalTargetProvider(target: Target<Drawable>) {
            GlideQiniuImageLoader.DEFAULT_TARGET = target
        }

        @JvmStatic
        fun createLoader(context: Context, oriUrl: String) =
                GlideQiniuImageLoader(context, oriUrl)

        @JvmStatic
        fun createLoader(imageView: ImageView, oriUrl: String) =
                GlideQiniuImageLoader(imageView, oriUrl)

        @JvmStatic
        internal fun display(
                imageView: ImageView,
                url: String,
                defaultDrawable: Drawable?,
                transformation: Transformation<Bitmap>?,
                requestListener: RequestListener<Drawable>?
        ) {
            Glide.with(imageView)
                    .load(url)
                    .apply {
                        transformation?.let { transform(it) }
                    }
                    .placeholder(defaultDrawable)
                    .listener(requestListener)
                    .into(imageView)
        }

        @JvmStatic
        internal fun fetch(context: Context, url: String, target: Target<Drawable>?) {
            Glide.with(context)
                    .load(url)
                    .apply {
                        if (target != null) {
                            into(target)
                        } else {
                            preload()
                        }
                    }
        }

    }
}