package com.example.appanalytics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appanalytics.databinding.ItemCategoryBinding
import com.example.appanalytics.listener.OnClickCategory
import com.example.appanalytics.model.Categories

class AdapterCategory(
    private var contactList: ArrayList<Categories>,
    private val listener: OnClickCategory
) : RecyclerView.Adapter<AdapterCategory.MyViewHolder>() {

    class MyViewHolder(private val itemBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(category: Categories) {
            itemBinding.nameCategory.text = category.name

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = contactList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = contactList[position]
        holder.bind(category)
        holder.itemView.setOnClickListener {
            listener.onClick(category.name!!)
        }
    }
}