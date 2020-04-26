package com.example.lumicentertestbed

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log

class LumiCenter(private val bitmap: Bitmap) {
    private val width = bitmap.width
    private val height = bitmap.height

    companion object {
        private const val TAG = "LumiCenter"
    }

//    inline fun itFromIndices(x: Int, y: Int): Int {
//        return width * y + x
//    }

    // Convert Color data to a simple number.
    //  If you want the code to be sensitive to a different color, make that change here.
    fun extractLumi(color: Int): Int {
        return Color.red(color)
    }

    fun sumOverImage(mult: (Int, Int) -> Int): Long {
        Log.d(TAG,"Random point on bitmap had R = ${extractLumi(bitmap.getPixel(100, 100))}")

        var acc: Long = 0
        for (x in 0 until width - 1 step 100) {
            for (y in 0 until height - 1 step 100) {
                val colorValue = extractLumi(bitmap.getPixel(x, y))
                acc += colorValue * mult(x, y)
            }
        }
        return acc
    }
}

