package com.example.socialmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.socialmedia.Models.User
import com.example.socialmedia.Utils.USER_NODE
import com.example.socialmedia.Utils.USER_PROFILE_FOLDER
import com.example.socialmedia.Utils.uploadImage
import com.example.socialmedia.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shashank.sony.fancytoastlib.FancyToast

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
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
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = User()

        binding.signUpBtn.setOnClickListener {
            if (binding.signUpName.editText?.text.toString()
                    .equals("") || binding.signUpEmail.editText?.text.toString()
                    .equals("") || binding.signUpPassword.editText?.text.toString().equals("")
            ) {
                FancyToast.makeText(
                    this,
                    "Please provide all details",
                    FancyToast.LENGTH_LONG,
                    FancyToast.WARNING,
                    true
                ).show()

            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.signUpEmail.editText?.text.toString(),
                    binding.signUpPassword.editText?.text.toString()
                ).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        user.name = binding.signUpName.editText?.text.toString()
                        user.email = binding.signUpEmail.editText?.text.toString()
                        user.password = binding.signUpPassword.editText?.text.toString()

                        Firebase.firestore.collection(USER_NODE)
                            .document(FirebaseAuth.getInstance().currentUser!!.uid).set(user)
                            .addOnSuccessListener {
                                startActivity(Intent(this, HomeActivity::class.java))
                            }.addOnFailureListener {
                                Log.d("Failure", "FireStore not successfully done")
                            }
                    } else {
                        FancyToast.makeText(
                            this,
                            "SomeThing went wrong",
                            FancyToast.LENGTH_LONG,
                            FancyToast.ERROR,
                            true
                        ).show()
                    }

                }

            }
        }
        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.plusImage.setOnClickListener {
            selectImage.launch(PickVisualMediaRequest())

        }
    }
}

