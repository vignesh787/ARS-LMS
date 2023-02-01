package com.ars.technologies.myapplication

import android.annotation.SuppressLint
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*

class TransactionViewModel(val dao:TransactionsDAO) : ViewModel() {

    var code = ""
    var documentid = ""
    var quantity = ""
    var transactionDate = ""
    var unit = ""

    private val transactions = dao.getAll()
    val transactionString  = Transformations.map(transactions){
            transactions->formatTransactions(transactions)
    }

    @SuppressLint("UseValueOf")
    fun addTransaction(transaction:Transactions){
        viewModelScope.launch {
            val transaction = Transactions(0,transaction.transactionDate,transaction.documentId,transaction.code,transaction.unit,transaction.quantity,transaction.transactionType)
//            transaction.code = code
//            transaction.documentId = documentid
//            transaction.quantity = (quantity).toLong()
//            transaction.transactionDate = Date(transactionDate)
//            transaction.unit = unit
            System.out.println(transaction.code+":::"+transaction.documentId+":::"+transaction.quantity+":::"+transaction.transactionDate+":::"+transaction.unit+":::"+transaction.transactionType)
            val retval = dao.insert(transaction)
            System.out.println("DB Insert Succes!! retval"+retval)
        }
    }

    fun formatTransactions(transactions:List<Transactions>) :String{
        return transactions.fold(""){
                str,item ->str + '\n' + formatTransaction(item)
        }
    }

    fun formatTransaction(transaction:Transactions) :String{
        var str = "ID:${transaction.transactionId}"
        str +='\n' + "Name: ${transaction.code}"
        str += '\n' +"Complete: ${transaction.documentId}" + '\n'
        return str

    }
}