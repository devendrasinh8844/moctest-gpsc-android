package com.moctestgpsc.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moctestgpsc.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up button listeners
        binding.startTestButton.setOnClickListener {
            // TODO: Navigate to test activity
        }

        binding.viewResultsButton.setOnClickListener {
            // TODO: Navigate to results activity
        }

        binding.settingsButton.setOnClickListener {
            // TODO: Navigate to settings activity
        }
    }
}