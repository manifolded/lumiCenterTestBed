package com.example.lumicentertestbed

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream
import kotlin.math.roundToInt
import kotlin.reflect.typeOf

//import androidx.camera.view.PreviewView

class MainActivity : AppCompatActivity() {
    private lateinit var constraintLayout: ConstraintLayout

    companion object {
        private const val TAG = "lumiCenterTestBed"
    }

    // following example code in:
    // https://developer.android.com/guide/topics/graphics/drawables
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        // Instantiate an ImageView and define its properties
        val imageView = ImageView(this).apply {
            setImageResource(R.drawable.lowerleftblob01)
            contentDescription = resources.getString(R.string.lowerleftblob01_desc)

            // set the ImageView bounds to match the Drawable's dimensions
            adjustViewBounds = true
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        }

//        val analysisResults = TextView(this).apply {
//            text = "to be replaced",
//            id = findViewById<TextView>(R.id.analysisResults),
//            layoutParams = ViewGroup.LayoutParams(
//                this,
//                textSize = "30sp",
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//        }

        // Create a ConstraintLayout in which to add the ImageView
        constraintLayout = ConstraintLayout(this).apply {

            // Add the ImageView to the layout
            addView(imageView)
            // Add the TextView to the layout
//            addView(analysisResults)
        }

        // Set the layout as the content view.
        setContentView(constraintLayout)


        // ----------------------------------------

//        val imageView: ImageView = readImageFromFile("lowerleftblob01.jpg")
        Log.d(TAG, "just read from file: $imageView")

//        val imageDisplay = findViewById<ImageView>(R.id.image_display)
//        imageDisplay.setImageResource(imageDisplay.id)
//        Log.d(TAG, "assigned image to display $imageDisplay")

        // crashes on this constructor
        val lumiCenter = LumiCenter(imageView)
//        imageDisplay.setImageBitmap(lumiCenter.bitmap)

        var sumOnVals = lumiCenter.sumOverImage() { _, _ -> 1 }

        Log.d(TAG, "sumOnVals = $sumOnVals")
        val xEst: Double = (lumiCenter.sumOverImage { x: Int, _ -> x})
        Log.d(TAG, "x-center: $xEst")
        val yEst: Double = (lumiCenter.sumOverImage { _, y: Int -> y})
        Log.d(TAG, "y-center: $yEst")


//        val analysisResults = findViewById<TextView>(R.id.analysisResults)

        Log.d(TAG, "analysisResults is null? $analysisResults")

        // Attempt to invoke virtual method 'java.lang.Class java.lang.Object.getClass()' on a null object reference
        Log.d(TAG, "type of analysisResults: ${analysisResults::class.simpleName}")
        analysisResults.text = "${xEst.toInt()}, ${yEst.toInt()}"

    }




    private fun readImageFromFile(filename: String): ImageView {
        var imageView: ImageView = ImageView(this)

        // from:
        // https://stackoverflow.com/questions/8642823/using-setimagedrawable-dynamically-to-set-image-in-an-imageview
        val id: Int = resources.getIdentifier("lumicentertestbed:drawable/$filename", null, null)
        imageView.setImageResource(id)

//        imageView = findViewById<ImageView>(R.drawable.LowerLeftBlob)

//        // from:
//        // https://stackoverflow.com/questions/7645268/how-to-load-a-image-from-assets
//        try {
//            val ims: InputStream = this.assets.open(filename)
//            // load image as Drawable
//            val d: Drawable = Drawable.createFromStream(ims, null)
//            // set image to ImageView
//            imageView.setImageDrawable(d)
//        } catch (ex: IOException) {
//            Log.e(TAG, "readImageFromFile() failed to open $filename")
//        }
        return imageView
    }






// https://abhiandroid.com/ui/imageview

}
