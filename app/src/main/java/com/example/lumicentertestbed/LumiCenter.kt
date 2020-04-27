package com.example.lumicentertestbed

import android.graphics.*

class LumiCenter(private val bitmap: Bitmap) {
    private val width = bitmap.width
    private val height = bitmap.height

//    companion object {
//        private const val TAG = "LumiCenter"
//    }

//    inline fun itFromIndices(x: Int, y: Int): Int {
//        return width * y + x
//    }

    // Convert Color data to a simple number.
    //  If you want the code to be sensitive to a different color, make that change here.
    private fun extractLumi(color: Int): Int {
        return Color.red(color)
    }

//    private fun sumOverImage(stride: Int, multiplier: (Int, Int) -> Int): Long {
//        var acc: Long = 0
//        for (x in 0 until width - 1 step stride) {
//            for (y in 0 until height - 1 step stride) {
//                val colorValue = extractLumi(bitmap.getPixel(x, y))
//                acc += colorValue * multiplier(x, y)
//            }
//        }
//        return acc
//    }

    fun computeStats(stride: Int) : Array<Long> {
        val result = Array<Long>(6 ) {0}

        for (x in 0 until width - 1 step stride) {
            for (y in 0 until height - 1 step stride) {
                // this is the expensive step
                val pixel: Long = extractLumi(bitmap.getPixel(x, y)).toLong()
                result[0] += pixel
                result[1] += pixel * x
                result[2] += pixel * y
                result[3] += pixel * x * x
                result[4] += pixel * y * y
                result[5] += pixel * x * y
            }
        }
        for(i : Int in 1..5) {
            result[i] /= result[0]
        }
        return result

//        result[0] = sumOverImage(stride) { _, _ -> 1 }
//        result[1] = (sumOverImage(stride) { x: Int, _ -> x}) / result[0]
//        result[2] = (sumOverImage(stride) { _, y: Int -> y}) / result[0]
//        result[3] = (sumOverImage(stride) { x: Int, _ -> x*x}) / result[0]
//        result[3] -= result[1]*result[1]
//        result[4] = (sumOverImage(stride) { _, y: Int -> y*y}) / result[0]
//        result[4] -= result[2]*result[2]
    }
}

