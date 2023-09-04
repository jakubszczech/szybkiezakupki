package com.example.szybkiezakupki.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.szybkiezakupki.R
import com.example.szybkiezakupki.databinding.FragmentHomeBinding
import com.example.szybkiezakupki.utils.ProductAdapter
import com.example.szybkiezakupki.utils.ProductData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class HomeFragment : Fragment(), AddProductFragment.DialogNextBtnClickListener,
    ProductAdapter.ProductAdapterClicksInterface {

    private lateinit var auth: FirebaseAuth
    private  lateinit var databaseRef : DatabaseReference
    private lateinit var navController: NavController
    private  lateinit var binding: FragmentHomeBinding
    private var popUpDialog: AddProductFragment?= null
    private lateinit var adapter: ProductAdapter
    private lateinit var mList:MutableList<ProductData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init(view)
        getDataFromFirebase()
        addProducts()

        binding.ProfileBtn.setOnClickListener{
            navController.navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }

    private fun getDataFromFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for ( taskSnapshot in snapshot.children){
                    val product= taskSnapshot.key?.let {
                        ProductData(it, taskSnapshot.value.toString())
                    }
                    if (product !=null){
                        mList.add(product)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    @SuppressLint("SuspiciousIndentation")
    private fun addProducts() {
        binding.btnAdd.setOnClickListener{
           if(popUpDialog!= null)
           childFragmentManager.beginTransaction().remove(popUpDialog!!).commit()
            popUpDialog= AddProductFragment()
            popUpDialog!!.setListener(this)
            popUpDialog!!.show(childFragmentManager, AddProductFragment.TAG)
        }
    }

    private fun init(view: View) {


        navController = Navigation.findNavController(view)

        auth= FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("Product").child(auth.currentUser?.uid.toString())


        binding.rvList.setHasFixedSize(true)
        binding.rvList.layoutManager = LinearLayoutManager(context)
        mList= mutableListOf()
        adapter= ProductAdapter(mList)
        adapter.setListener((this))
        binding.rvList.adapter= adapter
    }

    override fun onSaveProd(prod: String, etProductName: TextInputEditText) {
        databaseRef.push().setValue(prod).addOnCompleteListener{
            if(it.isSuccessful)
            {
                Toast.makeText(context, "Produkt dodany pomyslnie", Toast.LENGTH_SHORT).show()
                etProductName.text= null
            }
            else
            {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            etProductName.text= null
            popUpDialog!!.dismiss()
        }
    }

    override fun onUpdateProd(ProductData: ProductData, etProductName: TextInputEditText) {
        val map= HashMap<String, Any>()
        map[ProductData.taskId]= ProductData.task
        databaseRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Zmodyfikowano", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            etProductName.text= null
            popUpDialog!!.dismiss()
        }
    }

    override fun onDeleteTaskBtnClicked(ProductData: ProductData) {
        databaseRef.child(ProductData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful)
            {
                Toast.makeText(context, "Usunieto produkt", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditTaskBtnClicked(ProductData: ProductData) {
        if(popUpDialog!=null)
            childFragmentManager.beginTransaction().remove(popUpDialog!!).commit()


            popUpDialog= AddProductFragment.newInstance(ProductData.taskId, ProductData.task)
            popUpDialog!!.setListener(this)
            popUpDialog!!.show(childFragmentManager, AddProductFragment.TAG)

    }


}