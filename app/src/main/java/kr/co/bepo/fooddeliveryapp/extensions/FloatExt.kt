package kr.co.bepo.fooddeliveryapp.extensions

import android.content.res.Resources

fun Float.fromDpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()