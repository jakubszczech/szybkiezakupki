package com.example.szybkiezakupki.utils

import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.szybkiezakupki.databinding.ProductClientListItemBinding

class ProductClientAdapter(private val list:MutableList<ProductData> ) :
    RecyclerView.Adapter<ProductClientAdapter.ProductViewHolder>(){


    private var listener:ProductClientAdapterClicksInterface?= null

    fun setListener(listener: ProductClientAdapterClicksInterface){

        this.listener=listener
    }



    inner class ProductViewHolder( val binding: ProductClientListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductClientListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.todoTask.text= this.task
                binding.EtPrice.text= this.price.toString()
                binding.EtShelf.text=this.shelfNum.toString()
                binding.EtCategory.text=this.category


                binding.deleteTask.setOnClickListener{

                    listener?.onDeleteTaskBtnClicked(this)
                }
              //  binding.editTask.setOnClickListener {
              //      listener?.onEditTaskBtnClicked(this)
              //  }
                binding.PurchasedProd.setOnClickListener {
                    listener?.onPurchaseProdBtnClicked(this)
                }


            }
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

    interface ProductClientAdapterClicksInterface{
        fun onDeleteTaskBtnClicked(ProductData: ProductData)
        fun onEditTaskBtnClicked(ProductData: ProductData)
        fun onPurchaseProdBtnClicked(productData: ProductData)

    }

}