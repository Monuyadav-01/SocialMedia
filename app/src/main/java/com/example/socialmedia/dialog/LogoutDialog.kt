package com.example.socialmedia.dialog

import android.content.Intent
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.socialmedia.Models.User
import com.example.socialmedia.R
import com.example.socialmedia.RegisterActivity
import com.example.socialmedia.Utils.USER_NODE
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

fun Fragment.bottomSheetLogoutDialog() {
    val dialog = BottomSheetDialog(requireContext())
    val view = layoutInflater.inflate(R.layout.logout_dialog, null)

    dialog.setContentView(view)
    dialog.show()
    val logoutBtn = view.findViewById<AppCompatButton>(R.id.logout_btn)
    val logoutprofileImage = view.findViewById<CircleImageView>(R.id.logout_profile_image)
    val logoutName = view.findViewById<TextView>(R.id.logout_user_name)

    User()
    FirebaseFirestore.getInstance().collection(USER_NODE)
        .document(Firebase.auth.currentUser!!.uid).get()
        .addOnSuccessListener {
            val user: User = it.toObject<User>()!!
            logoutName.text= user.name
            if (!user.image.isNullOrEmpty()) {
                Picasso.get().load(user.image).into(logoutprofileImage)
            }
        }
    logoutBtn.setOnClickListener {
        Firebase.auth.signOut()
        startActivity(Intent(requireContext(), RegisterActivity::class.java))
    }


}