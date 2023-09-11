package com.example.szybkiezakupki.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.szybkiezakupki.R
import com.example.szybkiezakupki.utils.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.coroutines.delay


class EntryFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController
    private var currentUser: FirebaseUser? = null
    private var userId= FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var database: DatabaseReference
    private lateinit var userData1: UserData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_entry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        val isLogin: Boolean = mAuth.currentUser != null

        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({

            if (isLogin) {

                database.child("users").child(userId!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                userData1 = snapshot.getValue(UserData::class.java) ?: UserData("", "")
                              if(userData1.acctype==true)
                              {
                                  navController.navigate(R.id.action_entryFragment_to_shopHomeFragment)
                              }
                                else
                              {
                                  navController.navigate(R.id.action_entryFragment_to_homeFragment)
                              }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e(AddProductFragment.TAG, "Błąd odczytu danych: ${error.message}")
                        }


                    })


            }else
                navController.navigate(R.id.action_entryFragment_to_signinFragment)

        }, 2000)
    }

    private fun init(view: View) {
        mAuth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
        currentUser = mAuth.currentUser
        userId = FirebaseAuth.getInstance().currentUser?.uid
        database = FirebaseDatabase.getInstance().reference
    }
}