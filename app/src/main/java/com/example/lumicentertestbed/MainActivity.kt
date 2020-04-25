package com.example.lumicentertestbed

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.InputStream
import kotlin.math.roundToInt

//import androidx.camera.view.PreviewView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imageDisplay = findViewById<ImageView>(R.id.image_display)

        val imageView: ImageView = readImageFromFile("lowerleftblob01.jpg")

        val lumiCenter = LumiCenter(imageView)
        imageDisplay.setImageBitmap(lumiCenter.bitmap)

        val sumOnVals = lumiCenter.sumOverImage() {_, _ -> 1}
        val xEst: Int = (lumiCenter.sumOverImage() {x: Int, _ -> x}/sumOnVals).roundToInt()
        val yEst: Int = (lumiCenter.sumOverImage() {_, y: Int -> y}/sumOnVals).roundToInt()

        analysisResults.text = "$xEst, $yEst"
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
