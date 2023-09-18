package com.example.szybkiezakupki.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.szybkiezakupki.databinding.FragmentAddListBinding
import com.example.szybkiezakupki.utils.ListData
import com.google.android.material.textfield.TextInputEditText


class AddListFragment : DialogFragment() {


    private lateinit var binding: FragmentAddListBinding
    private lateinit var listener: DialogNextBtnClickListener
    private var ListData: ListData? = null

    fun setListener(listener: AddListFragment.DialogNextBtnClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "AddListFragment"

        @JvmStatic
        fun newInstance( listId:String, listName: String, isPurchased: Boolean) = AddListFragment().apply {
            arguments = Bundle().apply {
                putString("listId", listId)
                putString("listName", listName)

            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {

            // binding.etProductName.setText(ProductData?.task)
            val listId = arguments?.getString("listId").toString()
            val listName = arguments?.getString("listName").toString()


            ListData= ListData(listId, listName, false)

            // Teraz możesz użyć wszystkich zmiennych ProductData
            binding.etListName.setText(ListData?.listName)



            // binding.etProductName.setText(ProductData?.task)

            // Dodaj pozostałe zmienne ProductData w odpowiednich miejscach
        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.btnAdd1.setOnClickListener {
            val list = binding.etListName.text.toString()


            if (list.isNotEmpty()) {
                if(ListData==null) {
                    listener.onSaveProd(list, binding.etListName)
                }else{
                    ListData?.listName = list
                    listener.onUpdateProd(ListData!!, binding.etListName)
                }


            } else {
                Toast.makeText(context, "Podaj nazwę listy!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    interface DialogNextBtnClickListener {
        fun onSaveProd(list: String, etListName: EditText)
        fun onUpdateProd(ListData: ListData, etListName: EditText)


    }
}