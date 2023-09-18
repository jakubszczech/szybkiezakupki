package com.example.szybkiezakupki.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.szybkiezakupki.R
import com.example.szybkiezakupki.databinding.FragmentHomeBinding
import com.example.szybkiezakupki.utils.ListData
import com.example.szybkiezakupki.utils.ProductAdapter
import com.example.szybkiezakupki.utils.ProductData
import com.example.szybkiezakupki.utils.ProductPreviewAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class HomeFragment : Fragment(), AddProductFragment.DialogNextBtnClickListener,
    ProductPreviewAdapter.ProductPreviewAdapterClicksInterface {

    private lateinit var auth: FirebaseAuth
    private  lateinit var databaseRef : DatabaseReference
    private lateinit var navController: NavController
    private  lateinit var binding: FragmentHomeBinding
    private var popUpDialog: AddProductFragment?= null
    private lateinit var adapter: ProductPreviewAdapter
    private lateinit var mList:MutableList<ProductData>
    private var ListData: ListData? = null
    private var ProductData: ProductData? = null
    private  var prodIdList: MutableList<String> = mutableListOf()
    private  var UserIdList: MutableList<String> = mutableListOf()
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
        binding.MyListsBtn.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_listFragment)
        }

    }

    private fun getDataFromFirebase() {
        //pobranie ID produktow przypisanych do listy
//        databaseRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                mList.clear()
//
//
//                //dzialajace rozwiazanie
//                for (taskSnapshot in snapshot.children) {
//                    val taskId = taskSnapshot.key ?: ""
//                    if(!taskId.startsWith("nam")) {
//                        prodIdList.add(taskId)
//                        Log.d("getdatafromfirebase", "Received prod: ${prodIdList}")
//                    }
//                }
//                adapter.notifyDataSetChanged()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
//            }
//
//        })
        /////////////////////////////
        ///pobranie Id userow

        databaseRef = FirebaseDatabase.getInstance().reference.child("users")
        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()



                //dzialajace rozwiazanie
                for (userSnapshot in snapshot.children) {
                    val userId = userSnapshot.key ?: ""
                    Log.d("1.5", "Received userID: ${userId}")

                    UserIdList.add(userId)

                }
                mList.clear()

                for(element in UserIdList) {
                    Log.d("1.5getdatafromfirebase", "Received userID: ${element}")
                    databaseRef = FirebaseDatabase.getInstance().reference.child("Product").child(element)
                    databaseRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            //dzialajace rozwiazanie
                            for (taskSnapshot in snapshot.children) {
                                val taskId = taskSnapshot.key ?: ""
                                Log.d("2getdatafromfirebase", "Received prodid: ${taskId}")

                                    val task = taskSnapshot.child("name").getValue(String::class.java)
                                    val priceString = taskSnapshot.child("price").getValue(String::class.java)
                                    val price = priceString?.toFloat() ?: 0.0f
                                    val shelfNumString = taskSnapshot.child("shelfNum").getValue(String::class.java)
                                    val shelfNum = shelfNumString?.toInt() ?: 0
                                    val isPurchased = taskSnapshot.child("isPurchased").getValue(Boolean::class.java) ?: false
                                    val category = taskSnapshot.child("category").getValue(String::class.java)
                                    val product = ProductData(
                                        taskId ?: "",
                                        task ?: "",
                                        price ?: 0.0f,
                                        shelfNum ?: 0,
                                        isPurchased ?: false,
                                        category ?: ""
                                    )

                                    mList.add(product)

                                    Log.d("3getdatafromfirebase", "Received mlist: ${mList}")


                            }
                            adapter.notifyDataSetChanged()
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(context, error.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
                }

                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
        prodIdList.clear()
        UserIdList.clear()

        ///////////////////////////
        /*
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
               // for ( taskSnapshot in snapshot.children){
               //     val product= taskSnapshot.key?.let {
               //         ProductData(it, taskSnapshot.value.toString())
               //     }
               //     if (product !=null){
               //         mList.add(product)
               //     }
               // }

                //dzialajace rozwiazanie
                for (taskSnapshot in snapshot.children) {
                    val taskId = taskSnapshot.key ?: ""
                    val task = taskSnapshot.child("name").getValue(String::class.java)
                    val priceString = taskSnapshot.child("price").getValue(String::class.java)
                    val price = priceString?.toFloat() ?: 0.0f
                    val shelfNumString = taskSnapshot.child("shelfNum").getValue(String::class.java)
                    val shelfNum=shelfNumString?.toInt()?: 0
                    val isPurchased = taskSnapshot.child("isPurchased").getValue(Boolean::class.java) ?: false
                    val category = taskSnapshot.child("category").getValue(String::class.java)
                    val product = ProductData(taskId ?: "", task ?: "", price ?: 0.0f, shelfNum ?: 0, isPurchased ?: false, category ?:"")
                    Log.d("produkty", "produkt: ${product}")

                    mList.add(product)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })

         */
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
            .child("Product")//.child(auth.currentUser?.uid.toString()) //test dostepnosci wszystkich produktow
      //  val databaseRef = FirebaseDatabase.getInstance().reference



        binding.rvList.setHasFixedSize(true)
        binding.rvList.layoutManager = LinearLayoutManager(context)
        mList= mutableListOf()
        adapter= ProductPreviewAdapter(mList)
        adapter.setListener((this))
        binding.rvList.adapter= adapter

        // Pobieramy z argumentów informacje o liście
        ListData = ListData(arguments?.getString("listId").toString(), arguments?.getString("listName").toString(), false)
        Log.d("Homefragment", "Received listId: ${ListData?.listId}")

    }

   // override fun onSaveProd(prod: String, etProductName: TextInputEditText) {
   //     databaseRef.push().setValue(prod).addOnCompleteListener{
   //         if(it.isSuccessful)
   //         {
   //             Toast.makeText(context, "Produkt dodany pomyslnie", Toast.LENGTH_SHORT).show()
   //             etProductName.text= null
   //         }
   //         else
   //         {
   //             Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
   //         }
   //         etProductName.text= null
   //         popUpDialog!!.dismiss()
   //     }
   // }
//
   // override fun onUpdateProd(ProductData: ProductData, etProductName: TextInputEditText) {
   //     val map= HashMap<String, Any>()
   //     map[ProductData.taskId]= ProductData.task
   //     databaseRef.updateChildren(map).addOnCompleteListener {
   //         if (it.isSuccessful) {
   //             Toast.makeText(context, "Zmodyfikowano", Toast.LENGTH_SHORT).show()
//
   //         } else {
   //             Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
   //         }
   //         etProductName.text= null
   //         popUpDialog!!.dismiss()
   //     }
   // }
   override fun onSaveProd(prod: String, price: String, shelf: String, category: String, etProductName: EditText, EtPriceS: EditText, EtShelfNumber: EditText, EtCategory: EditText) {
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
               EtCategory.text= null

           } else {
               Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
           }
           popUpDialog!!.dismiss()
       }
   }

    override fun onUpdateProd(
        ProductData: ProductData,
        price: String,
        shelf: String,
        category: String,
        etProductName: EditText,
        EtPriceS: EditText,
        EtShelfNumber: EditText,
        EtCategory: EditText
    ) {
        TODO("Not yet implemented")
    }

    override fun onDeleteTaskBtnClicked(ProductData: ProductData) {
        TODO("Not yet implemented")
    }

    override fun onEditTaskBtnClicked(ProductData: ProductData) {
        TODO("Not yet implemented")
    }

    override fun onPurchaseProdBtnClicked(productData: ProductData) {
        TODO("Not yet implemented")
    }

    override fun onAddProdBtnClicked(productData: ProductData) {
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("List").child(auth.currentUser?.uid.toString()).child(ListData?.listId.toString())
        Log.d("OnAddProdBtn", "listId: ${ListData?.listId}")
        Log.d("OnAddProdBtn", "taskId: ${productData?.taskId}")
        Log.d("OnAddProdBtn", "userId: ${auth.currentUser?.uid.toString()}")

        Log.d("OnAddProdBtn", "DbrefId: ${databaseRef}")


        databaseRef.push().setValue(productData?.taskId).addOnCompleteListener{
               if(it.isSuccessful)
               {
                    Toast.makeText(context, "Produkt dodany do listy ", Toast.LENGTH_SHORT).show()

               }
               else
               {
                     Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
               }


             }
    }
    /*
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

     */
    /*
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
    */
/*
    @SuppressLint("SuspiciousIndentation")
    override fun onEditTaskBtnClicked(ProductData: ProductData) {
        if(popUpDialog!=null)
            childFragmentManager.beginTransaction().remove(popUpDialog!!).commit()

            //TUTAJ WYSYLA DANE DO EDITU, KONTYNUACJA W NEW INSTANCE W ADD PRODUCT FRAGMENT
            popUpDialog= AddProductFragment.newInstance(ProductData.taskId, ProductData.task, ProductData.price, ProductData.shelfNum, ProductData.category)
            popUpDialog!!.setListener(this)
            popUpDialog!!.show(childFragmentManager, AddProductFragment.TAG)

    }
*/

}