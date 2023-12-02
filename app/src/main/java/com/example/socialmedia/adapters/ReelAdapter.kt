package com.example.socialmedia.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialmedia.Models.Reel
import com.example.socialmedia.R
import com.example.socialmedia.databinding.ReelFragmentDgBinding
import com.squareup.picasso.Picasso

class ReelAdapter(var context: Context, var reelsList: ArrayList<Reel>) :
    RecyclerView.Adapter<ReelAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ReelFragmentDgBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelAdapter.ViewHolder {

        var binding = ReelFragmentDgBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(reelsList.get(position).profileLink).placeholder(R.drawable.user)
            .into(holder.binding.profileImage)
        holder.binding.reelCaption.setText(reelsList.get(position).caption)
        holder.binding.videoView.setVideoPath(reelsList.get(position).reelUrl)
        holder.binding.videoView.setOnPreparedListener {
            holder.binding.videoView.start()
        }
    }

    override fun getItemCount(): Int {
        return reelsList.size
    }


}