package com.example.fitfoody.views

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fitfoody.databinding.ActivityResultBinding
import com.example.fitfoody.helper.ImageClassifierHelper
import org.tensorflow.lite.support.label.Category
import java.text.NumberFormat
import java.util.Locale

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)?.let { Uri.parse(it) }
        if (imageUri != null) {
            Log.d("Image URI", "showImage: $imageUri")
            binding.resultImage.setImageURI(imageUri)
            startAnalyze(imageUri)
        } else {
            Toast.makeText(this, "Image URI is null", Toast.LENGTH_SHORT).show()
            finish() // Close activity if no image URI is passed
        }
    }

    private fun startAnalyze(imageUri: Uri) {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@ResultActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResults(results: List<Category>) {
                    runOnUiThread {
                        if (results.isNotEmpty()) {
                            val sortedCategories = results.sortedByDescending { category -> category.score }
                            val displayResult = sortedCategories.joinToString("\n") { category ->
                                "${category.label} " + NumberFormat.getPercentInstance().format(category.score).trim()
                            }
                            binding.resultText.text = displayResult
                        } else {
                            binding.resultText.text = ""
                        }
                    }
                }
            }
        )
        imageClassifierHelper.classifyStaticImage(imageUri)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}
