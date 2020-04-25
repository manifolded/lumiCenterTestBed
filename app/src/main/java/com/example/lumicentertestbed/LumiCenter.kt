package com.example.lumicentertestbed

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import java.nio.ByteBuffer

class LumiCenter(val imageView: ImageView) {
    private val width: Int = imageView.width
    private val height: Int = imageView.height
    val bitmap: Bitmap = convertImageViewToBitmap(imageView)

    companion object {
        private const val TAG = "LumiCenter"
    }

//    fun convertByteToInt(byte: Byte): Int {
//        return byte.toInt() and 0xFF
//    }

//    inline fun itFromIndices(x: Int, y: Int): Int {
//        return width * y + x
//    }

    private fun convertImageViewToBitmap(imageView: ImageView): Bitmap {

//        if(imageView == null)
//            Log.d(TAG, "imageView was null!!!")
//        if(imageView.drawable == null)
//            Log.d(TAG, "imageView.drawable was null!!!")
//
//        Log.d(TAG, "Is this class populated? ${imageView.drawable}")

        // convert to Bitmap
        // https://stackoverflow.com/questions/4715044/android-how-to-convert-whole-imageview-to-bitmap
        return (imageView.drawable as BitmapDrawable).bitmap
    }

    fun extractLumi(color: Int): Int {
        // https://developer.android.com/reference/android/graphics/Color
        val albedo: Int = (color shr 24) and 0xFF
        val red: Int = (color shr 16) and 0xFF
        val green: Int = (color shr 8)  and 0xFF
        val blue: Int = color and 0xFF
//        Log.d(TAG, "color values for that pixel are A: $A, R: $R, G: $G and B: $B")

        return red
    }

    fun sumOverImage(mult: (Int, Int) -> Int): Double {
        Log.d(TAG,"Random point on bitmap had R = ${extractLumi(bitmap.getPixel(100, 100))}")

        var acc = 0.0
        for (x in 0 until width - 1) {
            for (y in 0 until height - 1) {
                acc += extractLumi(bitmap.getPixel(x, y)) * mult(x,y)
//                acc += extractLumi(bitmap.getPixel(x, y))
            }
        }
        return acc
    }
}

