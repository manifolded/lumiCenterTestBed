package com.example.lumicentertestbed

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View

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

    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas
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

    fun drawShape(x: Float, y: Float, dx: Float, dy: Float) {
        clear()
        currentRect = RectF(x, y, x + dx, y + dy)
        invalidate()
    }
}