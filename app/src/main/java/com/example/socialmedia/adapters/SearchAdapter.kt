package com.example.socialmedia.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.socialmedia.Models.User
import com.example.socialmedia.R
import com.example.socialmedia.Utils.FOLLOW
import com.example.socialmedia.databinding.SearchViewBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchAdapter(val context: Context, val userList: ArrayList<User>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {


    inner class ViewHolder(var binding: SearchViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isFollow = false
        Glide.with(context).load(userList[position].image).placeholder(R.drawable.user)
            .into(holder.binding.profileImage)

        Firebase.firestore.collection(Firebase.auth.currentUser?.email!! + FOLLOW)
            .whereEqualTo("email", userList[position].email).get().addOnSuccessListener {
                if (it.documents.size == 0) {
                    isFollow = false

                } else {
                    holder.binding.followBtn.text = "UnFollow"
                    isFollow = true
                }
            }
        holder.binding.followBtn.setOnClickListener {

            if (isFollow) {
                Firebase.firestore.collection(Firebase.auth.currentUser?.email!! + FOLLOW)
                    .whereEqualTo("email", userList[position].email).get().addOnSuccessListener {

                        Firebase.firestore.collection(Firebase.auth.currentUser?.email + FOLLOW)
                            .document(
                                it.documents[0].id
                            ).delete()
                    }
                holder.binding.followBtn.text = "Follow"
                isFollow = false

            } else {
                Firebase.firestore.collection(Firebase.auth.currentUser?.email!! + FOLLOW)
                    .document()
                    .set(userList[position])
                holder.binding.followBtn.text = "UnFollow"
                isFollow = true
            }

        }
        holder.binding.userName.text = userList[position].name
    }
}