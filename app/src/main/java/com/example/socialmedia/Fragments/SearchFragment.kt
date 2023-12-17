package com.example.socialmedia.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmedia.Models.User
import com.example.socialmedia.Utils.USER_NODE
import com.example.socialmedia.adapters.SearchAdapter
import com.example.socialmedia.databinding.FragmentSearchBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase


class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding

    lateinit var adapter: SearchAdapter
    private var userList = ArrayList<User>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.searchUserNameRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(requireContext(), userList)
        binding.searchUserNameRv.adapter = adapter

//        Firebase.firestore.collection(USER_NODE).get().addOnSuccessListener {
//
//            var tempList = ArrayList<User>()
//            userList.clear()
//            if (it.isEmpty) {
//
//            } else {
//                for (i in it.documents) {
//                    if (i.id.toString().equals(Firebase.auth.currentUser!!.uid.toString())) {
//                    } else {
//                        val user: User = i.toObject<User>()!!
//                        tempList.add(user)
//                    }
//
//                }
//            }
//
//
//            userList.addAll(tempList)
//            adapter.notifyDataSetChanged()
//
//        }

        binding.searchUserButton.setOnClickListener {
            val text = binding.searchUser.text.toString()
            Firebase.firestore.collection(USER_NODE).whereEqualTo("name", text).get()
                .addOnSuccessListener {
                    val tempList = ArrayList<User>()
                    userList.clear()

                    if (it.isEmpty) {


                    } else {
                        for (i in it.documents) {

                            if (i.id.toString() .equals(Firebase.auth.currentUser!!.uid.toString())) {

                            } else {
                                val user: User = i.toObject<User>()!!
                                tempList.add(user)
                            }
                        }
                        userList.addAll(tempList)
                        adapter.notifyDataSetChanged()
                    }
                }
        }

            return binding.root
        }
    }



