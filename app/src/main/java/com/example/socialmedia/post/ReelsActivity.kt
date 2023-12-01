package com.example.socialmedia.post

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmedia.HomeActivity
import com.example.socialmedia.Models.Reel
import com.example.socialmedia.Utils.REEL
import com.example.socialmedia.Utils.REEL_FOLDER
import com.example.socialmedia.Utils.uploadVideo
import com.example.socialmedia.databinding.ActivityReelsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ReelsActivity : AppCompatActivity() {


    private lateinit var binding: ActivityReelsBinding
    private lateinit var videoUrl: String

    private var videoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                uploadVideo(uri, REEL_FOLDER, progressDialog = ProgressDialog(this)) { url ->
                    if (url != null) {
                        videoUrl = url
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


        binding.postButton.setOnClickListener {
            val reel: Reel = Reel(videoUrl, binding.caption.editText?.text.toString())
            Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL).document()
                    .set(reel)
                    .addOnSuccessListener {
                        Log.d("DONE", "DONE WORKED")
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
            }
        }
        binding.selectVideo.setOnClickListener {
            videoLauncher.launch("video/*")
        }

        binding.cancelBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

}



