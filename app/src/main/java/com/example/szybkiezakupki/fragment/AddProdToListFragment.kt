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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.szybkiezakupki.R
import com.example.szybkiezakupki.databinding.FragmentAddListBinding
import com.example.szybkiezakupki.databinding.FragmentAddProdToListBinding
import com.example.szybkiezakupki.databinding.FragmentHomeBinding
import com.example.szybkiezakupki.utils.ListData
import com.example.szybkiezakupki.utils.ProductAdapter
import com.example.szybkiezakupki.utils.ProductClientAdapter
import com.example.szybkiezakupki.utils.ProductData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class AddProdToListFragment : Fragment(),AddProductFragment.DialogNextBtnClickListener,
    ProductClientAdapter.ProductClientAdapterClicksInterface {

    private lateinit var binding: FragmentAddProdToListBinding
    private lateinit var navController: NavController
    private var ListData: ListData? = null
    private lateinit var auth: FirebaseAuth
    private  lateinit var databaseRef : DatabaseReference
    private lateinit var adapter: ProductClientAdapter
    private lateinit var mList:MutableList<ProductData>
    private  var prodIdList: MutableList<String> = mutableListOf()
    private  var UserIdList: MutableList<String> = mutableListOf()
    private  var listSum: Float= 0.0f


    companion object {
        const val TAG = "AddProdToListFragment"

        @JvmStatic
        fun newInstance(listId: String, listName: String) = AddProdToListFragment().apply {
            arguments = Bundle().apply {

                putString("listId", listId)
                putString("listName", listName)
                ListData= ListData(listId, listName, false)


                return AddProdToListFragment()

            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentAddProdToListBinding.inflate(inflater, container, false)

        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment

        binding.backbtn3.setOnClickListener {
            findNavController().popBackStack()  // Wróć do poprzedniego fragmentu
        }

        binding.AddProdToListBtn.setOnClickListener {

            SendDataAboutLsit()

        }

        // Pobieramy z argumentów informacje o liście
        ListData = ListData(arguments?.getString("listId").toString(), arguments?.getString("listName").toString(), false)




            // Teraz możesz użyć wszystkich zmiennych ProductData
            binding.EtListNameDisplay.setText(ListData?.listName)

        init(view)
        getDataFromFirebase()


    }
    private fun init(view: View) {


        navController = Navigation.findNavController(view)

        auth= FirebaseAuth.getInstance()
        // databaseRef = FirebaseDatabase.getInstance().reference
        //     .child("Product")//.child(auth.currentUser?.uid.toString()) //test dostepnosci wszystkich produktow
        databaseRef = FirebaseDatabase.getInstance().reference.child("List").child(auth.currentUser?.uid.toString()).child(ListData?.listId.toString())
        binding.rvList.setHasFixedSize(true)
        binding.rvList.layoutManager = LinearLayoutManager(context)
        mList= mutableListOf()
        adapter= ProductClientAdapter(mList)
        adapter.setListener((this))
        binding.rvList.adapter= adapter

    }
    private fun getDataFromFirebase() {
        //pobranie ID produktow przypisanych do listy
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()


                //dzialajace rozwiazanie
                for (taskSnapshot in snapshot.children) {
                    val taskId = taskSnapshot.key ?: ""
                    if(!taskId.startsWith("nam")) {
                        prodIdList.add(taskSnapshot.value.toString())
                        Log.d("getdatafromfirebase", "Received prod: ${prodIdList}")
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
        /////////////////////////////
        ///pobranie Id userow

                    databaseRef = FirebaseDatabase.getInstance().reference.child("users")
                    databaseRef.addValueEventListener(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {
                            mList.clear()
                            listSum=0.0f


                            //dzialajace rozwiazanie
                            for (userSnapshot in snapshot.children) {
                                val userId = userSnapshot.key ?: ""


                                    UserIdList.add(userId)

                            }
                            mList.clear()
                            listSum=0.0f
                            for(element in UserIdList) {

                                databaseRef = FirebaseDatabase.getInstance().reference.child("Product").child(element)
                                databaseRef.addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        //dzialajace rozwiazanie
                                        for (taskSnapshot in snapshot.children) {
                                            val taskId = taskSnapshot.key ?: ""

                                            if(taskId in prodIdList) {
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
                                                listSum+=price

                                                binding.listSum.setText(listSum.toString())
                                            }
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

    }


    fun SendDataAboutLsit()
    {
        val bundle = Bundle()
        bundle.putString("listId", ListData?.listId)
        bundle.putString("listName", this.ListData?.listName)

        val fragment = AddProdToListFragment()
        fragment.arguments = bundle// Przekazanie danych do nowego fragmentu
        Log.d("MyFragment", "listId: ${fragment.arguments}, listName: ${ListData?.listName}")

        //popUpDialog2= AddProdToListFragment.newInstance(ListData.listId, ListData.listName)

        navController.navigate(R.id.action_addProdToListFragment_to_homeFragment, bundle)
    }
    override fun onSaveProd(
        prod: String,
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


}