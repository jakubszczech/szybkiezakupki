package com.example.szybkiezakupki.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.szybkiezakupki.R
import com.example.szybkiezakupki.databinding.FragmentShopHomeBinding
import com.example.szybkiezakupki.utils.ProductAdapter
import com.example.szybkiezakupki.utils.ProductData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ShopHomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private  lateinit var databaseRef : DatabaseReference
    private lateinit var navController: NavController
    private  lateinit var binding: FragmentShopHomeBinding
    private var popUpDialog: AddProductFragment?= null
    private lateinit var adapter: ProductAdapter
    private lateinit var mList:MutableList<ProductData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentShopHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
       // getDataFromFirebase()
       // addProducts()

        binding.profileBtn.setOnClickListener{
            navController.navigate(R.id.action_shopHomeFragment_to_profileFragment)
        }
        binding.yourProdBtn.setOnClickListener {
            navController.navigate(R.id.action_shopHomeFragment_to_shopProductsFragment)
        }
        binding.OtherProdBtn.setOnClickListener {
            navController.navigate(R.id.action_shopHomeFragment_to_shopListFragment)
        }
    }
    private fun init(view: View) {


        navController = Navigation.findNavController(view)

        auth= FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Product").child(auth.currentUser?.uid.toString())


      //  binding.rvList.setHasFixedSize(true)
      //  binding.rvList.layoutManager = LinearLayoutManager(context)
      //  mList= mutableListOf()
      //  adapter= ProductAdapter(mList)
      //  adapter.setListener((this))
      //  binding.rvList.adapter= adapter
    }

}