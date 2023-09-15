package com.example.szybkiezakupki.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.szybkiezakupki.R
import com.example.szybkiezakupki.databinding.FragmentAddListBinding
import com.example.szybkiezakupki.databinding.FragmentAddProdToListBinding
import com.example.szybkiezakupki.utils.ListData
import com.example.szybkiezakupki.utils.ProductData


class AddProdToListFragment : Fragment() {

    private lateinit var binding: FragmentAddProdToListBinding

    private var ListData: ListData? = null
    private var  listIdAr: String? =null
    private var  listNameAr: String? =null
    companion object {
        const val TAG = "AddProdToListFragment"

        @JvmStatic
        fun newInstance(listId: String, listName: String) = AddProdToListFragment().apply {
            arguments = Bundle().apply {

                putString("listId", listId)
                putString("listName", listName)
                ListData= ListData(listId, listName, false)

                Log.d("newinstance", "Received listId: $arguments, listName: $listName")
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

        // Pobieramy z argumentów informacje o liście
        ListData = ListData(arguments?.getString("listId").toString(), arguments?.getString("listName").toString(), false)
        Log.d("OnCreatedView", "Received listName: ${ListData?.listId}")




            Log.d("OnCreatedView", "Received listName: ${ListData?.listName}")


            // Teraz możesz użyć wszystkich zmiennych ProductData
            binding.EtListNameDisplay.setText(ListData?.listName)



    }
   fun funSetString(first: String): String{

       var root= first

       return root

    }


}