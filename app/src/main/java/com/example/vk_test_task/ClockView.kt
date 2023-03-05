package com.example.vk_test_task

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import java.time.LocalDateTime
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * TODO: document your custom view class.
 */
class ClockView : androidx.appcompat.widget.AppCompatImageView {

    private var _exampleString: String? = null // TODO: use a default from R.string...
    private var _exampleColor: Int = Color.RED // TODO: use a default from R.color...
    private var _exampleDimension: Float = 0f // TODO: use a default from R.dimen...

    private lateinit var textPaint: TextPaint
    val Int.dp: Float
        get() = (this / Resources.getSystem().displayMetrics.density).toFloat()
    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()


    /**
     * The font color
     */
    var exampleColor: Int
        get() = _exampleColor
        set(value) {
            _exampleColor = value

        }

    /**
     * In the example view, this dimension is the font size.
     */
    var exampleDimension: Float
        get() = _exampleDimension
        set(value) {
            _exampleDimension = value

        }

    /**
     * In the example view, this drawable is drawn above the text.
     */
    var exampleDrawable: Drawable? = null

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

        _exampleString = a.getString(
            R.styleable.ClockView_exampleString
        )
        _exampleColor = a.getColor(
            R.styleable.ClockView_exampleColor,
            exampleColor
        )
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        _exampleDimension = a.getDimension(
            R.styleable.ClockView_exampleDimension,
            exampleDimension
        )

        if (a.hasValue(R.styleable.ClockView_exampleDrawable)) {
            exampleDrawable = a.getDrawable(
                R.styleable.ClockView_exampleDrawable
            )
            exampleDrawable?.callback = this
        }

        a.recycle()

        // Set up a default TextPaint object
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }

        // Update TextPaint and text measurements from attributes

        setBackgroundColor(Color.TRANSPARENT)
    }


    private val hourPaint = Paint().apply {
        color = Color.CYAN
        strokeWidth = 15f
    }

    private val minutePaint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 10f
    }

    private val secondPaint = Paint().apply {
        color = Color.RED
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
    private fun drawClockFace(canvas: Canvas?){
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.clock_face_funny_boy) // загрузка картинки из ресурсов
        val radius = min(width, height) / 2f
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, radius.toInt()*2, radius.toInt()*2, false)
        val shader = BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        if (canvas != null) {
            canvas.drawCircle(width / 2f, height / 2f, radius, paint)
        }
    }
    private fun drawSecondArrow(canvas: Canvas?, centerX:Float, centerY:Float){
        val secondHandLength = centerX - 30f
        val secondHandAngle = second * 6f - 90f.toDouble()
        val secondHandX = centerX + cos(Math.toRadians(secondHandAngle)) * secondHandLength
        val secondHandY = centerY + sin(Math.toRadians(secondHandAngle)) * secondHandLength

        canvas?.drawLine(centerX, centerY, secondHandX.toFloat(), secondHandY.toFloat(), secondPaint)
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

    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawClockFace(canvas)
        setTime(LocalDateTime.now().hour%12,LocalDateTime.now().minute,LocalDateTime.now().second)
        val centerX = width / 2f
        val centerY = height / 2f
        drawSecondArrow(canvas, centerX, centerY)
//        val picture =Picture().s

//        canvas?.drawCircle(centerX, centerY, Math.min(width, height) / 2f - 10f, hourPaint)

        val hourHandLength = centerX - 160f
        val hourHandAngle = (hour + minute / 60f) * 30f - 90f.toDouble()
        val hourHandX = centerX + cos(Math.toRadians(hourHandAngle)) * hourHandLength
        val hourHandY = centerY + sin(Math.toRadians(hourHandAngle)) * hourHandLength

        canvas?.drawLine(centerX, centerY, hourHandX.toFloat(), hourHandY.toFloat(), hourPaint)

        val minuteHandLength = centerX - 130f
        val minuteHandAngle = (minute + second / 60f) * 6f - 90f.toDouble()
        val minuteHandX = centerX + cos(Math.toRadians(minuteHandAngle)) * minuteHandLength
        val minuteHandY = centerY + sin(Math.toRadians(minuteHandAngle)) * minuteHandLength

        canvas?.drawLine(centerX, centerY, minuteHandX.toFloat(), minuteHandY.toFloat(), minutePaint)


    }
}