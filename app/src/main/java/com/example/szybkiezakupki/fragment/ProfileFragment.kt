package com.example.szybkiezakupki.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.szybkiezakupki.R
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.szybkiezakupki.databinding.FragmentProfileBinding
import com.example.szybkiezakupki.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class ProfileFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentProfileBinding
    private var currentUser: FirebaseUser? = null


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
                        navController.navigate(R.id.action_profileFragment_to_signinFragment)                    } else {

                    }
                        // Błąd usuwania
                    Toast.makeText(requireContext(), "Blad, konto nie usuniete" ,Toast.LENGTH_SHORT).show()
                    }
        }
    }


    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }
}