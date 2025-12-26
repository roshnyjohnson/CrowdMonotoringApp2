package com.example.crowdmonitoringapp2

import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class OpenCVPeopleCounter {

    fun estimatePeopleCount(mat: Mat): Int {
        val gray = Mat()
        val blurred = Mat()
        val edges = Mat()
        val contours = ArrayList<MatOfPoint>()

        Imgproc.cvtColor(mat, gray, Imgproc.COLOR_RGBA2GRAY)
        Imgproc.GaussianBlur(gray, blurred, Size(5.0, 5.0), 0.0)
        Imgproc.Canny(blurred, edges, 50.0, 150.0)
        Imgproc.findContours(
            edges,
            contours,
            Mat(),
            Imgproc.RETR_EXTERNAL,
            Imgproc.CHAIN_APPROX_SIMPLE
        )

        return contours.size
    }
}
