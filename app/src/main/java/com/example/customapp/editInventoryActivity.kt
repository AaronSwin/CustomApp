package com.example.customapp

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class editInventoryActivity: AppCompatActivity() {
    lateinit var date: LocalDate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inventory_edit_layout)

        val item = intent.getParcelableExtra<Item>("Item")

        val name = findViewById<TextView>(R.id.txtEditName)
        val price = findViewById<TextView>(R.id.txtEditPrice)
        val value = findViewById<TextView>(R.id.txtEditValue)
        val txtdate = findViewById<TextView>(R.id.txtEditDate)
        val check = findViewById<CheckBox>(R.id.check)

        name.text = item?.name
        price.text = item?.price
        value.text = item?.value
        txtdate.text = item?.dateAquired.toString()

        val c = Calendar.getInstance()
        var month = ""
        var day = ""

        val yy = c.get(Calendar.YEAR).toString()
        val mm  = c.get(Calendar.MONTH).toString()
        val dd = c.get(Calendar.DAY_OF_MONTH).toString()


        txtdate.setOnClickListener{
            val dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { datePicker, mYear, mMonth, mDay ->
                    when(mDay)
                    {
                        in 0..9 -> day = "0" + mDay
                        in 10..32 -> day = mDay.toString()
                    }
                    when(mMonth)
                    {
                        in 0..9 -> month = "0" + mMonth
                        in 10..13 -> month = mMonth.toString()
                    }
                    txtdate.text = LocalDate.parse(""+ mYear +"-"+ month + "-" + day, DateTimeFormatter.ISO_LOCAL_DATE).toString()
                    date = LocalDate.parse(""+ mYear +"-"+ month + "-" + day, DateTimeFormatter.ISO_LOCAL_DATE)
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH))
            dpd.show()
        }

        val button = findViewById<Button>(R.id.btnedit)
        button.setOnClickListener{
            inventory.inventory.remove(item)
            inventory.balance -= price.text.toString().toFloat() - item?.price.toString().toFloat()

            if(check.isChecked){
                inventory.balance += value.text.toString().toFloat()
                if (item != null) {
                    inventory.addToBalance(item.value.toFloat())
                    inventory.addInvested(-item.price.toFloat())
                }

            }else{
                inventory.Add(Item(name.text.toString(),price.text.toString(),value.text.toString(),txtdate.text.toString(),check.isChecked))
            }
            onBackPressed()
            }
        }

    override fun onBackPressed() {
        val i = Intent().apply{
            putExtra("balance",inventory.balance.toFloat())
        }
        setResult(Activity.RESULT_OK,i)
        super.onBackPressed()
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
    }
}

