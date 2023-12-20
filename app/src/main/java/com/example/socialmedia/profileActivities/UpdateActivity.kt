package com.example.socialmedia.profileActivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.socialmedia.Fragments.ProfileFragment
import com.example.socialmedia.Models.User
import com.example.socialmedia.RegisterActivity
import com.example.socialmedia.Utils.USER_NODE
import com.example.socialmedia.Utils.USER_PROFILE_FOLDER
import com.example.socialmedia.Utils.uploadImage
import com.example.socialmedia.databinding.ActivityUpdateBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var user: User
    private val selectImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                uploadImage(uri, USER_PROFILE_FOLDER) {
                    if (it == null) {

                    } else {
                        user.image = it
                        binding.profileImage.setImageURI(uri)

                    }
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)


        user = User()

//        FirebaseFirestore.getInstance().collection(USER_NODE)
//            .document(Firebase.auth.currentUser!!.uid).get().addOnSuccessListener {
//                user = it.toObject<User>()!!
//                if (!user.image.isNullOrEmpty()) {
//                    Picasso.get().load(user.image).into(binding.profileImage)
//                }
//                binding.updatedName.editText?.setText(user.name)
//                binding.updateEmail.editText?.setText(user.email)
//                binding.updatePassword.editText?.setText(user.password)
//                binding.updatedBio.editText?.setText(user.bio)
//            }
//    }
//}

        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                user = it.toObject<User>()!!
                if (!user.image.isNullOrEmpty()) {
                    Picasso.get().load(user.image).into(binding.profileImage)
                }
                binding.updatedName.setText(user.name)
                binding.updateEmail.setText(user.email)
                binding.updatePassword.setText(user.password)
                binding.updatedBio.setText(user.bio)

            }
        binding.updateBtn.setOnClickListener {
            user.name = binding.updatedName.text.toString()
            user.bio = binding.updatedBio.text.toString()
            user.email = binding.updateEmail.text.toString()
            user.password = binding.updatePassword.text.toString()
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
                .set(user).addOnSuccessListener {
                    startActivity(Intent(this, ProfileFragment::class.java))

                }.addOnFailureListener {
                    Toast.makeText(this, "Something went wrong with user", Toast.LENGTH_SHORT)
                        .show()
                }

        }
        binding.profileImage.setOnClickListener {
            selectImage.launch(PickVisualMediaRequest())
        }
        binding.logoutUser.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(this,RegisterActivity::class.java))
        }
    }
}