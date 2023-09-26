package com.example.socialmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.socialmedia.Models.User
import com.example.socialmedia.Util.uploadImage
import com.example.socialmedia.Utils.USER_NODE
import com.example.socialmedia.Utils.USER_PROFILE_IMAGE
import com.example.socialmedia.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var user: User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, USER_PROFILE_IMAGE) {
                if(it == null){

                }
                else{
                    user.image= it
                    binding.profileImage.setImageURI(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = User()
        binding.resister.setOnClickListener {
            if (
                (binding.signUpName.editText?.text.toString() == "") or
                (binding.signUpEmail.editText?.text.toString() == "")
                or (binding.signUpPassword.editText?.text.toString() == "")
            ) {
                Toast.makeText(
                    this@SignUpActivity,
                    "Please fill required information",
                    Toast.LENGTH_LONG
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
                            .document(Firebase.auth.currentUser!!.uid).set(user)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this@SignUpActivity,
                                    "Login",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            result.exception?.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        binding.plusImage.setOnClickListener {
            launcher.launch("image/*")
        }
    }

}