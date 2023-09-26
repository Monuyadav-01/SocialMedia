package com.example.socialmedia

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.socialmedia.Models.User
import com.example.socialmedia.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.createAcBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.loginBtn.setOnClickListener {
            if ((binding.loginEmail.editText?.text.toString() == "") or (binding.loginPass.editText?.text.toString() == "")) {
                Toast.makeText(this@LoginActivity, "Please Fill the all Details", Toast.LENGTH_LONG)
                    .show()
            }

            else{
                var user = User(binding.loginEmail.editText?.text.toString(),binding.loginPass.editText?.text.toString())
                Firebase.auth.signInWithEmailAndPassword(user.email!! , user.password!!).addOnCompleteListener {
                    if(it.isSuccessful){
                        startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this@LoginActivity ,  it.exception?.localizedMessage ,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
}