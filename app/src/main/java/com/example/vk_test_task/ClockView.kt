package com.example.vk_test_task

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import java.time.LocalDateTime
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


/**
 * TODO: document your custom view class.
 */
class ClockView : androidx.appcompat.widget.AppCompatImageView {


    private var _clockFace: Drawable? = null // TODO: use a default from R.dimen...
    private var _SecondHandColor: Int? = null
    private var _MinuteHandColor: Int? = null
    private var _HourHandColor: Int? = null


    val Int.dp: Float
        get() = (this / Resources.getSystem().displayMetrics.density).toFloat()
    val Float.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }


    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ClockView, defStyle, 0
        )
//        if(layoutParams.height!=layoutParams.width) {
////            throw Exception("Hey, I am testing it")
//            error("!2")
//        }
        _clockFace = a.getDrawable(
            R.styleable.ClockView_ClockFace
        )

        _SecondHandColor = a.getColor(
            R.styleable.ClockView_SecondHandColor, 0
        )

        _MinuteHandColor = a.getColor(
            R.styleable.ClockView_MinuteHandColor, 0
        )

        _HourHandColor = a.getColor(
            R.styleable.ClockView_HourHandColor, 0
        )
        a.recycle()
        setBackgroundColor(Color.TRANSPARENT)
    }


    private val hourPaint = Paint().apply {
        if (_HourHandColor != null)
            color = _HourHandColor!!
        else
            color = Color.GREEN
        strokeWidth = 20f
    }

    private val minutePaint = Paint().apply {
        if (_MinuteHandColor != null)
            color = _MinuteHandColor!!
        else
            color = Color.GREEN
        strokeWidth = 15f
    }

    private val secondPaint = Paint().apply {
        if (_SecondHandColor != null)
            color = _SecondHandColor!!
        else
            color = Color.GREEN
        strokeWidth = 10f

    }
    private var hour = 0
    private var minute = 0
    private var second = 0


    fun setTime(hour: Int, minute: Int, second: Int) {
        this.hour = hour
        this.minute = minute
        this.second = second

        invalidate()
    }

    private val paint = Paint()
    private fun drawClockFace(canvas: Canvas?) {
        paint.color = Color.GREEN

        val bitmap = (_clockFace as BitmapDrawable).bitmap
        val radius = min(width, height) / 2f
        canvas?.drawCircle(width / 2f, height / 2f, radius, paint)
        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap, radius.toInt() * 2, radius.toInt() * 2, false)
        val shader = BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        canvas?.drawCircle(width / 2f, height / 2f, radius, paint)
    }

    private fun drawSecondArrow(canvas: Canvas?, centerX: Float, centerY: Float) {
        val secondHandLength = centerX - 10f.px
        val secondHandAngle = second * 6f - 90f.toDouble()
        val secondHandX = centerX + cos(Math.toRadians(secondHandAngle)) * secondHandLength
        val secondHandY = centerY + sin(Math.toRadians(secondHandAngle)) * secondHandLength

        canvas?.drawLine(
            centerX,
            centerY,
            secondHandX.toFloat(),
            secondHandY.toFloat(),
            secondPaint
        )
    }

    private fun drawMinuteArrow(canvas: Canvas?, centerX: Float, centerY: Float) {
        val minuteHandLength = centerX - 30f.px
        val minuteHandAngle = (minute + second / 60f) * 6f - 90f.toDouble()
        val minuteHandX = centerX + cos(Math.toRadians(minuteHandAngle)) * minuteHandLength
        val minuteHandY = centerY + sin(Math.toRadians(minuteHandAngle)) * minuteHandLength

        canvas?.drawLine(
            centerX,
            centerY,
            minuteHandX.toFloat(),
            minuteHandY.toFloat(),
            minutePaint
        )
    }

    private fun drawHourArrow(canvas: Canvas?, centerX: Float, centerY: Float) {
        val hourHandLength = centerX - 50f.px
        val hourHandAngle = (hour + minute / 60f) * 30f - 90f.toDouble()
        val hourHandX = centerX + cos(Math.toRadians(hourHandAngle)) * hourHandLength
        val hourHandY = centerY + sin(Math.toRadians(hourHandAngle)) * hourHandLength

        canvas?.drawLine(centerX, centerY, hourHandX.toFloat(), hourHandY.toFloat(), hourPaint)
    }

//        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.arrow_red)
//         val arrowBitmap = BitmapFactory.decodeResource(resources, R.drawable.arrow_red)
//        val arrowWidth = arrowBitmap.width.toFloat()
//         val arrowHeight = arrowBitmap.height.toFloat()
//        val length = 100f

    // рисуем стрелку
//        canvas?.save()
//        canvas?.rotate(20f) // поворачиваем на 90 градусов (можно изменить на нужный угол)
//        canvas?.drawBitmap(arrowBitmap, centerX, centerY , null)
//        canvas?.restore()


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawClockFace(canvas)
        setTime(
            LocalDateTime.now().hour % 12,
            LocalDateTime.now().minute,
            LocalDateTime.now().second
        )
        val centerX = width / 2f
        val centerY = height / 2f
        drawSecondArrow(canvas, centerX, centerY)
        drawMinuteArrow(canvas, centerX, centerY)
        drawHourArrow(canvas, centerX, centerY)


    }
}