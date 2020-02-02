package com.githubrepo.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("image")
fun loadImage(imageView: ImageView, url: String) {
    Glide
        .with(imageView.context)
        .load(url)
        .apply(RequestOptions.circleCropTransform())
        .into(imageView)
}

@BindingAdapter("android:visibility")
fun setVisibility(view: TextView, value: String?) {
    if (value == null) {
        view.visibility = View.GONE
    } else {
        view.visibility = View.VISIBLE
    }
}
