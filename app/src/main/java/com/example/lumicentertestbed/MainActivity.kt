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
            val bitmap = convertDrawableToBitmap(drawable, width, height)
            val lumiCenter = LumiCenter(bitmap)

            val sumOnVals: Long = lumiCenter.sumOverImage { _, _ -> 1 }
            Log.d(TAG, "sumOnVals = $sumOnVals")
            val xEst: Long = (lumiCenter.sumOverImage { x: Int, _ -> x}) / sumOnVals
            Log.d(TAG, "x-center: $xEst")
            val yEst: Long = (lumiCenter.sumOverImage { _, y: Int -> y}) / sumOnVals
            Log.d(TAG, "y-center: $yEst")

            analysisResults.text = "$xEst, $yEst"
        }
    }

    private fun convertDrawableToBitmap(drawable: Drawable, width: Int, height: Int): Bitmap {
        // method appropriate for drawables
        // https://stackoverflow.com/questions/33181417/convert-gradientdrawable-to-bitmap
        var bitmap: Bitmap = Bitmap.createBitmap(imageView.width, imageView.height, Bitmap.Config.ARGB_8888)
        val canvas: Canvas = Canvas(bitmap)
        drawable.draw(canvas)
        return bitmap
    }

    private fun convertImageViewToBitmap(imageView: ImageView): Bitmap {
        // method appropriate for images
        // https://stackoverflow.com/questions/4715044/android-how-to-convert-whole-imageview-to-bitmap
        return (imageView.drawable as BitmapDrawable).bitmap
    }
}
