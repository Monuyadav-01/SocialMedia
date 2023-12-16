package com.example.socialmedia.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedia.Models.Post
import com.example.socialmedia.R
import com.example.socialmedia.Utils.POST
import com.example.socialmedia.adapters.PostAdapter
import com.example.socialmedia.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding

    private var postList = ArrayList<Post>()
    private lateinit var adapter: PostAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.materialToolbar2)

        adapter = PostAdapter(requireContext() , postList)
        binding.postRvHome.layoutManager = LinearLayoutManager(requireContext())
        binding.postRvHome.adapter = adapter

        Firebase.firestore.collection(POST).get().addOnSuccessListener {
            var tempList = ArrayList<Post>()
            postList.clear()

            for(i in it.documents){
                var post : Post = i.toObject<Post>()!!
                tempList.add(post)

            }
            postList.addAll(tempList)
            adapter.notifyDataSetChanged()
        }


        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


}