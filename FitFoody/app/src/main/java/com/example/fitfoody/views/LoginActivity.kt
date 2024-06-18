package com.example.fitfoody.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fitfoody.R
import com.example.fitfoody.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}