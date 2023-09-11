package com.example.szybkiezakupki.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.szybkiezakupki.R
import com.example.szybkiezakupki.databinding.FragmentSigninBinding
import com.example.szybkiezakupki.databinding.FragmentSignupBinding
import com.example.szybkiezakupki.utils.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class SigninFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentSigninBinding
    private var currentUser: FirebaseUser? = null
    private var userId= FirebaseAuth.getInstance().currentUser?.uid
    private lateinit var database: DatabaseReference
    private lateinit var userData1: UserData

    private var backPressedOnce = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //blokowanie powrotu bo wylogowaniu
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedOnce) {
                    requireActivity().finish()  // Zamknij aplikację po dwukrotnym kliknięciu
                } else {
                    backPressedOnce = true
                    Toast.makeText(requireContext(), "Kliknij ponownie, aby zamknąć aplikację", Toast.LENGTH_SHORT).show()

                    // Reset flagi po określonym czasie (np. 2 sekundy)
                    view?.postDelayed({ backPressedOnce = false }, 2000)
                }
                // Obsługa naciśnięcia przycisku "Wstecz"
                // Tutaj możesz nie robić nic lub wyświetlić odpowiednie powiadomienie
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)



        ///////////////////////////////////
        init(view)

        binding.textViewSignUp.setOnClickListener {
            navController.navigate(R.id.action_signinFragment_to_signupFragment)
        }

        binding.nextBtn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty())

                loginUser(email, pass)
            else
                Toast.makeText(context, "Puste pola nie sa dozwolone", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loginUser(email: String, pass: String) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
            if (it.isSuccessful) {
                val isLogin: Boolean = mAuth.currentUser != null
                if (isLogin) {

                    userId = FirebaseAuth.getInstance().currentUser?.uid

                    database.child("users").child(userId!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                userData1 =
                                    snapshot.getValue(UserData::class.java) ?: UserData("", "", false)
                                if (userData1.acctype == true) {
                                    navController.navigate(R.id.action_signinFragment_to_shopHomeFragment)
                                } else {
                                    navController.navigate(R.id.action_signinFragment_to_homeFragment)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e(AddProductFragment.TAG, "Błąd odczytu danych: ${error.message}")
                        }


                    })

            }
            }
            else
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()

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