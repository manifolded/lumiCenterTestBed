package com.example.lumicentertestbed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout



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

        val analysisResults = TextView(this).apply {
            textSize = 134f
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
        }

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

        // Create a ConstraintLayout in which to add the ImageView
        constraintLayout = ConstraintLayout(this).apply {

            // Add the ImageView to the layout
            addView(imageView)

            // add text view
            addView(analysisResults)
        }

        // Set the layout as the content view.
        setContentView(constraintLayout)

        // ----------------------------------------
        // crashes on this constructor
        val lumiCenter = LumiCenter(imageView)

        var sumOnVals = lumiCenter.sumOverImage() { _, _ -> 1 }

        Log.d(TAG, "sumOnVals = $sumOnVals")
        val xEst: Double = (lumiCenter.sumOverImage { x: Int, _ -> x})/sumOnVals
        Log.d(TAG, "x-center: $xEst")
        val yEst: Double = (lumiCenter.sumOverImage { _, y: Int -> y})/sumOnVals
        Log.d(TAG, "y-center: $yEst")

//        Log.d(TAG, "analysisResults is null? $analysisResults")

        // Attempt to invoke virtual method 'java.lang.Class java.lang.Object.getClass()' on a null object reference
        Log.d(TAG, "type of analysisResults: ${analysisResults::class.simpleName}")
        analysisResults.text = "${xEst}, ${yEst}"

    }

    private fun readImageFromFile(filename: String): ImageView {
        var imageView: ImageView = ImageView(this)

        // from:
        // https://stackoverflow.com/questions/8642823/using-setimagedrawable-dynamically-to-set-image-in-an-imageview
        val id: Int = resources.getIdentifier("lumicentertestbed:drawable/$filename", null, null)
        imageView.setImageResource(id)

        return imageView
    }
}
