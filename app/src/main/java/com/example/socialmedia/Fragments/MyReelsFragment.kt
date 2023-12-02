package com.example.socialmedia.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.socialmedia.Models.Post
import com.example.socialmedia.Models.Reel
import com.example.socialmedia.R
import com.example.socialmedia.Utils.REEL
import com.example.socialmedia.adapters.MyPostRvAdapter
import com.example.socialmedia.adapters.MyReelAdapter
import com.example.socialmedia.databinding.FragmentMyReelsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class MyReelsFragment : Fragment() {

    private lateinit var binding: FragmentMyReelsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = FragmentMyReelsBinding.inflate(inflater, container, false)

        val reelList = ArrayList<Reel>()
        val adapter = MyReelAdapter(requireContext(), reelList)
        binding.reelPost.layoutManager =
            StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.reelPost.adapter = adapter
        Firebase.firestore.collection(Firebase.auth.currentUser?.email + REEL).get().addOnSuccessListener {
            var tempList = arrayListOf<Reel>()
            for (i in it.documents) {
                val reel: Reel = i.toObject<Reel>()!!
                tempList.add(reel)
            }
            reelList.addAll(tempList)
            adapter.notifyDataSetChanged()

        }
        return binding.root
    }


}