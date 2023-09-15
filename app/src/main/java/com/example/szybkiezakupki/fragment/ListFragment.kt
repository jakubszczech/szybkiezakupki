package com.example.szybkiezakupki.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.szybkiezakupki.R
import com.example.szybkiezakupki.databinding.FragmentListBinding
import com.example.szybkiezakupki.utils.ListAdapter
import com.example.szybkiezakupki.utils.ListData
import com.example.szybkiezakupki.utils.ProductData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ListFragment : Fragment(), AddListFragment.DialogNextBtnClickListener,
    ListAdapter.ListAdapterClicksInterface {


    private lateinit var auth: FirebaseAuth
    private  lateinit var databaseRef : DatabaseReference
    private lateinit var navController: NavController
    private  lateinit var binding: FragmentListBinding
    private var popUpDialog: AddListFragment?= null
    private lateinit var adapter: ListAdapter
    private lateinit var mList:MutableList<ListData>
    private var popUpDialogList: AddListFragment?= null
    private var popUpDialog2: AddProdToListFragment?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFirebase()
        addList()

        binding.backbtn.setOnClickListener{
           findNavController().popBackStack()
        }

    }
    private fun getDataFromFirebase() {
        databaseRef.addValueEventListener(object : ValueEventListener {
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


                 for (taskSnapshot in snapshot.children) {
                     val listId = taskSnapshot.key ?: ""
                     val listName = taskSnapshot.child("name").getValue(String::class.java)

                     val list = ListData(listId ?: "", listName ?: "", false)


                     mList.add(list)
                 }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    @SuppressLint("SuspiciousIndentation")
    private fun addList() {
        binding.btnAddList.setOnClickListener{
            if(popUpDialog!= null)
                childFragmentManager.beginTransaction().remove(popUpDialog!!).commit()
            popUpDialog= AddListFragment()
            popUpDialog!!.setListener(this)
            popUpDialog!!.show(childFragmentManager, AddProductFragment.TAG)
        }
    }

    private fun init(view: View) {


        navController = Navigation.findNavController(view)
        auth= FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference
            .child("List").child(auth.currentUser?.uid.toString()) //test dostepnosci wszystkich produktow


        binding.rvListOfLists.setHasFixedSize(true)
        binding.rvListOfLists.layoutManager = LinearLayoutManager(context)
        mList= mutableListOf()
        adapter= ListAdapter(mList)
        adapter.setListener((this))
        binding.rvListOfLists.adapter= adapter
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
    override fun onSaveProd(list: String, etListName: TextInputEditText) {
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
        val ListData = mapOf(
            "name" to list


        )

        databaseRef.push().setValue(ListData).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Lista dodana pomyslnie", Toast.LENGTH_SHORT).show()
                etListName.text = null


            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            popUpDialog!!.dismiss()
        }
    }


    override fun onUpdateProd(ListData: ListData, etListName: TextInputEditText) {
        val map= HashMap<String, Any>()
        map[ListData.listId]= ListData.listName
        databaseRef.updateChildren(map).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, "Zmodyfikowano", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            etListName.text= null
            popUpDialog!!.dismiss()
        }
    }

    override fun onDeleteListBtnClicked(ListData: ListData) {
        databaseRef.child(ListData.listId).removeValue().addOnCompleteListener {
            if (it.isSuccessful)
            {
                Toast.makeText(context, "Usunięto listę", Toast.LENGTH_SHORT).show()
            }
            else
            {
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditListBtnClicked(ListData: ListData) {
        if(popUpDialog!=null)
            childFragmentManager.beginTransaction().remove(popUpDialog!!).commit()

        //TUTAJ WYSYLA DANE DO EDITU, KONTYNUACJA W NEW INSTANCE W ADD PRODUCT FRAGMENT
        popUpDialog= AddListFragment.newInstance(ListData.listId, ListData.listName, false)
        popUpDialog!!.setListener(this)
        popUpDialog!!.show(childFragmentManager, AddListFragment.TAG)

    }

    override fun onAddProdToListClicked(ListData: ListData) {
        val bundle = Bundle()
        bundle.putString("listId", ListData.listId)
        bundle.putString("listName", ListData.listName)
        val fragment = AddProdToListFragment()
        fragment.arguments = bundle// Przekazanie danych do nowego fragmentu
        Log.d("MyFragment", "listId: ${fragment.arguments}, listName: ${ListData.listName}")

        popUpDialog2= AddProdToListFragment.newInstance(ListData.listId, ListData.listName)

        navController.navigate(R.id.action_listFragment_to_addProdToListFragment)






    }


}