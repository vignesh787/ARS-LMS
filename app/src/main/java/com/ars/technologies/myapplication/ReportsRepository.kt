package com.ars.technologies.myapplication

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData


class ReportsRepository {

    private val transactionDAO: TransactionsDAO? = null
    private val transactions: List<Transactions> = TODO()

    constructor(application: Application?,fromDate:Long, toDate:Long, reportType:String){
        val dao = application?.let { InventoryDatabase.getInstance(it).transactionsDAO }
        if (dao != null) {
            transactions = dao.getTransactionByParams(fromDate,toDate,reportType)
        }
    }



    fun getAllTransactions(): List<Transactions> {
        return transactions
    }
}