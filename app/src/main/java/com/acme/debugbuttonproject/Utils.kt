package com.acme.debugbuttonproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator

var density = 2f

val Int.dp: Int
    get() = (this * density).toInt()
val Float.dp: Float
    get() = this * density
val Double.dp: Double
    get() = this * density

val Int.f: Float
    get() = this.toFloat()

val Double.f: Float
    get() = this.toFloat()

fun <T : View> T.objectAnimate(): ViewPropertyObjectAnimator = ViewPropertyObjectAnimator.animate(this)

inline fun View.onClick(crossinline clickListener: () -> Unit) {
    setOnClickListener {
        clickListener()
    }
}

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

inline fun <reified T: Activity> Context.intentFor() = Intent(this, T::class.java)

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}