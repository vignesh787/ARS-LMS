package com.ars.technologies.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(indices = [Index(value = ["code"],
        unique = true)],tableName = "master")
data class Master(
    @PrimaryKey(autoGenerate = true)
    var masterId:Long = 0L,
    @ColumnInfo(name="transaction_date")
    var transactionDate : Date = Date(),
    @ColumnInfo(name="code")
    var code:String = "",
    @ColumnInfo(name="description")
    var description:String="",
    @ColumnInfo(name="unit")
    var unit:String="",
    @ColumnInfo(name="quantity")
    var quantity:Double= 0.0,
    @ColumnInfo(name="rate")
    var rate:Long = 0L
)
