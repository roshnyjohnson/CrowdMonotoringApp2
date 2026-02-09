package com.example.crowdmonitoringapp2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.objectdetector.ObjectDetector
import java.util.Locale

class PeopleAnalyzer(
    context: Context,
    private val onResult: (Int, String, String) -> Unit
) : ImageAnalysis.Analyzer {

    private val options = ObjectDetector.ObjectDetectorOptions.builder()
        .setBaseOptions(
            BaseOptions.builder()
                .setModelAssetPath("person_detection.tflite")
                .build()
        )
        // Lowering threshold from 0.3 to 0.2 to catch more people in the crowd
        .setScoreThreshold(0.2f) 
        .setMaxResults(100)
        .setRunningMode(RunningMode.IMAGE)
        .build()

    private val detector = ObjectDetector.createFromOptions(context, options)

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        try {
            val bitmap = imageProxy.toBitmap()
            val rotatedBitmap = rotateBitmap(bitmap, imageProxy.imageInfo.rotationDegrees.toFloat())
            
            val mpImage = BitmapImageBuilder(rotatedBitmap).build()
            val result = detector.detect(mpImage)

            val debugOutput = StringBuilder()
            debugOutput.append("MediaPipe Engine: Searching for 100 people...\n")

            var peopleCount = 0
            result.detections().forEach { detection ->
                val category = detection.categories().firstOrNull()
                // Ensure we are only counting people
                if (category?.categoryName()?.contains("person", ignoreCase = true) == true) {
                    peopleCount++
                    debugOutput.append(String.format(Locale.US, " > Person: %.0f%%\n", category.score() * 100))
                }
            }

            val density = when {
                peopleCount <= 5 -> "Low"
                peopleCount <= 15 -> "Medium"
                else -> "High"
            }

            onResult(peopleCount, density, debugOutput.toString())

        } catch (e: Exception) {
            Log.e("PeopleAnalyzer", "MediaPipe Error: ${e.message}")
            onResult(0, "Error", "Error: ${e.message}")
        } finally {
            imageProxy.close()
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun ImageProxy.toBitmap(): Bitmap {
        val yBuffer = planes[0].buffer
        val uBuffer = planes[1].buffer
        val vBuffer = planes[2].buffer
        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()
        val nv21 = ByteArray(ySize + uSize + vSize)
        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)
        val yuvImage = android.graphics.YuvImage(nv21, android.graphics.ImageFormat.NV21, width, height, null)
        val out = java.io.ByteArrayOutputStream()
        yuvImage.compressToJpeg(android.graphics.Rect(0, 0, width, height), 100, out)
        return android.graphics.BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size())
    }
}
