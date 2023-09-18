package com.example.szybkiezakupki.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
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
        fun newInstance(taskId: String, task: String, price: Float?, shelf:Int?, category: String?) = AddProductFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
                if (price != null) {
                    putFloat("price", price)
                }
                if (shelf != null) {
                    putInt("shelfNum", shelf)
                }
                putString("category", category)
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
          // ProductData = ProductData(
          //     arguments?.getString("taskId").toString(),
          //     arguments?.getString("task").toString()
          // )
          // binding.etProductName.setText(ProductData?.task)
            val taskId = arguments?.getString("taskId").toString()
            val task = arguments?.getString("task").toString()
            val price = arguments?.getFloat("price", 0.0f)
            val shelfNum = arguments?.getInt("shelfNum", 0)
            val isPurchased = arguments?.getBoolean("isPurchased", false)
            val category= arguments?.getString("category").toString()

            ProductData = ProductData(taskId, task, price, shelfNum, isPurchased, category)

            // Teraz możesz użyć wszystkich zmiennych ProductData
            binding.etProductName.setText(ProductData?.task.toString())
            binding.EtPriceS.setText(ProductData?.price.toString())
            binding.EtShelfNumber.setText(ProductData?.shelfNum.toString())
            binding.EtCategoty.setText(ProductData?.category.toString())

            // binding.etProductName.setText(ProductData?.task)

            // Dodaj pozostałe zmienne ProductData w odpowiednich miejscach
        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.btnAdd1.setOnClickListener {
            val product = binding.etProductName.text.toString()
            val price = binding.EtPriceS.text.toString()
            val shelf = binding.EtShelfNumber.text.toString()
            val category= binding.EtCategoty.text.toString()


            if (product.isNotEmpty()&& price.isNotEmpty() && shelf.isNotEmpty()&& category.isNotEmpty()&& !price.startsWith("-")&& !shelf.startsWith("-")) {
                if(ProductData==null) {
                    listener.onSaveProd(product, price, shelf, category, binding.etProductName, binding.EtPriceS, binding.EtShelfNumber, binding.EtCategoty)
                }else{
                    ProductData?.task = product
                    listener.onUpdateProd(ProductData!!,price, shelf, category, binding.etProductName, binding.EtPriceS, binding.EtShelfNumber, binding.EtCategoty)
                }


            } else {
                Toast.makeText(context, "Podaj poprawne informacje o produkcie", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    interface DialogNextBtnClickListener {
        fun onSaveProd(prod: String, price: String, shelf: String, category: String, etProductName: TextInputEditText, EtPriceS: TextInputEditText, EtShelfNumber: TextInputEditText, EtCategory: TextInputEditText)
        fun onUpdateProd(ProductData: ProductData, price: String, shelf: String, category: String, etProductName: TextInputEditText, EtPriceS: TextInputEditText, EtShelfNumber: TextInputEditText, EtCategory: TextInputEditText)

    }
}