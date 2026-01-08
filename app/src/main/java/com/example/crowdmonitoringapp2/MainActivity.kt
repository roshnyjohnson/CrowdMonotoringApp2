package com.example.crowdmonitoringapp2

import android.Manifest
<<<<<<< Updated upstream
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Switch
=======
import android.content.pm.PackageManager
import android.os.Bundle
>>>>>>> Stashed changes
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
<<<<<<< Updated upstream
=======
import java.util.concurrent.ExecutorService
>>>>>>> Stashed changes
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

<<<<<<< Updated upstream
    private lateinit var previewView: PreviewView
    private lateinit var peopleCountText: TextView
    private lateinit var bgSwitch: Switch

    private val cameraExecutor = Executors.newSingleThreadExecutor()
=======
    private lateinit var cameraView: PreviewView
    private lateinit var peopleText: TextView
    private lateinit var densityText: TextView
    private lateinit var debugText: TextView // Added for debugging
    private lateinit var cameraExecutor: ExecutorService
>>>>>>> Stashed changes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

<<<<<<< Updated upstream
        previewView = findViewById(R.id.previewView)
        peopleCountText = findViewById(R.id.peopleCountText)
        bgSwitch = findViewById(R.id.bgSwitch)

        // Camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                100
            )
        } else {
            startCamera()
        }

        // 🔁 Foreground / Background switch
        bgSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Start background foreground-service
                stopCamera()
                val intent = Intent(this, PeopleCountService::class.java)
                ContextCompat.startForegroundService(this, intent)
                Toast.makeText(this, "Background counting ON", Toast.LENGTH_SHORT).show()
            } else {
                // Stop service, start camera preview again
                stopService(Intent(this, PeopleCountService::class.java))
                startCamera()
                Toast.makeText(this, "Foreground counting ON", Toast.LENGTH_SHORT).show()
            }
        }
=======
        cameraView = findViewById(R.id.cameraView)
        peopleText = findViewById(R.id.peopleCountText)
        densityText = findViewById(R.id.densityText)
        debugText = findViewById(R.id.debugText) // Added for debugging

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
>>>>>>> Stashed changes
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

<<<<<<< Updated upstream
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(
                cameraExecutor,
                PeopleAnalyzer { count,mode ->
                    runOnUiThread {
                        peopleCountText.text = "People Count: $count\nMode: $mode"
                    }
                }
            )
=======
            val peopleAnalyzer = PeopleAnalyzer { count, density, debugOutput ->
                runOnUiThread {
                    peopleText.text = "People: $count"
                    densityText.text = "Density: $density"
                    debugText.text = debugOutput // Added for debugging
                }
            }

            val analyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, peopleAnalyzer)
                }
>>>>>>> Stashed changes

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
<<<<<<< Updated upstream
                    imageAnalysis
                )
            } catch (e: Exception) {
                e.printStackTrace()
=======
                    analyzer
                )
            } catch (exc: Exception) {
                // Handle exceptions
>>>>>>> Stashed changes
            }

        }, ContextCompat.getMainExecutor(this))
    }

<<<<<<< Updated upstream
    private fun stopCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProviderFuture.get().unbindAll()
        }, ContextCompat.getMainExecutor(this))
=======
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
>>>>>>> Stashed changes
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
