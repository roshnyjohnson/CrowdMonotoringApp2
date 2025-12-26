package com.example.crowdmonitoringapp2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import android.widget.TextView
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var cameraView: PreviewView
    private lateinit var peopleText: TextView
    private lateinit var densityText: TextView

    private val handler = Handler(Looper.getMainLooper())
    private val captureInterval = 30_000L // 30 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraView = findViewById(R.id.cameraView)
        peopleText = findViewById(R.id.peopleCountText)
        densityText = findViewById(R.id.densityText)

        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(cameraView.surfaceProvider)
            }

            val analyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val peopleAnalyzer = PeopleAnalyzer { count, density ->
                peopleText.text = "People: $count"
                densityText.text = "Density: $density"
            }

            // Run analyzer ONLY every 30 seconds
            handler.post(object : Runnable {
                override fun run() {
                    analyzer.setAnalyzer(
                        Executors.newSingleThreadExecutor(),
                        peopleAnalyzer
                    )
                    handler.postDelayed(this, captureInterval)
                }
            })

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                analyzer
            )

        }, ContextCompat.getMainExecutor(this))
    }
}
