package com.example.lumicentertestbed

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log

class IterativeExplorer(private val bitmap: Bitmap) {
    private val width = bitmap.width
    private val height = bitmap.height
//    private var currentParams: Map<String, Int> =
//        mutableMapOf("xCenter" to 540, "yCenter" to 960, "xWidth" to 100, "yWidth" to 100,
//            "amplitude" to 0x80, "background" to 0x00)

    // start the params with a patch that covers the whole screen
    private var origParams: Array<Int> =  arrayOf(540, 960, 540, 960, 0x80, 0x00)

    companion object {
        private const val TAG = "IterativeExplorer"
    }

    // ===================================================================================
//    private fun computeModel(params: Map<String, Int>, x: Int, y: Int): Int {
//        var result: Int = 0
//        val xCenter: Int? = params["xCenter"]
//        val yCenter: Int? = params["yCenter"]
//        val xWidth: Int? = params["xWidth"]
//        val yWidth: Int? = params["yWidth"]
//        val amp: Int? = params["amp"]
//        val bgrnd: Int? = params["background"]
//
//        if(x > xCenter!! - xWidth!! && x < xCenter!! + xWidth!! &&
//            y > yCenter!! - yWidth!! && y < yCenter!! + yWidth!!)
//            return amp!! + bgrnd!!
//        else
//            return bgrnd!!
//    }

    // ===================================================================================
    private fun computeModel(params: Array<Int>, x: Int, y: Int): Int {
        val xCenter: Int = params[0]
        val yCenter: Int = params[1]
        val xWidth: Int = params[2]
        val yWidth: Int = params[3]
        val amp: Int = params[4]
        val bgrnd: Int = params[5]

        if(x > xCenter - xWidth && x < xCenter + xWidth &&
            y > yCenter - yWidth && y < yCenter + yWidth)
            return amp + bgrnd
        else
            return bgrnd
    }

    // ===================================================================================
    private fun computeParamDifferential(currParams: Array<Int>, whichParam: Int, x: Int, y: Int): Int {
        var params = currParams.copyOf()
//        Log.d(TAG, "pre-diff params are: ${params[0]} ${params[2]} ${params[1]} ${params[3]} ${params[4]} ${params[5]}")
        params[whichParam] += 1

        return computeModel(params, x, y) - computeModel(currParams, x, y)
//        Log.d(TAG, "post-diff params are: ${params[0]} ${params[2]} ${params[1]} ${params[3]} ${params[4]} ${params[5]}")
    }

    // ===================================================================================
    private fun computeAllParamDifferentials(params: Array<Int>, stride: Int, alpha: Double):
            Array<Int> {
        var xi: Array<Int> = arrayOf(0, 0, 0, 0, 0, 0)
        var lambdaAcc: Long = 0
        var diffAcc: Array<Double> = arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

//        Log.d(TAG, "pre-all params are: ${params[0]} ${params[2]} ${params[1]} ${params[3]} ${params[4]} ${params[5]}")

        for (x in 0 until width - 1 step stride) {
            for (y in 0 until height - 1 step stride) {
                // this is the expensive step
                val pixel: Int = extractColor(bitmap.getPixel(x, y))

                val delta: Int = pixel - computeModel(params, x, y)
                Log.d(TAG, "delta: $delta")
                lambdaAcc += delta*delta
                val diffs: Array<Int> = arrayOf(0, 0, 0, 0, 0, 0)
                for(m in xi.indices) {
                    diffs[m] = computeParamDifferential(params, m, x, y) // for debugging
                    diffAcc[m] += (delta*computeParamDifferential(params, m, x, y)).toDouble()
                }
                Log.d(TAG, "diffs: ${diffs[0]} ${diffs[2]} ${diffs[1]} ${diffs[3]} ${diffs[4]} ${diffs[5]}")
            }
        }

        Log.d(TAG, "diffAcc: ${diffAcc[0]} ${diffAcc[2]} ${diffAcc[1]} ${diffAcc[3]} ${diffAcc[4]} ${diffAcc[5]}")
        Log.d(TAG, "lambdaAcc: $lambdaAcc ")


        for(m in xi.indices) {
            // final calc
            xi[m] = ((alpha*lambdaAcc)/(diffAcc[m]*2.0)).toInt()
        }

        Log.d(TAG, "xi is: ${xi[0]} ${xi[2]} ${xi[1]} ${xi[3]} ${xi[4]} ${xi[5]}")
//        Log.d(TAG, "post-all params are: ${params[0]} ${params[2]} ${params[1]} ${params[3]} ${params[4]} ${params[5]}")
        return xi
    }

    // ===================================================================================
    // Convert Color data to a simple number.
    //  If you want the code to be sensitive to a different color, make that change here.
    private fun extractColor(color: Int): Int {
        return Color.red(color)
    }

    // ===================================================================================
    // compute number of pixels summed over N_s
    fun numPixels(stride: Int): Int {
        return width*height/(stride*stride)
    }

    // ===================================================================================
    // compute the squared error
    fun computeTotalError(params: Array<Int>, stride: Int): Long {
        var lambda: Long = 0
        for (x in 0 until width - 1 step stride) {
            for (y in 0 until height - 1 step stride) {
                // this is the expensive step
                val pixel: Int = extractColor(bitmap.getPixel(x, y))
                val delta: Int = pixel - computeModel(params, x, y)
                lambda += delta*delta
            }
        }
        lambda /= numPixels(stride)
        return lambda
    }

    // ===================================================================================
    private fun updateParams(currParams: Array<Int>, alpha: Double, stride: Int): Array<Int> {
        var params: Array<Int> = currParams
//        Log.d(TAG, "pre-update params are: ${params[0]} ${params[2]} ${params[1]} ${params[3]} ${params[4]} ${params[5]}")

        val xi: Array<Int> = computeAllParamDifferentials(params, stride, alpha)
//        Log.d(TAG, "xi still is: ${xi[0]} ${xi[2]} ${xi[1]} ${xi[3]} ${xi[4]} ${xi[5]}")

        for(m in params.indices)
            params[m] += xi[m]


//        Log.d(TAG, "post-update params are: ${params[0]} ${params[2]} ${params[1]} ${params[3]} ${params[4]} ${params[5]}")
        return params
    }

    // ===================================================================================
    fun iterateParams(lambdaTarget: Int, alpha: Double, stride: Int, maxIts: Int): Array<Int> {
        var params: Array<Int> = origParams
        var lambda: Long
        var iteration: Int = 0

        do {
            lambda = computeTotalError(params, stride)
            Log.d(TAG, "total error energy on iteration $iteration is: $lambda")
            Log.d(TAG, "params are: ${params[0]} ${params[2]} ${params[1]} ${params[3]} ${params[4]} ${params[5]}")

            params = updateParams(params, alpha, stride)
//            Log.d(TAG, "after delta params are: ${params[0]} ${params[2]} ${params[1]} ${params[3]} ${params[4]} ${params[5]}")
            iteration++
        } while(lambda > lambdaTarget && iteration < maxIts)

        if(iteration == maxIts) {
            Log.d(TAG, "Iteration reached maxIts.")
        }
        Log.d(TAG, "Done iterating after $iteration steps")
        Log.d(TAG, "final params are $params")
        return params
    }

}