package com.example.szybkiezakupki.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.szybkiezakupki.R
import com.example.szybkiezakupki.databinding.FragmentInformationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.szybkiezakupki.utils.UserData

class InformationFragment : Fragment() {

    private lateinit var binding: FragmentInformationBinding
    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private lateinit var database: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding= FragmentInformationBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)


        binding.nextBtn.setOnClickListener {

            val name = binding.ClientNameEt.text.toString()
            val surname = binding.ClientSurnameEt.text.toString()
            if (name.isNotEmpty() && surname.isNotEmpty()) {

              //  val userData = mapOf(
              //      "name" to name,
              //      "surname" to surname
              //  )
                val userData= UserData(name, surname, false)
                // Zapisanie danych do Firebase Realtime Database
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    database.child("users").child(userId).setValue(userData)
                        .addOnSuccessListener {
                            // Sukces - dane zapisane
                            navController.navigate(R.id.action_informationFragment_to_homeFragment)
                            Toast.makeText(context, "Dane zapisane", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            // Błąd - dane niezapisane
                        }
                }
            }
        }

    }


    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        database = FirebaseDatabase.getInstance().reference
    }
}