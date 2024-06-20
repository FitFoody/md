package com.example.fitfoody.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log

class ImageClassifierHelper(
    var maxResults: Int = 1,
    val modelName: String = "cancer_classification.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?,
    var threshold: Float = 0.1f

) {

    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
        // TODO: Menyiapkan Image Classifier untuk memproses gambar.
    }

    fun classifyStaticImage(imageUri: Uri) {
        try {
            val bitmap = uriToBitmap(imageUri)
            val tensorImage = TensorImage.fromBitmap(bitmap)
            val results = imageClassifier?.classify(tensorImage)

            if (results != null) {
                classifierListener?.onResults(
                    results,
                )
            } else {
                classifierListener?.onError("No classification result")
            }
        } catch (e: Exception) {
            Log.e("ImageClassifier", "Classification error: ${e.localizedMessage}", e)
            classifierListener?.onError("Error during classification: ${e.localizedMessage}")
        }
        // TODO: mengklasifikasikan imageUri dari gambar statis.
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
        fun onResults(
            results: List<Classifications>?,
        )
    }
    companion object {
        private const val TAG = "ImageClassifierHelper"
    }

