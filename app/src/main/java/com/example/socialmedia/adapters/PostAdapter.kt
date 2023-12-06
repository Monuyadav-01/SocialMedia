package com.example.socialmedia.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmedia.Models.Post
import com.example.socialmedia.Models.User
import com.example.socialmedia.R
import com.example.socialmedia.Utils.USER_NODE
import com.example.socialmedia.databinding.MyPostRvDesignBinding
import com.example.socialmedia.databinding.PostRvBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class PostAdapter(var context: Context, var postList: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: PostRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Firebase.firestore.collection(USER_NODE).document(postList.get(position).uid).get()
            .addOnSuccessListener {
                var user = it.toObject<User>()


                if (user != null) {
                    Glide.with(context).load(user.image).placeholder(R.drawable.user)
                }

                holder.binding.name.text = user?.name

            }
        Glide.with(context).load(postList.get(position).postUrl).placeholder(R.drawable.user)

        holder.binding.time.text = postList.get(position).time

        holder.binding.caption.text = postList.get(position).caption


    }
}