package com.ars.technologies.myapplication

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName="transactions")
data class Transactions(
    @PrimaryKey(autoGenerate = true)
    var transactionId:Long = 0L,
    @ColumnInfo(name="transaction_date")
    var transactionDate : Date = Date(),
    @ColumnInfo(name="document_id")
    var documentId:String = "",
    @ColumnInfo(name="code")
    var code:String="",
    @ColumnInfo(name="unit")
    var unit:String="",
    @ColumnInfo(name="quantity")
    var quantity:Double= 0.0,
    @ColumnInfo(name="transactionType")
    var transactionType:String= ""


)
