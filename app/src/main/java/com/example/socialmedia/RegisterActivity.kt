package com.example.socialmedia

import android.annotation.SuppressLint
import android.content.Intent
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso

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


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = User()

        if (intent.hasExtra("MODE")) {
            if (intent.getIntExtra("MODE", -1) == 1) {
                binding.signUpBtn.text = "update profile"

                FirebaseFirestore.getInstance().collection(USER_NODE)
                    .document(Firebase.auth.currentUser!!.uid).get()
                    .addOnSuccessListener {

                        user = it.toObject<User>()!!

                        if (!user.image.isNullOrEmpty()) {
                            Picasso.get().load(user.image).into(binding.profileImage)

                        }
                        binding.signUpName.editText?.setText(user.name)
                        binding.signUpEmail.editText?.setText(user.email)
                        binding.signUpPassword.editText?.setText(user.password)

                    }
            }
        }


        binding.signUpBtn.setOnClickListener {
            if (intent.hasExtra("MODE")) {
                if (intent.getIntExtra("MODE", -1) == 1) {
                    Firebase.firestore.collection(USER_NODE)
                        .document(FirebaseAuth.getInstance().currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }
                }
            } else {


                if (binding.signUpName.editText?.text.toString() == "" || binding.signUpEmail.editText?.text.toString() == "" || binding.signUpPassword.editText?.text.toString().equals("")
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
        }
        binding.loginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.plusImage.setOnClickListener {
            selectImage.launch(PickVisualMediaRequest())

        }
    }
}

