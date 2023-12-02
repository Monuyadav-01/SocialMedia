package com.example.socialmedia.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import com.example.socialmedia.Models.Reel
import com.example.socialmedia.R
import com.example.socialmedia.Utils.REEL
import com.example.socialmedia.adapters.ReelAdapter
import com.example.socialmedia.databinding.FragmentReelBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class ReelFragment : Fragment() {

    private lateinit var binding: FragmentReelBinding
    lateinit var adapter: ReelAdapter
    var reelList = ArrayList<Reel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentReelBinding.inflate(inflater, container, false)




        adapter = ReelAdapter(requireContext(),reelList)
        binding.viewPagerReel.adapter = adapter

        Firebase.firestore.collection(REEL).get().addOnSuccessListener {
            var tempList = ArrayList<Reel>()
            for( i in it.documents){
                var reel = i.toObject<Reel>()!!
                tempList.add(reel)
                Log.d("DONE","REEL SHOW DONE")

            }
            reelList.addAll(tempList)
            reelList.random()
            reelList.reverse()
            adapter.notifyDataSetChanged()
            Log.d("DONE","REEL SHOW DONE")

        }


        return binding.root
    }


}