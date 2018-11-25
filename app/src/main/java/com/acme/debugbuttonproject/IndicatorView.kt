package com.acme.debugbuttonproject

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.AttrRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

class IndicatorView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    var indicatorCount = 0
        set(value) {
            field = value
            invalidate()
        }
    var selectedIndicatorIndex = 0
        set(value) {
            field = value
            invalidate()
        }

    private val linePaint = Paint()
    private val selectedLinePaint = Paint()

    init {
        if(!isInEditMode) {
            linePaint.color = ContextCompat.getColor(context, R.color.gameDetailsPageIndicatorInactive)
            selectedLinePaint.color = ContextCompat.getColor(context, R.color.gameDetailsPageIndicatorActive)
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (indicatorCount == 0) {
            return
        }

        for (i in 0 until indicatorCount) {
            drawIndicator(canvas, linePaint, i)
        }

        drawIndicator(canvas, selectedLinePaint, selectedIndicatorIndex)
    }

    private fun drawIndicator(canvas: Canvas, paint: Paint, indicatorIndex: Int) {
        val left = width.f * indicatorIndex / indicatorCount
        val right = width.f * (indicatorIndex+1) / indicatorCount
        canvas.drawRect(left + if (indicatorIndex == 0) 0 else 1.dp,
            0f, if (indicatorIndex < indicatorCount - 1) {
            right
        } else width.f, height.f, paint)
    }
}