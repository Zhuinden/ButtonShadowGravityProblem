package com.acme.debugbuttonproject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.AttrRes
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class CustomSecondaryShadowLineLayout : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var shouldRenderLine = true
        private set(value) {
            val current = field
            field = value
            if (current != value) {
                invalidate()
            }
        }

    private val linePaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
    }

    var lineColor = Color.parseColor("#FFFFFF")
        set(value) {
            field = value
            linePaint.color = value
            invalidate()
        }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            handler.postDelayed({
                shouldRenderLine = false
            }, 30L) // the middle of the 60ms long translation anim inside CustomButton
        }
        if (ev.action == MotionEvent.ACTION_UP) {
            handler.postDelayed({
                shouldRenderLine = true
            }, 30L) // the middle of the 60ms long translation anim inside CustomButton
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (shouldRenderLine) {
            canvas.drawRect(0f, (canvas.height - 4.dp).f, canvas.width.f, canvas.height.f, linePaint)
        }
    }
}