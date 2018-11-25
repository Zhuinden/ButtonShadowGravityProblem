package com.acme.debugbuttonproject

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.annotation.AttrRes
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.MotionEvent

class CustomButton : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    enum class ShadowMode { ARC, LINE }

    enum class ShowLineMode { IF_ENABLED, ALWAYS, NEVER }

    private var leftLineEnabled = false
    private var leftLineColor = Color.parseColor("#000000")
        set(value) {
            field = value
            leftLinePaint.color = value
            invalidate()
        }

    private var shadowMode: ShadowMode = ShadowMode.ARC
    private var showLineMode: ShowLineMode = ShowLineMode.IF_ENABLED

    var lineColor = Color.parseColor("#FFFFFF")
        set(value) {
            field = value
            linePaint.color = value
            invalidate()
        }

    private val leftLinePaint = Paint().apply {
        color = leftLineColor
        style = Paint.Style.FILL_AND_STROKE
    }

    private val linePaint = Paint().apply {
        color = lineColor
        style = Paint.Style.FILL_AND_STROKE
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CustomButton,
            0, 0)

        lineColor = a.getColor(R.styleable.CustomButton_lineColor, lineColor)
        shadowMode = ShadowMode.values()[a.getInt(R.styleable.CustomButton_shadowMode, ShadowMode.ARC.ordinal)]
        showLineMode = ShowLineMode.values()[a.getInt(R.styleable.CustomButton_showLineMode, ShowLineMode.IF_ENABLED.ordinal)]

        leftLineColor = a.getColor(R.styleable.CustomButton_leftColor, leftLineColor)
        leftLineEnabled = a.getBoolean(R.styleable.CustomButton_leftLineEnabled, false)
        a.recycle()
    }

    var hasFocus = false

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        val result = super.onGenericMotionEvent(event)

        val currentFocus = hasFocus

        when (event.action) {
            MotionEvent.ACTION_HOVER_ENTER -> hasFocus = true
            MotionEvent.ACTION_HOVER_EXIT -> hasFocus = false
        }

        runAnimatorsIfNeeded(currentFocus)

        return result
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val result = super.onTouchEvent(event)

        val currentFocus = hasFocus

        when (event.action) {
            MotionEvent.ACTION_DOWN -> hasFocus = true
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> hasFocus = false
        }

        runAnimatorsIfNeeded(currentFocus)

        return result
    }

    private fun runAnimatorsIfNeeded(currentFocus: Boolean) {
        if (isEnabled && hasFocus != currentFocus) {
            objectAnimate().translationY(when {
                hasFocus -> 4.dp.f
                else -> 0.f
            }).setDuration(60L)
                .get()
                .apply {
                    addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                        private var didInvalidate = false

                        override fun onAnimationUpdate(animator: ValueAnimator) {
                            val translation = animator.animatedValue as Float
                            if (!didInvalidate && ((hasFocus && translation > 2) || !hasFocus && translation < 2)) {
                                invalidate()
                                didInvalidate = true
                            }
                        }

                    })
                }.start()
        }
    }

    private val rectTopLeft: RectF = RectF()
    private val rectTopRight: RectF = RectF()
    private val rectBottomLeft: RectF = RectF()
    private val rectBottomRight: RectF = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (width > 0 && height > 0) {
            if (leftLineEnabled) {
                canvas.drawRect(0f, 0f, 1.dp.f, height.f, leftLinePaint)
            }

            if (!hasFocus && (showLineMode == ShowLineMode.ALWAYS || ((isEnabled) && showLineMode == ShowLineMode.IF_ENABLED))) {
                when (shadowMode) {
                    ShadowMode.LINE -> canvas.drawRect(0.f, (measuredHeight - 4.dp).f, measuredWidth.f, (measuredHeight).f, linePaint)
                    ShadowMode.ARC -> {
                        rectTopLeft.set(0.f, (measuredHeight - 16.dp).f, 20.dp.f, measuredHeight.f)
                        canvas.drawArc(rectTopLeft, 90.f, 90.f, false, linePaint)

                        rectTopRight.set((measuredWidth - 20.dp).f, (measuredHeight - 16.dp).f, measuredWidth.f, measuredHeight.f)
                        canvas.drawArc(rectTopRight, 0.f, 90.f, false, linePaint)

                        rectBottomLeft.set(0.f, (measuredHeight - 8.dp).f, 10.dp.f, measuredHeight.f)
                        canvas.drawArc(rectBottomLeft, 90.f, 90.f, true, linePaint)

                        rectBottomRight.set((measuredWidth - 10.dp).f, (measuredHeight - 8.dp).f, measuredWidth.f, measuredHeight.f)
                        canvas.drawArc(rectBottomRight, 0.f, 90.f, true, linePaint)

                        canvas.drawRect(0.f, (measuredHeight - 8.dp).f, 1.dp.f, (measuredHeight - 3.dp).f, linePaint)
                        canvas.drawRect((measuredWidth).f, (measuredHeight - 8.dp).f, (measuredWidth - 1.dp).f, (measuredHeight - 3.dp).f, linePaint)

                        canvas.drawRect(4.dp.f, (measuredHeight - 4.dp).f, (measuredWidth - 4.dp).f, (measuredHeight).f, linePaint)
                    }
                }
            }
        }
    }
}