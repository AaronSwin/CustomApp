package com.example.customapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.balance_change.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val balance = findViewById<TextView>(R.id.txtBalance)
        loadData()


        val inventory = findViewById<LinearLayout>(R.id.inventoryLayout)
        val statement = findViewById<LinearLayout>(R.id.statmentLayout)

        inventory.setOnClickListener{
            val intent = Intent(this, inventoryActivity::class.java).apply {

            }
            startActivityForResult(intent,0)
        }
        statement.setOnClickListener{
            val intent = Intent(this, statementActivity::class.java).apply {
            }
            startActivityForResult(intent,0)
        }
        balance.setOnClickListener{
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.balance_change,null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Current Balance")
            val mAlertDialog = mBuilder.show()
            mDialogView.balanceEnter.setOnClickListener{
                mAlertDialog.dismiss()
                val amount = mDialogView.balance.text.toString()
                balance.text = amount
                com.example.customapp.inventory.balance = amount.toFloat()
                com.example.customapp.inventory.changedBalance(amount.toFloat())
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val balance = findViewById<TextView>(R.id.txtBalance)
        inventory.balance = Math.round(inventory.balance * 100) /100.toFloat()
        balance.text = inventory.balance.toString()
    }

    override fun onPause() {
        saveData()
        super.onPause()
    }

    private fun saveData() {
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
        //editor
        //    .clear()
        //    .apply()
    }
    private fun loadData(){
        val perfs = getSharedPreferences("test", MODE_PRIVATE)
        val balance = findViewById<TextView>(R.id.txtBalance)
        val gson = Gson()
        val json = perfs.getString("inventory",null)
        val jsonCBalance = perfs.getString("CBalance",null)
        val jsonInvested = perfs.getString("Invested",null)
        try{
            inventory.inventory = Gson().fromJson<MutableList<Item>>(json!!)
            inventory.cBalance = Gson().fromJson<MutableList<Float>>(jsonCBalance!!)
            inventory.moneyInvested = Gson().fromJson<MutableList<Float>>(jsonInvested!!)
            balance.text = perfs.getString("Balance","0")
            inventory.balance = balance.text.toString().toFloat()
        }
        catch (e: kotlin.NullPointerException)
        {}
    }
    inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)
}