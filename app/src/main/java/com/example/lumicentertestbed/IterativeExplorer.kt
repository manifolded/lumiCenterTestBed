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
    // Maps in Kotlin, with their fucking implicit default values and null receivers and safe
    // assignments are fucking nightmare and I give up.  You fucks deserve to burn in hell.
    // I'm going back to using a fucking functional array.
    //   You know how we always say to prefer compile time errors to runtime errors.  Well now I
    // know it's a lie.  If Map would just shut the fuck up and compile I could accept a runtime
    // error if I try to access a non-existent pair.


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
    private fun computeParamDifferential(params: Array<Int>, whichParam: Int, x: Int, y: Int): Int {
        var newParams = params
        newParams[whichParam] += 1

        return computeModel(newParams, x, y) - computeModel(params, x, y)
    }

    // ===================================================================================
    private fun computeAllParamDifferentials(params: Array<Int>, stride: Int, alpha: Double):
            Array<Int> {
        var xi: Array<Int> = arrayOf(0, 0, 0, 0, 0, 0)
        var lambdaAcc: Long = 0
        var diffAcc: Array<Double> = arrayOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

        for (x in 0 until width - 1 step stride) {
            for (y in 0 until height - 1 step stride) {
                // this is the expensive step
                val pixel: Int = extractColor(bitmap.getPixel(x, y))

                val delta: Int = pixel - computeModel(params, x, y)
                lambdaAcc += delta*delta
                for(m in xi.indices) {
                    diffAcc[m] += (delta*computeParamDifferential(params, m, x, y)).toDouble()
                }
            }
        }

        for(m in xi.indices) {
            // final calc
            xi[m] = ((alpha*lambdaAcc)/(diffAcc[m]*2.0)).toInt()
        }
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
    fun updateParams(params: Array<Int>, alpha: Double, stride: Int): Array<Int> {
        var currParams: Array<Int> = params
        val deltas: Array<Int> = computeAllParamDifferentials(currParams, stride, alpha)

        for(m in currParams.indices)
            currParams[m] += deltas[m]

        return currParams
    }

    // ===================================================================================
    fun iterateParams(lambdaTarget: Int, alpha: Double, stride:Int):Array<Int> {
        var params: Array<Int> = origParams
        var lambda: Long
        var iteration: Int = 0

        do {
            lambda = computeTotalError(params, stride)
            Log.d(TAG, "total error energy on iteration $iteration is: $lambda")
            Log.d(TAG, "params are: $params")

            params = updateParams(params, alpha, stride)
            iteration++
        } while(lambda < lambdaTarget)

        Log.d(TAG, "Done iterating after $iteration steps")
        Log.d(TAG, "final params are $params")
        return params
    }

}