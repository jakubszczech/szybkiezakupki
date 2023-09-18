package com.example.szybkiezakupki.utils

import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.szybkiezakupki.databinding.ProductClientListItemBinding
import com.example.szybkiezakupki.databinding.ProductPreviewListItemBinding

class ProductPreviewAdapter(private val list:MutableList<ProductData> ) :
    RecyclerView.Adapter<ProductPreviewAdapter.ProductViewHolder>(){


    private var listener:ProductPreviewAdapterClicksInterface?= null

    fun setListener(listener: ProductPreviewAdapterClicksInterface){

        this.listener=listener
    }



    inner class ProductViewHolder( val binding: ProductPreviewListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductPreviewListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.todoTask.text= this.task
                binding.EtPrice.text= this.price.toString()
                binding.EtShelf.text=this.shelfNum.toString()
                binding.EtCategory.text=this.category


               binding.EtPrice.setOnClickListener{
//
                   listener?.onAddProdBtnClicked(this)
                }
               ///  binding.editTask.setOnClickListener {
                //      listener?.onEditTaskBtnClicked(this)
                //  }
                //binding.PurchasedProd.setOnClickListener {
               //     listener?.onPurchaseProdBtnClicked(this)
               // }


            }
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

    interface ProductPreviewAdapterClicksInterface{
        fun onDeleteTaskBtnClicked(ProductData: ProductData)
        fun onEditTaskBtnClicked(ProductData: ProductData)
        fun onPurchaseProdBtnClicked(productData: ProductData)
        fun onAddProdBtnClicked(productData: ProductData)

    }

}