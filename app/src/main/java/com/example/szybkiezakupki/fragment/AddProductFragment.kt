package com.example.szybkiezakupki.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.szybkiezakupki.R
import com.example.szybkiezakupki.databinding.FragmentAddProductBinding
import com.example.szybkiezakupki.utils.ProductData
import com.google.android.material.textfield.TextInputEditText


class AddProductFragment : DialogFragment() {


    private lateinit var binding: FragmentAddProductBinding
    private lateinit var listener: DialogNextBtnClickListener
    private var ProductData: ProductData? = null

    fun setListener(listener: DialogNextBtnClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "AddProductFragment"

        @JvmStatic
        fun newInstance(taskId: String, task: String) = AddProductFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            ProductData = ProductData(
                arguments?.getString("taskId").toString(),
                arguments?.getString("task").toString()
            )
            binding.etProductName.setText(ProductData?.task)
        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.btnAdd1.setOnClickListener {
            val product = binding.etProductName.text.toString()

            if (product.isNotEmpty()) {
                if(ProductData==null) {
                    listener.onSaveProd(product, binding.etProductName)
                }else{
                    ProductData?.task = product
                    listener.onUpdateProd(ProductData!!, binding.etProductName)
                }


            } else {
                Toast.makeText(context, "Wpisz nazwe produktu", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    interface DialogNextBtnClickListener {
        fun onSaveProd(prod: String, etProductName: TextInputEditText)
        fun onUpdateProd(ProductData: ProductData, etProductName: TextInputEditText)

    }
}