package com.example.socialmedia.post

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import com.example.socialmedia.HomeActivity
import com.example.socialmedia.R
import com.example.socialmedia.Utils.POST_FOLDER
import com.example.socialmedia.Utils.uploadImage
import com.example.socialmedia.databinding.ActivityReelsBinding

class ReelsActivity : AppCompatActivity() {


    private lateinit var binding: ActivityReelsBinding
    private lateinit var imageUrl: String

    private var videoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uploadImage(uri, POST_FOLDER) { url ->
                    if (url != null) {

                        imageUrl = url
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReelsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        binding.selectVideo.setOnClickListener {
            videoLauncher.launch("video/#")
        }
    }
}



