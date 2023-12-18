package com.example.socialmedia.profileActivities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmedia.Models.User
import com.example.socialmedia.Utils.USER_NODE
import com.example.socialmedia.databinding.ActivityProfilePhotoDisplayBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class ProfilePhotoDisplayActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePhotoDisplayBinding
    lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePhotoDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = User()
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
                .addOnSuccessListener {
                    user = it.toObject<User>()!!

                    if (!user.image.isNullOrEmpty()) {
                        Picasso.get().load(user.image).into(binding.profileImage)
                    }

                }
        }


    }
