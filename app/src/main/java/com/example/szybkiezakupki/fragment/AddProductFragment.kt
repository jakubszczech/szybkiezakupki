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
import com.google.android.material.textfield.TextInputEditText


class AddProductFragment : DialogFragment() {


    private lateinit var  binding: FragmentAddProductBinding
    private lateinit var listener: DialogNextBtnClickListener

    fun setListener(listener: DialogNextBtnClickListener){
        this.listener= listener
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()
    }

    private fun registerEvents() {
        binding.btnAdd1.setOnClickListener{
            val product= binding.etProductName.text.toString()

            if (product.isNotEmpty())
            {
                listener.onSaveProd(product, binding.etProductName)
            }
            else
            {
                Toast.makeText(context, "Wpisz nazwe produktu", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    interface DialogNextBtnClickListener{
        fun onSaveProd(prod: String, etProductName : TextInputEditText)
    }
}