package com.example.socialmedia.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.socialmedia.Models.User
import com.example.socialmedia.Utils.FOLLOW
import com.example.socialmedia.Utils.USER_NODE
import com.example.socialmedia.adapters.ViewPagerAdapter
import com.example.socialmedia.databinding.FragmentProfileBinding
import com.example.socialmedia.dialog.bottomSheetLogoutDialog
import com.example.socialmedia.profileActivities.FollowingListActivity
import com.example.socialmedia.profileActivities.ProfilePhotoDisplayActivity
import com.example.socialmedia.profileActivities.UpdateActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.editProfile.setOnClickListener {
            val intent = Intent(activity, UpdateActivity::class.java)
            intent.putExtra("MODE", 1)
            activity?.startActivity(intent)

        }
        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragments(MyPostFragment(), "Post")
        viewPagerAdapter.addFragments(MyReelsFragment(), "Reel")
        binding.viewPager.adapter = viewPagerAdapter

        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.profileImage.setOnClickListener {
            startActivity(Intent(requireContext(), ProfilePhotoDisplayActivity::class.java))
        }
        binding.followingCnt.setOnClickListener {
            startActivity(Intent(requireContext(), FollowingListActivity::class.java))
        }
        binding.text.setOnClickListener {
            startActivity(Intent(requireContext(), FollowingListActivity::class.java))
        }



        return binding.root
    }


    override fun onStart() {
        super.onStart()
        FirebaseFirestore.getInstance().collection(USER_NODE)
            .document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val user: User = it.toObject<User>()!!
                binding.name.text = user.name
                binding.bio.text = user.bio
                if (!user.image.isNullOrEmpty()) {
                    Picasso.get().load(user.image).into(binding.profileImage)
                }
            }


        val flCnt = ArrayList<User>()
        Firebase.firestore.collection(Firebase.auth.currentUser!!.email + FOLLOW).get()
            .addOnSuccessListener {
                val followerCountList = ArrayList<User>()
                for (i in it.documents) {
                    followerCountList.add(i.toObject<User>()!!)
                }
                flCnt.addAll(followerCountList)
                binding.followingCnt.text = flCnt.size.toString()
            }

        binding.name.setOnClickListener {
            bottomSheetLogoutDialog()
        }

    }
}