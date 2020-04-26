package com.example.lumicentertestbed

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView

class LumiCenter(private val imageView: ImageView) {
    private val width: Int = imageView.width
    private val height: Int = imageView.height
    private val bitmap: Bitmap = convertImageViewToBitmap(imageView)

    companion object {
        private const val TAG = "LumiCenter"
    }

//    inline fun itFromIndices(x: Int, y: Int): Int {
//        return width * y + x
//    }

    private fun convertImageViewToBitmap(imageView: ImageView): Bitmap {
        // convert to Bitmap
        // https://stackoverflow.com/questions/4715044/android-how-to-convert-whole-imageview-to-bitmap
        return (imageView.drawable as BitmapDrawable).bitmap
    }

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

