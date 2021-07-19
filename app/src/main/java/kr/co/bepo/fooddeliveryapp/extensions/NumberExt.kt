package kr.co.bepo.fooddeliveryapp.extensions

import android.content.res.Resources

fun Number.fromDpToPx(): Int = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()