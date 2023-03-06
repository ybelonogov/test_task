package com.example.vk_test_task

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import java.time.LocalDateTime
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


/**
 * TODO: document your custom view class.
 */
class ClockView  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private var _clockFace: Drawable? = null // TODO: use a default from R.dimen...
    private var _secondHandColor: Int = Color.GREEN
    private var _minuteHandColor: Int = Color.GREEN
    private var _hourHandColor: Int = Color.GREEN


    val Int.dp: Float
        get() = (this / Resources.getSystem().displayMetrics.density)
    val Float.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()


//    constructor(context: Context) : super(context) {
//        init(null, 0)
//    }
//
//    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        init(attrs, 0)
//    }
//
//    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
//        context,
//        attrs,
//        defStyle
//    ) {
//        init(attrs, defStyle)
//    }


    init{
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ClockView, defStyleAttr, 0
        )
//        if(layoutParams.height!=layoutParams.width) {
////            throw Exception("Hey, I am testing it")
//            error("!2")
//        }
        _clockFace = a.getDrawable(
            R.styleable.ClockView_ClockFace
        )

        _secondHandColor = a.getColor(
            R.styleable.ClockView_SecondHandColor, 0
        )
        a.recycle()
        _minuteHandColor = a.getColor(
            R.styleable.ClockView_MinuteHandColor, 0
        )

        _hourHandColor = a.getColor(
            R.styleable.ClockView_HourHandColor, 0
        )
        a.recycle()
        setBackgroundColor(Color.TRANSPARENT)
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }


    private val hourPaint = Paint().apply {
        color = _hourHandColor
//        strokeWidth =height*0.0003f
        strokeWidth = 20f
    }

    private val minutePaint = Paint().apply {
        color = _minuteHandColor
//        strokeWidth =height*0.0002f
        strokeWidth = 15f
    }

    private val secondPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = _secondHandColor
//        strokeWidth =height*0.0001f
        strokeWidth=10f
    }
    private var hour = 0
    private var minute = 0
    private var second = 0


    private fun setTime(hour: Int, minute: Int, second: Int) {
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
        val secondHandLength = centerX -height*0.05f
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
        val minuteHandLength = centerX - height*0.1f
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
        val hourHandLength = centerX -height*0.2f
        val hourHandAngle = (hour + minute / 60f) * 30f - 90f.toDouble()
        val hourHandX = centerX + cos(Math.toRadians(hourHandAngle)) * hourHandLength
        val hourHandY = centerY + sin(Math.toRadians(hourHandAngle)) * hourHandLength

        canvas?.drawLine(centerX,
            centerY,
            hourHandX.toFloat(),
            hourHandY.toFloat(),
            hourPaint)
    }




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