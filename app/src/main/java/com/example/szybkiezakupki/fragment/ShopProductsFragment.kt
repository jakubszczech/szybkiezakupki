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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.szybkiezakupki.databinding.FragmentShopProductsBinding
import com.example.szybkiezakupki.utils.ProductAdapter
import com.example.szybkiezakupki.utils.ProductData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ShopProductsFragment: Fragment(), AddProductFragment.DialogNextBtnClickListener,
    ProductAdapter.ProductAdapterClicksInterface {

    private lateinit var auth: FirebaseAuth
    private  lateinit var databaseRef : DatabaseReference
    private lateinit var navController: NavController
    private  lateinit var binding: FragmentShopProductsBinding
    private var popUpDialog: AddProductFragment?= null
    private lateinit var adapter: ProductAdapter
    private lateinit var mList:MutableList<ProductData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentShopProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        init(view)
        getDataFromFirebase()
        addProducts()

    }

    private fun getDataFromFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
        //       for ( taskSnapshot in snapshot.children){
        //           val product= taskSnapshot.key?.let {
        //               ProductData(it, taskSnapshot.value.toString() )
        //           }
        //           if (product !=null){
        //               mList.add(product)
        //           }
        //       }
        //       adapter.notifyDataSetChanged()
        //   }


                for (taskSnapshot in snapshot.children) {
                    // Pobierz wszystkie zmienne produktu z Firebase
                    val taskId = taskSnapshot.key
                    val task = taskSnapshot.child("name").getValue(String::class.java)
                    val priceString = taskSnapshot.child("price").getValue(String::class.java)
                    val price = priceString?.toFloat() ?: 0.0f
                    val shelfNumString = taskSnapshot.child("shelfNum").getValue(String::class.java)
                    val shelfNum=shelfNumString?.toInt()?: 0
                    val isPurchased = taskSnapshot.child("isPurchased").getValue(Boolean::class.java)
                    val category = taskSnapshot.child("category").getValue(String::class.java)


                    // Tworzenie obiektu ProductData na podstawie pobranych zmiennych
                    val product = ProductData(taskId ?: "", task ?: "", price ?: 0.0f, shelfNum ?: 0, isPurchased ?: false, category ?:"")

                    mList.add(product)
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

    override fun onSaveProd(prod: String, price: String, shelf: String, category: String, etProductName: TextInputEditText, EtPriceS: TextInputEditText, EtShelfNumber: TextInputEditText, EtCategory: TextInputEditText) {
      // databaseRef.push().setValue(prod).addOnCompleteListener{
      //     if(it.isSuccessful)
      //     {
      //         Toast.makeText(context, "Produkt dodany pomyslnie", Toast.LENGTH_SHORT).show()
      //         etProductName.text= null
      //     }
      //     else
      //     {
      //         Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
      //     }
      //     etProductName.text= null
      //     popUpDialog!!.dismiss()
      // }
        val productData = mapOf(
            "name" to prod,
            "price" to price,
            "shelfNum" to shelf,
            "category" to category
        )

        databaseRef.push().setValue(productData).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Produkt dodany pomyslnie", Toast.LENGTH_SHORT).show()
                etProductName.text = null
                EtPriceS.text = null
                EtShelfNumber.text = null
                EtCategory.text=null
            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            popUpDialog!!.dismiss()
        }
    }

    override fun onUpdateProd(ProductData: ProductData, price: String, shelf: String, category: String, etProductName: TextInputEditText, EtPriceS: TextInputEditText, EtShelfNumber: TextInputEditText, EtCategory: TextInputEditText) {
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


        popUpDialog= AddProductFragment.newInstance(ProductData.taskId, ProductData.task, ProductData.price, ProductData.shelfNum, ProductData.category)
        popUpDialog!!.setListener(this)
        popUpDialog!!.show(childFragmentManager, AddProductFragment.TAG)

    }


}