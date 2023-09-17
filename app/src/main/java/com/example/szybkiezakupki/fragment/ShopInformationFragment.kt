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
import androidx.navigation.fragment.findNavController
import com.example.szybkiezakupki.R
import com.example.szybkiezakupki.databinding.ShopFragmentInformationBinding
import com.example.szybkiezakupki.utils.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ShopInformationFragment : Fragment() {


    private lateinit var binding: ShopFragmentInformationBinding
    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private var currentUser: FirebaseUser? = null
    private lateinit var database: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding= ShopFragmentInformationBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)


        binding.nextBtn.setOnClickListener {

            val name = binding.shopNameEt.text.toString()
            val surname = binding.addressEt.text.toString()
            if (name.isNotEmpty() && surname.isNotEmpty()) {

                //  val userData = mapOf(
                //      "name" to name,
                //      "surname" to surname
                //  )
                val userData= UserData(name, surname, true)
                // Zapisanie danych do Firebase Realtime Database
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    database.child("users").child(userId).setValue(userData)
                        .addOnSuccessListener {
                            // Sukces - dane zapisane
                            navController.navigate(R.id.action_shopInformationFragment_to_shopHomeFragment)
                            Toast.makeText(context, "Dane zapisane", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            // Błąd - dane niezapisane
                        }
                }
            }
        }

        binding.backbtn.setOnClickListener {
            findNavController().popBackStack()  // Wróć do poprzedniego fragmentu
        }

    }


    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        database = FirebaseDatabase.getInstance().reference
    }
}