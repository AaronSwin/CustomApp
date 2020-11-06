package com.example.customapp

import java.time.LocalDate

object inventory {
    var inventory =  mutableListOf<Item>()
    var cBalance = mutableListOf<Float>()
    var moneyInvested = mutableListOf<Float>()
    var balance = 0.0f
    lateinit var soldDate: LocalDate
    fun Add(item: Item)
    {
        inventory.add(item)
    }
    fun addToBalance(float: Float)
    {
        cBalance.add(cBalance.last() + float)
    }
    fun addInvested(float: Float)
    {
        if(moneyInvested.isNotEmpty()){
            moneyInvested.add(moneyInvested.last() + float)
        }else{
            moneyInvested.add( float)
        }

    }
    fun changedBalance(float: Float)
    {
        cBalance.add(float)
    }
    fun  count():Int
    {
        return inventory.size
    }
}