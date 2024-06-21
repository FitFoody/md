package com.example.fitfoody.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.example.fitfoody.R
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.label.Category
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.detector.ObjectDetector

class ImageClassifierHelper(
    var maxResults: Int = 1,
    val modelName: String = "model.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?,
    var threshold: Float = 0.1f
) {

    private var imageClassifier: ObjectDetector? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ObjectDetector.ObjectDetectorOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ObjectDetector.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        try {
            val bitmap = uriToBitmap(imageUri)
            val tensorImage = TensorImage.fromBitmap(bitmap)
            val results = imageClassifier?.detect(tensorImage)

            if (results != null && results.isNotEmpty()) {
                val categories = results.flatMap { it.categories }
                classifierListener?.onResults(categories)
            } else {
                classifierListener?.onError("No classification result")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Classification error: ${e.localizedMessage}", e)
            classifierListener?.onError("Error during classification: ${e.localizedMessage}")
        }
    }

    private fun uriToBitmap(uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, e.message.toString())
            null
        }
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(results: List<Category>)
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}
