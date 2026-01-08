package com.example.crowdmonitoringapp2

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class PeopleAnalyzer(
    private val onResult: (Int, String, String) -> Unit
) : ImageAnalysis.Analyzer {

    // Set up face detector options
    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .build()

    private val detector = FaceDetection.getClient(options)

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            detector.process(image)
                .addOnSuccessListener { faces ->
                    val peopleCount = faces.size
                    
                    val debugOutput = "Faces detected: $peopleCount"

                    val density = when {
                        peopleCount <= 2 -> "Low"
                        peopleCount <= 5 -> "Medium"
                        else -> "High"
                    }

                    onResult(peopleCount, density, debugOutput)
                }
                .addOnFailureListener { e ->
                    onResult(0, "Error", "Error: ${e.message}")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
