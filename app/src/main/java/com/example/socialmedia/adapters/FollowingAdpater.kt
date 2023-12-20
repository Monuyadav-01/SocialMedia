package com.example.socialmedia.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmedia.Models.User
import com.example.socialmedia.R
import com.example.socialmedia.Utils.FOLLOW
import com.example.socialmedia.databinding.UserFollowingListBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FollowingAdpater(var context: Context, private var userList: ArrayList<User>) :
    RecyclerView.Adapter<FollowingAdpater.ViewHolder>() {


    inner class ViewHolder(var binding: UserFollowingListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserFollowingListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(context).load(userList[position].image).placeholder(R.drawable.user)
            .into(holder.binding.profileImage)


        Firebase.firestore.collection(Firebase.auth.currentUser!!.email + FOLLOW).document().get()
            .addOnSuccessListener {
                Glide.with(context).load(userList[position].image).placeholder(R.drawable.user)
                    .into(holder.binding.profileImage)
                holder.binding.userName.text = userList[position].name
            }

    }
}