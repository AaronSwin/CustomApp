package com.example.customapp

import android.R.attr
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.core3.inventoryAdapter
import com.github.florent37.runtimepermission.RuntimePermission.askPermission
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import kotlinx.android.synthetic.main.inventory_add_layout.*
import kotlinx.android.synthetic.main.inventory_layout.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Math.round
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.jar.Manifest
import kotlin.math.roundToLong


class addInventoryActivity: AppCompatActivity() {
    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inventory_add_layout)



        val txtDate  = findViewById<TextView>(R.id.txtDate)

        val c = Calendar.getInstance()
        var month = ""
        var day = ""
        var date = LocalDate.parse("2020-12-12")
        val yy = c.get(Calendar.YEAR).toString()
        val mm  = c.get(Calendar.MONTH).toString()
        val dd = c.get(Calendar.DAY_OF_MONTH).toString()
        txtDate.text = yy + "-" + mm + "-" + dd

        txtDate.setOnClickListener{
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
                    txtDate.text = LocalDate.parse(""+ mYear +"-"+ month + "-" + day, DateTimeFormatter.ISO_LOCAL_DATE).toString()
                    date = LocalDate.parse(""+ mYear +"-"+ month + "-" + day, DateTimeFormatter.ISO_LOCAL_DATE)
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH))
            dpd.show()
        }

        val add = findViewById<Button>(R.id.btnAdd)
        add.setOnClickListener{
            inventory.Add(Item(txtName.text.toString(),txtPrice.text.toString(),txtValue.text.toString(), date.toString(),false))
            inventory.balance -= txtPrice.text.toString().toFloat()
            inventory.addToBalance(-txtPrice.text.toString().toFloat())
            inventory.addInvested(txtPrice.text.toString().toFloat())
            saveData()

        }
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
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