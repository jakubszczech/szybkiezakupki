package com.example.szybkiezakupki.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.szybkiezakupki.R
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.szybkiezakupki.databinding.FragmentProfileBinding
import com.example.szybkiezakupki.databinding.FragmentSignupBinding
import com.example.szybkiezakupki.fragment.AddProductFragment.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.example.szybkiezakupki.utils.UserData



class ProfileFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentProfileBinding
    private var currentUser: FirebaseUser? = null
    private var userId= FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var database: DatabaseReference
    private lateinit var userData1: UserData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentProfileBinding.inflate(inflater, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        val clientname= binding.textView3.text.toString()
        val clientsurname= binding.textView4.text.toString()

     //   data class UserData(
     //      val name: String? = "",
     //      val surname: String? = ""

     //  )
     //   {
     //       constructor(): this ("","")
     //   }

        if (userId != null) {
            // Pobranie danych użytkownika z Firebase

            database.child("users").child(userId!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            userData1 = snapshot.getValue(UserData::class.java) ?: UserData("", "", null)
                            if (userData1 != null) {
                                // Przypisanie danych do pól name i surname
                                binding.textView3.text = userData1.name
                                binding.textView4.text = userData1.surname_address
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Błąd odczytu danych: ${error.message}")
                    }


                })
        }



        //logout+ wyjscie do signin
        binding.LogoutButton.setOnClickListener {
            mAuth.signOut()
          navController.navigate(R.id.action_profileFragment_to_signinFragment)
        }
        //
        //delete acc+ wyjscie do signin
        binding.deleteaccbtn.setOnClickListener {
            currentUser?.delete()
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Powodzenie usuwania
                        Toast.makeText(requireContext(), "Konto usuniete" ,Toast.LENGTH_SHORT).show()
                        navController.navigate(R.id.action_profileFragment_to_signinFragment)                    }
                    else {

                    }
                        // Błąd usuwania
                    Toast.makeText(requireContext(), "Blad, konto nie usuniete" ,Toast.LENGTH_SHORT).show()
                    }
        }
        /////
        //back button
        binding.backbtn.setOnClickListener {
            findNavController().popBackStack()  // Wróć do poprzedniego fragmentu
        }
        binding.dataChgBtn.setOnClickListener {
            if(userData1.acctype==false)
            navController.navigate(R.id.action_profileFragment_to_informationFragment2)
            else
                navController.navigate(R.id.action_profileFragment_to_shopInformationFragment)
        }
    }


    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        userId = FirebaseAuth.getInstance().currentUser?.uid
        database = FirebaseDatabase.getInstance().reference
    }

}