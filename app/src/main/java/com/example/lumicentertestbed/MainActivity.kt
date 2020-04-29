package com.example.lumicentertestbed

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "lumiCenterTestBed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

//            val lumiCenter = LumiCenter(bitmap)
//            val stats = lumiCenter.computeStats(10)
//
////            Log.d(TAG, "sumOnVals: ${stats[0]}")
////            Log.d(TAG, "x-center: ${stats[1]}")
////            Log.d(TAG, "y-center: ${stats[2]}")
//
//            // write output to TextView
////            analysisResults.text = "${stats[1]}, ${stats[2]}"
//            analysisResults.text = "%d, %.0f\n%d, %.0f"
//                .format(stats[1], sqrt(stats[3].toDouble()), stats[2], sqrt(stats[4].toDouble()))


            val analyzer = IterativeExplorer(bitmap)
            val finalParams = analyzer.iterateParams(100, 0.3, 10, 2, 1)

            analysisResults.text = "${finalParams[0]} ${finalParams[2]}\n${finalParams[1]} ${finalParams[3]}"



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
