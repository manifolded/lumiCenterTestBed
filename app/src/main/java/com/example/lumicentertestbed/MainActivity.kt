package com.example.lumicentertestbed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "lumiCenterTestBed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Instantiate an ImageView and define its properties
        imageView.setImageResource(R.drawable.lowerleftblob01)

        imageView.post {
            // ----------------------------------------
            val lumiCenter = LumiCenter(imageView)

            val sumOnVals: Long = lumiCenter.sumOverImage { _, _ -> 1 }
            Log.d(TAG, "sumOnVals = $sumOnVals")
            val xEst: Long = (lumiCenter.sumOverImage { x: Int, _ -> x}) / sumOnVals
            Log.d(TAG, "x-center: $xEst")
            val yEst: Long = (lumiCenter.sumOverImage { _, y: Int -> y}) / sumOnVals
            Log.d(TAG, "y-center: $yEst")

            analysisResults.text = "$xEst, $yEst"
        }
    }
}
