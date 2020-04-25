package com.example.lumicentertestbed

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import java.nio.ByteBuffer

class LumiCenter(val imageView: ImageView) {
    private val width: Int = imageView.width
    private val height: Int = imageView.height
    val bitmap: Bitmap = convertImageViewToBitmap(imageView)

//    fun convertByteToInt(byte: Byte): Int {
//        return byte.toInt() and 0xFF
//    }

//    inline fun itFromIndices(x: Int, y: Int): Int {
//        return width * y + x
//    }

    private fun convertImageViewToBitmap(imageView: ImageView): Bitmap {
        // convert to Bitmap
        // https://stackoverflow.com/questions/4715044/android-how-to-convert-whole-imageview-to-bitmap
        return (imageView.drawable as BitmapDrawable).bitmap
    }

    fun sumOverImage(mult: (Int, Int) -> Int): Double {
        var acc = 0.0
        for (x in 0 until width - 1) {
            for (y in 0 until height - 1) {
                acc += bitmap.getPixel(x, y) * mult(x, y)
            }
        }
        return acc
    }


}

