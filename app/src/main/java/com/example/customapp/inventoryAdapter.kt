package com.example.core3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customapp.Item
import com.example.customapp.R
import com.example.customapp.inventory
import java.text.NumberFormat
import java.text.NumberFormat.*
import java.util.*

class inventoryAdapter(var clickListener: OnTownItemClickListener):RecyclerView.Adapter<inventoryAdapter.ViewHolder>() {


    inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v) {
        val itemName = v.findViewById<TextView>(R.id.txtRecylerName)
        val itemPrice = v.findViewById<TextView>(R.id.txtRecyclerPrice)

        fun bind(item: Item, action: OnTownItemClickListener){
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("AUD")


            itemName.text = item.name
            itemPrice.text = format.format(item.price.toString().toFloat())

            v.setOnClickListener {
                action.onItemClick(item,adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.inventory_l_layout,parent,false) as View
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = inventory.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(inventory.inventory.get(position),clickListener)
    }

}

interface OnTownItemClickListener{
    fun onItemClick(item: Item, position: Int)
}

