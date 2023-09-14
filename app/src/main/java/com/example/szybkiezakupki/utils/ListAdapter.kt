package com.example.szybkiezakupki.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.szybkiezakupki.databinding.ListListItemBinding
import com.example.szybkiezakupki.fragment.ListFragment

class ListAdapter(private val list:MutableList<ListData> ) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>(){


    private var listener:ListAdapterClicksInterface?= null

    fun setListener(listener: ListFragment){

        this.listener=listener
    }


    inner class ListViewHolder( val binding: ListListItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.EtListName.text= this.listName.toString()



                binding.deleteList.setOnClickListener{

                    listener?.onDeleteListBtnClicked(this)
                }
                binding.editList.setOnClickListener {
                    listener?.onEditListBtnClicked(this)
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

    interface ListAdapterClicksInterface{
        fun onDeleteListBtnClicked(ListData: ListData)
        fun onEditListBtnClicked(ListData: ListData)
    }
}