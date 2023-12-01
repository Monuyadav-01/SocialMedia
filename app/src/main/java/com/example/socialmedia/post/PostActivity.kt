package com.example.socialmedia.post

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmedia.HomeActivity
import com.example.socialmedia.Models.Post
import com.example.socialmedia.Models.User
import com.example.socialmedia.Utils.POST
import com.example.socialmedia.Utils.POST_FOLDER
import com.example.socialmedia.Utils.uploadImage
import com.example.socialmedia.databinding.ActivityPostBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private var imageUrl: String? = null
    private  lateinit var user: User
    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                uploadImage(uri, POST_FOLDER) {url->
                    if (url != null) {
                        binding.selectImage.setImageURI(uri)
                        imageUrl = url

                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
        }

        binding.postButton.setOnClickListener {
            val post: Post = Post(imageUrl!!, binding.caption.editText?.text.toString())
            Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document().set(post)
                    .addOnSuccessListener {
                        Log.d("DONE","DONE WORKED")
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    }
            }
        }

        binding.selectImage.setOnClickListener {
            selectImageLauncher.launch(PickVisualMediaRequest())
        }
        binding.cancelBtn.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}