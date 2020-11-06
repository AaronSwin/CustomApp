package com.example.customapp

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.net.URI
import java.time.LocalDate

@Parcelize
data class Item(var name:String, var price: String, var value: String, var dateAquired: String, var sold: Boolean): Parcelable {

}