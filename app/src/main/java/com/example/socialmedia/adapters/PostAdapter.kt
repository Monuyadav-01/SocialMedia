package com.example.socialmedia.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmedia.Models.Post
import com.example.socialmedia.Models.User
import com.example.socialmedia.R
import com.example.socialmedia.Utils.USER_NODE
import com.example.socialmedia.databinding.PostRvBinding
import com.github.marlonlom.utilities.timeago.TimeAgo
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

        try {
            Firebase.firestore.collection(USER_NODE).document(postList.get(position).uid).get()
                .addOnSuccessListener {
                    var user = it.toObject<User?>()
                    if (user != null) {
                        Glide.with(context).load(user.image).placeholder(R.drawable.user_icon)
                            .into(holder.binding.profileImage)
                    }

                    holder.binding.name.text = user?.name

                }
        } catch (e: Exception) {

        }
        Glide.with(context).load(postList[position].postUrl).placeholder(R.drawable.loading)
            .into(holder.binding.post)
        try {
            val text = TimeAgo.using(postList[position].time.toLong())
            holder.binding.time.text =text
        }
        catch (e : Exception){
            holder.binding.time.text =null
        }

        holder.binding.share.setOnClickListener {
            val i  = Intent(Intent.ACTION_SEND)
            i.type  ="text/plain"
            i.putExtra(Intent.EXTRA_TEXT,postList[position].postUrl)
            context.startActivity(i)
        }

        holder.binding.caption.text = postList[position].caption

        holder.binding.like.setOnClickListener {
            holder.binding.like.setImageResource(R.drawable.likedpost)
        }


    }
}