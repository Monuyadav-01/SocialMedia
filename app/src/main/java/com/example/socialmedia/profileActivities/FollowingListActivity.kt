package com.example.socialmedia.profileActivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedia.Fragments.ProfileFragment
import com.example.socialmedia.Models.User
import com.example.socialmedia.Utils.FOLLOW
import com.example.socialmedia.adapters.FollowingAdpater
import com.example.socialmedia.databinding.ActivityFollowingListBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class FollowingListActivity : AppCompatActivity() {


    private lateinit var binding: ActivityFollowingListBinding
    lateinit var adapter: FollowingAdpater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowingListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this, ProfileFragment::class.java))
        }


        var userList = ArrayList<User>()
        binding.followingListRv.layoutManager = LinearLayoutManager(this)
        adapter = FollowingAdpater(this, userList)
        binding.followingListRv.adapter = adapter

        Firebase.firestore.collection(Firebase.auth.currentUser!!.email + FOLLOW).get()
            .addOnSuccessListener {
                var tempList = ArrayList<User>()
                for (i in it.documents) {
                    var user = i.toObject<User>()!!
                    tempList.add(user)
                }
                userList.addAll(tempList)
                adapter.notifyDataSetChanged()
            }
        binding.materialToolbar.title = "Following : ${userList.size}"
    }
}