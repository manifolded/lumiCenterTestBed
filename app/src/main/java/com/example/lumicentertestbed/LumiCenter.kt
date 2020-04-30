package com.example.lumicentertestbed

import android.graphics.*

data class LumiResult(
    val centerX: Long,
    val centerY: Long,
    val stdX: Long,
    val stdY: Long,
    val corrXY: Long,
    val background: Long,
    val amplitude: Long
)

typealias LumiResultHandler = (LumiResult) -> Unit

class LumiCenter(private val bitmap: Bitmap) {
    private val width = bitmap.width
    private val height = bitmap.height

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

    // ===================================================================================
    // compute number of pixels summed over
    fun numPixels(stride: Int): Int {
        return width*height/(stride*stride)
    }

    // ===================================================================================
    fun detectBackground(stride: Int): Pair<Long, Long> {
        val hist = Array<Int>(0xFF + 1 ) {0}
        var acc: Long = 0
        var result: LumiResult

        for (x in 0 until width - 1 step stride) {
            for (y in 0 until height - 1 step stride) {
                val pixel: Int = extractLumi(bitmap.getPixel(x, y))
                hist[pixel] += 1
                acc += pixel
            }
        }

        // Use mean as threshold
        // Compute most frequent value below this threshold
        val mean = (acc/numPixels(stride)).toInt()
        var background = 0
        var numLow = 0
        for(c in 0 until mean - 1) {
            background += c*hist[c]
            numLow += hist[c]
        }
        background /= numLow

        // Compute most frequent value above this threshold
        var amplitude: Int = 0
        var numHigh: Int = 0
        for(c in mean until hist.size - 1) {
            amplitude += c*hist[c]
            numHigh += hist[c]
        }
        amplitude /= numHigh

        return Pair(amplitude.toLong(), background.toLong())
    }

    // ===================================================================================
    fun computeStats(stride: Int, callback: LumiResultHandler) {
        val (amplitude, background) = detectBackground(stride)
        val result = Array<Long>(6 ) {0}

        // sum stats over image
        for (x in 0 until width - 1 step stride) {
            for (y in 0 until height - 1 step stride) {
                // this is the expensive step
                val pixel: Long = extractLumi(bitmap.getPixel(x, y)).toLong() - background
                result[0] += pixel
                result[1] += pixel * x
                result[2] += pixel * y
                result[3] += pixel * x * x
                result[4] += pixel * y * y
                result[5] += pixel * x * y
            }
        }

        // normalize
        for(i : Int in 1..5) {
            result[i] /= result[0]
        }

        // subtract off second order piece
        result[3] -= result[1]*result[1]
        result[4] -= result[2]*result[2]
        result[5] -= result[1]*result[2]

        val lresult = LumiResult(
            centerX = result[1],
            centerY = result[2],
            stdX = Math.sqrt(result[3].toDouble()).toLong(),
            stdY = Math.sqrt(result[4].toDouble()).toLong(),
            corrXY = Math.sqrt(result[5].toDouble()).toLong(),
            background = background,
            amplitude = amplitude
        )

        callback.invoke(lresult)

//        result[0] = sumOverImage(stride) { _, _ -> 1 }
//        result[1] = (sumOverImage(stride) { x: Int, _ -> x}) / result[0]
//        result[2] = (sumOverImage(stride) { _, y: Int -> y}) / result[0]
//        result[3] = (sumOverImage(stride) { x: Int, _ -> x*x}) / result[0]
//        result[3] -= result[1]*result[1]
//        result[4] = (sumOverImage(stride) { _, y: Int -> y*y}) / result[0]
//        result[4] -= result[2]*result[2]
    }
}

