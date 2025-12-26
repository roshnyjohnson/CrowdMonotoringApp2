package com.example.crowdmonitoringapp2

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

class PeopleAnalyzer(
    private val onResult: (Int, String) -> Unit
) : ImageAnalysis.Analyzer {

    private val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.STREAM_MODE)
        .enableClassification()
        .build()

    private val detector = ObjectDetection.getClient(options)

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }

        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        detector.process(image)
            .addOnSuccessListener { objects ->
                val peopleCount = objects.count {
                    it.labels.any { label -> label.text == "Person" }
                }

                val density = when {
                    peopleCount <= 2 -> "Low"
                    peopleCount <= 5 -> "Medium"
                    else -> "High"
                }

                onResult(peopleCount, density)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}
