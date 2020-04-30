package com.example.lumicentertestbed

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {

    private val executor by lazy { Executors.newSingleThreadExecutor() }
    companion object {
        private const val TAG = "lumiCenterTestBed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.apply {
            // Hide both the navigation bar and the status bar.
            // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
            // a general rule, you should design your app to hide the status bar whenever you
            // hide the navigation bar.
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        imageView.post {
            // method for importing images
//        imageView.setImageResource(R.drawable.lowerleftblob01)
//        val bitmap = convertImageViewToBitmap(imageView)

            // method for importing shape drawables
            imageView.setImageResource(R.drawable.blob_test_drawable)
            val drawable: Drawable = imageView.drawable

            // ----------------------------------------
            val height: Int = imageView.height
            val width: Int = imageView.width
            val bitmap: Bitmap = convertDrawableToBitmap(drawable, width, height)

            // =================================================================================
            val lumiCenter = LumiCenter(bitmap)

            executor.execute {
                lumiCenter.computeStats(30) { stats ->
                    runOnUiThread {
                        analysisResults.text = "%d, %d\n%d, %d"
                            .format(stats.centerX, stats.stdX, stats.centerY, stats.stdY)

                        val dx = stats.stdX * 4
                        val x =  stats.centerX - (dx / 2)
                        val dy  = stats.stdY * 4
                        val y =  stats.centerY - (dy / 2)


                        paintView.drawShape(
                            x.toFloat(),
                            y.toFloat(),
                            dx.toFloat(),
                            dy.toFloat()
                        )
                    }
                }
            }





//            Log.d(TAG, "sumOnVals: ${stats[0]}")
//            Log.d(TAG, "x-center: ${stats[1]}")
//            Log.d(TAG, "y-center: ${stats[2]}")

            // write output to TextView
//            analysisResults.text = "${stats[1]}, ${stats[2]}"


            // =================================================================================
            // For running IterativeExplorer conventionally
//            val analyzer = IterativeExplorer(bitmap)
//            val origParams: Array<Int> =  arrayOf(540, 960, 400, 400, 0x80, 0x00)
//            val finalParams=
//                analyzer.iterateParams(100, 0.3, 30, 2, 2)
//
//            analysisResults.text = "${finalParams[0]} ${finalParams[2]}\n${finalParams[1]} ${finalParams[3]}"

            // =================================================================================
            // for debugging of IterativeExplorer with output of paramDifferentials
//            val origParams: Array<Int> =  arrayOf(540, 960, 530, 950, 0x80, 0x00)
//            analyzer.computeAllParamDifferentials(origParams, 30, 0.1)
//            var diffs: Array<Int> = arrayOf(0, 0, 0, 0, 0, 0)
//            for(m in diffs.indices) {
//                diffs[m] =  analyzer.computeParamDifferential(origParams, m, 9, 960)
//            }
//            Log.d(TAG, "${diffs[0]} ${diffs[2]} ${diffs[1]} ${diffs[3]} ${diffs[4]} ${diffs[5]}")


        }
    }

    private fun convertDrawableToBitmap(drawable: Drawable, width: Int, height: Int): Bitmap {
        // method appropriate for drawables
        // https://stackoverflow.com/questions/33181417/convert-gradientdrawable-to-bitmap
        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.draw(canvas)
        return bitmap
    }

    private fun convertImageViewToBitmap(imageView: ImageView): Bitmap {
        // method appropriate for images
        // https://stackoverflow.com/questions/4715044/android-how-to-convert-whole-imageview-to-bitmap
        return (imageView.drawable as BitmapDrawable).bitmap
    }
}
