package com.example.customapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core3.OnTownItemClickListener
import com.example.core3.inventoryAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.balance_change.view.*
import kotlinx.android.synthetic.main.inventory_layout.*

class inventoryActivity: AppCompatActivity(), OnTownItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inventory_layout)
        val inventory = findViewById<ImageView>(R.id.addInventory)
        inventory.setOnClickListener{
            val intent = Intent(this, addInventoryActivity::class.java).apply {
            }
            startActivityForResult(intent,0)
        }
        val list = findViewById<RecyclerView>(R.id.recycler)

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recycler)

        list.adapter = inventoryAdapter(this)
        list.layoutManager = LinearLayoutManager(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val list = findViewById<RecyclerView>(R.id.recycler)
        //fBalance = data?.getFloatExtra("balance",1.1f)!!.toDouble()

        list.adapter = inventoryAdapter(this)
        list.layoutManager = LinearLayoutManager(this)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onItemClick(item: Item, position: Int) {
        val intent = Intent(this, editInventoryActivity::class.java).apply {
            putExtra("Item",item)
        }
        startActivityForResult(intent,0)
    }
    override fun onBackPressed()
    {
        super.onBackPressed()
    }

    override fun onPause() {
        saveData()
        super.onPause()
    }

    fun saveData() {
        val perfs = getSharedPreferences("test", MODE_PRIVATE)
        val editor = perfs.edit()

        val gson = Gson()
        val json = gson.toJson(inventory.inventory)
        val jsonBalance = gson.toJson(inventory.cBalance)
        val jsonInvested = gson.toJson(inventory.moneyInvested)

        editor
            .putString("inventory",json)
            .apply()

        editor
            .putString("Balance", inventory.balance.toString())
            .apply()

        editor
            .putString("CBalance",jsonBalance)
            .apply()
        editor
            .putString("Invested",jsonInvested)
            .apply()
    }
    private val itemTouchHelperCallback =
        object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //Deletes Item
                if(direction == 8){
                    inventory.balance += inventory.inventory.elementAt(viewHolder.adapterPosition).price.toFloat()
                    inventory.addInvested(-inventory.inventory.elementAt(viewHolder.adapterPosition).price.toFloat())
                    inventory.addToBalance(-inventory.inventory.elementAt(viewHolder.adapterPosition).price.toFloat())
                    inventory.inventory.removeAt(viewHolder.adapterPosition)
                    recycler.adapter?.notifyDataSetChanged()
                }
                //Edits Item
                else if(direction == 4)
                {
                    onItemClick(inventory.inventory.elementAt(viewHolder.adapterPosition),viewHolder.adapterPosition)
                }
            }

        }
}