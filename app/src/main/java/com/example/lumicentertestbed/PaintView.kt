package com.example.lumicentertestbed

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView

class PaintView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        color = Color.YELLOW
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        alpha = 0xff
        xfermode = null
        strokeWidth = 20f
    }

    private lateinit var currentRect: RectF
    private var xradius = -1
    private var yradius = -1

    private var bitmap: Bitmap
    private var canvas: Canvas
    private val bitmapPaint = Paint(Paint.DITHER_FLAG)

    private val backgroundColor = Color.TRANSPARENT

    init {
        val metrics = DisplayMetrics()
        val activity = context as Activity
        activity.windowManager.defaultDisplay.getMetrics(metrics)

        val width = metrics.widthPixels
        val height = metrics.heightPixels

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
    }


    override fun onDraw(canvas: Canvas) {
        canvas.save()

        this.canvas.drawColor(backgroundColor)

        if (::currentRect.isInitialized) {
            this.canvas.drawRoundRect(currentRect, currentRect.width(), currentRect.height(), paint)
        }

        canvas.drawBitmap(bitmap, 0f, 0f, bitmapPaint)
        canvas.restore()
    }

    private fun clear() {
        val width = bitmap.width
        val height = bitmap.height

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
    }

    fun drawShape(xArg: Float, yArg: Float, dxArg: Float, dyArg: Float) {
        val lambda: Float = height / 1920.0f
        val x: Float = lambda * xArg + (1080.0f - width)/2
        val y: Float = lambda * yArg + (1920.0f - height)/2
        val dx: Float = lambda * dxArg
        val dy: Float = lambda * dyArg

        clear()
        currentRect = RectF(x, y, x + dx, y + dy)
        invalidate()
    }
}